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

import io.ballerina.runtime.api.TypeTags;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.api.values.BTypedesc;
import org.ballerinalang.langlib.value.CloneWithType;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_FIELD_VALUE;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.TIMESTAMP_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BOOL_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BYTES_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_DOUBLE_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_FLOAT_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT32_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT64_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_STRING_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT32_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT64_TYPE_NAME;

/**
 * This class will hold the native APIs for Ballerina pack and unpack APIs.
 *
 * @since 1.0.1
 */
public class AnyTypeCreator {

    private AnyTypeCreator() {

    }

    public static BString externGetNameFromRecord(BMap<BString, Object> value) {

        return StringUtils.fromString(value.getType().getName());
    }

    public static Object unpack(BMap<BString, Object> value, BTypedesc targetType) {

        int expectedTypeTag = targetType.getDescribingType().getTag();
        String typeUrl = value.getStringValue(StringUtils.fromString(ANY_FIELD_TYPE_URL)).getValue();

        if (isMatchingType(typeUrl, expectedTypeTag)) {
            switch (typeUrl) {
                case WRAPPER_DOUBLE_TYPE_NAME:
                case WRAPPER_FLOAT_TYPE_NAME:
                    return value.getFloatValue(StringUtils.fromString(ANY_FIELD_VALUE));
                case WRAPPER_INT64_TYPE_NAME:
                case WRAPPER_UINT64_TYPE_NAME:
                case WRAPPER_INT32_TYPE_NAME:
                case WRAPPER_UINT32_TYPE_NAME:
                    return value.getIntValue(StringUtils.fromString(ANY_FIELD_VALUE));
                case WRAPPER_BOOL_TYPE_NAME:
                    return value.getBooleanValue(StringUtils.fromString(ANY_FIELD_VALUE));
                case WRAPPER_STRING_TYPE_NAME:
                    return value.getStringValue(StringUtils.fromString(ANY_FIELD_VALUE));
                case WRAPPER_BYTES_TYPE_NAME:
                    if (targetType.getDescribingType().toString().equals("byte[]")) {
                        return value.getArrayValue(StringUtils.fromString(ANY_FIELD_VALUE));
                    }
                    break;
                case EMPTY_TYPE_NAME:
                    return null;
                case TIMESTAMP_TYPE_NAME:
                    BArray utcTime = value.getArrayValue(StringUtils.fromString(ANY_FIELD_VALUE));
                    utcTime.freezeDirect();
                    return utcTime;
                case DURATION_TYPE_NAME:
                    return value.get(StringUtils.fromString(ANY_FIELD_VALUE));
                default:
                    break;
            }
        }
        if (expectedTypeTag == TypeTags.RECORD_TYPE_TAG) {
            return CloneWithType.cloneWithType(value.getMapValue(StringUtils.fromString(ANY_FIELD_VALUE)),
                    targetType);
        } else {
            String errorMessage = "Type " + typeUrl + " cannot unpack to " +
                    targetType.getDescribingType().getName();
            return ErrorGenerator.createError(Errors.TypeMismatchError, errorMessage);
        }
    }

    private static boolean isMatchingType(String typeUrl, int typeTag) {
        if (ModuleUtils.getAnyTypeMap().containsKey(typeUrl)) {
            return ModuleUtils.getAnyTypeMap().get(typeUrl) == typeTag;
        }
        return false;
    }
}
