package hu.msrp.test;

import java.net.URI;
import java.net.URISyntaxException;

public class MSRPMessage {

	private int method = 0;
	private int sumByte = 12;
	private int firstByte = 0;
	private int lastByte = 12;
	
	private URI fromPath = null;
	private URI toPath = null;
	private String contentType;
	private String messageId = "asdfghj";
	private byte[] content = new String("én vagyok a content").getBytes();
	private String tId = "12345678";
	
	public void createToPath(String host, int port, String sessionId) throws URISyntaxException {
		if (toPath == null) {
			toPath = new URI("msrp", null, host, port, "/" + sessionId +";tcp", null, null);
		}
	}
	
	public void createFromPath(String host, int port, String sessionId) throws URISyntaxException {
		if (fromPath == null) {
			fromPath = new URI("msrp", null, host, port, "/" + sessionId +";tcp", null, null);
		}
	}
	
	public void createToPath(String uri) throws URISyntaxException {
		toPath = new URI(uri);
	}
	
	
	@Override
	public String toString() {
		String msg = new String();
		msg += "MSRP " + tId + " SEND\r\n";
		msg += "To-Path: " + toPath + "\r\n";
		msg += "From-Path: " + fromPath + "\r\n";
		msg += "Message-ID: " + messageId + "\r\n";
		msg += "Byte-Range: " + firstByte + "-" + lastByte + "/" + sumByte + "\r\n";
		msg += "Content-Type: " + contentType + "\r\n\r\n";
		msg += new String(content) + "\r\n";
		msg += "-------" + tId;
		return msg;
	}

	public int getSumByte() {
		return sumByte;
	}
	public void setSumByte(int sumByte) {
		this.sumByte = sumByte;
	}
	public int getFirstByte() {
		return firstByte;
	}
	public void setFirstByte(int firstByte) {
		this.firstByte = firstByte;
	}
	public int getLastByte() {
		return lastByte;
	}
	public void setLastByte(int lastByte) {
		this.lastByte = lastByte;
	}
	public URI getFromPath() {
		return fromPath;
	}
	public void setFromPath(URI fromPath) {
		this.fromPath = fromPath;
	}
	public URI getToPath() {
		return toPath;
	}
	public void setToPath(URI toPath) {
		this.toPath = toPath;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public byte[] getContent() {
		return content;
	}
	
}
