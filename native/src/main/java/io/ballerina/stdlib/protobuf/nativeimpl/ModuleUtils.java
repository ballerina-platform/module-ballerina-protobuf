/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package io.ballerina.stdlib.protobuf.nativeimpl;

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.TypeTags;

import java.util.HashMap;
import java.util.Map;

import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.ANY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.DURATION_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.EMPTY_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.STRUCT_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.TIMESTAMP_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BOOL_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_BYTES_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_DOUBLE_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_FLOAT_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT32_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_INT64_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_STRING_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT32_TYPE_URL;
import static io.ballerina.stdlib.protobuf.nativeimpl.ProtobufConstants.WRAPPER_UINT64_TYPE_URL;

/**
 * APIs to set and get module-level details.
 *
 * @since 1.0.1
 */
public class ModuleUtils {

    /**
     * Package ID of the protobuf.types.'any sub-module.
     */
    private static Module protoTypesAnyModule = null;
    private static final Map<String, Integer> anyTypesMap = new HashMap<>();

    private ModuleUtils() {

    }

    public static void setProtoTypesAnyModule(Environment env) {

        protoTypesAnyModule = env.getCurrentModule();
        anyTypesMap.put(WRAPPER_DOUBLE_TYPE_URL, TypeTags.FLOAT_TAG);
        anyTypesMap.put(WRAPPER_FLOAT_TYPE_URL, TypeTags.FLOAT_TAG);
        anyTypesMap.put(WRAPPER_INT64_TYPE_URL, TypeTags.INT_TAG);
        anyTypesMap.put(WRAPPER_UINT64_TYPE_URL, TypeTags.INT_TAG);
        anyTypesMap.put(WRAPPER_INT32_TYPE_URL, TypeTags.INT_TAG);
        anyTypesMap.put(WRAPPER_UINT32_TYPE_URL, TypeTags.INT_TAG);
        anyTypesMap.put(WRAPPER_BOOL_TYPE_URL, TypeTags.BOOLEAN_TAG);
        anyTypesMap.put(WRAPPER_STRING_TYPE_URL, TypeTags.STRING_TAG);
        anyTypesMap.put(WRAPPER_BYTES_TYPE_URL, TypeTags.ARRAY_TAG);
        anyTypesMap.put(EMPTY_TYPE_URL, TypeTags.NULL_TAG);
        anyTypesMap.put(TIMESTAMP_TYPE_URL, TypeTags.INTERSECTION_TAG);
        anyTypesMap.put(DURATION_TYPE_URL, TypeTags.DECIMAL_TAG);
        anyTypesMap.put(STRUCT_TYPE_URL, TypeTags.MAP_TAG);
        anyTypesMap.put(ANY_TYPE_URL, TypeTags.RECORD_TYPE_TAG);
    }

    public static Module getProtoTypesAnyModule() {

        return protoTypesAnyModule;
    }

    public static Map<String, Integer> getAnyTypeMap() {

        return anyTypesMap;
    }
}
