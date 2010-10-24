package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;

public class MSRPStack {

	private Connection connection = null;
	
	public void createConnection(InetAddress localhostAddress, int localPort) throws IOException {
		this.connection = new Connection(localhostAddress, localPort);
	}
	
	public void createConnection(InetAddress localhostAddr, int localPort, 
									   InetAddress remotehostAddr, int remotePort) throws IOException {
		
		this.connection = new Connection(localhostAddr, localPort, remotehostAddr, remotePort, this);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void update() {
		System.out.println("MSRPStack update hehehe");
		if (!getConnection().getSenderConn().isStarted()) {
			getConnection().getSenderConn().start();
		}
	}
}
