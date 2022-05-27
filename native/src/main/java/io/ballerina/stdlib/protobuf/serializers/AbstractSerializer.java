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
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.ballerina.stdlib.protobuf.exceptions.AnnotationUnavailableException;
import io.ballerina.stdlib.protobuf.messages.BMessage;

import java.io.IOException;

/**
 * The abstract serializer class.
 */
public abstract class AbstractSerializer {

    protected com.google.protobuf.CodedOutputStream output;
    protected Descriptors.FieldDescriptor fieldDescriptor;
    protected BMessage bMessage;
    protected BString bFieldName;
    protected String messageName;

    public abstract void computeMessageSize() throws Descriptors.DescriptorValidationException, IOException,
            AnnotationUnavailableException;

    public abstract void serialize() throws IOException, Descriptors.DescriptorValidationException,
            AnnotationUnavailableException;

    protected AbstractSerializer(com.google.protobuf.CodedOutputStream output,
                                 Descriptors.FieldDescriptor fieldDescriptor, BMessage bMessage) {

        this.fieldDescriptor = fieldDescriptor;
        this.bMessage = bMessage;
        this.bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        this.output = output;
    }

    protected AbstractSerializer(Descriptors.FieldDescriptor fieldDescriptor, BMessage bMessage) {

        this.fieldDescriptor = fieldDescriptor;
        this.bMessage = bMessage;
        this.messageName = fieldDescriptor.getMessageType().getName();
        this.bFieldName = StringUtils.fromString(fieldDescriptor.getName());
        this.output = null;
    }

    public boolean isBMap() {

        return bMessage.getContent() instanceof BMap;
    }

    public boolean isBArray() {

        return bMessage.getContent() instanceof BArray;
    }

    protected int getIntValue(Object value) {

        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        return (int) value;
    }

    protected int getSize() {

        return this.bMessage.getSize();
    }
}
