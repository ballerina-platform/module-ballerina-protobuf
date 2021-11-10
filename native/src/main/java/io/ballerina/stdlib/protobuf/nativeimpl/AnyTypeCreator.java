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

    private AnyTypeCreator() {}

    public static BString externGetNameFromRecord(BMap<BString, Object> value) {

        return StringUtils.fromString(value.getType().getName());
    }

    public static Object unpack(BMap<BString, Object> value, BTypedesc targetType) {

        int expectedTypeTag = targetType.getDescribingType().getTag();
        String typeUrl = value.getStringValue(StringUtils.fromString(ANY_FIELD_TYPE_URL)).getValue();

        if ((WRAPPER_DOUBLE_TYPE_NAME.equals(typeUrl) || WRAPPER_FLOAT_TYPE_NAME.equals(typeUrl)) &&
                expectedTypeTag == TypeTags.FLOAT_TAG) {
            return value.getFloatValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if ((WRAPPER_INT64_TYPE_NAME.equals(typeUrl) || WRAPPER_UINT64_TYPE_NAME.equals(typeUrl) ||
                WRAPPER_INT32_TYPE_NAME.equals(typeUrl) || WRAPPER_UINT32_TYPE_NAME.equals(typeUrl)) &&
                expectedTypeTag == TypeTags.INT_TAG) {
            return value.getIntValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (WRAPPER_BOOL_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.BOOLEAN_TAG) {
            return value.getBooleanValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (WRAPPER_STRING_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.STRING_TAG) {
            return value.getStringValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (WRAPPER_BYTES_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.BYTE_ARRAY_TAG) {
            return value.getArrayValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (EMPTY_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.NONE_TAG) {
            return value.get(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (TIMESTAMP_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.INTERSECTION_TAG) {
            BArray utcTime = value.getArrayValue(StringUtils.fromString(ANY_FIELD_VALUE));
            utcTime.freezeDirect();
            return utcTime;
        } else if (DURATION_TYPE_NAME.equals(typeUrl) && expectedTypeTag == TypeTags.DECIMAL_TAG) {
            return value.get(StringUtils.fromString(ANY_FIELD_VALUE));
        } else if (expectedTypeTag == TypeTags.RECORD_TYPE_TAG) {
            return value.getMapValue(StringUtils.fromString(ANY_FIELD_VALUE));
        } else {
            String errorMessage = "Type " + typeUrl + " cannot unpack to " + targetType.getDescribingType().getName();
            return ErrorGenerator.createError(Errors.TypeMismatchError, errorMessage);
        }
    }
}
