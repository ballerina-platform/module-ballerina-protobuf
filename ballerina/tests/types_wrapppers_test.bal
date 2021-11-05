// Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/test;
import ballerina/protobuf.types.wrappers;

@test:Config {}
isolated function testWrappersString() {
    string[] stringArray = ["Alex", "Nicky", "John", "Mike"];
    stream<string> stringStream = stringArray.toStream();

    wrappers:ContextString contextString = {content: "test message",
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextString.content, "test message");

    wrappers:ContextStringStream contextStringStream = {content: stringStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextStringStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}

@test:Config {}
isolated function testWrappersInt() {
    int[] intArray = [1, 2, 3, 4];
    stream<int> intStream = intArray.toStream();
    
    wrappers:ContextInt contextInt = {content: 845315,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextInt.content, 845315);

    wrappers:ContextIntStream contextIntStream = {content: intStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextIntStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}

@test:Config {}
isolated function testWrappersFloat() {
    float[] floatArray = [1.25, 2.56, 3.14, 4.8];
    stream<float> floatStream = floatArray.toStream();
    
    wrappers:ContextFloat contextFloat = {content: 845.315,
                                       headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextFloat.content, 845.315);

    wrappers:ContextFloatStream contextFloatStream = {content: floatStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextFloatStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}

@test:Config {}
isolated function testWrappersBoolean() {
    boolean[] booleanArray = [true, false, false, true];
    stream<boolean> booleanStream = booleanArray.toStream();
    
    wrappers:ContextBoolean contextBoolean = {content: true,
                                       headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextBoolean.content, true);

    wrappers:ContextBooleanStream contextBooleanStream = {content: booleanStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextBooleanStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}

@test:Config {}
isolated function testWrappersBytes() {
    byte[][] bytesArray = [
                           [1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9],
                           [2, 4,16, 25, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9],
                           [10, 6, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9],
                           [21, 44,168, 25, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9]
                          ];
    stream<byte[]> bytesStream = bytesArray.toStream();
    
    wrappers:ContextBytes contextBytes = {content: [1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9],
                                       headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextBytes.content, [1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9, 1, 2, 4, 9]);

    wrappers:ContextBytesStream contextBytesStream = {content: bytesStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextBytesStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}
