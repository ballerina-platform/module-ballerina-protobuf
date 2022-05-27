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

@test:Config {}
isolated function testPackWithAnnotatedMessage() returns error? {
    AnnotatedMessage value = {
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
        messageData: {messageData1: "Ballerina"}
    };
    Any a = check pack(value);
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessage", value: "0900000000000025401500002841180A200B280E302D3D7A0000004159010000000000004801520457534F325A0B0A0942616C6C6572696E61"};
    test:assertEquals(a, expected);

}

@test:Config {}
isolated function testPackWithAnnotatedRepeatMessageWithoutPack() returns error? {
    AnnotatedMessageWithRepeats value = {
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
    Any expected = {typeUrl: "type.googleapis.com/AnnotatedMessageWithRepeats", value: "0900000000000025400933333333333325401500003841159A99394118091808200B200C280C280D300A300B3D0E0000003D0F000000410D000000000000004110000000000000004801520457534F325A0B0A0942616C6C6572696E61"};
    Any a = check pack(value);
    test:assertEquals(a, expected);
}

