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

import ballerina/jballerina.java;
import ballerina/time;
import ballerina/protobuf;

# Subtypes that are allowed as Any type.
public type ValueType int|float|string|boolean|time:Utc|time:Seconds|record {}|()|byte[]|map<anydata>;

# Type descriptor of ValueType.
public type ValueTypeDesc typedesc<ValueType>;

# Generic error of protobuf.types.'any submodule.
public type Error distinct protobuf:Error;

# Type mismatch error that returns when a user specifies an incorrect type.
public type TypeMismatchError distinct Error;

# Represent protobuf `Any` type.
#
# + typeUrl - The URL identifier of the message
# + value - The Any data message
public type Any record {|
    string typeUrl = "";
    ValueType value = ();
|};

# Context representation of the `Any` type.
#
# + content - Any data content
# + headers - Header values
public type ContextAny record {|
    Any content;
    map<string|string[]> headers;
|};

# Streaming representation of the `Any` type.
#
# + content - Any data stream
# + headers - Header values
public type ContextAnyStream record {|
    stream<Any, error?> content;
    map<string|string[]> headers;
|};

# Generate and return the generic `'any:Any` record that is used to represent protobuf `Any` type.
#
# + message - The record or the scalar value to be packed as Any type
# + return - Any value representation of the given message or an error
public isolated function pack(ValueType message) returns Any|Error {
    string typeUrl = "type.googleapis.com/" + getUrlSuffixFromValue(message);
    string content = check getSerializedString(message, typeUrl);
    return {typeUrl: typeUrl, value: content};
}

# Unpack and return the specified Ballerina value
#
# + anyValue - Any value to be unpacked
# + targetTypeOfAny - Type descriptor of the return value
# + return - Return a value of the given type
public isolated function unpack(Any anyValue, ValueTypeDesc targetTypeOfAny = <>) returns targetTypeOfAny|Error = @java:Method {
    'class: "io.ballerina.stdlib.protobuf.nativeimpl.AnyTypeCreator"
} external;

isolated function getSerializedString(ValueType message, string typeUrl) returns string|Error = @java:Method {
    'class: "io.ballerina.stdlib.protobuf.nativeimpl.AnyTypeCreator"
} external;

# Return the Any type URL from the given Ballerina type.
#
# + anyMessage - Any type message
# + return - Type URL suffix of the given message type
isolated function getUrlSuffixFromValue(ValueType anyMessage) returns string {
    if anyMessage is float {
        return "google.protobuf.FloatValue";
    } else if anyMessage is int {
        return "google.protobuf.Int64Value";
    } else if anyMessage is string {
        return "google.protobuf.StringValue";
    } else if anyMessage is byte[] {
        return "google.protobuf.BytesValue";
    } else if anyMessage is boolean {
        return "google.protobuf.BoolValue";
    } else if anyMessage is time:Utc {
        return "google.protobuf.Timestamp";
    } else if anyMessage is time:Seconds {
        return "google.protobuf.Duration";
    } else if anyMessage is () {
        return "google.protobuf.Empty";
    } else if anyMessage is record {||} {
        return "google.protobuf.Empty";
    } else {
        return externGetNameFromRecord(<record {}>anyMessage);
    }
}

isolated function externGetNameFromRecord(record {} rec) returns string = @java:Method {
    'class: "io.ballerina.stdlib.protobuf.nativeimpl.AnyTypeCreator",
    name: "externGetNameFromRecord"
} external;
