/**
 * Use this class for unit testing.
 * 
 * 2/25/2017
 * @author Group 5
 * 		Shannon Weir
 * 		David Pyenson
 * 		Elliot Allen
 */

//imports
import java.io.*;
import java.util.Base64;

public class test {
	public static void main(String[] args) {
		testPasswordHashing("abcdefghijkl");
		
		testPasswordSaving("testAccount", "abcdefghijkl", "testFile.txt", false);
		testPasswordSaving("shannon.weir", "helloWorld", "testFile.txt", true);
		testPasswordSaving("derp", "derpderp", "testFile.txt", true);
		
		System.out.printf("Retrieval Test #1: %s%n", (testPasswordRetrieval("testAccount", "abcdefghijkl", "testFile.txt") ? "Password retrieved and matched (success)" : "Password not retrieved or not matched (failure)"));
		System.out.printf("Retrieval Test #2: %s%n", (testPasswordRetrieval("testAccount", "helloworld", "testFile.txt") ? "Password retrieved and matched (failure)" : "Password not retrieved or not matched (success)"));
		System.out.printf("Retrieval Test #3: %s%n", (testPasswordRetrieval("shannon.weir", "helloWorld", "testFile.txt") ? "Password retrieved and matched (success)" : "Password not retrieved or not matched (failure)"));
	}
	
	private static void testPasswordHashing(String password) {
		String testHash1 = "abcdefghijk";
		String testHash2 = "abcdefghijklmn";
		String testHash3 = "ThisIsATest";
		String testHash4 = "abcdefghijkl";
		String testHash5 = "lkjihgfedcba";
		
		int hash1 = password.hashCode();
		int hash2 = testHash4.hashCode();
		System.out.printf("Hash 1 = %d%nHash 2 = %d%n%n", hash1, hash2);
		
		byte[] passHash = Security.hash(password);
		
		boolean match1 = Security.verifyPassword(testHash1, passHash);
		boolean match2 = Security.verifyPassword(testHash2, passHash);
		boolean match3 = Security.verifyPassword(testHash3, passHash);
		boolean match4 = Security.verifyPassword(testHash4, passHash);
		boolean match5 = Security.verifyPassword(testHash5, passHash);
		
		System.out.printf("Test #1: %s%n", (match1 ? "Password matches (failure)" : "Password does not match (success)"));
		System.out.printf("Test #2: %s%n", (match2 ? "Password matches (failure)" : "Password does not match (success)"));
		System.out.printf("Test #3: %s%n", (match3 ? "Password matches (failure)" : "Password does not match (success)"));
		System.out.printf("Test #4: %s%n", (match4 ? "Password matches (success)" : "Password does not match (failure)"));
		System.out.printf("Test #5: %s%n", (match5 ? "Password matches (failure)" : "Password does not match (success)"));
	}
	
	private static void testPasswordSaving(String account, String password, String fileName, boolean append) {	
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append))){
			String hashPass = Base64.getEncoder().encodeToString(Security.hash(password));
			
			writer.write(account);
			writer.newLine();
			writer.write(hashPass);
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean testPasswordRetrieval(String account, String password, String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String retAccount = null;
			String retPass = null;
			
			String line1 = "";
			String line2 = "";
			while((line1 = br.readLine()) != null &&
					(line2 = br.readLine()) != null) {
				if(line1.equals(account)) {
					retAccount = line1;
					retPass = line2;
					break;
				}
			}
			
			if(retAccount != null && retPass != null) {
				//The account name was found.
				if(retAccount.equals(account)) {
					return Security.verifyPassword(password, Base64.getDecoder().decode(retPass));
				}
			}
			else
			{
				//Account was not found in file.
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Error occurred during file reading.
		return false;
	}
}
