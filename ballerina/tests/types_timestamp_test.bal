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

import ballerina/time;
import ballerina/test;
import ballerina/protobuf.types.timestamp;

@test:Config {}
isolated function testTimestamp() {
    time:Utc[] timeUtcArray = [[1629435635, 0.970213000], [1629435691, 0.629909000], [1629435768, 0.118879000],
                               [1629435786, 0.952124000]];
    stream<time:Utc> timeUtcStream = timeUtcArray.toStream();
    timestamp:TimestampStream outputStream = new timestamp:TimestampStream(timeUtcStream);

    test:assertEquals(outputStream.next(), {"value": timeUtcArray[0]});
    test:assertEquals(outputStream.next(), {"value": timeUtcArray[1]});
    test:assertEquals(outputStream.next(), {"value": timeUtcArray[2]});

    var result = outputStream.close();
    test:assertFalse(result is error);

    timestamp:ContextTimestamp contextTimestamp = {content: [1629435635, 0.970213000],
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextTimestamp.content, <[int, decimal] & readonly>[1629435635, 0.970213000]);

    timestamp:ContextTimestampStream contextTimestampStream = {content: timeUtcStream,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextTimestampStream.headers, {h1: ["bar", "baz"], h2: ["bar2", "baz2"]});
}
