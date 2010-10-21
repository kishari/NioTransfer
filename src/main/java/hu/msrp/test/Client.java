package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;

public class Client implements Runnable {

	private Connection connection;
	
	public Client(InetAddress localHost, int port, InetAddress remoteAddress, int remotePort, boolean server) throws IOException {
		this.connection = new Connection(localHost, port, remoteAddress, remotePort, server);
	}
	public void run() {
		connection.getSenderConn().start();
		connection.getReceiverConn().start();
	}
	
	public static void main(String[] args) {
		try {
			new Thread(new Client(InetAddress.getLocalHost(), 9092, InetAddress.getLocalHost(), 9090, false)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
