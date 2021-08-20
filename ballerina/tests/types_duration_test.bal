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
import ballerina/protobuf.types.duration;

@test:Config {}
isolated function testDuration() {
    time:Seconds[] timeSecondsArray = [13920.47174281, 13925.537172236, 13949.130786649, 14268.816789336];
    stream<time:Seconds> timeUtcStream = timeSecondsArray.toStream();
    duration:DurationStream outputStream = new duration:DurationStream(timeUtcStream);

    test:assertEquals(outputStream.next(), {"value": timeSecondsArray[0]});
    test:assertEquals(outputStream.next(), {"value": timeSecondsArray[1]});
    test:assertEquals(outputStream.next(), {"value": timeSecondsArray[2]});

    var result = outputStream.close();
    test:assertFalse(result is error);

    duration:ContextDuration contextDuration = {content: 13920.47174281,
                                   headers: {h1: ["bar", "baz"], h2: ["bar2", "baz2"]}};
    test:assertEquals(contextDuration.content, <decimal>13920.47174281);
}
