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

import org.testng.Assert;
import org.testng.annotations.Test;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.STRUCT_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.TIMESTAMP_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BOOL_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BYTES_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_DOUBLE_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_FLOAT_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT32_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT64_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_STRING_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT32_TYPE_NAME;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT64_TYPE_NAME;

/**
 * Tests of StandardDescriptorBuilder.
 */
public class StandardDescriptorBuilderTest {

    @Test()
    public void testGetFileDescriptorFromMessageName() {
        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(ANY_TYPE_NAME),
        com.google.protobuf.AnyProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(EMPTY_TYPE_NAME),
                com.google.protobuf.EmptyProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(TIMESTAMP_TYPE_NAME),
                com.google.protobuf.TimestampProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(DURATION_TYPE_NAME),
                com.google.protobuf.DurationProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(STRUCT_TYPE_NAME),
                com.google.protobuf.StructProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_DOUBLE_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_FLOAT_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_INT64_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_UINT64_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_INT32_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_UINT32_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_BOOL_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_STRING_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());

        Assert.assertEquals(StandardDescriptorBuilder.getFileDescriptorFromMessageName(WRAPPER_BYTES_TYPE_NAME),
                com.google.protobuf.WrappersProto.getDescriptor());
    }
}
