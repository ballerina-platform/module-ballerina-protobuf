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
