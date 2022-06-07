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
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.utils.TypeUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import io.ballerina.stdlib.protobuf.deserializers.DeserializeHandler;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.serializers.SerializeHandler;
import org.ballerinalang.langlib.value.CloneWithType;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_VALUE;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.BALLERINA_ANY_VALUE_ENTRY;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.BALLERINA_PROTOBUF_ANY_PACKAGE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_ANY_MESSAGE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.STRUCT_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.STRUCT_TYPE_URL;
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
import static io.ballerina.stdlib.protobuf.utils.Utils.deserialize;
import static io.ballerina.stdlib.protobuf.utils.Utils.getAnnotatedDescriptorFromRecord;
import static io.ballerina.stdlib.protobuf.utils.Utils.getMessageNameFromTypeUrl;
import static io.ballerina.stdlib.protobuf.utils.Utils.isMatchingType;

/**
 * This class will hold the native APIs for Ballerina pack and unpack APIs.
 *
 * @since 1.0.1
 */
public class AnyTypeCreator {

    private AnyTypeCreator() {

    }

    public static BString externGetNameFromRecord(BMap<BString, Object> value) {

        String name = value.getType().getName();
        if ("map".equals(name)) {
            return StringUtils.fromString(STRUCT_TYPE_NAME);
        }
        String packageName = value.getType().getPackage().getName();
        if (GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(name) && BALLERINA_PROTOBUF_ANY_PACKAGE_NAME.equals(packageName)) {
            return StringUtils.fromString(ANY_TYPE_NAME);
        }
        return StringUtils.fromString(name);
    }

    public static Object unpack(BMap<BString, Object> value, BTypedesc targetType) {

        int expectedTypeTag = TypeUtils.getReferredType(targetType.getDescribingType()).getTag();
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
                                value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue(),
                                TypeUtils.getReferredType(targetType.getDescribingType()), null);
                        deserializeHandler.deserialize();
                        return deserializeHandler.getBMessage();
                    case EMPTY_TYPE_URL:
                        return null;
                    case TIMESTAMP_TYPE_URL:
                        Descriptors.Descriptor timestampDescriptor = com.google.protobuf.Timestamp.getDescriptor();
                        DeserializeHandler timestampDeserializeHandler = new DeserializeHandler(timestampDescriptor,
                                value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue(),
                                TypeUtils.getReferredType(targetType.getDescribingType()),
                                TypeUtils.getReferredType(targetType.getDescribingType()));
                        timestampDeserializeHandler.deserialize();
                        BArray utcTime = (BArray) timestampDeserializeHandler.getBMessage();
                        utcTime.freezeDirect();
                        return utcTime;
                    case DURATION_TYPE_URL:
                        Descriptors.Descriptor durationDescriptor = com.google.protobuf.Duration.getDescriptor();
                        DeserializeHandler durationDeserializeHandler = new DeserializeHandler(durationDescriptor,
                                value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue(),
                                TypeUtils.getReferredType(targetType.getDescribingType()),
                                TypeUtils.getReferredType(targetType.getDescribingType()));
                        durationDeserializeHandler.deserialize();
                        return durationDeserializeHandler.getBMessage();
                    case STRUCT_TYPE_URL:
                        Descriptors.Descriptor structDescriptor = com.google.protobuf.Struct.getDescriptor();
                        DeserializeHandler structDeserializeHandler = new DeserializeHandler(structDescriptor,
                                value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue(),
                                TypeUtils.getReferredType(targetType.getDescribingType()),
                                TypeUtils.getReferredType(targetType.getDescribingType()));
                        structDeserializeHandler.deserialize();
                        return structDeserializeHandler.getBMessage();
                    case ANY_TYPE_URL:
                        Descriptors.Descriptor anyDescriptor = com.google.protobuf.Any.getDescriptor();
                        DeserializeHandler anyDeserializeHandler = new DeserializeHandler(anyDescriptor,
                                value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue(),
                                TypeUtils.getReferredType(targetType.getDescribingType()),
                                TypeUtils.getReferredType(targetType.getDescribingType()));
                        return anyDeserializeHandler.getBMessage();
                    default:
                        break;
                }
            } catch (IOException | Descriptors.DescriptorValidationException | AnnotationUnavailableException e) {
                return ErrorGenerator.createError(Errors.TypeMismatchError, e.toString());
            }

        }
        if (expectedTypeTag == TypeTags.RECORD_TYPE_TAG) {
            if (value.get(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)) instanceof BString) {
                try {
                    Object data = deserialize(TypeUtils.getReferredType(targetType.getDescribingType()),
                            (RecordType) TypeUtils.getReferredType(targetType.getDescribingType()),
                            value.getStringValue(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY)).getValue());
                    return CloneWithType.cloneWithType(data, targetType);
                } catch (Descriptors.DescriptorValidationException | IOException e) {
                    return ErrorGenerator.createError(Errors.TypeMismatchError, e.toString());
                } catch (AnnotationUnavailableException e) {
                    return ErrorGenerator.createError(Errors.TypeMismatchError, e.getMessage());
                }
            } else {
                return CloneWithType.cloneWithType(value.getMapValue(StringUtils.fromString(ANY_FIELD_VALUE)),
                        targetType);
            }
        } else {
            String errorMessage = "Type " + typeUrl + " cannot unpack to " +
                    TypeUtils.getReferredType(targetType.getDescribingType()).getName();
            return ErrorGenerator.createError(Errors.TypeMismatchError, errorMessage);
        }
    }

    public static Object getSerializedString(Object value, BString bTypeUrl) {

        String typeUrl = bTypeUrl.getValue();
        Descriptors.Descriptor descriptor = null;
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
                    descriptor = com.google.protobuf.WrappersProto.getDescriptor()
                            .findMessageTypeByName(getMessageNameFromTypeUrl(typeUrl));
                    break;
                case EMPTY_TYPE_URL:
                    return StringUtils.fromString("");
                case TIMESTAMP_TYPE_URL:
                    descriptor = com.google.protobuf.Timestamp.getDescriptor();
                    break;
                case DURATION_TYPE_URL:
                    descriptor = com.google.protobuf.Duration.getDescriptor();
                    break;
                case STRUCT_TYPE_URL:
                    descriptor = com.google.protobuf.Struct.getDescriptor();
                    break;
                case ANY_TYPE_URL:
                    descriptor = com.google.protobuf.Any.getDescriptor();
                    break;
                default:
                    descriptor = getAnnotatedDescriptorFromRecord(value);
                    break;
            }
            SerializeHandler serializeHandler = new SerializeHandler(descriptor, value);
            serializeHandler.serialize();
            return serializeHandler.getContentAsBString();
        } catch (IOException | Descriptors.DescriptorValidationException | AnnotationUnavailableException e) {
            return ErrorGenerator.createError(Errors.TypeMismatchError, e.toString());
        }
    }
}
