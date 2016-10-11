#!/bin/sh

mvn  exec:exec -Dexec.executable="java" -Dexec.workingdir="." -Dexec.args="-cp target/classes:target/dependency/* org.simpl4.karaf.flinkfeature.WordCount  localhost 6322 target/flinkfeature-0.1.jar "
