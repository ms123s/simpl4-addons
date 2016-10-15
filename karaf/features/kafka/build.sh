#!/bin/sh

export MAVEN_OPTS="-Xmx2048m"
mvn  install -Dmaven.javadoc.skip=true -Dmaven.test.skip=true
