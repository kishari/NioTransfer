package hu.regex.test;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexTestHarness {

    public static void main(String[] args){
    	
         Pattern pattern =  Pattern.compile("(^MSRP) ([\\p{Alnum}]{8,20}) ([\\p{Upper}]{1,20})\r\n(.*)", Pattern.DOTALL);
//    	   Pattern pattern =  Pattern.compile("(^MSRP) ([\\p{Alnum}]{8,20}) ([\\p{Upper}]{1,20})\r\n", Pattern.DOTALL);
// 	   	   Pattern pattern =  Pattern.compile("(To-Path:) (http://[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}:[\\p{Digit}]{4,5}/([\\p{Alnum}]{10,50});tcp)\r\n");
//    	   Pattern pattern =  Pattern.compile("(From-Path:) (http://[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}.?[\\p{Alnum}]{1,}:[\\p{Digit}]{4,5}/([\\p{Alnum}]{10,50});tcp)\r\n");
//    	   Pattern pattern =  Pattern.compile("(Message-Id:) ([\\p{Alnum}]{10,50})\r\n");
//    	   Pattern pattern =  Pattern.compile("(Byte-Range:) ([\\p{Digit}]{1,})-([\\p{Digit}]{1,})/([\\p{Digit}]{1,})\r\n");
//    	   Pattern pattern =  Pattern.compile("(Content-Type:) ([\\p{Alpha}]{1,}/[\\p{Alpha}]{1,})\r\n\r\n");
//     	   Pattern pattern =  Pattern.compile("\r\n([-]{7})([\\p{Alnum}]{8,20})([+$#]{1})");
    	
//    	Pattern pattern =  Pattern.compile("\r\n([-]{7})([\\p{Alnum}]{8,20})([+$#]{1})");
    	
    	   String m = "MSRP 12345678 SEND\r\n" +
					 "To-Path: http://www.jozsika.huhu:5656/hkjsdahfdlkjasdhdl;tcp\r\n" +
					 "From-Path: http://www.jozsika.huhu:5656/jjjishdkjsggjgasjg;tcp\r\n" +
					 "Message-Id: wkqjdlajdilksahjdksla\r\n" +
					 "Byte-Range: 0-12345/34567\r\n" +
					 "Content-Type: text/plain\r\n\r\n" +
					 "Hello, józsika vagyok bassza meg!\r\n" +
					 "-------12345678$";
    	   
    	  
 	   	   Matcher matcher = pattern.matcher(m);

        boolean found = false;
        while (matcher.find()) {
             System.out.println(String.format("I found the text \"%s\" starting at " +
                "index %d and ending at index %d.",
                  matcher.group(), matcher.start(), matcher.end()));
              found = true;
              System.out.println(matcher.group(3));
       }
       if(!found){
           System.out.println("No match found!");
       }
    }
}
