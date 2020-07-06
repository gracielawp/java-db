# Java Database System
Homebrew Java-based database system that accepts SQL queries. This system stores data in .csv format for readability. No external libraries were used to create this system.

## Usage
Run these commands on two different terminals:
```
$ java DBServer
$ java DBClient
```
The server can accept multiple clients and handle sudden client disconnection. Once the server is running, you can enter SQL queries in the client.

## SQL Syntax
BNF grammar for accepted SQL queries:
```
<Command>        ::=  <CommandType>;

<CommandType>    ::=  <Use> | <Create> | <Drop> | <Alter> | <Insert> |
                      <Select> | <Update> | <Delete> | <Join>

<Use>            ::=  USE <DatabaseName>

<Create>         ::=  <CreateDatabase> | <CreateTable>

<CreateDatabase> ::=  CREATE DATABASE <DatabaseName>

<CreateTable>    ::=  CREATE TABLE <TableName> | CREATE TABLE <TableName> ( <AttributeList> )

<Drop>           ::=  DROP <Structure> <StructureName>

<Structure>      ::=  DATABASE | TABLE

<Alter>          ::=  ALTER TABLE <TableName> <AlterationType> <AttributeName>

<Insert>         ::=  INSERT INTO <TableName> VALUES ( <ValueList> )

<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
                      SELECT <WildAttribList> FROM <TableName> WHERE <Condition> 

<Update>         ::=  UPDATE <TableName> SET <NameValueList> WHERE <Condition> 

<Delete>         ::=  DELETE FROM <TableName> WHERE <Condition>

<Join>           ::=  JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>

<NameValueList>  ::=  <NameValuePair> | <NameValuePair> , <NameValueList>

<NameValuePair>  ::=  <AttributeName> = <Value>

<AlterationType> ::=  ADD | DROP

<ValueList>      ::=  <Value>  |  <Value> , <ValueList>

<Value>          ::=  '<StringLiteral>'  |  <BooleanLiteral>  |  <FloatLiteral>  |  <IntegerLiteral>

<BooleanLiteral> ::=  true | false

<WildAttribList> ::=  <AttributeList> | *

<AttributeList>  ::=  <AttributeName> | <AttributeName> , <AttributeList>

<Condition>      ::=  ( <Condition> ) AND ( <Condition> )  |
                      ( <Condition> ) OR ( <Condition> )   |
                      <AttributeName> <Operator> <Value>

<Operator>       ::=   ==   |   >   |   <   |   >=   |   <=   |   !=   |   LIKE
```

## Disclaimer
This work was submitted for the COMSM0103 Object Oriented Programming with Java at University of Bristol.
