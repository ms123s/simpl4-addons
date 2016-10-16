package org.simpl4.addons.flink

import akka.osgi.ActorSystemActivator
import akka.osgi.OsgiActorSystemFactory
import akka.actor.{Props, ActorSystem}
import akka.event.{LogSource, Logging}
import org.osgi.framework.{ServiceRegistration, BundleContext}
import com.typesafe.config.{ConfigFactory, Config}
import org.apache.flink.runtime.akka.{AkkaUtils, ListeningBehaviour}
import org.apache.flink.runtime.jobmanager.JobManager;
import org.apache.flink.runtime.jobmanager.MemoryArchivist;
import org.apache.flink.runtime.jobmanager.JobManagerMode;
import org.apache.flink.runtime.util.LeaderRetrievalUtils;
import org.apache.flink.runtime.webmonitor.{WebMonitor, WebMonitorUtils}
import org.apache.flink.configuration.{ConfigConstants, Configuration, GlobalConfiguration}
import org.apache.flink.runtime.taskmanager.TaskManager
import org.apache.flink.runtime.clusterframework.types.ResourceID
import java.util.Properties;
import java.io.FileReader;

class Activator extends ActorSystemActivator {

  import Activator._

  def configure(context: BundleContext, system: ActorSystem) {}

  private def startJobManager(context: BundleContext, configuration: Configuration, system: ActorSystem) {
    val log = Logging(system, this);
    log.info("Flink bundle configured:" + configuration);
    println("Flink bundle configured");
    registerService(context, system);
    JobManager.startJobManagerActors(configuration, system, classOf[JobManager], classOf[MemoryArchivist]);
    log.info("Flink service registered");
    println("Flink service registered");
    startWeb(configuration, system);
    val taskManagerListeningAddress = "localhost";
    startTask(configuration, system, taskManagerListeningAddress);
    println("Flink WebMonitor startet");
  }

  override def start(context: BundleContext): Unit = {
		val properties = new Properties();
		properties.load( new FileReader( "etc/flink.properties" ));
    val configuration = new Configuration();
		//configuration.addAllToProperties(properties);
    val ipcaddress = properties.getProperty(ConfigConstants.JOB_MANAGER_IPC_ADDRESS_KEY, "localhost");
    val ipcport = properties.getProperty(ConfigConstants.JOB_MANAGER_IPC_PORT_KEY, "6322").toInt;
    val webport = properties.getProperty(ConfigConstants.JOB_MANAGER_WEB_PORT_KEY, "8082").toInt;
    val numTaskSlots = properties.getProperty(ConfigConstants.TASK_MANAGER_NUM_TASK_SLOTS, "3").toInt;
    val parallelDefault = properties.getProperty(ConfigConstants.DEFAULT_PARALLELISM_KEY, "2").toInt;
    println("FlinkService.ipc:"+ipcaddress+"/"+ipcport+"/"+webport);
    println("FlinkService.tasks:"+numTaskSlots+"/"+parallelDefault);

    configuration.setInteger(ConfigConstants.JOB_MANAGER_WEB_PORT_KEY, webport);
    configuration.setString(ConfigConstants.JOB_MANAGER_IPC_ADDRESS_KEY, ipcaddress);
    configuration.setInteger(ConfigConstants.JOB_MANAGER_IPC_PORT_KEY, ipcport)
    configuration.setInteger(ConfigConstants.TASK_MANAGER_NUM_TASK_SLOTS, numTaskSlots)
    configuration.setInteger(ConfigConstants.DEFAULT_PARALLELISM_KEY , parallelDefault)
    val akkaConfig = AkkaUtils.getAkkaConfig(configuration, Some(ipcaddress, ipcport))
    val system = Some(OsgiActorSystemFactory(context, akkaConfig).createActorSystem(Option(getActorSystemName(context))))
    system foreach (addLogServiceListener(context, _))
    system foreach (startJobManager(context, configuration, _))
  }
  override def stop(context: BundleContext) {
    println("Flink service stopped")
    super.stop(context)
  }

  private def startWeb(configuration: Configuration, system: ActorSystem) {
    val leaderRetrievalService = LeaderRetrievalUtils.createLeaderRetrievalService(configuration);
    println("LeaderRetrievalUtils:" + leaderRetrievalService);
    val webServer = WebMonitorUtils.startWebRuntimeMonitor(configuration, leaderRetrievalService, system);
    println("webServer:" + webServer)
    val webMonitor: Option[WebMonitor] = Option(webServer);
    // start web monitor
    webMonitor.foreach { monitor =>
      val jobManagerAkkaUrl = JobManager.getRemoteJobManagerAkkaURL(configuration)
      println("jobManagerAkkaUrl:" + jobManagerAkkaUrl)
      monitor.start(jobManagerAkkaUrl)
    }
  }
  private def startTask(configuration: Configuration, system: ActorSystem, listeningAddress:String) {
     val taskManagerActor = TaskManager.startTaskManagerComponentsAndActor(
       configuration,
       ResourceID.generate(),
       system,
       listeningAddress,
       Some(TaskManager.TASK_MANAGER_NAME),
       None,
       localTaskManagerCommunication = true,
       classOf[TaskManager])
  }
  override def getActorSystemName(context: BundleContext): String = "flink"
}

object Activator {
  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = o.getClass.getName
    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }
}
