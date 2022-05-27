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
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_ANY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY;

/**
 * The deserializer class, that deserializes the strings.
 */
@SuppressWarnings("unchecked")
public class StringDeserializer extends AbstractDeserializer {

    public StringDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                              BMessage bMessage, Type targetType) {

        super(input, fieldDescriptor, bMessage, targetType);
    }

    @Override
    public void deserialize() throws IOException {

        BString bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        if (isBMap()) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) bMessage.getContent();
            if (fieldDescriptor.isRepeated()) {
                BArray stringArray = (BArray) bMap.get(bFieldName);
                stringArray.add(stringArray.size(), readContent());
            } else if (fieldDescriptor.getContainingOneof() != null) {
                bMap.put(StringUtils.fromString(fieldDescriptor.getName()), readContent());
            } else {
                bMap.put(bFieldName, readContent());
            }
        } else if (isBArray() && GOOGLE_PROTOBUF_STRUCT_FIELDS_ENTRY_KEY.equals(fieldDescriptor.getFullName())) {
            BArray bArray = (BArray) bMessage.getContent();
            bArray.add(0, readContent());
        } else if (!GOOGLE_PROTOBUF_ANY_TYPE_URL.equals(fieldDescriptor.getFullName())) {
            bMessage.setContent(readContent());
        }
    }

    private BString readContent() throws IOException {

        return StringUtils.fromString(input.readStringRequireUtf8());
    }
}
