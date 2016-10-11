#!/bin/sh

mvn  exec:exec -Dexec.executable="java" -Dexec.workingdir="." -Dexec.args="-cp target/classes:target/dependency/* org.myorg.quickstart.WordCount  localhost 6322 target/quickstart-0.1.jar "
