package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

public class Connection implements Observer {
	
	private ReceiverConnection receiverConn = null;
	private SenderConnection senderConn = null;
	private MSRPStack msrpStack;
	
	public Connection(InetAddress localHostAddress, int localPort, 
					  InetAddress remoteHostAddress, int remotePort,
					  MSRPStack msrpStack) throws IOException {
		this.receiverConn = new ReceiverConnection(localHostAddress, localPort, this);
		this.senderConn = new SenderConnection(remoteHostAddress, remotePort, this);
		this.msrpStack = msrpStack;
	}
	
	public Connection(InetAddress localhostAddress, int remotePort) throws IOException {
		this.receiverConn = new ReceiverConnection(localhostAddress, remotePort, this);
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


	@Override
	public void update(Observable o, Object arg) {
		System.out.println("MSRPStack update: " + o.toString() + " " + arg.toString());
		try {
			msrpStack.update();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
