/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package io.ballerina.stdlib.protobuf.deserializers;

import com.google.protobuf.Descriptors;
import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.types.ArrayType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Abstract deserializer class to deserialize the Protobuf messages.
 */
public abstract class AbstractDeserializer {

    com.google.protobuf.CodedInputStream input;
    Descriptors.FieldDescriptor fieldDescriptor;
    BMessage bMessage;
    Type messageType;
    Type targetType;
    boolean isPacked = false;

    protected static final String GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY = "google.protobuf.Struct.FieldsEntry.key";
    protected static final String GOOGLE_PROTOBUF_ANY_TYPE_URL = "google.protobuf.Any.type_url";
    protected static final String GOOGLE_PROTOBUF_TIMESTAMP_SECONDS = "google.protobuf.Timestamp.seconds";
    protected static final String GOOGLE_PROTOBUF_TIMESTAMP_NANOS = "google.protobuf.Timestamp.nanos";
    protected static final String GOOGLE_PROTOBUF_DURATION_SECONDS = "google.protobuf.Duration.seconds";
    protected static final String GOOGLE_PROTOBUF_DURATION_NANOS = "google.protobuf.Duration.nanos";
    protected static final String GOOGLE_PROTOBUF_STRUCT = "google.protobuf.Struct";
    protected static final String GOOGLE_PROTOBUF_STRUCT_FIELDS = "google.protobuf.Struct.fields";
    protected static final String GOOGLE_PROTOBUF_STRUCT_FIELDSENTRY_VALUE = "google.protobuf.Struct.FieldsEntry.value";
    protected static final String GOOGLE_PROTOBUF_VALUE_LIST_VALUE = "google.protobuf.Value.list_value";
    protected static final String GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE = "google.protobuf.Value.struct_value";
    protected static final String GOOGLE_PROTOBUF_LISTVALUE_VALUES = "google.protobuf.ListValue.values";
    protected static final String GOOGLE_PROTOBUF_STRUCTVALUE_VALUES = "google.protobuf.StructValue.values";
    protected static final BigDecimal ANALOG_GIGA = new BigDecimal(1000000000);

    protected static final ArrayType STRING_ARRAY_TYPE = TypeCreator.createArrayType(PredefinedTypes.TYPE_STRING);
    protected static final ArrayType BOOLEAN_ARRAY_TYPE = TypeCreator.createArrayType(PredefinedTypes.TYPE_BOOLEAN);
    protected static final ArrayType INT_ARRAY_TYPE = TypeCreator.createArrayType(PredefinedTypes.TYPE_INT);
    protected static final ArrayType INT32_ARRAY_TYPE = TypeCreator.createArrayType(
            PredefinedTypes.TYPE_INT_UNSIGNED_32);
    protected static final ArrayType FLOAT_ARRAY_TYPE = TypeCreator.createArrayType(PredefinedTypes.TYPE_FLOAT);

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.targetType = targetType;
    }

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType, boolean isPacked) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.isPacked = isPacked;
        this.targetType = targetType;
    }

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType, Type messageType) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.messageType = messageType;
        this.targetType = targetType;
    }

    public boolean isBMap() {

        return bMessage.getContent() instanceof BMap;
    }

    public boolean isBArray() {

        return bMessage.getContent() instanceof BArray;
    }

    public abstract void deserialize() throws IOException, Descriptors.DescriptorValidationException;
}
