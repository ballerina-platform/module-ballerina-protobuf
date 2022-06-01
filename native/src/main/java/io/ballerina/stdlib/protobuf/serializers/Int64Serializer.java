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
import io.ballerina.runtime.api.values.BDecimal;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_DURATION_SECONDS;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_TIMESTAMP_SECONDS;

/**
 * The serializer class, that serializes the int64 values.
 */
@SuppressWarnings("unchecked")
public class Int64Serializer extends AbstractSerializer {

    public Int64Serializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
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
                        bMessage.incrementSize(computeMessageSize(valueArray.getInt(i)));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize((long) bValue));
                }
            }
        } else if (bMessage.getContent() instanceof Long) {
            bMessage.incrementSize(computeMessageSize((long) bMessage.getContent()));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_TIMESTAMP_SECONDS) && isBArray()) {
            BArray array = (BArray) bMessage.getContent();
            bMessage.incrementSize(computeMessageSize(array.getInt(0)));
        } else if (bMessage.getContent() instanceof BDecimal
                && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_DURATION_SECONDS)) {
            bMessage.incrementSize(computeMessageSize(((BDecimal) bMessage.getContent()).value().intValue()));
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
                        writeContent(valueArray.getInt(i));
                    }
                } else {
                    writeContent((long) bValue);
                }
            }
        } else if (isBArray() && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_TIMESTAMP_SECONDS)) {
            BArray bArray = (BArray) bMessage.getContent();
            writeContent((long) (bArray.get(0)));
        } else if (bMessage.getContent() instanceof BDecimal
                && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_DURATION_SECONDS)) {
            writeContent(((BDecimal) bMessage.getContent()).value().intValue());
        } else if (bMessage.getContent() instanceof Long) {
            writeContent((long) bMessage.getContent());
        }
    }

    private int computeMessageSize(long value) {

        return com.google.protobuf.CodedOutputStream.computeInt64Size(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(long content) throws IOException {

        output.writeInt64(fieldDescriptor.getNumber(), content);
    }
}
