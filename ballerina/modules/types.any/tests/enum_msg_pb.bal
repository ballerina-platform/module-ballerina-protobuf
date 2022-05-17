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

const string ROOT_DESCRIPTOR_ENUM_MSG = "0A0E656E756D5F6D73672E70726F746F22410A18416E6E6F74617465644D65737361676557697468456E756D12250A08656E756D4461746118012001280E32092E456E756D446174615208656E756D4461746122490A20416E6E6F74617465644D657373616765576974685265706561746564456E756D12250A08656E756D4461746118012003280E32092E456E756D446174615208656E756D4461746122720A1D416E6E6F74617465644D65737361676557697468456E756D4F6E656F6612270A08656E756D4461746118012001280E32092E456E756D4461746148005208656E756D4461746112200A0A737472696E67446174611802200128094800520A737472696E674461746142060A04646174612A370A08456E756D44617461120D0A09656E756D44617461301000120D0A09656E756D44617461311001120D0A09656E756D44617461321002620670726F746F33";

@protobuf:Descriptor {
    value: ROOT_DESCRIPTOR_ENUM_MSG
}
public type AnnotatedMessageWithEnumOneof record {|
    EnumData enumData?;
    string stringData?;
|};

isolated function isValidAnnotatedmessagewithenumoneof(AnnotatedMessageWithEnumOneof r) returns boolean {
    int dataCount = 0;
    if !(r?.enumData is ()) {
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

isolated function setAnnotatedMessageWithEnumOneof_EnumData(AnnotatedMessageWithEnumOneof r, EnumData enumData) {
    r.enumData = enumData;
    _ = r.removeIfHasKey("stringData");
}

isolated function setAnnotatedMessageWithEnumOneof_StringData(AnnotatedMessageWithEnumOneof r, string stringData) {
    r.stringData = stringData;
    _ = r.removeIfHasKey("enumData");
}

@protobuf:Descriptor {
    value: ROOT_DESCRIPTOR_ENUM_MSG
}
public type AnnotatedMessageWithRepeatedEnum record {|
    EnumData[] enumData = [];
|};

@protobuf:Descriptor {
    value: ROOT_DESCRIPTOR_ENUM_MSG
}
public type AnnotatedMessageWithEnum record {|
    EnumData enumData = enumData0;
|};

public enum EnumData {
    enumData0,
    enumData1,
    enumData2
}

