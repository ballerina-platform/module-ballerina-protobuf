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
import ballerina/time;
import ballerina/jballerina.java;

type BytesType byte[];

type NilType ();

public type Person record {|
    string name = "";
    int code = 0;
|};

@test:Config {}
isolated function testPackAndUnpackForNil() returns Error? {

    Any anyNil1 = pack(());
    Any expectedNil = {typeUrl: "type.googleapis.com/google.protobuf.Empty", value: ()};
    test:assertEquals(anyNil1, expectedNil);

    NilType unpackedNil1 = check unpack(anyNil1, NilType);
    test:assertEquals(unpackedNil1, ());

    Any anyNil2 = {typeUrl: "type.googleapis.com/google.protobuf.Empty", value: {}};
    NilType unpackedNil2 = check unpack(anyNil2, NilType);
    test:assertEquals(unpackedNil2, ());
}

@test:Config {}
isolated function testGetUrlSuffix() {
    Person person = {name: "John", code: 23};

    test:assertEquals(getUrlSuffixFromValue(234f), "google.protobuf.FloatValue");
    test:assertEquals(getUrlSuffixFromValue(234), "google.protobuf.Int64Value");
    test:assertEquals(getUrlSuffixFromValue("Ballerina"), "google.protobuf.StringValue");
    test:assertEquals(getUrlSuffixFromValue(false), "google.protobuf.BoolValue");
    test:assertEquals(getUrlSuffixFromValue(time:utcNow()), "google.protobuf.Timestamp");
    test:assertEquals(getUrlSuffixFromValue(<time:Seconds>1002d), "google.protobuf.Duration");
    test:assertEquals(getUrlSuffixFromValue(()), "google.protobuf.Empty");
    test:assertEquals(getUrlSuffixFromValue(person), "Person");
}

@test:Config {}
isolated function testGetNameFromRecord() {
    Person person = {name: "John", code: 23};
    test:assertEquals(externGetNameFromRecord(person), "Person");
}

@test:Config {}
isolated function testUnpackWithAnnotatedMessage() returns error? {
    Any a = {"typeUrl": "type.googleapis.com/AnnotatedMessage", "value": "0900000000000025401500003841180A200B280C310D000000000000003D0E00000040014A0457534F32620B0A0942616C6C6572696E61"};
    AnnotatedMessage msg = check unpack(a, AnnotatedMessage);
    AnnotatedMessage expected = {
        doubleData: 10.5,
        floatData: 11.5,
        int64Data: 10,
        uInt64Data: 11,
        int32Data: 12,
        fixed64Data: 13,
        fixed32Data: 14,
        booleanData: true,
        stringData: "WSO2",
        enumData: enumData0,
        messageData: {"messageData1": "Ballerina"}
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackWithAnnotatedRepeatMessage() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithRepeats", value: "0A10000000000000254033333333333325401204000038411A010A22010B2A010C32080D000000000000003A040E0000004201014A0457534F32620B0A0942616C6C6572696E61"};
    AnnotatedMessageWithRepeats msg = check unpack(a, AnnotatedMessageWithRepeats);
    AnnotatedMessageWithRepeats expected = {
        doubleData: [10.5, 10.6],
        floatData: [11.5],
        int64Data: [10],
        uInt64Data: [11],
        int32Data: [12],
        fixed64Data: [13],
        fixed32Data: [14],
        booleanData: [true],
        stringData: ["WSO2"],
        enumData: [],
        messageData: [{messageData1: "Ballerina"}]
    };
    test:assertEquals(msg, expected);
}

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
isolated function testGetProtoTypesAnyModule() {
    _ = getProtoTypesAnyModuleForTest();
}

isolated function getProtoTypesAnyModuleForTest() returns handle = @java:Method {
    'class: "io.ballerina.stdlib.protobuf.nativeimpl.ProtoTypesUtils",
    name: "getProtoTypesAnyModule"
} external;
