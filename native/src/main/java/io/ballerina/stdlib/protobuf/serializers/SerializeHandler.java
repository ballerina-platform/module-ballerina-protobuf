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

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

import static io.ballerina.stdlib.protobuf.utils.Utils.bytesToHex;

/**
 * The serialize handler class.
 */
public class SerializeHandler {

    private final Descriptors.Descriptor messageDescriptor;
    private CodedOutputStream outputStream;
    private final BMessage bMessage;
    private final int size;
    private final byte[] content;

    public SerializeHandler(Descriptors.Descriptor messageDescriptor, Object value)
            throws IOException, Descriptors.DescriptorValidationException, AnnotationUnavailableException {

        this.messageDescriptor = messageDescriptor;
        this.bMessage = new BMessage(value);
        this.size = calculateSize();
        this.content = new byte[size];
        this.outputStream = CodedOutputStream.newInstance(this.content);
    }

    public SerializeHandler(CodedOutputStream outputStream, Descriptors.Descriptor messageDescriptor, Object value)
            throws IOException, Descriptors.DescriptorValidationException, AnnotationUnavailableException {

        this.messageDescriptor = messageDescriptor;
        this.bMessage = new BMessage(value);
        this.size = calculateSize();
        this.content = new byte[0];
        this.outputStream = outputStream;
    }

    public Object getBMessage() {

        return bMessage.getContent();
    }

    public int getSize() {

        return bMessage.getSize();
    }

    public void serialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        for (Descriptors.FieldDescriptor fieldDescriptor : messageDescriptor.getFields()) {
            switch (fieldDescriptor.getType().toProto().getNumber()) {
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE_VALUE: {
                    new DoubleSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT_VALUE: {
                    new FloatSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64_VALUE: {
                    new Int64Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64_VALUE: {
                    new UInt64Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32_VALUE: {
                    new Int32Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32_VALUE: {
                    new UInt32Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64_VALUE: {
                    new Fixed64Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32_VALUE: {
                    new Fixed32Serializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL_VALUE: {
                    new BooleanSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING_VALUE: {
                    new StringSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE_VALUE: {
                    new MessageSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM_VALUE: {
                    new EnumSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES_VALUE: {
                    new BytesSerializer(outputStream, fieldDescriptor, bMessage).serialize();
                    break;
                }
                default: {

                }
            }
        }
    }

    public int calculateSize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException {

        for (Descriptors.FieldDescriptor fieldDescriptor : messageDescriptor.getFields()) {
            switch (fieldDescriptor.getType().toProto().getNumber()) {
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE_VALUE: {
                    new DoubleSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FLOAT_VALUE: {
                    new FloatSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT64_VALUE: {
                    new Int64Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT64_VALUE: {
                    new UInt64Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32_VALUE: {
                    new Int32Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_UINT32_VALUE: {
                    new UInt32Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED64_VALUE: {
                    new Fixed64Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_FIXED32_VALUE: {
                    new Fixed32Serializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BOOL_VALUE: {
                    new BooleanSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING_VALUE: {
                    new StringSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE_VALUE: {
                    new MessageSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_ENUM_VALUE: {
                    new EnumSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                case DescriptorProtos.FieldDescriptorProto.Type.TYPE_BYTES_VALUE: {
                    new BytesSerializer(outputStream, fieldDescriptor, bMessage).computeMessageSize();
                    break;
                }
                default: {

                }
            }
        }

        return bMessage.getSize();

    }

    public BString getContentAsBString() {

        return StringUtils.fromString(bytesToHex(this.content));
    }
}
