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
isolated function testPackDouble() returns Error? {

    string content = check getSerializedString(10.5f,
    "type.googleapis.com/google.protobuf.DoubleValue");
    test:assertEquals(content, "090000000000002540");
}

@test:Config {}
isolated function testPackInt32() returns Error? {

    string content = check getSerializedString(11,
    "type.googleapis.com/google.protobuf.Int32Value");
    test:assertEquals(content, "080B");
}

@test:Config {}
isolated function testPackUInt32() returns Error? {

    string content = check getSerializedString(11,
    "type.googleapis.com/google.protobuf.UInt32Value");
    test:assertEquals(content, "080B");
}

@test:Config {}
isolated function testPackUInt64() returns Error? {

    string content = check getSerializedString(10,
    "type.googleapis.com/google.protobuf.UInt64Value");
    test:assertEquals(content, "080A");
}
