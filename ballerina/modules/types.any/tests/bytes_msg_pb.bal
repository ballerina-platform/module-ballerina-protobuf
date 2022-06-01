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

import protobuf;

const string ROOT_DESCRIPTOR_BYTES_MSG = "0A0F62797465735F6D73672E70726F746F22390A19416E6E6F74617465644D657373616765576974684279746573121C0A0962797465734461746118012001280C5209627974657344617461226A0A1E416E6E6F74617465644D6573736167655769746842797465734F6E656F66121E0A0962797465734461746118012001280C4800520962797465734461746112200A0A737472696E67446174611802200128094800520A737472696E674461746142060A0464617461620670726F746F33";

@protobuf:Descriptor {
    value: ROOT_DESCRIPTOR_BYTES_MSG
}
public type AnnotatedMessageWithBytes record {|
    byte[] bytesData = [];
|};

@protobuf:Descriptor {
    value: ROOT_DESCRIPTOR_BYTES_MSG
}
public type AnnotatedMessageWithBytesOneof record {|
    byte[] bytesData?;
    string stringData?;
|};

isolated function isValidAnnotatedmessagewithbytesoneof(AnnotatedMessageWithBytesOneof r) returns boolean {
    int dataCount = 0;
    if !(r?.bytesData is ()) {
        dataCount += 1;
    }
    if !(r?.stringData is ()) {
        dataCount += 1;
    }
    if (dataCount > 1) {
        return false;
    }
    return true;
}

isolated function setAnnotatedMessageWithBytesOneof_BytesData(AnnotatedMessageWithBytesOneof r, byte[] bytesData) {
    r.bytesData = bytesData;
    _ = r.removeIfHasKey("stringData");
}

isolated function setAnnotatedMessageWithBytesOneof_StringData(AnnotatedMessageWithBytesOneof r, string stringData) {
    r.stringData = stringData;
    _ = r.removeIfHasKey("bytesData");
}

