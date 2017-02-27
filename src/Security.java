/**
 * Class for static methods used in account verification.
 * Used to hash passwords and check hashed passwords.
 * 
 * 2/25/2017
 * @author Group 5
 * 		Shannon Weir
 * 		David Pyenson
 * 		Elliot Allen
 */

//imports
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Security {
	private static final int salt_iterations = 100;
	private static final int hash_iterations = 100000;
	private static final int key_length = 256;
	
	/**
	 * Static utility class cannot be instantiated
	 */
	private Security() { }
	
	/**
	 * Produces a random salt based on the user-supplied password
	 * The salt is 16 bytes, or 104 bits long.
	 * 
	 * @param seed		Seed for the random number generator.
	 * @return			16-byte array for the salt value
	 */
	private static byte[] getSalt(int seed) {
		byte[] salt = new byte[16];
		Random rand = new Random(seed);
		for(int count = 0;count < salt_iterations;++count) {
			rand.nextBytes(salt);
		}
		return salt;
	}
	
	/**
	 * Given a password, hash the password and return.
	 * Hash is based on PBKDF2 and SHA1
	 * 
	 * Note - the password is destroyed as a result of this
	 * function.
	 * 
	 * @param password		Password to be hashed
	 * @return				salted and hashed password
	 * @throws AssertionError	Thrown error in case of hash fail.
	 */
	public static byte[] hash(String password) 
		throws AssertionError {
		char[] passChar = password.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(passChar, getSalt(password.hashCode()), hash_iterations, key_length);
		password = "";
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}
	
	/**
	 * Verifies whether a given password will hash to the same submitted hash value
	 * 
	 * @param password		Password to be tested
	 * @param hashValue		Hash value to test against, created using the Security.hash function
	 * @return				true if password's hash matches given hash value, false otherwise
	 */
	public static boolean verifyPassword(String password, byte[] hashValue) {
		byte[] pwdHash = hash(password);
		password = "";
		if(pwdHash.length != hashValue.length) {
			//The lengths aren't the same, so they can't be equal.
			return false;
		}
		for(int i = 0;i < pwdHash.length;++i) {
			if(pwdHash[i] != hashValue[i]) {
				//Password byte did not match hash byte.
				return false;
			}
		}
		
		//At this point, the password hash matches the hash byte
		return true;
	}
	
	/**
	 * Returns the length of the key
	 * 
	 * @return	Length of key in bytes
	 */
	public static int getKeyLength() {
		return key_length/8;
	}
}
