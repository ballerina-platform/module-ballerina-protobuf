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

package io.ballerina.stdlib.protobuf.serializers;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.WireFormat;
import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;
import java.util.Map;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_LIST_VALUE_VALUES;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_STRUCT_FIELDS;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_VALUE;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_VALUE_LIST_VALUE;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE;

/**
 * The serializer class, that serializes the messages.
 */
@SuppressWarnings("unchecked")
public class MessageSerializer extends AbstractSerializer {

    public MessageSerializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
                             BMessage bMessage) {

        super(output, fieldDescriptor, bMessage);
    }

    public MessageSerializer(Descriptors.FieldDescriptor fieldDescriptor, BMessage bMessage) {

        super(fieldDescriptor, bMessage);
    }

    @Override
    public void computeMessageSize() throws Descriptors.DescriptorValidationException, IOException,
            AnnotationUnavailableException {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (fieldDescriptor.isRepeated() && bValue instanceof BArray) {
                    BArray valueArray = (BArray) bValue;
                    for (int i = 0; i < valueArray.size(); i++) {
                        Object value = valueArray.getRefValue(i);
                        bMessage.incrementSize(computeMessageSize(fieldDescriptor, value));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize(fieldDescriptor, bValue));
                }
            } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS)) {
                for (Map.Entry<BString, Object> entry : bMap.entrySet()) {
                    BArray valueArray = ValueCreator.createArrayValue(TypeCreator
                            .createArrayType(PredefinedTypes.TYPE_ANY), 2);
                    valueArray.add(0, (Object) entry.getKey());
                    valueArray.add(1, entry.getValue());
                    bMessage.incrementSize(computeMessageSize(fieldDescriptor, valueArray));
                }
            } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE)) {
                bMessage.incrementSize(computeMessageSize(fieldDescriptor, bMap));
            }
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_VALUE)) {
            bMessage.incrementSize(computeMessageSize(fieldDescriptor, ((BArray) bMessage.getContent()).get(1)));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_LIST_VALUE) && isBArray()) {
            bMessage.incrementSize(computeMessageSize(fieldDescriptor, bMessage.getContent()));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_LIST_VALUE_VALUES) && isBArray()) {
            BArray bArray = (BArray) bMessage.getContent();
            for (int i = 0; i < bArray.size(); i++) {
                bMessage.incrementSize(computeMessageSize(fieldDescriptor, bArray.get(i)));
            }
        }
    }

    @Override
    public void serialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (fieldDescriptor.isRepeated() && bValue instanceof BArray) {
                    BArray valueArray = (BArray) bValue;
                    for (int i = 0; i < valueArray.size(); i++) {
                        writeContent(valueArray.getRefValue(i));
                    }
                } else {
                    writeContent(bValue);
                }
            } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS)) {
                for (Map.Entry<BString, Object> entry : bMap.entrySet()) {
                    BArray valueArray = ValueCreator.createArrayValue(TypeCreator
                            .createArrayType(PredefinedTypes.TYPE_ANY), 2);
                    valueArray.add(0, (Object) entry.getKey());
                    valueArray.add(1, entry.getValue());
                    writeContent(valueArray);
                }
            } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_STRUCT_VALUE)) {
                writeContent(bMap);
            }
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_VALUE) && isBArray()) {
            BArray bArray = (BArray) bMessage.getContent();
            writeContent(bArray.get(1));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_VALUE_LIST_VALUE) && isBArray()) {
            BArray bArray = (BArray) bMessage.getContent();
            writeContent(bArray);
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_LIST_VALUE_VALUES) && isBArray()) {
            BArray bArray = (BArray) bMessage.getContent();
            for (int i = 0; i < bArray.size(); i++) {
                writeContent(bArray.get(i));
            }
        }
    }

    private int computeMessageSize(Descriptors.FieldDescriptor fieldDescriptor, Object content)
            throws Descriptors.DescriptorValidationException, IOException, AnnotationUnavailableException {

        SerializeHandler serializeHandler = new SerializeHandler(fieldDescriptor.getMessageType(), content);
        return CodedOutputStream.computeTagSize(fieldDescriptor.getNumber()) +
                CodedOutputStream.computeUInt32SizeNoTag(serializeHandler.getSize()) + serializeHandler.getSize();
    }

    private void writeContent(Object content) throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        SerializeHandler serializeHandler = new SerializeHandler(output, fieldDescriptor.getMessageType(), content);
        output.writeTag(fieldDescriptor.getNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
        output.writeUInt32NoTag(serializeHandler.getSize());
        serializeHandler.serialize();
    }
}
