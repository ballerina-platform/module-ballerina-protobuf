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

# Represents a stream of timestamps.
public class TimestampStream {
    private stream<anydata, error?> anydataStream;

    # Initialize the stream.
    #
    # + anydataStream - anydata stream
    public isolated function init(stream<anydata, error?> anydataStream) {
        self.anydataStream = anydataStream;
    }

    # Retrieve the next value of the stream.
    #
    # + return - Returns the next value of the stream or else an error
    public isolated function next() returns record {|time:Utc value;|}|error? {
        var streamValue = self.anydataStream.next();
        if (streamValue is ()) {
            return streamValue;
        } else if (streamValue is error) {
            return streamValue;
        } else {
            record {|time:Utc value;|} nextRecord = {value: <time:Utc>streamValue.value.cloneReadOnly()};
            return nextRecord;
        }
    }

    # Close the stream.
    #
    # + return - Returns an error if falied to close the stream
    public isolated function close() returns error? {
        return self.anydataStream.close();
    }
}

# A context of timestamp stream.
#
# + content - Content stream
# + headers - Headers map
public type ContextTimestampStream record {|
    stream<time:Utc, error?> content;
    map<string|string[]> headers;
|};

# A timestamp context.
#
# + content - Content
# + headers - Headers map
public type ContextTimestamp record {|
    time:Utc content;
    map<string|string[]> headers;
|};
