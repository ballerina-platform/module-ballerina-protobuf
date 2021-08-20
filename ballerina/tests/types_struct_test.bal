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
import ballerina/protobuf.types.struct;

@test:Config {}
isolated function testStruct() {
    map<anydata>[] structArray = [{name: "Alex", age: 29}, {subject: "Maths", pass: true}, {GPA: 3.985},
                                  {country: "USA", state: "California", city: "San Fransisco"}];
    stream<map<anydata>> timeUtcStream = structArray.toStream();
    struct:StructStream outputStream = new struct:StructStream(timeUtcStream);

    test:assertEquals(outputStream.next(), {"value": {name: "Alex", age: 29}});
    test:assertEquals(outputStream.next(), {"value": {subject: "Maths", pass: true}});
    test:assertEquals(outputStream.next(), {"value": {GPA: 3.985}});

    var result = outputStream.close();
    test:assertFalse(result is error);
}
