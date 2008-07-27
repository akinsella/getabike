package org.helyx.app.j2me.lib.text;

import java.util.Random;


public class AsciiUtil {
	
   public static boolean isWhitespace(char c) {
        // Optimized case for ASCII
        if ((c >= 0x1c && c <= 0x20) || (c >= 0x9 && c <= 0xd)) {
            return true;
        }
        if (c == 0x1680) {
            return true;
        }
        if (c < 0x2000 || c == 0x2007) {
            return false;
        }
        return c <= 0x200b || c == 0x2028 || c == 0x2029 || c == 0x3000;
    }


	public static String generatePrintableString(int minValue, int maxValue) throws Exception {
    	StringBuffer sb;
    	Random random;
    	
       	if (minValue <= 0 || minValue > maxValue) throw new Exception("La taille demandée n'est pas valide!"); 

       	random = new Random();
       	int randomInt = random.nextInt();
    	int randomValue = (int)(Math.abs(randomInt) % (maxValue + 1)) + minValue;
    	
    	sb = new StringBuffer(randomValue);
    	
    	for(int i = 0 ; i < randomValue ; i++) {
    		int result = (int)(Math.abs(random.nextInt()) % 62);
    		if (result < 26) sb.append((char)(result + 65));
    		else if (result >= 26 && result < 36) sb.append((char)(result - 26 + 48));
    		else if (result >= 36) sb.append((char)(result - 36 + 97));
    	}
    	return sb.toString();
	}
	
	public static String generatePrintableString(int size) throws Exception {
		StringBuffer sb;
    	Random random;
    	
    	if (size <= 0) throw new Exception("La taille demandée n'est pas valide!"); 

    	random = new Random();
    	sb = new StringBuffer(size);
    	for(int i = 0 ; i < size ; i++) {
    		int result = (int)(Math.abs(random.nextInt()) % 62);
    		if (result < 26) sb.append((char)(result + 65));
    		else if (result >= 26 && result < 36) sb.append((char)(result - 26 + 48));
    		else if (result >= 36) sb.append((char)(result - 36 + 97));
    	}
    	return sb.toString();
	}
	
	public static String generatePrintableString() throws Exception {
    	return generatePrintableString(1, Short.MAX_VALUE);    	
	}	
	
	public static byte[] generateValues(int minValue, int maxValue) throws Exception {
    	byte[] bytes;
    	Random random;
    	
       	if (minValue <= 0 || minValue > maxValue) throw new Exception("La taille demandée n'est pas valide!"); 

    	random = new Random();    	
 
    	int randomValue = (int)(Math.abs(random.nextInt()) % (maxValue + 1)) + minValue;
    	
    	
    	bytes = new byte[randomValue];
    	for(int i = 0 ; i < randomValue ; i++) {
    		bytes[i] = (byte)(Math.abs(random.nextInt()) % 128);
    	}
    	return bytes;
	}
	
	public static byte[] generateValues(int size) throws Exception {
    	byte[] bytes;
    	Random random;
    	
       	if (size <= 0) throw new Exception("La taille demandée n'est pas valide!"); 
   	
    	random = new Random();
 
    	bytes = new byte[size];
    	for(int i = 0 ; i < size ; i++) {
    		bytes[i] = (byte)(Math.abs(random.nextInt()) % 128);
    	}
    	return bytes;
	}
	
	public static byte[] generateValues() throws Exception {
    	return generateValues(1, Short.MAX_VALUE);    	
	}	
	
	public static String generateHexaString(int minValue, int maxValue) throws Exception {
    	StringBuffer sb;
    	Random random;
    	
    	if (minValue <= 0 || minValue > maxValue) throw new Exception("La taille demandée n'est pas valide!"); 

    	random = new Random();
    	
    	int randomValue = (int)(Math.abs(random.nextInt()) % (maxValue + 1)) + minValue;	
    	
    	sb = new StringBuffer(randomValue);
    	for(int i = 0 ; i < randomValue ; i++) {
    		int result = (int)(Math.abs(random.nextInt()) % 16);
    		if (result < 10) sb.append((char)(result + 48));
    		else if (result >= 10) sb.append((char)(result - 10 + 65));
    	}
    	return sb.toString();
	}
	
	public static String generateHexaString(int size) throws Exception {
		StringBuffer sb;
    	Random random;
    
       	if (size <= 0) throw new Exception("La taille demandée n'est pas valide!"); 

    	random = new Random();
    	sb = new StringBuffer(size);
    	for(int i = 0 ; i < size ; i++) {
    		int result = (int)(Math.abs(random.nextInt()) % 16);
    		if (result < 10) sb.append((char)(result + 48));
    		else if (result >= 10) sb.append((char)(result - 10 + 65));
    	}
    	return sb.toString();
	}
	
	public static String generateHexaString() throws Exception {
    	return generatePrintableString(1, Short.MAX_VALUE);    	
	}		
	
}

