/*
 *  Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.stdlib.protobuf.utils;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.PROTOBUF_DESC_ANNOTATION;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.PROTOBUF_DESC_ANNOTATION_VALUE;
import static io.ballerina.stdlib.protobuf.utils.StandardDescriptorBuilder.findGoogleDescriptorFromName;
import static io.ballerina.stdlib.protobuf.utils.StandardDescriptorBuilder.getFieldWireType;
import static io.ballerina.stdlib.protobuf.utils.Utils.hexToBytes;

/**
 * Provides protobuf descriptor for well known dependency.
 */
public class DescriptorBuilder {

    private DescriptorBuilder() {

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

    public static Descriptors.Descriptor findFieldDescriptorFromTypeUrl(Object value, String messageName)
            throws Descriptors.DescriptorValidationException, InvalidProtocolBufferException,
            AnnotationUnavailableException {

        if (messageName.startsWith("google.protobuf")) {
            return findGoogleDescriptorFromName(messageName);
        } else if (value instanceof BMap) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) value;
            RecordType recordType = (RecordType) bMap.getType();
            if (isDescriptorAnnotationAvailable(recordType)) {
                return getDescriptorFromRecord(recordType);
            }
        }
        throw new AnnotationUnavailableException("Expected annotation unavailable in the record " + messageName);
    }

    public static Descriptors.Descriptor findFieldDescriptorFromTypeUrl(RecordType recordType, String messageName)
            throws AnnotationUnavailableException, Descriptors.DescriptorValidationException,
            InvalidProtocolBufferException {

        if (messageName.startsWith("google.protobuf")) {
            return findGoogleDescriptorFromName(messageName);
        } else if (isDescriptorAnnotationAvailable(recordType)) {
            return getDescriptorFromRecord(recordType);
        }
        throw new AnnotationUnavailableException("Expected annotation unavailable in the record " +
                recordType.getName());
    }

    public static boolean isDescriptorAnnotationAvailable(RecordType recordType) {

        return Arrays.stream(recordType.getAnnotations().getKeys()).anyMatch(
                s -> PROTOBUF_DESC_ANNOTATION.equals(s.getValue()));
    }

    public static com.google.protobuf.Descriptors.Descriptor getDescriptorFromRecord(RecordType recordType)
            throws InvalidProtocolBufferException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        if (isDescriptorAnnotationAvailable(recordType)) {
            BMap<BString, Object> annotations = recordType.getAnnotations();
            String annotation = annotations.getMapValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION))
                    .getStringValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION_VALUE)).getValue();
            byte[] annotationAsBytes = hexToBytes(annotation);
            DescriptorProtos.FileDescriptorProto file = DescriptorProtos.FileDescriptorProto.parseFrom(
                    annotationAsBytes);
            Descriptors.FileDescriptor fileDescriptor;
            fileDescriptor = Descriptors.FileDescriptor.buildFrom(file, new Descriptors.FileDescriptor[]{},
                    true);
            Descriptors.Descriptor desc = fileDescriptor.findMessageTypeByName(recordType.getName());
            if (desc != null) {
                return desc;
            }
        }
        throw new AnnotationUnavailableException("Expected annotation unavailable in the record " +
                recordType.getName());
    }

    public static CodedInputStream getCodedInputStream(String hexInput) {

        byte[] byteContent = hexToBytes(hexInput);
        return CodedInputStream.newInstance(byteContent);
    }

    public static Descriptors.Descriptor getMessageDescriptor(String descriptor, String targetTypeName)
            throws Descriptors.DescriptorValidationException, InvalidProtocolBufferException {

        byte[] descriptorBytes = hexToBytes(descriptor);
        DescriptorProtos.FileDescriptorProto file = DescriptorProtos.FileDescriptorProto.parseFrom(descriptorBytes);
        Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(file,
                new Descriptors.FileDescriptor[]{}, true);
        return fileDescriptor.findMessageTypeByName(targetTypeName);
    }

    public static com.google.protobuf.Descriptors.Descriptor getMessageDescriptor(
            Descriptors.FieldDescriptor fieldDescriptor, Type messageType)
            throws InvalidProtocolBufferException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

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

    private static com.google.protobuf.Descriptors.Descriptor getDescriptorForPredefinedTypes(String messageName) {

        Descriptors.FileDescriptor fileDescriptor = StandardDescriptorBuilder
                .getFileDescriptorFromMessageName(messageName);
        return fileDescriptor.findMessageTypeByName(extractMessageNameWithoutNamespace(messageName));
    }

    private static String extractMessageNameWithoutNamespace(String messageName) {

        String[] messageEntries = messageName.split("\\.");
        return messageEntries[messageEntries.length - 1];
    }
}
