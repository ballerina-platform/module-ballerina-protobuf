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

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import io.ballerina.runtime.api.PredefinedTypes;
import io.ballerina.runtime.api.TypeTags;
import io.ballerina.runtime.api.creators.TypeCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.types.TupleType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.ballerina.stdlib.protobuf.utils.DescriptorBuilder.computeFieldTagValues;
import static io.ballerina.stdlib.protobuf.utils.DescriptorBuilder.getCodedInputStream;
import static io.ballerina.stdlib.protobuf.utils.DescriptorBuilder.getMessageDescriptor;
import static io.ballerina.stdlib.protobuf.utils.Utils.bytesToHex;

/**
 * The deserialize handler class.
 */
public class DeserializeHandler {

    private final Descriptors.Descriptor messageDescriptor;
    private final CodedInputStream input;
    private final BMessage bMessage;
    private final Map<Integer, Descriptors.FieldDescriptor> fieldDescriptors;
    private final Type messageType;
    private final Type targetType;

    public DeserializeHandler(String descriptor, String hexInput, Type targetType, Type messageType)
            throws Descriptors.DescriptorValidationException, IOException {

        String targetTypeName = messageType.getName();
        this.input = getCodedInputStream(hexInput);
        this.messageDescriptor = getMessageDescriptor(descriptor, targetTypeName);
        this.fieldDescriptors = computeFieldTagValues(messageDescriptor);
        this.messageType = messageType;
        this.targetType = targetType;
        this.bMessage = new BMessage(this.initBMessage(input, fieldDescriptors, messageType, targetTypeName));
    }

    public DeserializeHandler(Descriptors.FieldDescriptor fieldDescriptor, CodedInputStream input, Type targetType,
                              Type messageType)
            throws IOException, Descriptors.DescriptorValidationException, AnnotationUnavailableException {

        this.input = input;
        this.messageDescriptor = getMessageDescriptor(fieldDescriptor, messageType);
        this.fieldDescriptors = computeFieldTagValues(messageDescriptor);
        this.messageType = messageType;
        this.targetType = targetType;
        this.bMessage = new BMessage(this.initBMessage(input, fieldDescriptors, messageType,
                messageDescriptor.getFullName()));
    }

    public DeserializeHandler(Descriptors.Descriptor messageDescriptor, String hexInput, Type targetType,
                              Type messageType)
            throws IOException {

        this.input = getCodedInputStream(hexInput);
        this.messageDescriptor = messageDescriptor;
        this.fieldDescriptors = computeFieldTagValues(messageDescriptor);
        this.messageType = messageType;
        this.targetType = targetType;
        this.bMessage = new BMessage(this.initBMessage(input, fieldDescriptors, messageType,
                messageDescriptor.getFullName()));
    }

    public Object getBMessage() {

        return bMessage.getContent();
    }

