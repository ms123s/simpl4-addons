package org.simpl4.addons.zookeeper;

import java.io.File;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleActivator;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.Properties;
import java.io.FileReader;

public class ZookeeperServiceImpl extends ZooKeeperServerMain implements BundleActivator, ZookeeperService {
	private static final Logger log = LoggerFactory.getLogger(ZookeeperService.class);

	private Thread thread;
	private ServerConfig config;

	public void start(BundleContext context) {
		info("ZookeeperService activate");
		config = getConfig();
		//config.parse(new String[] { "2181", new File("data/zookeeper").getAbsolutePath() });
		thread = new Thread(this::zk, "org.simpl4.addons.zookeeper");
		thread.start();
	}

	public void stop(BundleContext context) {
		shutdown();
		thread.interrupt();
	}
	private ServerConfig getConfig(){
		Properties properties = new Properties();
		try{
			properties.load( new FileReader( "etc/zookeeper.properties" ));
		}catch(Exception e){
			throw new RuntimeException("ZookeeperServiceImpl.getProperties:", e);
		}
		QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
		try {
			quorumConfiguration.parseProperties(properties);
		} catch(Exception e) {
			throw new RuntimeException("ZookeeperServiceImpl.getConfig:",e);
		}
		ServerConfig config = new ServerConfig();
		config.readFrom(quorumConfiguration);
		return config;
	}

	private void info(String msg){
		System.out.println(msg);
		log.info(msg);
	}
	public void zk() {
		try {
			info("ZookeeperService starting");
			runFromConfig(config);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("ZookeeperService:", e);
		}
		info("ZookeeperService exiting");
	}
}

