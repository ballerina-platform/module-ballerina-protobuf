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
isolated function testPackAndUnpackForFloat() returns Error? {

    float floatValue = 234f;
    Any anyFloat = pack(floatValue);
    float unpackedFloat = check unpack(anyFloat, float);
    test:assertEquals(unpackedFloat, floatValue);
}

@test:Config {}
isolated function testPackAndUnpackForDouble() returns Error? {

    float doubleValue = 234f;
    Any anyDouble = {typeUrl: "type.googleapis.com/google.protobuf.DoubleValue", value: doubleValue};
    float unpackedDouble = check unpack(anyDouble, float);
    test:assertEquals(unpackedDouble, doubleValue);
}

@test:Config {}
isolated function testPackAndUnpackForInt64() returns Error? {

    int int64Value = 234;
    Any anyInt64 = pack(int64Value);
    int unpackedInt64 = check unpack(anyInt64, int);
    test:assertEquals(unpackedInt64, int64Value);
}

@test:Config {}
isolated function testPackAndUnpackForUint64() returns Error? {

    int uint64Value = 234;
    Any anyUint64 = {typeUrl: "type.googleapis.com/google.protobuf.UInt64Value", value: uint64Value};
    int unpackedUint64 = check unpack(anyUint64, int);
    test:assertEquals(unpackedUint64, uint64Value);
}

@test:Config {}
isolated function testPackAndUnpackForInt32() returns Error? {

    int int32Value = 234;
    Any anyInt32 = {typeUrl: "type.googleapis.com/google.protobuf.Int32Value", value: int32Value};
    int unpackedInt32 = check unpack(anyInt32, int);
    test:assertEquals(unpackedInt32, int32Value);
}

@test:Config {}
isolated function testPackAndUnpackForUint32() returns Error? {

    int uint32Value = 234;
    Any anyUint32 = {typeUrl: "type.googleapis.com/google.protobuf.UInt32Value", value: uint32Value};
    int unpackedUint32 = check unpack(anyUint32, int);
    test:assertEquals(unpackedUint32, uint32Value);
}

@test:Config {}
isolated function testPackAndUnpackForString() returns Error? {

    string stringValue = "string value";
    Any anyString = pack(stringValue);
    string unpackedString = check unpack(anyString, string);
    test:assertEquals(unpackedString, stringValue);
}

@test:Config {}
isolated function testPackAndUnpackForBytes() returns Error? {

    string stringValue = "string value";
    byte[] bytes = stringValue.toBytes();
    Any anyBytes = pack(bytes);
    Any expectedBytes = {typeUrl: "type.googleapis.com/google.protobuf.BytesValue", value: bytes};
    test:assertEquals(anyBytes, expectedBytes);

    BytesType unpackedBytes = check unpack(anyBytes, BytesType);
    test:assertEquals(unpackedBytes, bytes);
}

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
isolated function testPackAndUnpackForBoolean() returns Error? {

    boolean booleanValue = true;
    Any anyBoolean = pack(booleanValue);
    boolean unpackedBoolean = check unpack(anyBoolean, boolean);
    test:assertEquals(unpackedBoolean, booleanValue);
}

@test:Config {}
isolated function testPackAndUnpackForTimestamp() returns Error? {

    time:Utc timestampValue = time:utcNow();
    Any anyTimestamp = pack(timestampValue);
    time:Utc unpackedTimestamp = check unpack(anyTimestamp, time:Utc);
    test:assertEquals(unpackedTimestamp, timestampValue);
}

@test:Config {}
isolated function testPackAndUnpackForDuration() returns Error? {

    time:Seconds durationValue = 1002d;
    Any anyDuration = pack(durationValue);
    time:Seconds unpackedDuration = check unpack(anyDuration, time:Seconds);
    test:assertEquals(unpackedDuration, durationValue);
}

@test:Config {}
isolated function testPackAndUnpackForRecord() returns Error? {

    Person person = {name: "John", code: 23};
    Any anyRecord = pack(person);
    Person unpackedPerson = check unpack(anyRecord, Person);
    test:assertEquals(unpackedPerson, person);
}

@test:Config {}
isolated function testPackAndUnpackForGenericValue() returns Error? {

    ValueType genericValue = 234f;
    Any anyFloatGeneric = pack(genericValue);
    float unpackedFloatGeneric = check unpack(anyFloatGeneric, float);
    test:assertEquals(unpackedFloatGeneric, genericValue);
}

@test:Config {}
isolated function testUnpackError() {

    float floatValue = 234f;
    Any anyFloat = pack(floatValue);
    int|Error err = unpack(anyFloat, int);
    if err is Error {
        string expectedErr = "Type type.googleapis.com/google.protobuf.FloatValue cannot unpack to int";
        test:assertEquals(err.message(), expectedErr);
    } else {
        test:assertFail("Expected 'any:Error not found");
    }
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
isolated function testAnyRecords() {
    Any[] anyTypeArr = [
        pack(<Person>{name: "John", code: 23}),
        pack("Hello"),
        pack(10)
    ];
    Person person = {name: "John", code: 23};
    Any anyRecord = pack(person);
    ContextAny anyContext = {content: anyRecord, headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    ContextAnyStream contextAnyStream = {content: anyTypeArr.toStream(), headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};

    test:assertEquals(anyRecord.typeUrl, "type.googleapis.com/Person");
    test:assertEquals(anyRecord.value, person);
    test:assertEquals(anyContext.content, anyRecord);
    test:assertEquals(anyContext.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
    test:assertEquals(contextAnyStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}

@test:Config {}
isolated function testPack() returns error? {
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
isolated function testPack1() returns error? {

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
isolated function testGetProtoTypesAnyModule() {
    _ = getProtoTypesAnyModuleForTest();
}

isolated function getProtoTypesAnyModuleForTest() returns handle = @java:Method {
    'class: "io.ballerina.stdlib.protobuf.nativeimpl.ProtoTypesUtils",
    name: "getProtoTypesAnyModule"
} external;
