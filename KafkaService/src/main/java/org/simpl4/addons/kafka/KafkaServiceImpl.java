package org.simpl4.addons.kafka;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class KafkaServiceImpl implements BundleActivator, KafkaService {
	private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

	private Thread thread;
	private ServerConfig config;

	public void start(BundleContext context) {
		System.out.println("KafkaService activate");
		log.info("KafkaService activate");
	}

	public void stop(BundleContext context) {
	}

}

