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

    Any anyNil1 = check pack(());
    Any expectedNil = {typeUrl: "type.googleapis.com/google.protobuf.Empty", value: ""};
    test:assertEquals(anyNil1, expectedNil);

    NilType unpackedNil1 = check unpack(anyNil1, NilType);
    test:assertEquals(unpackedNil1, ());

    record {} empty = {};
    Any anyNil2 = {typeUrl: "type.googleapis.com/google.protobuf.Empty", value: empty};
    NilType unpackedNil2 = check unpack(anyNil2, NilType);
    test:assertEquals(unpackedNil2, ());
}

@test:Config {}
isolated function testGetUrlSuffix() {
    Person person = {name: "John", code: 23};
    byte[] bytesData = "WSO2".toBytes();

    test:assertEquals(getUrlSuffixFromValue(234f), "google.protobuf.FloatValue");
    test:assertEquals(getUrlSuffixFromValue(234), "google.protobuf.Int64Value");
    test:assertEquals(getUrlSuffixFromValue("Ballerina"), "google.protobuf.StringValue");
    test:assertEquals(getUrlSuffixFromValue(false), "google.protobuf.BoolValue");
    test:assertEquals(getUrlSuffixFromValue(time:utcNow()), "google.protobuf.Timestamp");
    test:assertEquals(getUrlSuffixFromValue(<time:Seconds>1002d), "google.protobuf.Duration");
    test:assertEquals(getUrlSuffixFromValue(()), "google.protobuf.Empty");
    test:assertEquals(getUrlSuffixFromValue(bytesData), "google.protobuf.BytesValue");
    test:assertEquals(getUrlSuffixFromValue(person), "Person");
}

@test:Config {}
isolated function testUnpackError() returns error? {

    float floatValue = 234f;
    Any anyFloat = check pack(floatValue);
    int|Error err = unpack(anyFloat, int);
    if err is Error {
        string expectedErr = "Type type.googleapis.com/google.protobuf.FloatValue cannot unpack to int";
        test:assertEquals(err.message(), expectedErr);
    } else {
        test:assertFail("Expected 'any:Error not found");
    }
}

@test:Config {}
isolated function testGetNameFromRecord() {
    Person person = {name: "John", code: 23};
    test:assertEquals(externGetNameFromRecord(person), "Person");
}

@test:Config {}
isolated function testUnpackWithAnnotatedMessage() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessage", value: "0900000000000025401500002841180A200B280E302D3D7A0000004159010000000000004801520457534F325A0B0A0942616C6C6572696E61"};
    AnnotatedMessage msg = check unpack(a, AnnotatedMessage);
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

@test:Config {}
isolated function testUnpackWithAnnotatedRepeatMessageWithPack() returns error? {
    Any a = {
        typeUrl: "type.googleapis.com/AnnotatedMessageWithRepeats",
        value: "0A10000000000000254033333333333325401208000038419A9939411A02090822020B0C2A020C0D32020A0B3A080E0000000F00000042100D0000000000000010000000000000004A0101520457534F325A0B0A0942616C6C6572696E61"
    };
    AnnotatedMessageWithRepeats msg = check unpack(a, AnnotatedMessageWithRepeats);
    AnnotatedMessageWithRepeats expected = {
        doubleData: [10.5, 10.6],
        floatData: [11.5, 11.6],
        uInt32Data: [9, 8],
        uInt64Data: [11, 12],
        int32Data: [12, 13],
        int64Data: [10, 11],
        fixed32Data: [14, 15],
        fixed64Data: [13, 16],
        booleanData: [true],
        stringData: ["WSO2"],
        messageData: [{messageData1: "Ballerina"}]
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackWithAnnotatedRepeatMessageWithoutPack() returns error? {
    Any a = {
        typeUrl: "type.googleapis.com/AnnotatedMessageWithRepeats",
        value: "0900000000000025400933333333333325401500003841159A99394118091808200B200C280C280D300A300B3D0E0000003D0F000000410D000000000000004110000000000000004801520457534F325A0B0A0942616C6C6572696E61"
    };
    AnnotatedMessageWithRepeats msg = check unpack(a, AnnotatedMessageWithRepeats);
    AnnotatedMessageWithRepeats expected = {
        doubleData: [10.5, 10.6],
        floatData: [11.5, 11.6],
        uInt32Data: [9, 8],
        uInt64Data: [11, 12],
        int32Data: [12, 13],
        int64Data: [10, 11],
        fixed32Data: [14, 15],
        fixed64Data: [13, 16],
        booleanData: [true],
        stringData: ["WSO2"],
        messageData: [{messageData1: "Ballerina"}]
    };
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
