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
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

/**
 * Abstract deserializer class to deserialize the Protobuf messages.
 */
public abstract class AbstractDeserializer {

    protected com.google.protobuf.CodedInputStream input;
    protected Descriptors.FieldDescriptor fieldDescriptor;
    protected BMessage bMessage;
    protected Type messageType;
    protected Type targetType;
    protected boolean isPacked = false;

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.targetType = targetType;
    }

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType, boolean isPacked) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.isPacked = isPacked;
        this.targetType = targetType;
    }

    public AbstractDeserializer(com.google.protobuf.CodedInputStream input, Descriptors.FieldDescriptor fieldDescriptor,
                                BMessage bMessage, Type targetType, Type messageType) {

        this.bMessage = bMessage;
        this.fieldDescriptor = fieldDescriptor;
        this.input = input;
        this.messageType = messageType;
        this.targetType = targetType;
    }

    public boolean isBMap() {

        return bMessage.getContent() instanceof BMap;
    }

    public boolean isBArray() {

        return bMessage.getContent() instanceof BArray;
    }

    public abstract void deserialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException;
}
