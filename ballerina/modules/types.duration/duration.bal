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

# Context representation record of a duration stream.
#
# + content - Stream of duration(`time:Seconds`) values
# + headers - The headers map
public type ContextDurationStream record {|
    stream<time:Seconds, error?> content;
    map<string|string[]> headers;
|};

# Context representation record of a duration.
#
# + content - Time duration represented using `time:Seconds`. The `time:Seconds` is a subtype of Ballerina `decimal` type.
# + headers - The headers map
public type ContextDuration record {|
    time:Seconds content;
    map<string|string[]> headers;
|};
