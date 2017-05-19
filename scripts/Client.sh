#!/bin/sh

javac -d ../bin -cp ../\*: ../src/*.java
java -cp ../\*: Client $1 $2
