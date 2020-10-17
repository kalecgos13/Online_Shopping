package Admin;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Algorithm_password {

    public Algorithm_password() {
    }

        //run whenever change in password or new account
    public String generate_salt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[64];
        random.nextBytes(bytes);
        String salt = Hex.encodeHexString(bytes);
        System.out.println(salt);
        return salt;
    }
    
    public String Encrypt_password(String pass, String salt, int iterations, int keyLength) {
        try {
            char[] password = pass.toCharArray();
            byte[] saltBytes = Hex.decodeHex(salt.toCharArray());
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, saltBytes, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return Hex.encodeHexString(res);
        }
        catch(NoSuchAlgorithmException | DecoderException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}