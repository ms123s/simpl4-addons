#!/bin/sh

export MAVEN_OPTS="-Xmx2048m"
mvn  clean install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true
