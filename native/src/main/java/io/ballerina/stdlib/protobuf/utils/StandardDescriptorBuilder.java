/*
 *  Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.stdlib.protobuf.utils;

import com.google.protobuf.AnyProto;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DurationProto;
import com.google.protobuf.EmptyProto;
import com.google.protobuf.StructProto;
import com.google.protobuf.TimestampProto;
import com.google.protobuf.WrappersProto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.STRUCT_TYPE_NAME;
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
 * Provides protobuf descriptor for well known dependency.
 */
public class StandardDescriptorBuilder {

    private StandardDescriptorBuilder() {

    }

    private static final Map<String, Descriptors.FileDescriptor> standardLibDescriptorMapForMessageName;

    static {
        standardLibDescriptorMapForMessageName = new HashMap<>();
        standardLibDescriptorMapForMessageName.put(ANY_TYPE_NAME,
                com.google.protobuf.AnyProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(EMPTY_TYPE_NAME,
                com.google.protobuf.EmptyProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(TIMESTAMP_TYPE_NAME,
                com.google.protobuf.TimestampProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(DURATION_TYPE_NAME,
                com.google.protobuf.DurationProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(STRUCT_TYPE_NAME,
                com.google.protobuf.StructProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_DOUBLE_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_FLOAT_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_INT64_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_UINT64_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_INT32_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_UINT32_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_BOOL_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_STRING_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
        standardLibDescriptorMapForMessageName.put(WRAPPER_BYTES_TYPE_NAME,
                com.google.protobuf.WrappersProto.getDescriptor());
    }

    public static Descriptors.FileDescriptor getFileDescriptorFromMessageName(String messageName) {

        return standardLibDescriptorMapForMessageName.get(messageName);
    }

    public static final Map<DescriptorProtos.FieldDescriptorProto.Type, Integer> WIRE_TYPE_MAP;

    static {
        Map<DescriptorProtos.FieldDescriptorProto.Type, Integer> wireMap = new HashMap<>();
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED32, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED64, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING, 2);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES, 2);
        WIRE_TYPE_MAP = Collections.unmodifiableMap(wireMap);
    }

    public static int getFieldWireType(Descriptors.FieldDescriptor.Type fieldType) {

        if (fieldType == null) {
            return -1;
        }
        Integer wireType = WIRE_TYPE_MAP.get(fieldType.toProto());
        if (wireType != null) {
            return wireType;
        } else {
            // Returns embedded messages, packed repeated fields message type, if field type doesn't map with the
            // predefined proto types.
            return 2;
        }
    }

    public static Descriptors.Descriptor findGoogleDescriptorFromName(String typeName) {

        String messageName = typeName.replace("google.protobuf.", "");
        switch (typeName) {
            case WRAPPER_DOUBLE_TYPE_NAME:
            case WRAPPER_FLOAT_TYPE_NAME:
            case WRAPPER_INT64_TYPE_NAME:
            case WRAPPER_UINT64_TYPE_NAME:
            case WRAPPER_INT32_TYPE_NAME:
            case WRAPPER_UINT32_TYPE_NAME:
            case WRAPPER_BOOL_TYPE_NAME:
            case WRAPPER_STRING_TYPE_NAME:
            case WRAPPER_BYTES_TYPE_NAME: {
                return WrappersProto.getDescriptor().findMessageTypeByName(messageName);
            }
            case ANY_TYPE_NAME: {
                return AnyProto.getDescriptor().findMessageTypeByName(messageName);
            }
            case EMPTY_TYPE_NAME: {
                return EmptyProto.getDescriptor().findMessageTypeByName(messageName);
            }
            case TIMESTAMP_TYPE_NAME: {
                return TimestampProto.getDescriptor().findMessageTypeByName(messageName);
            }
            case DURATION_TYPE_NAME: {
                return DurationProto.getDescriptor().findMessageTypeByName(messageName);
            }
            case STRUCT_TYPE_NAME: {
                return StructProto.getDescriptor().findMessageTypeByName(messageName);
            }
            default: {
                return null;
            }
        }
    }
}
