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

package io.ballerina.stdlib.protobuf.utils;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import io.ballerina.runtime.api.types.RecordType;
import io.ballerina.runtime.api.types.Type;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.deserializers.DeserializeHandler;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.nativeimpl.ModuleUtils;

import java.io.IOException;
import java.util.Arrays;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.PROTOBUF_DESC_ANNOTATION;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.PROTOBUF_DESC_ANNOTATION_VALUE;
import static io.ballerina.stdlib.protobuf.utils.DescriptorBuilder.getDescriptorFromRecord;

/**
 * The general utility class of the protobuf module.
 */
public class Utils {

    private Utils() {

    }

    public static String bytesToHex(byte[] content) {

        char[] hexChars = new char[content.length * 2];
        for (int j = 0; j < content.length; j++) {
            int v = content[j] & 0xFF;
            hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];
            hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Convert Hex string value to byte array.
     *
     * @param content hexadecimal string value
     * @return Byte array
     */
    public static byte[] hexToBytes(String content) {

        if (content == null) {
            return new byte[0];
        }
        int len = content.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(content.charAt(i), 16) << 4)
                    + Character.digit(content.charAt(i + 1), 16));
        }
        return data;
    }

    public static String getMessageNameFromTypeUrl(String typeUrl) {

        String[] literals = typeUrl.split("\\.");
        return literals[literals.length - 1];
    }

    public static Object deserialize(Type targetType, RecordType recordType, String content)
            throws Descriptors.DescriptorValidationException, IOException, AnnotationUnavailableException {

        if (isDescriptorAnnotationAvailable(recordType)) {
            String annotation = getProtobufDescAnnotation(recordType);
            DeserializeHandler m2 = new DeserializeHandler(annotation, content, targetType, recordType);
            m2.deserialize();
            return m2.getBMessage();
        } else {
            throw new AnnotationUnavailableException("Unavailable annotation for record " + recordType.getName());
        }
    }

    public static boolean isDescriptorAnnotationAvailable(RecordType recordType) {

        return Arrays.stream(recordType.getAnnotations().getKeys()).anyMatch(
                s -> PROTOBUF_DESC_ANNOTATION.equals(s.getValue()));
    }

    @SuppressWarnings("unchecked")
    public static Descriptors.Descriptor getAnnotatedDescriptorFromRecord(Object value)
            throws Descriptors.DescriptorValidationException, InvalidProtocolBufferException,
            AnnotationUnavailableException {

        if (value instanceof BMap) {
            BMap<BString, Object> bMap = (BMap<BString, Object>) value;
            RecordType recordType = (RecordType) bMap.getType();
            return getDescriptorFromRecord(recordType);
        }
        throw new AnnotationUnavailableException("Expected annotation unavailable " + value.toString());
    }

    public static String getProtobufDescAnnotation(RecordType recordType) {

        BMap<BString, Object> annotations = recordType.getAnnotations();
        return annotations.getMapValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION))
                .getStringValue(StringUtils.fromString(PROTOBUF_DESC_ANNOTATION_VALUE)).getValue();
    }

    public static boolean isMatchingType(String typeUrl, int typeTag) {

        if (ModuleUtils.getAnyTypeMap().containsKey(typeUrl)) {
            return ModuleUtils.getAnyTypeMap().get(typeUrl) == typeTag;
        }
        return false;
    }
}
