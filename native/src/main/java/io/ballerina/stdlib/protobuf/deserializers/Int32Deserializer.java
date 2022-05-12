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
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BDecimal;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * The deserializer class, that deserializes the integer 32 data.
 */
@SuppressWarnings("unchecked")
public class Int32Deserializer extends AbstractDeserializer {

    public Int32Deserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                             BMessage bMessage, Type targetType, boolean isPacked) {

        super(input, fieldDescriptor, bMessage, targetType, isPacked);
    }

    @Override
    public void deserialize() throws IOException {

        BString bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (fieldDescriptor.isRepeated()) {
                BArray int32Array = ValueCreator.createArrayValue(INT32_ARRAY_TYPE);
                if (bMap.containsKey(bFieldName)) {
                    int32Array = (BArray) bMap.get(bFieldName);
                } else {
                    bMap.put(bFieldName, int32Array);
                }
                if (isPacked) {
                    while (input.getBytesUntilLimit() > 0) {
                        int32Array.add(int32Array.size(), readContent());
                    }
                } else {
                    int32Array.add(int32Array.size(), readContent());
                }
            } else if (fieldDescriptor.getContainingOneof() != null) {
                bMap.put(StringUtils.fromString(fieldDescriptor.getName()), readContent());
            } else {
                bMap.put(bFieldName, readContent());
            }
        } else if (isBArray() && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_TIMESTAMP_NANOS)) {
            BArray bArray = (BArray) bMessage.getContent();
            BigDecimal nanos = new BigDecimal(readContent())
                    .divide(ANALOG_GIGA, MathContext.DECIMAL128);
            bArray.add(1, ValueCreator.createDecimalValue(nanos));
        } else if (bMessage instanceof BDecimal
                && fieldDescriptor.getFullName().equals(GOOGLE_PROTOBUF_DURATION_NANOS)) {
            BigDecimal nanos = new BigDecimal(readContent())
                    .divide(ANALOG_GIGA, MathContext.DECIMAL128);
            BigDecimal secondsValue = ((BDecimal) bMessage.getContent()).value();
            bMessage.setContent(ValueCreator.createDecimalValue(secondsValue.add(nanos)));
        } else {
            bMessage.setContent(readContent());
        }
    }

    private long readContent() throws IOException {

        return input.readInt32();
    }
}
