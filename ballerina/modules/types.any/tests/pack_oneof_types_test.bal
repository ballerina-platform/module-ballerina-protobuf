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
isolated function testPackDoubleOneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        doubleData: 10.5
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "090000000000002540"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackFloatOneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        floatData: 10.5
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "1500002841"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackUInt32OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        uInt32Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "1864"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackUInt64OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        uInt64Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "2064"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackInt32OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        int32Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "2864"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackInt64OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        int64Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "3064"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackFixed32OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        fixed32Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "3D64000000"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackFixed64OneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        fixed64Data: 100
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "416400000000000000"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackBooleanOneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        booleanData: false
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "4800"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackStringOneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        stringData: "Ballerina"
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "520942616C6C6572696E61"};
    test:assertEquals(a, expected);
}

@test:Config {}
isolated function testPackMessageOneofDataWithAnnotation() returns error? {
    AnnotatedMessageWithOneof value = {
        messageData: {"messageData1": "Ballerina"}
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "5A0B0A0942616C6C6572696E61"};
    test:assertEquals(a, expected);
}

