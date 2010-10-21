package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;

public class Connection {
	
	private ReceiverConnection receiverConn;
	private SenderConnection senderConn;
	
	public Connection(InetAddress localHostAddress, int localPort, InetAddress remoteHostAddress, int remotePort, boolean server) throws IOException {
		this.receiverConn = new ReceiverConnection(localHostAddress, localPort, this, server);
		this.senderConn = new SenderConnection(remoteHostAddress, remotePort, this, server);
	}
	

	public ReceiverConnection getReceiverConn() {
		return receiverConn;
	}
	
	public SenderConnection getSenderConn() {
		return senderConn;
	}
		
	public void send(String data) throws IOException {
		getSenderConn().send(data);
	}
}
