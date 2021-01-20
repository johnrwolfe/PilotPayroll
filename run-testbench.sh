#!/bin/bash
CIERA_VERSION=2.3.1-RELEASE
CLASSPATH=$HOME/.m2/repository/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$HOME/.m2/repository/org/json/json/20180813/json-20180813.jar:$HOME/.m2/repository/org/xtuml/AutomatedTestbench/1.0.0-SNAPSHOT/AutomatedTestbench-1.0.0-SNAPSHOT.jar
java -cp $CLASSPATH -jar $HOME/.m2/repository/org/xtuml/AutomatedTestbench/1.0.0-SNAPSHOT/AutomatedTestbench-1.0.0-SNAPSHOT.jar