package org.simpl4.addons.zookeeper;

import java.io.File;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ZookeeperServiceImpl extends ZooKeeperServerMain implements BundleActivator, ZookeeperService {
	private static final Logger log = LoggerFactory.getLogger(ZookeeperService.class);

	private Thread thread;
	private ServerConfig config;

	public void start(BundleContext context) {
		System.out.println("ZookeeperService activate");
		log.info("ZookeeperService activate");
		config = new ServerConfig();
		config.parse(new String[] { "2181", new File("data/zookeeper").getAbsolutePath() });
		thread = new Thread(this::zk, "org.simpl4.addons.zookeeper");
		thread.start();
	}

	public void stop(BundleContext context) {
		shutdown();
		thread.interrupt();
	}

	public void zk() {
		try {
			System.out.println("ZookeeperService starting");
			log.info("ZookeeperService starting");
			runFromConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ZookeeperService:", e);
		}
		log.info("ZookeeperService exiting");
		System.out.println("ZookeeperService exiting");
	}
}

