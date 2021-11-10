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

/**
 * Constants of the protobuf library.
 *
 * @since 1.0.1
 */
public class ProtobufConstants {

    private ProtobufConstants() {}

    // Protobuf predefined messages paths
    public static final String WRAPPER_DOUBLE_TYPE_NAME = "type.googleapis.com/google.protobuf.DoubleValue";
    public static final String WRAPPER_FLOAT_TYPE_NAME = "type.googleapis.com/google.protobuf.FloatValue";
    public static final String WRAPPER_INT64_TYPE_NAME = "type.googleapis.com/google.protobuf.Int64Value";
    public static final String WRAPPER_UINT64_TYPE_NAME = "type.googleapis.com/google.protobuf.UInt64Value";
    public static final String WRAPPER_INT32_TYPE_NAME = "type.googleapis.com/google.protobuf.Int32Value";
    public static final String WRAPPER_UINT32_TYPE_NAME = "type.googleapis.com/google.protobuf.UInt32Value";
    public static final String WRAPPER_BOOL_TYPE_NAME = "type.googleapis.com/google.protobuf.BoolValue";
    public static final String WRAPPER_STRING_TYPE_NAME = "type.googleapis.com/google.protobuf.StringValue";
    public static final String WRAPPER_BYTES_TYPE_NAME = "type.googleapis.com/google.protobuf.BytesValue";
    public static final String EMPTY_TYPE_NAME = "type.googleapis.com/google.protobuf.Empty";
    public static final String TIMESTAMP_TYPE_NAME = "type.googleapis.com/google.protobuf.Timestamp";
    public static final String DURATION_TYPE_NAME = "type.googleapis.com/google.protobuf.Duration";

    // Protobuf Any type fields
    public static final String ANY_FIELD_TYPE_URL = "typeUrl";
    public static final String ANY_FIELD_VALUE = "value";

}
