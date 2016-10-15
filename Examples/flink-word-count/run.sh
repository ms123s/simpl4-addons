#!/bin/sh

mvn  exec:exec -Dexec.executable="java" -Dexec.workingdir="." -Dexec.args="-cp target/classes:target/dependency/* org.simpl4.addons.examples.WordCount  localhost 6322 target/wordcount-0.1.jar "
