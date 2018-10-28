#!/bin/sh

# If variable not present use default values
: ${CORDA_HOME:=/opt/corda/src/build/nodes/${NODE_NAME}}
: ${JAVA_OPTIONS:=-Xmx512m}

export CORDA_HOME JAVA_OPTIONS

cd ${CORDA_HOME}
#java $JAVA_OPTIONS -jar ${CORDA_HOME}/corda-webserver.jar 2>&1 &
#java $JAVA_OPTIONS -jar ${CORDA_HOME}/corda.jar 2>&1

if [ $NODE_NAME = "Notary" ]
then
   java $JAVA_OPTIONS -jar corda.jar
else
   nohup java $JAVA_OPTIONS -jar corda.jar & java $JAVA_OPTIONS -jar corda-webserver.jar
fi
