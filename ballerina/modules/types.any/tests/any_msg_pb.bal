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

const string ANNOTATED_MSG_DESC = "0A0D616E795F6D73672E70726F746F228D030A1B416E6E6F74617465644D6573736167655769746852657065617473121E0A0A646F75626C6544617461180120032801520A646F75626C6544617461121C0A09666C6F6174446174611802200328025209666C6F617444617461121E0A0A75496E7433324461746118032003280D520A75496E74333244617461121E0A0A75496E74363444617461180420032804520A75496E74363444617461121C0A09696E743332446174611805200328055209696E74333244617461121C0A09696E743634446174611806200328035209696E7436344461746112200A0B6669786564333244617461180720032807520B666978656433324461746112200A0B6669786564363444617461180820032806520B666978656436344461746112200A0B626F6F6C65616E44617461180920032808520B626F6F6C65616E44617461121E0A0A737472696E6744617461180A20032809520A737472696E6744617461122E0A0B6D65737361676544617461180B2003280B320C2E4D65737361676544617461520B6D657373616765446174612282030A10416E6E6F74617465644D657373616765121E0A0A646F75626C6544617461180120012801520A646F75626C6544617461121C0A09666C6F6174446174611802200128025209666C6F617444617461121E0A0A75496E7433324461746118032001280D520A75496E74333244617461121E0A0A75496E74363444617461180420012804520A75496E74363444617461121C0A09696E743332446174611805200128055209696E74333244617461121C0A09696E743634446174611806200128035209696E7436344461746112200A0B6669786564333244617461180720012807520B666978656433324461746112200A0B6669786564363444617461180820012806520B666978656436344461746112200A0B626F6F6C65616E44617461180920012808520B626F6F6C65616E44617461121E0A0A737472696E6744617461180A20012809520A737472696E6744617461122E0A0B6D65737361676544617461180B2001280B320C2E4D65737361676544617461520B6D6573736167654461746122A9030A19416E6E6F74617465644D657373616765576974684F6E656F6612200A0A646F75626C65446174611801200128014800520A646F75626C6544617461121E0A09666C6F61744461746118022001280248005209666C6F61744461746112200A0A75496E7433324461746118032001280D4800520A75496E7433324461746112200A0A75496E743634446174611804200128044800520A75496E74363444617461121E0A09696E7433324461746118052001280548005209696E74333244617461121E0A09696E7436344461746118062001280348005209696E7436344461746112220A0B66697865643332446174611807200128074800520B666978656433324461746112220A0B66697865643634446174611808200128064800520B666978656436344461746112220A0B626F6F6C65616E446174611809200128084800520B626F6F6C65616E4461746112200A0A737472696E6744617461180A200128094800520A737472696E674461746112300A0B6D65737361676544617461180B2001280B320C2E4D657373616765446174614800520B6D6573736167654461746142060A046461746122310A0B4D6573736167654461746112220A0C6D6573736167654461746131180120012809520C6D65737361676544617461312A370A08456E756D44617461120D0A09656E756D44617461301000120D0A09656E756D44617461311001120D0A09656E756D44617461321002620670726F746F33";

type mapTypeForTest map<anydata>;

type bytesTypeForTest byte[];

@protobuf:Descriptor {
    value: ANNOTATED_MSG_DESC
}
public type AnnotatedMessageWithOneof record {|
    float doubleData?;
    float floatData?;
    int uInt32Data?;
    int uInt64Data?;
    int int32Data?;
    int int64Data?;
    int fixed32Data?;
    int fixed64Data?;
    boolean booleanData?;
    string stringData?;
    MessageData messageData?;
|};

