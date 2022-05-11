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
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.types.AnydataType;
import io.ballerina.runtime.api.types.ArrayType;
import io.ballerina.runtime.api.types.MapType;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.TupleType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;
import java.util.Arrays;

/**
 * The deserializer class, that deserializes messages.
 */
@SuppressWarnings("unchecked")
public class MessageDeserializer extends AbstractDeserializer {

    public MessageDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                               Type type, BMessage bMessage) {

        super(input, fieldDescriptor, type, bMessage);
    }

    @Override
    public void deserialize() throws IOException, Descriptors.DescriptorValidationException {

        RecordType recordType;
        if (type instanceof RecordType) {
            recordType = (RecordType) type;
        } else if (type instanceof MapType || type instanceof TupleType ||
                type instanceof AnydataType ||
                type.getTag() == TypeCreator.createArrayType(PredefinedTypes.TYPE_ANYDATA).getTag()) {
            recordType = null;
        } else {
            throw new IOException("Error while decoding request " +
                    "message. record type is not supported : " +
                    fieldDescriptor.getType());
        }
        BString bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS) ||
                    fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCTVALUE_VALUES)) {
                DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                        TypeCreator.createTupleType(Arrays.asList(PredefinedTypes.TYPE_STRING,
                                PredefinedTypes.TYPE_ANYDATA)), input);
                deserializeHandler.deserialize();
                BArray tupleval = (BArray) (deserializeHandler.getBMessage());
                bMap.put(tupleval.getBString(0), tupleval.get(1));
            } else if (fieldDescriptor.isRepeated() && recordType != null) {
                BArray valueArray = bMap.get(bFieldName) != null ?
                        (BArray) bMap.get(bFieldName) : null;
                Type fieldType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                if (valueArray == null || valueArray.size() == 0) {
                    valueArray = ValueCreator.createArrayValue((ArrayType) fieldType);
                    bMap.put(bFieldName, valueArray);
                }
                DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                        ((ArrayType) fieldType).getElementType(), input);
                deserializeHandler.deserialize();
                valueArray.add(valueArray.size(), deserializeHandler.getBMessage());
            } else if (fieldDescriptor.getContainingOneof() != null && recordType != null) {
                Type fieldType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor, fieldType, input);
                deserializeHandler.deserialize();
                Object bValue = deserializeHandler.getBMessage();
                bMap.put(StringUtils.fromString(fieldDescriptor.getName()), bValue);
            } else if (fieldDescriptor.getMessageType().getFullName().equals(GOOGLE_PROTOBUF_STRUCT) &&
                    recordType != null) {
                Type fieldType = TypeCreator.createMapType(PredefinedTypes.TYPE_ANYDATA);
                DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor, fieldType, input);
                deserializeHandler.deserialize();
                bMap.put(bFieldName, deserializeHandler.getBMessage());
            } else if (recordType != null) {
                Type fieldType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor, fieldType, input);
                deserializeHandler.deserialize();
                bMap.put(bFieldName, deserializeHandler.getBMessage());
            }
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDSENTRY_VALUE)) {
            BArray bArray = (BArray) bMessage.getContent();
            DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                    PredefinedTypes.TYPE_ANYDATA, input);
            deserializeHandler.deserialize();
            bArray.add(1, deserializeHandler.getBMessage());
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_LIST_VALUE)) {
            DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                    TypeCreator.createArrayType(PredefinedTypes.TYPE_ANYDATA), input);
            deserializeHandler.deserialize();
            bMessage.setContent(deserializeHandler.getBMessage());
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_LISTVALUE_VALUES)) {
            BArray bArray = (BArray) bMessage.getContent();
            DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                    PredefinedTypes.TYPE_ANYDATA, input);
            deserializeHandler.deserialize();
            bArray.add(bArray.size(), deserializeHandler.getBMessage());
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE)) {
            DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor,
                    TypeCreator.createMapType(PredefinedTypes.TYPE_ANYDATA), input);
            deserializeHandler.deserialize();
            bMessage.setContent(deserializeHandler.getBMessage());
        } else if (recordType != null) {
            Type fieldType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
            DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor, fieldType, input);
            deserializeHandler.deserialize();
            bMessage.setContent(deserializeHandler.getBMessage());
        }
    }
}
