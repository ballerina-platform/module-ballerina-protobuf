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
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.TupleType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.utils.StandardDescriptorBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The deserialize handler class.
 */
public class DeserializeHandler {

    private final Descriptors.Descriptor messageDescriptor;
    private final CodedInputStream input;
    private final Object bMessage;
    private final Map<Integer, Descriptors.FieldDescriptor> fieldDescriptors;
    private final Type messageType;
    private boolean isAnyTypedMessage = false;

    public DeserializeHandler(String descriptor, String hexInput, Type messageType)
            throws Descriptors.DescriptorValidationException, IOException {

        String targetTypeName = messageType.getName();
        this.input = getCodedInputStream(hexInput);
        this.messageDescriptor = getMessageDescriptor(descriptor, targetTypeName);
        this.fieldDescriptors = computeFieldTagValues(messageDescriptor);
        this.messageType = messageType;
        this.bMessage = this.initBMessage(input, fieldDescriptors, messageType, targetTypeName);
    }

    public DeserializeHandler(Descriptors.FieldDescriptor fieldDescriptor, Type messageType, CodedInputStream input)
            throws IOException {

        this.input = input;
        this.messageDescriptor = getMessageDescriptor(fieldDescriptor, messageType);
        this.fieldDescriptors = computeFieldTagValues(messageDescriptor);
        this.messageType = messageType;
        this.bMessage = this.initBMessage(input, fieldDescriptors, messageType, messageDescriptor.getFullName());
    }

    private CodedInputStream getCodedInputStream(String hexInput) {

        byte[] byteContent = hexStringToByteArray(hexInput);
        return CodedInputStream.newInstance(byteContent);
    }

    private Descriptors.Descriptor getMessageDescriptor(String descriptor, String targetTypeName)
            throws Descriptors.DescriptorValidationException, InvalidProtocolBufferException {

        byte[] descriptorBytes = hexStringToByteArray(descriptor);
        DescriptorProtos.FileDescriptorProto file = DescriptorProtos.FileDescriptorProto.parseFrom(descriptorBytes);
        Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(file,
                new Descriptors.FileDescriptor[]{}, true);
        return fileDescriptor.findMessageTypeByName(targetTypeName);
    }

    private com.google.protobuf.Descriptors.Descriptor getMessageDescriptor(Descriptors.FieldDescriptor fieldDescriptor,
                                                                            Type messageType)
            throws InvalidProtocolBufferException {

        if (fieldDescriptor.getMessageType().getFile().getFullName().endsWith(".placeholder.proto")) {
            if (fieldDescriptor.getMessageType().getFile().getFullName().startsWith("google.protobuf")) {
                String messageName = fieldDescriptor.getMessageType().getFile().getFullName()
                        .replace(".placeholder.proto", "");
                return getDescriptorForPredefinedTypes(messageName);
            } else if (messageType instanceof RecordType) {
                return getDescriptorFromRecord((RecordType) messageType);
            }
        }
        return fieldDescriptor.getMessageType();
    }

    private com.google.protobuf.Descriptors.Descriptor getDescriptorFromRecord(RecordType recordType)
            throws InvalidProtocolBufferException {

        if (isDescriptorAnnotationAvailable(recordType)) {
            BMap<BString, Object> annotations = recordType.getAnnotations();
            String annotation = annotations.getMapValue(StringUtils.fromString("ballerina/protobuf:1:Descriptor"))
                    .getStringValue(StringUtils.fromString("value")).getValue();
            byte[] annotationAsBytes = hexStringToByteArray(annotation);
            DescriptorProtos.FileDescriptorProto file = DescriptorProtos.FileDescriptorProto
                    .parseFrom(annotationAsBytes);
            Descriptors.FileDescriptor fileDescriptor;
            try {
                fileDescriptor = Descriptors.FileDescriptor.buildFrom(file, new Descriptors.FileDescriptor[]{},
                        true);
                Descriptors.Descriptor desc = fileDescriptor.findMessageTypeByName(recordType.getName());
                if (desc != null) {
                    return desc;
                }
            } catch (Descriptors.DescriptorValidationException e) {
                return null;
            }

        }
        return null;
    }

    private boolean isDescriptorAnnotationAvailable(RecordType recordType) {

        return Arrays.stream(recordType.getAnnotations().getKeys()).anyMatch(
                s -> "ballerina/protobuf:1:Descriptor".equals(s.getValue()));
    }

    private com.google.protobuf.Descriptors.Descriptor getDescriptorForPredefinedTypes(String messageName) {

        Descriptors.FileDescriptor fileDescriptor = StandardDescriptorBuilder
                .getFileDescriptorFromMessageName(messageName);
        return fileDescriptor.findMessageTypeByName(extractMessageNameWithoutNamespace(messageName));
    }

    private String extractMessageNameWithoutNamespace(String messageName) {

        String[] messageEntries = messageName.split("\\.");
        return messageEntries[messageEntries.length - 1];
    }

    public Object getBMessage() {

        return bMessage;
    }

