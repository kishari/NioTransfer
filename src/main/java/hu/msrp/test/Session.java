package hu.msrp.test;

public class Session {
	private String sessionId;
	private Connection parentConnection;
	private TransactionManager transactionManager = new TransactionManager();
	
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

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
