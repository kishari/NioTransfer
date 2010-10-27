package hu.msrp.test;

import java.util.List;

public class SessionManager {

	private List<Session> sessions = null;
	
	
	public Session findSession(String sessionId) {
		if (sessions != null) {
			for (Session s : sessions) {
				if (sessionId.equals(s.getSessionId())) {
					return s;
				}
			}			
		}
		return null;
	}


	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
	
	
}
