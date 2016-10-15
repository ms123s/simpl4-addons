#!/bin/sh

mvn  exec:exec -Dexec.executable="java" -Dexec.workingdir="." -Dexec.args="-cp target/classes:target/dependency/* com.dataartisans.ReadFromKafka  --topic test --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181/kafka --group.id myGroup"
