#!/bin/sh

mvn  exec:exec -Dexec.executable="java" -Dexec.workingdir="." -Dexec.args="-cp target/classes:target/dependency/* com.dataartisans.WriteIntoKafka  --topic test --bootstrap.servers localhost:9092"
