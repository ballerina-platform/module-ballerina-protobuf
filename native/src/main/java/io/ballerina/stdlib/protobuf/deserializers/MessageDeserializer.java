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
                               Type type, BMessage bMessage, Type targetType) {

        super(input, fieldDescriptor, bMessage, targetType, type);
    }

    @Override
    public void deserialize() throws IOException, Descriptors.DescriptorValidationException {

        RecordType recordType;
        if (messageType instanceof RecordType) {
            recordType = (RecordType) messageType;
        } else if (messageType instanceof MapType || messageType instanceof TupleType ||
                messageType instanceof AnydataType ||
                messageType.getTag() == TypeCreator.createArrayType(PredefinedTypes.TYPE_ANYDATA).getTag()) {
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
                Type messageType = TypeCreator.createTupleType(Arrays.asList(PredefinedTypes.TYPE_STRING,
                        PredefinedTypes.TYPE_ANYDATA));
                BArray tupleVal = (BArray) (readMessage(messageType));
                bMap.put(tupleVal.getBString(0), tupleVal.get(1));
                bMessage.setContent(bMap);
            } else if (fieldDescriptor.isRepeated() && recordType != null) {
                BArray valueArray = bMap.get(bFieldName) != null ?
                        (BArray) bMap.get(bFieldName) : null;
                Type fieldType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                if (valueArray == null || valueArray.size() == 0) {
                    valueArray = ValueCreator.createArrayValue((ArrayType) fieldType);
                    bMap.put(bFieldName, valueArray);
                }
                valueArray.add(valueArray.size(), readMessage(((ArrayType) fieldType).getElementType()));
                bMessage.setContent(bMap);
            } else if (fieldDescriptor.getContainingOneof() != null && recordType != null) {
                Type messageType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                Object bValue = readMessage(messageType);
                bMap.put(StringUtils.fromString(fieldDescriptor.getName()), bValue);
                bMessage.setContent(bMap);
            } else if (fieldDescriptor.getMessageType().getFullName().equals(GOOGLE_PROTOBUF_STRUCT) &&
                    recordType != null) {
                Type messageType = TypeCreator.createMapType(PredefinedTypes.TYPE_ANYDATA);
                bMap.put(bFieldName, readMessage(messageType));
                bMessage.setContent(bMap);
            } else if (recordType != null) {
                Type messageType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
                bMap.put(bFieldName, readMessage(messageType));
            }
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDSENTRY_VALUE)) {
            BArray bArray = (BArray) bMessage.getContent();
            bArray.add(1, readMessage(PredefinedTypes.TYPE_ANYDATA));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_LIST_VALUE)) {
            bMessage.setContent(readMessage(PredefinedTypes.TYPE_ANYDATA));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_LISTVALUE_VALUES)) {
            BArray bArray = (BArray) bMessage.getContent();
            bArray.add(bArray.size(), readMessage(PredefinedTypes.TYPE_ANYDATA));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE)) {
            bMessage.setContent(readMessage(PredefinedTypes.TYPE_ANYDATA));
        } else if (recordType != null) {
            Type messageType = recordType.getFields().get(bFieldName.getValue()).getFieldType();
            bMessage.setContent(readMessage(messageType));
        }
    }

    private Object readMessage(Type messageType) throws IOException, Descriptors.DescriptorValidationException {

        int length = input.readRawVarint32();
        int limit = input.pushLimit(length);
        DeserializeHandler deserializeHandler = new DeserializeHandler(fieldDescriptor, input, targetType, messageType);
        deserializeHandler.deserialize();
        input.popLimit(limit);
        return deserializeHandler.getBMessage();
    }
}
