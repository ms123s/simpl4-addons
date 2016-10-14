package org.simpl4.addons.zookeeper;

import java.io.File;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;

public class ZookeeperServiceImpl extends ZooKeeperServerMain implements BundleActivator, ZookeeperService {

	private Thread thread;
	private ServerConfig config;

	public void start(BundleContext context) {
		System.out.println("ZookeeperService activate");
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
			runFromConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ZookeeperService exiting");
	}
}