    public void deserialize() throws IOException, Descriptors.DescriptorValidationException {

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
                    new DoubleDeserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT_VALUE: {
                    new FloatDeserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64_VALUE: {
                    new Int64Deserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64_VALUE: {
                    new UInt64Deserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32_VALUE: {
                    new Int32Deserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64_VALUE: {
                    new Fixed64Deserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32_VALUE: {
                    new Fixed32Deserializer(input, fieldDescriptor, bMessage, isPacked).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL_VALUE: {
                    new BooleanDeserializer(input, fieldDescriptor, bMessage).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING_VALUE: {
                    new StringDeserializer(input, fieldDescriptor, bMessage).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM_VALUE: {
                    new EnumDeserializer(input, fieldDescriptor, bMessage).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES_VALUE: {
                    new BytesDeserializer(input, fieldDescriptor, bMessage).deserialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE_VALUE: {
                    new MessageDeserializer(input, fieldDescriptor, messageType, bMessage)
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

    public static Map<Integer, Descriptors.FieldDescriptor> computeFieldTagValues(
            Descriptors.Descriptor messageDescriptor) {

        Map<Integer, Descriptors.FieldDescriptor> fieldDescriptors = new HashMap<>();
        for (Descriptors.FieldDescriptor fieldDescriptor : messageDescriptor.getFields()) {
            Descriptors.FieldDescriptor.Type fieldType = fieldDescriptor.getType();
            int number = fieldDescriptor.getNumber();
            int byteCode = ((number << 3) + getFieldWireType(fieldType));
            fieldDescriptors.put(byteCode, fieldDescriptor);
            if (fieldDescriptor.isRepeated()) {
                byteCode = ((number << 3) + 2);
                fieldDescriptors.put(byteCode, fieldDescriptor);
            }

        }
        return fieldDescriptors;
    }

    public static final Map<DescriptorProtos.FieldDescriptorProto.Type, Integer> WIRE_TYPE_MAP;

    static {
        Map<DescriptorProtos.FieldDescriptorProto.Type, Integer> wireMap = new HashMap<>();
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT32, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SINT64, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED32, 5);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_SFIXED64, 1);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM, 0);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING, 2);
        wireMap.put(DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES, 2);
        WIRE_TYPE_MAP = Collections.unmodifiableMap(wireMap);
    }

    static int getFieldWireType(Descriptors.FieldDescriptor.Type fieldType) {

        if (fieldType == null) {
            return -1;
        }
        Integer wireType = WIRE_TYPE_MAP.get(fieldType.toProto());
        if (wireType != null) {
            return wireType;
        } else {
            // Returns embedded messages, packed repeated fields message type, if field type doesn't map with the
            // predefined proto types.
            return 2;
        }
    }

    static byte[] hexStringToByteArray(String sDescriptor) {

        if (sDescriptor == null) {
            return new byte[0];
        }
        int len = sDescriptor.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(sDescriptor.charAt(i), 16) << 4)
                    + Character.digit(sDescriptor.charAt(i + 1), 16));
        }
        return data;
    }

    Object initBMessage(com.google.protobuf.CodedInputStream input, Map<Integer,
            Descriptors.FieldDescriptor> fieldDescriptors, Type type, String messageName) throws IOException {

        BMap<BString, Object> bBMap = null;
        BArray bArray;
        Object bMessage = null;
        isAnyTypedMessage = "google.protobuf.Any".equals(messageName) &&
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

        if (input == null) {
            if (bBMap != null) {
                for (Map.Entry<Integer, Descriptors.FieldDescriptor> entry : fieldDescriptors.entrySet()) {
                    BString bFieldName =
                            StringUtils.fromString(entry.getValue().getName());
                    if (entry.getValue().getType().toProto().getNumber() ==
                            DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE_VALUE &&
                            !entry.getValue().isRepeated()) {
                        bBMap.put(bFieldName, null);
                    } else if (entry.getValue().getType().toProto().getNumber() ==
                            DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM_VALUE) {
                        bBMap.put(bFieldName, StringUtils
                                .fromString(entry.getValue().getEnumType().findValueByNumber(0).toString()));
                    }
                }
            } else {
                // Here fieldDescriptors map size should be one. Because the value can assign to one scalar field.
                for (Map.Entry<Integer, Descriptors.FieldDescriptor> entry : fieldDescriptors.entrySet()) {
                    switch (entry.getValue().getType().toProto().getNumber()) {
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE_VALUE:
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT_VALUE: {
                            bMessage = (double) 0;
                            break;
                        }
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64_VALUE:
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64_VALUE:
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32_VALUE:
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64_VALUE:
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32_VALUE: {
                            bMessage = (long) 0;
                            break;
                        }
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING_VALUE: {
                            bMessage = StringUtils.fromString("");
                            break;
                        }
                        case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL_VALUE: {
                            bMessage = Boolean.FALSE;
                            break;
                        }
                        default: {
                        }
                    }
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

    public static String bytesToHex(byte[] data) {

        char[] hexChars = new char[data.length * 2];
        for (int j = 0; j < data.length; j++) {
            int v = data[j] & 0xFF;
            hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];
            hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
        }
        return new String(hexChars);
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
