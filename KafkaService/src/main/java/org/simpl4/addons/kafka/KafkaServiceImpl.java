package org.simpl4.addons.kafka;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Bundle;
//import org.osgi.framework.wiring.BundleWiring;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import java.util.Properties;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class KafkaServiceImpl implements BundleActivator, KafkaService {
	private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
	public KafkaServerStartable kafka;

	public void start(BundleContext context) {
		Thread.currentThread().setContextClassLoader(KafkaServiceImpl.class.getClassLoader());
		System.out.println("KafkaService activate.cl:"+KafkaServiceImpl.class.getClassLoader());
		log.info("KafkaService activate");
		Properties kafkaProperties = getProperties();
		KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);
		kafka = new KafkaServerStartable(kafkaConfig);
		System.out.println("KafkaService:starting kafka broker...");
		log.info("KafkaService:starting kafka broker...");
		kafka.startup();
		System.out.println("KafkaService:kafka broker starting done");
		log.info("KafkaService:kafka broker starting done");
	}

	public void stop(BundleContext context) {
		System.out.println("KafkaService.stopping kafka...");
		log.info("KafkaService.stopping kafka...");
		kafka.shutdown();
		System.out.println("done");
	}

	private Properties getProperties(){
		Properties props = new Properties();
		props.setProperty("broker.id","0");
		props.setProperty("listeners","PLAINTEXT://:9092");
		props.setProperty("num.network.threads","3");
		props.setProperty("num.io.threads","8");
		props.setProperty("socket.send.buffer.bytes","102400");
		props.setProperty("socket.receive.buffer.bytes","102400");
		props.setProperty("socket.request.max.bytes","104857600");
		props.setProperty("log.dirs","data/kafka-logs");
		props.setProperty("num.partitions","1");
		props.setProperty("num.recovery.threads.per.data.dir","1");
		props.setProperty("log.retention.hours","168");
		props.setProperty("log.segment.bytes","1073741824");
		props.setProperty("log.retention.check.interval.ms","300000");
		props.setProperty("zookeeper.connect","localhost:2181/kafka");
		props.setProperty("zookeeper.connection.timeout.ms","6000");
		return props;
	}
/*	private ClassLoader getBundleClassLoader(BundleContext context){
		Bundle bundle = context.getBundle();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		return bundleWiring.getClassLoader();
	}*/
}

