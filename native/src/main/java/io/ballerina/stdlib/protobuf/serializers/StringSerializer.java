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
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.BALLERINA_TYPE_URL_ENTRY;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_ANY_MESSAGE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_ANY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY;

/**
 * The serializer class, that serializes the string values.
 */
@SuppressWarnings("unchecked")
public class StringSerializer extends AbstractSerializer {

    public StringSerializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
                            BMessage bMessage) {

        super(output, fieldDescriptor, bMessage);
    }

    @Override
    public void computeMessageSize() {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (bValue instanceof BArray) {
                    BArray valueArray = (BArray) bValue;
                    for (int i = 0; i < valueArray.size(); i++) {
                        bMessage.incrementSize(computeMessageSize(valueArray.getBString(i).getValue()));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize(((BString) bValue).getValue()));
                }
            }
        } else if (isBArray() && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY)) {
            bMessage.incrementSize(computeMessageSize(((BArray) bMessage.getContent()).getBString(0).getValue()));
        } else if (bMessage.getContent() instanceof BString) {
            bMessage.incrementSize(computeMessageSize(((BString) bMessage.getContent()).getValue()));
        } else if (GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
            bMessage.incrementSize(computeMessageSize(((BMap) bMessage.getContent()).getStringValue(
                    StringUtils.fromString(BALLERINA_TYPE_URL_ENTRY)).getValue()));
        }
    }

    @Override
    public void serialize() throws IOException {

        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (bMap.containsKey(bFieldName)) {
                Object bValue = bMap.get(bFieldName);
                if (bValue instanceof BArray) {
                    BArray valueArray = (BArray) bValue;
                    for (int i = 0; i < valueArray.size(); i++) {
                        writeContent(valueArray.getBString(i).getValue());
                    }
                } else {
                    writeContent(((BString) bValue).getValue());
                }
            }
        } else if (isBArray() && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY)) {
            BArray bArray = (BArray) bMessage.getContent();
            writeContent(((BString) bArray.get(0)).getValue());
        } else if (bMessage.getContent() instanceof BString &&
                !fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_ANY_TYPE_URL)) {
            writeContent(((BString) bMessage.getContent()).getValue());
        } else if (GOOGLE_PROTOBUF_ANY_MESSAGE_NAME.equals(this.messageName)) {
            writeContent(((BMap) bMessage.getContent()).getStringValue(
                    StringUtils.fromString(BALLERINA_TYPE_URL_ENTRY)).getValue());
        }
    }

    private int computeMessageSize(String value) {

        return com.google.protobuf.CodedOutputStream.computeStringSize(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(String content) throws IOException {

        output.writeString(fieldDescriptor.getNumber(), content);
    }
}
