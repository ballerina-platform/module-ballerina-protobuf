# Specification: Ballerina Protobuf Library

_Owners_: @daneshk @MadhukaHarith92  
_Reviewers_: @daneshk  
_Created_: 2021/11/17  
_Updated_: 2021/11/17  
_Issue_: [#2343](https://github.com/ballerina-platform/ballerina-standard-library/issues/2343)

# Introduction
The Protobuf library has APIs to represent a set of pre-defined protobuf types. It is part of Ballerina Standard Library. [Ballerina programming language](https://ballerina.io/) is an open-source programming language for the cloud that makes it easier to use, combine, and create network services.

# Contents

1. [Overview](#1-overview)
2. [Wrappers](#2-wrappers)
    * 2.1. [String](#21-string)
    * 2.2. [Int](#22-int)
    * 2.3. [Float](#23-float)
    * 2.4. [boolean](#24-boolean)
    * 2.5. [bytes](#25-bytes)
3. [Duration](#3-duration)
4. [Struct](#4-struct)
5. [Timestamp](#5-timestamp)
6. [Empty](#6-empty)
7. [Any](#7-any)

## 1. Overview
This specification elaborates on the pre-defined record types and functions available in the Protobuf library.

## 2. Wrappers 
This provides APIs to represent `google/protobuf/wrappers.proto`. The protobuf module supports 5 wrapper types; `string`, `int`, `float`, `boolean`, and `bytes`.

### 2.1. String type
The `ContextStringStream` is a context representation record of a string stream.
```
public type ContextStringStream record {|
    stream<string, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextString` is a context representation record of a string value.
```
public type ContextString record {|
    string content;
    map<string|string[]> headers;
|};
```

### 2.2. Integer type
The `ContextIntStream` is a context representation record of an integer stream.
```
public type ContextIntStream record {|
    stream<int, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextInt` is a context representation record of an integer value.
```
public type ContextInt record {|
    int content;
    map<string|string[]> headers;
|};
```

### 2.3. Float type
The `ContextFloatStream` is a context representation record of a float stream.
```
public type ContextFloatStream record {|
    stream<float, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextFloat` is a context representation record of a float value.
```
public type ContextFloat record {|
    float content;
    map<string|string[]> headers;
|};
```

### 2.4. Boolean type
The `ContextBooleanStream` is a context representation record of a boolean stream.
```
public type ContextBooleanStream record {|
    stream<boolean, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextBoolean` is a context representation record of a boolean value.
```
public type ContextBoolean record {|
    boolean content;
    map<string|string[]> headers;
|};
```

### 2.5. Bytes type
The `ContextBytesStream` is a context representation record of a byte array stream.
```
public type ContextBytesStream record {|
    stream<byte[], error?> content;
    map<string|string[]> headers;
|};
```

The `ContextBytes` is a context representation record of a byte array.
```
public type ContextBytes record {|
    byte[] content;
    map<string|string[]> headers;
|};
```

## 3. Duration 
This provides APIs to represent `google/protobuf/duration.proto`.

The `ContextDurationStream` is a context representation record of a duration stream.
```
public type ContextDurationStream record {|
    stream<time:Seconds, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextDuration` is a context representation record of a duration. The content is a time duration represented using `time:Seconds`. The `time:Seconds` is a subtype of Ballerina `decimal` type.
```
public type ContextDuration record {|
    time:Seconds content;
    map<string|string[]> headers;
|};
```

## 4. Struct
This provides APIs to represent `google/protobuf/struct.proto`.

The `ContextStructStream` is a context representation record of a struct stream.
```
public type ContextStructStream record {|
    stream<map<anydata>, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextStruct` is a representation record of a struct.
```
public type ContextStruct record {|
    map<anydata> content;
    map<string|string[]> headers;
|};
```

## 5. Timestamp
This provides APIs to represent `google/protobuf/timestamp.proto`.

The `ContextTimestampStream` is a context representation record of a timestamp stream.
```
public type ContextTimestampStream record {|
    stream<time:Utc, error?> content;
    map<string|string[]> headers;
|};
```

The `ContextTimestamp` is a representation record of a timestamp.
```
public type ContextTimestamp record {|
    time:Utc content;
    map<string|string[]> headers;
|};
```

## 6. Empty
This provides APIs to represent `google/protobuf/empty.proto`.

The `ContextNil` is a representation record of a gRPC Empty message.
```
public type ContextNil record {|
    map<string|string[]> headers;
|};
```

`Empty` represents an empty record.
```
public type Empty record {|
|};
```

## 7. Any
