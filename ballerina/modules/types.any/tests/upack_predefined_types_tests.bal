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
isolated function testUnpackDouble() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.DoubleValue", value: "090000000000002540"};
    float msg = check unpack(a, float);
    float expected = 10.5f;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackFloat() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.FloatValue", value: "0D00002841"};
    float msg = check unpack(a, float);
    float expected = 10.5f;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackInt64() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.Int64Value", value: "080A"};
    int msg = check unpack(a, int);
    int expected = 10;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackUInt64() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.UInt64Value", value: "080B"};
    int msg = check unpack(a, int);
    int expected = 11;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackInt32() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.Int32Value", value: "080B"};
    int msg = check unpack(a, int);
    int expected = 11;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackUInt32() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.UInt32Value", value: "080B"};
    int msg = check unpack(a, int);
    int expected = 11;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackBoolean() returns Error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.BoolValue", value: "0801"};
    boolean msg = check unpack(a, boolean);
    boolean expected = true;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackStringValue() returns error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.StringValue", value: "0A0457534F32"};
    string msg = check unpack(a, string);
    test:assertEquals(msg, "WSO2");
}

@test:Config {}
isolated function testUnpackTimestamp() returns error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.Timestamp", value: "08CC99F4930610E08792C601"};
    time:Utc msg = check unpack(a, time:Utc);
    time:Utc expected = [1652362444, 0.415532d];
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackDuration() returns error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.Duration", value: "08641080DCEB5D"};
    time:Seconds msg = check unpack(a, time:Seconds);
    time:Seconds expected = 100.1968;
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackBytes() returns error? {

    Any a = {typeUrl: "type.googleapis.com/google.protobuf.BytesValue", value: "0A0457534F32"};
    bytesTypeForTest msg = check unpack(a, bytesTypeForTest);
    byte[] expected = "WSO2".toBytes();
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackBytesInMessage() returns error? {

    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithBytes", value: "0A0457534F32"};
    AnnotatedMessageWithBytes msg = check unpack(a, AnnotatedMessageWithBytes);
    AnnotatedMessageWithBytes expected = {bytesData: "WSO2".toBytes()};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackBytesOneof() returns error? {

    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithBytesOneof", value: "0A0457534F32"};
    AnnotatedMessageWithBytesOneof msg = check unpack(a, AnnotatedMessageWithBytesOneof);
    AnnotatedMessageWithBytesOneof expected = {bytesData: "WSO2".toBytes()};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackEnumMessage() returns error? {

    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithEnum", value: "0801"};
    AnnotatedMessageWithEnum msg = check unpack(a, AnnotatedMessageWithEnum);
    AnnotatedMessageWithEnum expected = {enumData: enumData1};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackEnumMessageOneof() returns error? {

    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithEnumOneof", value: "0801"};
    AnnotatedMessageWithEnumOneof msg = check unpack(a, AnnotatedMessageWithEnumOneof);
    AnnotatedMessageWithEnumOneof expected = {enumData: enumData1};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackStruct1() returns error? {

    Any a = {
        typeUrl: "type.googleapis.com/google.protobuf.Struct",
        value: "0A0E0A044E616D6512061A044A6F686E0A100A0341676512091100000000000038400A0D0A074D61727269656412022000"
    };
    map<anydata> msg = check unpack(a, mapTypeForTest);
    map<anydata> expected = {"Name": "John", "Age": 24.0, "Married": false};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackStruct2() returns error? {

    Any a = {
        typeUrl: "type.googleapis.com/google.protobuf.Struct",
        value: "0A0E0A044E616D6512061A0457534F320A100A034167651209110000000000003840"
    };
    map<anydata> msg = check unpack(a, mapTypeForTest);
    map<anydata> expected = {"Name": "WSO2", "Age": 24.0};
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackNestedAny() returns error? {

    Any a = {
        typeUrl: "type.googleapis.com/google.protobuf.Any",
        value: "0A24747970652E676F6F676C65617069732E636F6D2F416E6E6F74617465644D65737361676512390900000000000025401500002841180A200B280E302D3D7A0000004159010000000000004801520457534F325A0B0A0942616C6C6572696E61"
    };
    Any msg1 = check unpack(a, Any);
    AnnotatedMessage msg = check unpack(msg1, AnnotatedMessage);
    AnnotatedMessage expected = {
        doubleData: 10.5,
        floatData: 10.5,
        uInt32Data: 10,
        uInt64Data: 11,
        int32Data: 14,
        int64Data: 45,
        fixed32Data: 122,
        fixed64Data: 345,
        booleanData: true,
        stringData: "WSO2",
        messageData: {"messageData1": "Ballerina"}
    };
    test:assertEquals(msg, expected);
}
