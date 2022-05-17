// Copyright (c) 2022 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

@test:Config {}
isolated function testUnpackToIncorrectType() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.DoubleValue", value: "090000000000002540"};
    int|Error err = unpack(a, int);
    test:assertTrue(err is Error);
    test:assertEquals((<Error>err).message(), "Type type.googleapis.com/google.protobuf.DoubleValue cannot unpack to int");
}

@test:Config {}
isolated function testUnpackToRecordWithoutAnnotation() returns error? {
    Any a = {"typeUrl": "type.googleapis.com/MessageWithoutAnnotation", "value": "0900000000000025401500002841180A200B280E302D3D7A0000004159010000000000004801520457534F325A0B0A0942616C6C6572696E61"};
    MessageWithoutAnnotation|Error err = unpack(a, MessageWithoutAnnotation);
    test:assertTrue(err is Error);
    test:assertEquals((<Error>err).message(), "Unavailable annotation for record MessageWithoutAnnotation");
}
