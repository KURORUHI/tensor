package ssh.client;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import ssh.weight.OverViewCtlr;
import ssh.weight.SearchPort;

public class conn implements Runnable{
	protected static final Logger logger = LoggerFactory.getLogger(SearchPort.class);
	private String host;
	public static String id;
	public static String passwd;

	public static conn instance;
	public static conn returnInstance() {
		return instance;
	}

	static ChannelExec channelExec;
	static Session session;
	static Channel channel;
	static ChannelSftp channelsftp;
	public void run(){
		instance = this;
		logger.info("connecting.." + host);
		System.out.println(id + " " + passwd);
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(id,host,22);
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		session.setConfig("StrictHostKeyChecking","no");
		session.setPassword(passwd);
		try {
			session.connect();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			channel = session.openChannel("sftp");
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		try {
			channel.connect();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		channelsftp = (ChannelSftp) channel;
		logger.info("==>Connected to" + host);
		try {
			conn.upload("RHDSetup.log", "/home/tensor");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*public static void stopTomcat() throws Exception{
		connect();
		System.out.println("w"+host);
		channelExec.setCommand("mkdir "+"testprogram");
		channelExec.connect();
		System.out.println("==>Stopped Tomcat @"+host);
	}*/
	public static void upload(String fileName,String remoteDir) throws Exception {
		while(true) {
		String url = "/Users/yuntaekim/Desktop/image/";
		File dir = new File(url);
		File[] fileList = dir.listFiles();
		
		logger.info("==>Scanning.. ");
		FileInputStream in = null;
		try {
			for(int i = 0 ; i < fileList.length ; i++) {
			File files = fileList[i];
			channelsftp.cd(remoteDir);
					if(files.isFile()) {
						logger.info("File Sending.");
						File file = new File(url+files.getName());
						logger.info("File Sending...");
						in = new FileInputStream(file);
						logger.info("File Sending....");
						channelsftp.put(in,file.getName());
						logger.debug("Send : "+file.getName());
						in.close();
						logger.info("Complete");
						OverViewCtlr.i = 1;	
						file.delete();
						}
				}
			}
		catch(Exception e) {e.printStackTrace();}
		Thread.sleep(10000);
		}
	}
	
	//Getter && Setter 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}


