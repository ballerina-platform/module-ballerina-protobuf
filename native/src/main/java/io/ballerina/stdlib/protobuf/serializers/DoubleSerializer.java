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
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

/**
 * The serializer class, that serializes the double values.
 */
@SuppressWarnings("unchecked")
public class DoubleSerializer extends AbstractSerializer {

    public DoubleSerializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
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
                        bMessage.incrementSize(computeMessageSize(valueArray.getFloat(i)));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize((double) bValue));
                }
            }
        } else if (bMessage.getContent() instanceof Integer) {
            bMessage.incrementSize(computeMessageSize((int) bMessage.getContent()));
        } else if (bMessage.getContent() instanceof Long) {
            bMessage.incrementSize(computeMessageSize((long) bMessage.getContent()));
        } else if (bMessage.getContent() instanceof Double) {
            bMessage.incrementSize(computeMessageSize((double) bMessage.getContent()));
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
                        writeContent(valueArray.getFloat(i));
                    }
                } else {
                    writeContent((Double) bValue);
                }
            }
        } else if (bMessage.getContent() instanceof Double) {
            writeContent((Double) bMessage.getContent());
        } else if (bMessage.getContent() instanceof Integer) {
            writeContent((Integer) bMessage.getContent());
        } else if (bMessage.getContent() instanceof Long) {
            writeContent((Long) bMessage.getContent());
        }
    }

    private int computeMessageSize(double value) {

        return com.google.protobuf.CodedOutputStream.computeDoubleSize(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(double content) throws IOException {

        output.writeDouble(fieldDescriptor.getNumber(), content);
    }
}
