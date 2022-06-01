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
isolated function testUnpackDoubleOneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "090000000000002540"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        doubleData: 10.5
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackFloatOneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "1500002841"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        floatData: 10.5
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackUInt32OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "1864"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        uInt32Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackUInt64OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "2064"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        uInt64Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackInt32OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "2864"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        int32Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackInt64OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "3064"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        int64Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackFixed32OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "3D64000000"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        fixed32Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackFixed64OneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "416400000000000000"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        fixed64Data: 100
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackBooleanOneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "4800"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        booleanData: false
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackStringOneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "520942616C6C6572696E61"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        stringData: "Ballerina"
    };
    test:assertEquals(msg, expected);
}

@test:Config {}
isolated function testUnpackMessageOneofDataWithAnnotation() returns error? {
    Any a = {typeUrl: "type.googleapis.com/AnnotatedMessageWithOneof", value: "5A0B0A0942616C6C6572696E61"};
    AnnotatedMessageWithOneof msg = check unpack(a, AnnotatedMessageWithOneof);
    AnnotatedMessageWithOneof expected = {
        messageData: {"messageData1": "Ballerina"}
    };
    test:assertEquals(msg, expected);
}

