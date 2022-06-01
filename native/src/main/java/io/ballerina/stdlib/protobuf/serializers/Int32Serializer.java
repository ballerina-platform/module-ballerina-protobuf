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
import java.math.BigDecimal;
import java.math.MathContext;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANALOG_GIGA;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_DURATION_NANOS;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_TIMESTAMP_NANOS;

/**
 * The serializer class, that serializes the int32 values.
 */
@SuppressWarnings("unchecked")
public class Int32Serializer extends AbstractSerializer {

    public Int32Serializer(com.google.protobuf.CodedOutputStream output, Descriptors.FieldDescriptor fieldDescriptor,
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
                        bMessage.incrementSize(computeMessageSize(getIntValue(valueArray.getInt(i))));
                    }
                } else {
                    bMessage.incrementSize(computeMessageSize(getIntValue(bValue)));
                }
            }
        } else if (bMessage.getContent() instanceof Long) {
            bMessage.incrementSize(computeMessageSize(getIntValue(bMessage.getContent())));
        } else if (fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_TIMESTAMP_NANOS) && isBArray()) {
            BArray array = (BArray) bMessage.getContent();
            BigDecimal nanos = new BigDecimal(array.get(1)
                    .toString()).multiply(ANALOG_GIGA, MathContext.DECIMAL128);
            bMessage.incrementSize(computeMessageSize(nanos.intValue()));
        } else if (bMessage.getContent() instanceof BDecimal
                && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_DURATION_NANOS)) {
            int intVal = ((BDecimal) bMessage.getContent()).value().intValue();
            BigDecimal b = ((BDecimal) bMessage.getContent()).value().subtract(new BigDecimal(intVal))
                    .multiply(ANALOG_GIGA, MathContext.DECIMAL128);
            bMessage.incrementSize(computeMessageSize(b.intValue()));
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
                        writeContent(getIntValue(valueArray.getInt(i)));
                    }
                } else {
                    writeContent(getIntValue(bValue));
                }
            }
        } else if (isBArray() && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_TIMESTAMP_NANOS)) {
            BArray bArray = (BArray) bMessage.getContent();
            BigDecimal nanos = new BigDecimal((bArray).get(1).toString()).multiply(ANALOG_GIGA);
            writeContent(nanos.intValue());
        } else if (bMessage.getContent() instanceof BDecimal
                && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_DURATION_NANOS)) {
            int intVal = ((BDecimal) bMessage.getContent()).value().intValue();
            BigDecimal b = ((BDecimal) bMessage.getContent()).value().subtract(new BigDecimal(intVal));
            writeContent(b.multiply(ANALOG_GIGA).intValue());
        } else if (bMessage.getContent() instanceof Long) {
            writeContent(getIntValue(bMessage.getContent()));
        }
    }

    private int computeMessageSize(int value) {

        return com.google.protobuf.CodedOutputStream.computeInt32Size(fieldDescriptor.getNumber(), value);
    }

    private void writeContent(int content) throws IOException {

        output.writeInt32(fieldDescriptor.getNumber(), content);
    }
}
