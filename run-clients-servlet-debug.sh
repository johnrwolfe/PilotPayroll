#!/bin/bash
CIERA_VERSION=2.1.1-SNAPSHOT
CLASSPATH=$HOME/.m2/repository/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$HOME/.m2/repository/org/json/json/20180813/json-20180813.jar:$HOME/.m2/repository/org/xtuml/PayrollDeployment/1.0.0-SNAPSHOT/PayrollDeployment-1.0.0-SNAPSHOT.jar
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=y -cp $CLASSPATH -jar $HOME/.m2/repository/org/xtuml/PayrollDeployment/1.0.0-SNAPSHOT/PayrollDeployment-1.0.0-SNAPSHOT.jar