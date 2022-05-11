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
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;

import java.io.IOException;

/**
 * The deserializer class, that deserializes the enumerations.
 */
@SuppressWarnings("unchecked")
public class EnumDeserializer extends AbstractDeserializer {

    public EnumDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                            Object bMessage) {

        super(input, fieldDescriptor, bMessage);
    }

    @Override
    public void deserialize() throws IOException {

        BString bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage;
            if (fieldDescriptor.isRepeated()) {
                BArray stringArray = ValueCreator.createArrayValue(STRING_ARRAY_TYPE);
                if (bMap.containsKey(bFieldName)) {
                    stringArray = (BArray) bMap.get(bFieldName);
                } else {
                    bMap.put(bFieldName, stringArray);
                }
                stringArray.add(stringArray.size(), readContent());
                bMap.put(bFieldName, stringArray);
            } else if (fieldDescriptor.getContainingOneof() != null) {
                bMap.put(StringUtils.fromString(fieldDescriptor.getName()), readContent());
            } else {
                bMap.put(bFieldName, readContent());
            }
        } else {
            bMessage = readContent();
        }
    }

    private BString readContent() throws IOException {

        return StringUtils.fromString(fieldDescriptor.getEnumType().findValueByNumber(input.readEnum()).toString());
    }
}