isolated function isValidAnnotatedmessagewithoneof(AnnotatedMessageWithOneof r) returns boolean {
    int dataCount = 0;
    if !(r?.doubleData is ()) {
        dataCount += 1;
    }
    if !(r?.floatData is ()) {
        dataCount += 1;
    }
    if !(r?.uInt32Data is ()) {
        dataCount += 1;
    }
    if !(r?.uInt64Data is ()) {
        dataCount += 1;
    }
    if !(r?.int32Data is ()) {
        dataCount += 1;
    }
    if !(r?.int64Data is ()) {
        dataCount += 1;
    }
    if !(r?.fixed32Data is ()) {
        dataCount += 1;
    }
    if !(r?.fixed64Data is ()) {
        dataCount += 1;
    }
    if !(r?.booleanData is ()) {
        dataCount += 1;
    }
    if !(r?.stringData is ()) {
        dataCount += 1;
    }
    if !(r?.messageData is ()) {
        dataCount += 1;
    }
    if (dataCount > 1) {
        return false;
    }
    return true;
}

isolated function setAnnotatedMessageWithOneof_DoubleData(AnnotatedMessageWithOneof r, float doubleData) {
    r.doubleData = doubleData;
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_FloatData(AnnotatedMessageWithOneof r, float floatData) {
    r.floatData = floatData;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_UInt32Data(AnnotatedMessageWithOneof r, int uInt32Data) {
    r.uInt32Data = uInt32Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_UInt64Data(AnnotatedMessageWithOneof r, int uInt64Data) {
    r.uInt64Data = uInt64Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_Int32Data(AnnotatedMessageWithOneof r, int int32Data) {
    r.int32Data = int32Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_Int64Data(AnnotatedMessageWithOneof r, int int64Data) {
    r.int64Data = int64Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_Fixed32Data(AnnotatedMessageWithOneof r, int fixed32Data) {
    r.fixed32Data = fixed32Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_Fixed64Data(AnnotatedMessageWithOneof r, int fixed64Data) {
    r.fixed64Data = fixed64Data;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_BooleanData(AnnotatedMessageWithOneof r, boolean booleanData) {
    r.booleanData = booleanData;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("stringData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_StringData(AnnotatedMessageWithOneof r, string stringData) {
    r.stringData = stringData;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("messageData");
}

isolated function setAnnotatedMessageWithOneof_MessageData(AnnotatedMessageWithOneof r, MessageData messageData) {
    r.messageData = messageData;
    _ = r.removeIfHasKey("doubleData");
    _ = r.removeIfHasKey("floatData");
    _ = r.removeIfHasKey("uInt32Data");
    _ = r.removeIfHasKey("uInt64Data");
    _ = r.removeIfHasKey("int32Data");
    _ = r.removeIfHasKey("int64Data");
    _ = r.removeIfHasKey("fixed32Data");
    _ = r.removeIfHasKey("fixed64Data");
    _ = r.removeIfHasKey("booleanData");
    _ = r.removeIfHasKey("stringData");
}

@protobuf:Descriptor {
    value: ANNOTATED_MSG_DESC
}
public type MessageData record {|
    string messageData1 = "";
|};

@protobuf:Descriptor {
    value: ANNOTATED_MSG_DESC
}
public type AnnotatedMessage record {|
    float doubleData = 0.0;
    float floatData = 0.0;
    int uInt32Data = 0;
    int uInt64Data = 0;
    int int32Data = 0;
    int int64Data = 0;
    int fixed32Data = 0;
    int fixed64Data = 0;
    boolean booleanData = false;
    string stringData = "";
    MessageData messageData = {};
|};

@protobuf:Descriptor {
    value: ANNOTATED_MSG_DESC
}
public type AnnotatedMessageWithRepeats record {|
    float[] doubleData = [];
    float[] floatData = [];
    int[] uInt32Data = [];
    int[] uInt64Data = [];
    int[] int32Data = [];
    int[] int64Data = [];
    int[] fixed32Data = [];
    int[] fixed64Data = [];
    boolean[] booleanData = [];
    string[] stringData = [];
    MessageData[] messageData = [];
|};

public type MessageWithoutAnnotation record {|
    float doubleData = 0.0;
    float floatData = 0.0;
    int uInt32Data = 0;
    int uInt64Data = 0;
    int int32Data = 0;
    int int64Data = 0;
    int fixed32Data = 0;
    int fixed64Data = 0;
    boolean booleanData = false;
    string stringData = "";
    MessageData messageData = {};
|};
