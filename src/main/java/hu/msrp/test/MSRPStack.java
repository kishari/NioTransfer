package hu.msrp.test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MSRPStack {

	private Connection connection = null;
	private List<Session> sessions = new ArrayList<Session>();
	
	private SessionManager sessionManager = new SessionManager();

	public void createConnection(InetAddress localhostAddress, int localPort) throws IOException {
		this.connection = new Connection(localhostAddress, localPort);
	}
	
	public void createConnection(InetAddress localhostAddr, int localPort, 
									   InetAddress remotehostAddr, int remotePort) throws IOException {
		
		this.connection = new Connection(localhostAddr, localPort, remotehostAddr, remotePort, this);
	}
	
	public Session createSession() {
		String id = new String(genRandomString(64));
		Session s = new Session(this.connection);
		s.setSessionId(id);
		
		sessions.add(s);
		return s;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public List<Session> getSessions() {
		return sessions;
	}
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}
	
	public void update() throws URISyntaxException {
		System.out.println("MSRPStack update");
		if (!getConnection().getSenderConn().isStarted()) {
			getConnection().getSenderConn().start();
		}
	}
	
	public void sendMessage(String message) throws IOException {
		getConnection().getSenderConn().send(message);	
	}
	
	private String genRandomString(int length) {
		double divisor = length / 32.0; //32 karakter egy UUID '-' jelek nélkül
		System.out.println(divisor);
		int cycle = (int) Math.round(divisor);
		cycle++; //Növeljük eggyel, hogy a pl. 4.2 --> 4 kerekítés után is jó legyen 
		
		String random = new String();
		for(int i = 0; i < cycle; i++) {
			random += UUID.randomUUID().toString().replaceAll("-", "");
		}
		
		return random.substring(0, length);
	}
}
