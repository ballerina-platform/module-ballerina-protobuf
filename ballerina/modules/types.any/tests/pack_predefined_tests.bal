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
import ballerina/time;

@test:Config {}
isolated function testPackFloat() returns Error? {

    float value = 10.5f;
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.FloatValue", value: "0D00002841"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackInt64() returns Error? {

    int value = 10;
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.Int64Value", value: "080A"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackBoolean() returns Error? {

    Any a = check pack(true);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.BoolValue", value: "0801"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackString() returns Error? {

    Any a = check pack("WSO2");
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.StringValue", value: "0A0457534F32"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackTimestamp() returns error? {

    time:Utc value = [1652362444, 0.415532d];
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.Timestamp", value: "08CC99F4930610E08792C601"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackDuration() returns error? {

    time:Seconds value = 100.1968;
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.Duration", value: "08641080DCEB5D"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackBytes() returns error? {

    byte[] value = "WSO2".toBytes();
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/google.protobuf.BytesValue", value: "0A0457534F32"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackBytesInMessage() returns error? {

    AnnotatedMessageWithBytes value = {bytesData: "WSO2".toBytes()};
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithBytes", value: "0A0457534F32"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackBytesOneof() returns error? {

    AnnotatedMessageWithBytesOneof value = {bytesData: "WSO2".toBytes()};
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithBytesOneof", value: "0A0457534F32"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackEnumMessage() returns error? {

    AnnotatedMessageWithEnum value = {enumData: enumData1};
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithEnum", value: "0801"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackEnumMessageOneof() returns error? {

    AnnotatedMessageWithEnumOneof value = {enumData: enumData1};
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithEnumOneof", value: "0801"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackStruct1() returns error? {

    map<anydata> value = {"Name": "John", "Age": 24.0, "Married": false};
    Any a = check pack(value);
    Any expected = {
        typeUrl: "type.googleapis.com/google.protobuf.Struct",
        value: "0A0E0A044E616D6512061A044A6F686E0A100A0341676512091100000000000038400A0D0A074D61727269656412022000"
    };
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackStruct2() returns error? {

    map<anydata> value = {"Name": "WSO2", "Age": 24.0};
    Any a = check pack(value);
    Any expected = {
        typeUrl: "type.googleapis.com/google.protobuf.Struct",
        value: "0A0E0A044E616D6512061A0457534F320A100A034167651209110000000000003840"
    };
    test:assertEquals(a, expected);
}


