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
 * The serializer class, that serializes the float values.
 */
@SuppressWarnings("unchecked")
public class FloatSerializer extends AbstractSerializer {

    public FloatSerializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
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
                        bMessage.incrementSize(
                                computeMessageSize(Float.parseFloat(String.valueOf(valueArray.getFloat(i)))));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize(Float.parseFloat(String.valueOf(bValue))));
                }
            }
        } else if (bMessage.getContent() instanceof Double) {
            bMessage.incrementSize(computeMessageSize(Float.parseFloat(String.valueOf(bMessage.getContent()))));
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
                        writeContent(Float.parseFloat(String.valueOf(valueArray.getFloat(i))));
                    }
                } else {
                    writeContent(Float.parseFloat(String.valueOf(bValue)));
                }
            }
        } else if (bMessage.getContent() instanceof Double) {
            writeContent(Float.parseFloat(String.valueOf(bMessage.getContent())));
        }
    }

    private int computeMessageSize(float value) {

        return com.google.protobuf.CodedOutputStream.computeFloatSize(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(float content) throws IOException {

        output.writeFloat(fieldDescriptor.getNumber(), content);
    }
}
