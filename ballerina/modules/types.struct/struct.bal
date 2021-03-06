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

# Context representation record of a struct stream.
#
# + content - Stream of structs(gRPC `Struct` is represented using `map<anydata>`)
# + headers - The headers map
public type ContextStructStream record {|
    stream<map<anydata>, error?> content;
    map<string|string[]> headers;
|};

# Context representation record of a struct.
#
# + content - The struct value represented using `map<anydata>`
# + headers - The headers map
public type ContextStruct record {|
    map<anydata> content;
    map<string|string[]> headers;
|};
