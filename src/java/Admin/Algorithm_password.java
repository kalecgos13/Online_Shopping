package Admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Algorithm_password {

    public Algorithm_password() {
    }

        //run whenever change in password or new account
    public String generate_salt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[32];
        random.nextBytes(bytes);
        String salt = bytesToHex(bytes);
        System.out.println(salt);
        return salt;
    }
    
    public String Encrypt_password(String msg, String salt) {
    	byte[] password = msg.getBytes();
    	byte[] salted = hexStringToByteArray(salt);
    	String hash = null;
    	try {
    		MessageDigest md = MessageDigest.getInstance("SHA-256");
    		md.update(salted);
    		byte[] bytes = md.digest(password);
    		StringBuilder sb = new StringBuilder();
    		for (int i = 0;i<bytes.length;i++) {
    			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    		}
    		hash = sb.toString();
    	}
    	catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
    
    
    
    private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}