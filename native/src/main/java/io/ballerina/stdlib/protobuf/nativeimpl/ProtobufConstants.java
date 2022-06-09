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

import java.math.BigDecimal;

/**
 * Constants of the protobuf library.
 *
 * @since 1.0.1
 */
public class ProtobufConstants {

    private ProtobufConstants() {

    }

    // Protobuf predefined messages type URLs
    public static final String WRAPPER_DOUBLE_TYPE_URL = "type.googleapis.com/google.protobuf.DoubleValue";
    public static final String WRAPPER_FLOAT_TYPE_URL = "type.googleapis.com/google.protobuf.FloatValue";
    public static final String WRAPPER_INT64_TYPE_URL = "type.googleapis.com/google.protobuf.Int64Value";
    public static final String WRAPPER_UINT64_TYPE_URL = "type.googleapis.com/google.protobuf.UInt64Value";
    public static final String WRAPPER_INT32_TYPE_URL = "type.googleapis.com/google.protobuf.Int32Value";
    public static final String WRAPPER_UINT32_TYPE_URL = "type.googleapis.com/google.protobuf.UInt32Value";
    public static final String WRAPPER_BOOL_TYPE_URL = "type.googleapis.com/google.protobuf.BoolValue";
    public static final String WRAPPER_STRING_TYPE_URL = "type.googleapis.com/google.protobuf.StringValue";
    public static final String WRAPPER_BYTES_TYPE_URL = "type.googleapis.com/google.protobuf.BytesValue";
    public static final String EMPTY_TYPE_URL = "type.googleapis.com/google.protobuf.Empty";
    public static final String TIMESTAMP_TYPE_URL = "type.googleapis.com/google.protobuf.Timestamp";
    public static final String DURATION_TYPE_URL = "type.googleapis.com/google.protobuf.Duration";
    public static final String STRUCT_TYPE_URL = "type.googleapis.com/google.protobuf.Struct";
    public static final String ANY_TYPE_URL = "type.googleapis.com/google.protobuf.Any";

    // Proto predefined messages names
    public static final String WRAPPER_DOUBLE_TYPE_NAME = "google.protobuf.DoubleValue";
    public static final String WRAPPER_FLOAT_TYPE_NAME = "google.protobuf.FloatValue";
    public static final String WRAPPER_INT64_TYPE_NAME = "google.protobuf.Int64Value";
    public static final String WRAPPER_UINT64_TYPE_NAME = "google.protobuf.UInt64Value";
    public static final String WRAPPER_INT32_TYPE_NAME = "google.protobuf.Int32Value";
    public static final String WRAPPER_UINT32_TYPE_NAME = "google.protobuf.UInt32Value";
    public static final String WRAPPER_BOOL_TYPE_NAME = "google.protobuf.BoolValue";
    public static final String WRAPPER_STRING_TYPE_NAME = "google.protobuf.StringValue";
    public static final String WRAPPER_BYTES_TYPE_NAME = "google.protobuf.BytesValue";
    public static final String ANY_TYPE_NAME = "google.protobuf.Any";
    public static final String EMPTY_TYPE_NAME = "google.protobuf.Empty";
    public static final String TIMESTAMP_TYPE_NAME = "google.protobuf.Timestamp";
    public static final String DURATION_TYPE_NAME = "google.protobuf.Duration";
    public static final String STRUCT_TYPE_NAME = "google.protobuf.Struct";

    public static final String GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY = "google.protobuf.Struct.FieldsEntry.key";
    public static final String GOOGLE_PROTOBUF_ANY_TYPE_URL = "google.protobuf.Any.type_url";
    public static final String GOOGLE_PROTOBUF_ANY_MESSAGE_NAME = "Any";
    public static final String GOOGLE_PROTOBUF_TIMESTAMP_SECONDS = "google.protobuf.Timestamp.seconds";
    public static final String GOOGLE_PROTOBUF_TIMESTAMP_NANOS = "google.protobuf.Timestamp.nanos";
    public static final String GOOGLE_PROTOBUF_DURATION_SECONDS = "google.protobuf.Duration.seconds";
    public static final String GOOGLE_PROTOBUF_DURATION_NANOS = "google.protobuf.Duration.nanos";
    public static final String GOOGLE_PROTOBUF_STRUCT = "google.protobuf.Struct";
    public static final String GOOGLE_PROTOBUF_STRUCT_FIELDS = "google.protobuf.Struct.fields";
    public static final String GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_VALUE = "google.protobuf.Struct.FieldsEntry.value";
    public static final String GOOGLE_PROTOBUF_VALUE_LIST_VALUE = "google.protobuf.Value.list_value";
    public static final String GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE = "google.protobuf.Value.struct_value";
    public static final String GOOGLE_PROTOBUF_LIST_VALUE_VALUES = "google.protobuf.ListValue.values";
    public static final String GOOGLE_PROTOBUF_STRUCT_VALUE_VALUES = "google.protobuf.StructValue.values";
    public static final String BALLERINA_ANY_VALUE_ENTRY = "value";
    public static final String BALLERINA_TYPE_URL_ENTRY = "typeUrl";
    public static final String BALLERINA_PROTOBUF_ANY_PACKAGE_NAME = "protobuf.types.any";
    public static final BigDecimal ANALOG_GIGA = new BigDecimal(1000000000);

    // Protobuf Any type fields
    public static final String ANY_FIELD_TYPE_URL = "typeUrl";
    public static final String ANY_FIELD_VALUE = "value";
    public static final String PROTOBUF_DESC_ANNOTATION_VALUE = "value";

}
