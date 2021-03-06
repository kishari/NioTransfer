package hu.msrp.test;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
	
	private static Pattern methodPattern =  Pattern.compile("(^MSRP) ([\\p{Alnum}]{8,20}) ([\\p{Upper}]{1,20})\r\n(.*)", Pattern.DOTALL);
	//private Pattern methodPattern =  Pattern.compile("(^MSRP) ([\\p{Alnum}]{8,20}) ([\\p{Upper}]{1,20})\r\n", Pattern.DOTALL);
	private static Pattern toPathPattern =  Pattern.compile("(To-Path:) (msrp://[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}:[\\p{Digit}]{4,5}/([\\p{Alnum}]{10,50});tcp)\r\n");
	private static Pattern fromPathPattern =  Pattern.compile("(From-Path:) (msrp://[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}:[\\p{Digit}]{4,5}/([\\p{Alnum}]{10,50});tcp)\r\n");
	private static Pattern messageIdPattern =  Pattern.compile("(Message-ID:) ([\\p{Alnum}]{10,50})\r\n");
	private static Pattern byteRangePattern =  Pattern.compile("(Byte-Range:) ([\\p{Digit}]{1,})-([\\p{Digit}]{1,})" +
															   "/([\\p{Digit}]{1,})\r\n");
	//private static Pattern contentTypePattern =  Pattern.compile("(Content-Type:) ([\\p{Alpha}]{1,}/[\\p{Alpha}]{1,})\r\n\r\n");
	private static Pattern contentPattern =  Pattern.compile("(Content-Type:) ([\\p{Alpha}]{1,}/[\\p{Alpha}]{1,})\r\n\r\n" +
															 "(.*)\r\n");
	private static Pattern endLinePattern =  Pattern.compile("\r\n([-]{7})([\\p{Alnum}]{8,20})([+$#]{1})");
	
	public static MSRPMessage createMessage(String msg) {
		MSRPMessage m = new MSRPMessage();
		
		System.out.println("MESSAGEUTIL inc msg: \n" + msg);
		
		Matcher matcher = methodPattern.matcher(msg);
		
		String method = null;
		if (matcher.find()) {
			method = matcher.group(3);
		}
		if ("SEND".equals(method)) {
			m.setMethod(Constants.methodSEND);
			
			m.setTransactionId(matcher.group(2));
			
			matcher = toPathPattern.matcher(msg);
			String toPath = null;
			if (matcher.find()) {
				toPath = matcher.group(2);
			}
									
			matcher = fromPathPattern.matcher(msg);
			String fromPath = null;
			if (matcher.find()) {
				fromPath = matcher.group(2);
			}
						
			try {
				if (toPath != null) {
					m.createToPath(toPath);				
				}
				if (fromPath != null) { 
					m.createFromPath(fromPath);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			matcher = messageIdPattern.matcher(msg);
			if (matcher.find()) {
				m.setMessageId(matcher.group(2));
			}				
			
			matcher = byteRangePattern.matcher(msg);
			if (matcher.find()) {
				m.setFirstByte(Integer.valueOf(matcher.group(2)));
				m.setLastByte(Integer.valueOf(matcher.group(3)));
				m.setSumByte(Integer.valueOf(matcher.group(4)));				
			}
			
			matcher = contentPattern.matcher(msg);
			if (matcher.find()) {
				m.setContentType(matcher.group(2));
				m.setContent(matcher.group(3).getBytes());
			}
						
			matcher = endLinePattern.matcher(msg);
			if (matcher.find()) {
				String tId = matcher.group(2);
				if (!m.getTransactionId().equals(tId)) {
					System.out.println("A nyitó tranzakcióId (" + m.getTransactionId() + ") " +
									   "nem egyezik a záró tranzakcióId-val (" + tId + ")");
				}
				m.setEndToken(matcher.group(3).charAt(0));
			}
			System.out.println("message after create: \n"  + m.toString());			
		}
		
		return m;
	}
}
