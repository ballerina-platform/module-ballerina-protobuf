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

@test:Config {
    dataProvider: nestedStructData
}
isolated function testPackUnpackNestedStruct(map<anydata> value) returns error? {
    map<anydata> result = check unpack(check pack(value));
    test:assertEquals(result, value);
}

isolated function nestedStructData() returns map<[map<anydata>]> => {
    "nestedMap": [{"owner": {"login": "sam", "id": 42.0}}],
    "list": [{"labels": ["alpha", "beta"]}],
    "emptyNested": [{"owner": {}, "labels": []}],
    "listOfLists": [{"matrix": [[1.0, 2.0], ["a", true]]}],
    "listOfMaps": [{"members": [{"name": "sam"}, {"name": "ann", "active": false}]}],
    "deepMix": [
        {
            "event": {
                "actor": {"login": "sam", "roles": ["admin", "dev"]},
                "address": {"street": "Main", "city": "Colombo", "geo": {"lat": 6.9, "lng": 79.8}},
                "tags": [{"key": "env", "values": ["prod", "dr"]}]
            }
        }
    ]
};
