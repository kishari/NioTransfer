package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;

public class Server implements Runnable {

	private Connection connection;
	
	public Server(InetAddress localHost, int port, InetAddress remoteHost, int remotePort, boolean server) throws IOException {
		this.connection = new Connection(localHost, port, remoteHost, remotePort, server);
	}
	
	public void run() {
		System.out.println("Server start!");
		connection.getReceiverConn().start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		connection.getSenderConn().start();
	}
	
	public static void main(String[] args) {
		try {
			new Thread(new Server(InetAddress.getLocalHost(), 9090, InetAddress.getLocalHost(), 9092, true)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
