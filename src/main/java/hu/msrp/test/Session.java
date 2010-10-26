package hu.msrp.test;

public class Session {
	private String sessionId;
	private Connection parentConnection;
	
	public Session(Connection parentConnection) {
		this.parentConnection = parentConnection;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Connection getParentConnection() {
		return parentConnection;
	}

	public void setParentConnection(Connection parentConnection) {
		this.parentConnection = parentConnection;
	}
}
