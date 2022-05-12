/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.stdlib.protobuf.nativeimpl;

import com.google.protobuf.Descriptors;
import io.ballerina.runtime.api.TypeTags;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.protobuf.deserializers.DeserializeHandler;
import org.ballerinalang.langlib.value.CloneWithType;

import java.io.IOException;
import java.util.Arrays;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_VALUE;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.TIMESTAMP_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BOOL_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BYTES_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_DOUBLE_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_FLOAT_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT32_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT64_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_STRING_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT32_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT64_TYPE_URL;

/**
 * This class will hold the native APIs for Ballerina pack and unpack APIs.
 *
 * @since 1.0.1
 */
public class AnyTypeCreator {

    private static final String PROTOBUF_DESC_ANNOTATION = "ballerina/protobuf:1:Descriptor";
    private static final String PROTOBUF_DESC_ANNOTATION_VALUE = "value";

    private AnyTypeCreator() {

    }

    public static BString externGetNameFromRecord(BMap<BString, Object> value) {

        return StringUtils.fromString(value.getType().getName());
    }

    public static Object unpack(BMap<BString, Object> value, BTypedesc targetType) {

        int expectedTypeTag = targetType.getDescribingType().getTag();
        String typeUrl = value.getStringValue(StringUtils.fromString(ANY_FIELD_TYPE_URL)).getValue();

        if (isMatchingType(typeUrl, expectedTypeTag)) {
            try {
                switch (typeUrl) {
                    case WRAPPER_DOUBLE_TYPE_URL:
                    case WRAPPER_FLOAT_TYPE_URL:
                    case WRAPPER_INT64_TYPE_URL:
                    case WRAPPER_UINT64_TYPE_URL:
                    case WRAPPER_INT32_TYPE_URL:
                    case WRAPPER_UINT32_TYPE_URL:
                    case WRAPPER_BOOL_TYPE_URL:
                    case WRAPPER_STRING_TYPE_URL:
                    case WRAPPER_BYTES_TYPE_URL:
                        Descriptors.Descriptor descriptor = com.google.protobuf.WrappersProto.getDescriptor()
                                .findMessageTypeByName(getMessageNameFromTypeUrl(typeUrl));
                        DeserializeHandler deserializeHandler = new DeserializeHandler(descriptor,
                                value.getStringValue(StringUtils.fromString("value")).getValue(),
                                targetType.getDescribingType(), null);
                        deserializeHandler.deserialize();
                        return deserializeHandler.getBMessage();
                    case EMPTY_TYPE_URL:
                        return null;
                    case TIMESTAMP_TYPE_URL:
                        Descriptors.Descriptor timestampDescriptor = com.google.protobuf.Timestamp.getDescriptor();
                        DeserializeHandler timestampDeserializeHandler = new DeserializeHandler(timestampDescriptor,
                                value.getStringValue(StringUtils.fromString("value")).getValue(),
                                targetType.getDescribingType(), targetType.getDescribingType());
                        timestampDeserializeHandler.deserialize();
                        BArray utcTime = (BArray) timestampDeserializeHandler.getBMessage();
                        utcTime.freezeDirect();
                        return utcTime;
                    case DURATION_TYPE_URL:
                        Descriptors.Descriptor durationDescriptor = com.google.protobuf.Duration.getDescriptor();
                        DeserializeHandler durationDeserializeHandler = new DeserializeHandler(durationDescriptor,
                                value.getStringValue(StringUtils.fromString("value")).getValue(),
                                targetType.getDescribingType(), targetType.getDescribingType());
                        durationDeserializeHandler.deserialize();
                        return durationDeserializeHandler.getBMessage();
                    default:
                        break;
                }
            } catch (IOException | Descriptors.DescriptorValidationException e) {
                return ErrorGenerator.createError(Errors.TypeMismatchError, e.toString());
            }

        }
        if (expectedTypeTag == TypeTags.RECORD_TYPE_TAG) {
            if (value.get(StringUtils.fromString("value")) instanceof BString) {
                try {
                    Object data = deserialize(targetType.getDescribingType(),
                            (RecordType) targetType.getDescribingType(),
                            value.getStringValue(StringUtils.fromString("value")).getValue());
                    return CloneWithType.cloneWithType(data, targetType);
                } catch (Descriptors.DescriptorValidationException | IOException e) {
                    return ErrorGenerator.createError(Errors.TypeMismatchError, e.toString());
                }
            } else {
                return CloneWithType.cloneWithType(value.getMapValue(StringUtils.fromString(ANY_FIELD_VALUE)),
                        targetType);
            }
        } else {
            String errorMessage = "Type " + typeUrl + " cannot unpack to " +
                    targetType.getDescribingType().getName();
            return ErrorGenerator.createError(Errors.TypeMismatchError, errorMessage);
        }
    }

    private static String getMessageNameFromTypeUrl(String typeUrl) {

        String[] literals = typeUrl.split("\\.");
        return literals[literals.length - 1];
    }

    private static Object deserialize(Type targetType, RecordType recordType, String content)
            throws Descriptors.DescriptorValidationException, IOException {

        if (isDescriptorAnnotationAvailable(recordType)) {
            String annotation = getProtobufDescAnnotation(recordType);
            DeserializeHandler m2 = new DeserializeHandler(annotation, content, targetType, recordType);
            m2.deserialize();
            return m2.getBMessage();
        } else {
            return ErrorGenerator.createError(Errors.TypeMismatchError,
                    "Unavailable annotation for record " + recordType.getName());
        }
    }

    private static boolean isDescriptorAnnotationAvailable(RecordType recordType) {

        return Arrays.stream(recordType.getAnnotations().getKeys()).anyMatch(
                s -> PROTOBUF_DESC_ANNOTATION.equals(s.getValue()));
    }

    private static String getProtobufDescAnnotation(RecordType recordType) {

        BMap<BString, Object> annotations = recordType.getAnnotations();
        return annotations.getMapValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION))
                .getStringValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION_VALUE)).getValue();
    }

    private static boolean isMatchingType(String typeUrl, int typeTag) {

        if (ModuleUtils.getAnyTypeMap().containsKey(typeUrl)) {
            return ModuleUtils.getAnyTypeMap().get(typeUrl) == typeTag;
        }
        return false;
    }
}
