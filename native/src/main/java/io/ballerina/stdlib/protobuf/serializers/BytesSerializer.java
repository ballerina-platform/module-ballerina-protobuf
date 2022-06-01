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

import com.google.protobuf.Descriptors;
import com.google.protobuf.WireFormat;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.BALLERINA_ANY_VALUE_ENTRY;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.BALLERINA_TYPE_URL_ENTRY;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_ANY_MESSAGE_NAME;
import static io.ballerina.stdlib.protobuf.utils.DescriptorBuilder.findFieldDescriptorFromTypeUrl;

/**
 * The serializer class, that serializes the bytes.
 */
@SuppressWarnings("unchecked")
public class BytesSerializer extends AbstractSerializer {

    public BytesSerializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
                           BMessage bMessage) {

        super(output, fieldDescriptor, bMessage);
    }

    @Override
    public void computeMessageSize() throws Descriptors.DescriptorValidationException, IOException,
            AnnotationUnavailableException {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (bValue instanceof BArray && !GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
                    BArray valueArray = (BArray) bValue;
                    bMessage.incrementSize(computeMessageSize(valueArray.getBytes()));
                } else if (GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
                    String typeUrl = ((BMap<BString, Object>) this.bMessage.getContent())
                            .getStringValue(StringUtils.fromString(BALLERINA_TYPE_URL_ENTRY)).getValue();
                    String typeName = getTypeNameFromTypeUrl(typeUrl);
                    Object value = ((BMap<BString, Object>) this.bMessage.getContent())
                            .get(StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY));
                    Descriptors.Descriptor descriptor = findFieldDescriptorFromTypeUrl(value, typeName);
                    SerializeHandler serializeHandler = new SerializeHandler(output, descriptor, value);
                    bMessage.incrementSize(serializeHandler.getSize());
                }
            }
        } else if (isBArray()) {
            BArray valueArray = (BArray) bMessage.getContent();
            bMessage.incrementSize(computeMessageSize(valueArray.getBytes()));
        }
    }

    @Override
    public void serialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (bValue instanceof BArray && !GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
                    BArray valueArray = (BArray) bValue;
                    output.writeByteArray(fieldDescriptor.getNumber(), valueArray.getBytes());
                } else if (GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
                    String typeUrl = ((BMap<BString, Object>) this.bMessage.getContent()).getStringValue(
                            StringUtils.fromString(BALLERINA_TYPE_URL_ENTRY)).getValue();
                    String typeName = getTypeNameFromTypeUrl(typeUrl);
                    Object value = ((BMap<BString, Object>) this.bMessage.getContent()).get(
                            StringUtils.fromString(BALLERINA_ANY_VALUE_ENTRY));
                    Descriptors.Descriptor descriptor = findFieldDescriptorFromTypeUrl(value, typeName);
                    BMessage intermediateMessage = new BMessage(value);
                    MessageSerializer messageSerializer = new MessageSerializer(descriptor.findFieldByName(typeUrl),
                            intermediateMessage);
                    messageSerializer.computeMessageSize();
                    output.writeTag(fieldDescriptor.getNumber(), WireFormat.WIRETYPE_LENGTH_DELIMITED);
                    output.writeUInt32NoTag(messageSerializer.getSize());
                    messageSerializer.serialize();
                }
            }
        } else if (isBArray()) {
            BArray valueArray = (BArray) bMessage.getContent();
            writeContent(valueArray.getBytes());
        }
    }

    private String getTypeNameFromTypeUrl(String typeUrl) {

        String[] types = typeUrl.split("/");
        return types[types.length - 1].trim();
    }

    private int computeMessageSize(byte[] value) {

        return com.google.protobuf.CodedOutputStream
                .computeByteArraySize(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(byte[] content) throws IOException {

        output.writeByteArray(fieldDescriptor.getNumber(), content);
    }
}