    public void deserialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        while (!(input.isAtEnd())) {
            int tag;
            try {
                tag = input.readTag();
            } catch (InvalidProtocolBufferException e) {
                tag = input.getLastTag();
            }

            Descriptors.FieldDescriptor fieldDescriptor = fieldDescriptors.get(tag);

            if (fieldDescriptor == null) {
                continue;
            }

            boolean isPacked = isPacked(fieldDescriptor, tag);
            int limit = 0;
            if (isPacked) {
                limit = pushLimit();
            }

            switch (fieldDescriptor.getType().toProto().getNumber()) {
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE_VALUE: {
                    new DoubleDeserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT_VALUE: {
                    new FloatDeserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64_VALUE: {
                    new Int64Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64_VALUE: {
                    new UInt64Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32_VALUE: {
                    new Int32Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32_VALUE: {
                    new UInt32Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64_VALUE: {
                    new Fixed64Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32_VALUE: {
                    new Fixed32Deserializer(input, fieldDescriptor, bMessage, targetType, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL_VALUE: {
                    new BooleanDeserializer(input, fieldDescriptor, bMessage, targetType).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING_VALUE: {
                    new StringDeserializer(input, fieldDescriptor, bMessage, targetType).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM_VALUE: {
                    new EnumDeserializer(input, fieldDescriptor, bMessage, targetType).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES_VALUE: {
                    new BytesDeserializer(input, fieldDescriptor, bMessage, targetType).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE_VALUE: {
                    new MessageDeserializer(input, fieldDescriptor, messageType, bMessage, targetType)
                            .deserialize();
                    break;
                }
                default: {

                }
            }
            if (isPacked) {
                popLimit(limit);
            }
        }
    }

    private boolean isPacked(Descriptors.FieldDescriptor fieldDescriptor, int tag) {

        if (fieldDescriptor.isRepeated() && fieldDescriptor.isPacked()) {
            int byteCode = (fieldDescriptor.getNumber() << 3) + 2;
            return byteCode == tag;
        }
        return false;
    }

    private int pushLimit() throws IOException {

        int length = input.readRawVarint32();
        return input.pushLimit(length);
    }

    private void popLimit(int limit) {

        input.popLimit(limit);
    }

    Object initBMessage(com.google.protobuf.CodedInputStream input, Map<Integer,
            Descriptors.FieldDescriptor> fieldDescriptors, Type type, String messageName) throws IOException {

        BMap<BString, Object> bBMap = null;
        BArray bArray;
        Object bMessage = null;
        if (type != null) {
            boolean isAnyTypedMessage = "google.protobuf.Any".equals(messageName) &&
                    fieldDescriptors.values().stream().allMatch(fd -> fd.getFullName().contains("google.protobuf.Any"));
            boolean isTimestampMessage = (type.getTag() == TypeTags.INTERSECTION_TAG ||
                    type.getTag() == TypeTags.TUPLE_TAG) && messageName.equals("google.protobuf.Timestamp");
            if (type.getTag() == TypeTags.RECORD_TYPE_TAG && !isAnyTypedMessage) {
                bBMap = ValueCreator.createRecordValue(type.getPackage(), type.getName());
                bMessage = bBMap;
            } else if (isTimestampMessage) { // for Timestamp
                TupleType tupleType = TypeCreator.createTupleType(
                        Arrays.asList(PredefinedTypes.TYPE_INT, PredefinedTypes.TYPE_DECIMAL));
                bArray = ValueCreator.createTupleValue(tupleType);
                bMessage = bArray;
            } else if (type.getTag() == TypeTags.DECIMAL_TAG) { // for Duration type
                bMessage = ValueCreator.createDecimalValue(new BigDecimal(0));
            } else if (type.getTag() == TypeTags.MAP_TAG && !isAnyTypedMessage) { // for Struct type
                bBMap = ValueCreator.createMapValue(TypeCreator.createMapType(PredefinedTypes.TYPE_ANYDATA));
                bMessage = bBMap;
            } else if (type.getTag() == TypeTags.TUPLE_TAG) { // for each field in Struct type
                TupleType tupleType = TypeCreator.createTupleType(
                        Arrays.asList(PredefinedTypes.TYPE_STRING, PredefinedTypes.TYPE_ANYDATA));
                bArray = ValueCreator.createTupleValue(tupleType);
                bMessage = bArray;
            } else if (type.getTag() == TypeTags.ARRAY_TAG) { // for array values inside structs
                bArray = ValueCreator.createArrayValue(TypeCreator.createArrayType(PredefinedTypes.TYPE_ANYDATA));
                bMessage = bArray;
            } else if (isAnyTypedMessage && input != null) {
                int typeUrlTag = input.readTag();

                if (typeUrlTag == DescriptorProtos.FieldDescriptorProto.Type.TYPE_GROUP_VALUE) {
                    String typeUrl = input.readStringRequireUtf8();
                    skipUnnecessaryAnyTypeTags(input);
                    String s = bytesToHex(codeInputStreamAnyTypeByteArray(input));
                    bMessage = StringUtils.fromString(s);
                    BMap<BString, Object> anyMap = ValueCreator.createRecordValue(type.getPackage(), type.getName());
                    anyMap.put(StringUtils.fromString("typeUrl"), StringUtils.fromString(typeUrl));
                    anyMap.put(StringUtils.fromString("value"), bMessage);
                    bMessage = anyMap;
                    return bMessage;
                }
            }
        }
        return bMessage;
    }

    public void skipUnnecessaryAnyTypeTags(com.google.protobuf.CodedInputStream input) throws IOException {

        try {
            input.readTag();
            input.readTag();
        } catch (InvalidProtocolBufferException e) {
            input.getLastTag();
        }
    }

    private byte[] codeInputStreamAnyTypeByteArray(com.google.protobuf.CodedInputStream input) throws IOException {

        List<Byte> byteList = new ArrayList<>();
        while (!(input.isAtEnd())) {
            byteList.add(input.readRawByte());
        }
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

}
