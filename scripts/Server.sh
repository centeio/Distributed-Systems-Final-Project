#!/bin/sh

javac -d ../bin -cp ../\*: ../src/*.java
java -cp ../\*: Server $1 $2
