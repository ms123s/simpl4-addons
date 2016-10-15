package org.simpl4.addons.kafka;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.Bundle;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import java.util.Properties;
import java.net.Socket;
import java.io.FileReader;
import java.net.ConnectException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class KafkaServiceImpl implements BundleActivator, KafkaService {
	private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
	public KafkaServerStartable kafka;

	public void start(BundleContext context) {
		Thread.currentThread().setContextClassLoader(KafkaServiceImpl.class.getClassLoader());
		info("KafkaService activate");
		waitForZookeeper("localhost", 2181);
		Properties kafkaProperties = getProperties();
		KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);
		kafka = new KafkaServerStartable(kafkaConfig);
		info("KafkaService:starting kafka broker...");
		kafka.startup();
		info("KafkaService:kafka broker starting done");
	}

	public void stop(BundleContext context) {
		info("KafkaService.stopping kafka...");
		kafka.shutdown();
		info("KafkaService.stopped");
	}

	private void waitForZookeeper(String host, int port) {
		while(true){
			try {
				(new Socket(host, port)).close();
			} catch(Exception e) {
				if( e instanceof ConnectException){
					try{ info("waitForZookeeper.zookeeper not ready:"+e); Thread.sleep( 2000L ); }catch(Exception ex){ }
					continue;
				}
				info("waitForZookeeper.Exception:"+e);
				throw new RuntimeException("waitForZookeeper:", e);//Should not happen;
			}
			info("waitForZookeeper.zookeeper ready");
			return;
		}
	}
	private Properties getProperties(){
		try{
			Properties kafkaProperties = new Properties();
			kafkaProperties.load( new FileReader( "etc/kafkaserver.properties" ));
			return kafkaProperties;
		}catch(Exception e){
			throw new RuntimeException("KafkaServiceImpl.getProperties:", e);
		}
	}

	private void info(String msg){
		System.out.println(msg);
		log.info(msg);
	}
}

