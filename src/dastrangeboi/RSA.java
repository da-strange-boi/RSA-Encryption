package dastrangeboi;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.math.BigInteger;

public class RSA {

	// Returns GCD
	private static int gcd(int a, int b) {
		if (b == 0) {
			return a;
		}
		return gcd(b, a % b);
	}
	
	// Generate Prime Numbers
	public static List<Integer> sieveOfEratosthenes(int n) {
		boolean prime[] = new boolean[n + 1];
		Arrays.fill(prime,  true);
		for (int p = 2; p * p <= n; p++) {
			if (prime[p]) {
				for (int i = p * 2; i <= n; i += p) {
					prime[i] = false;
				}
			}
		}
		List<Integer> primeNumbers = new LinkedList<>();
		for (int i = 2; i <= n; i++) {
			if (prime[i]) {
				primeNumbers.add(i);
			}
		}
		return primeNumbers;
	}
	
	// Get random prime numbers
	public static int randomPrime(List<Integer> primes) {
		double randomNumber = Math.floor(Math.random() * (primes.size() - 25) + 25);
		return primes.get((int)randomNumber);
	}
	
	// Returns ArrayList of encrypted text
	public static String encryptText(String planText, String publicKey) {
		// Encrypt plan text, C = m^2 mod N
		
		ArrayList<String> decodedPublicKey = new ArrayList<String>(Arrays.asList(new String(Base64.getDecoder().decode(publicKey.getBytes())).split("\\|")));
		int N = Integer.parseInt(decodedPublicKey.get(0));
		int E = Integer.parseInt(decodedPublicKey.get(1));
		
		ArrayList<Integer> cipherText = new ArrayList<Integer>(); 
		
		for (int i = 0; i < planText.length(); i++) {
			int letterCode = planText.codePointAt(i);
			double encryptedLetter = Math.pow(letterCode, E) % N;
			cipherText.add((int)encryptedLetter);
		}
		
		String cipherTextString = "";
		for (int i = 0; i < cipherText.size(); i++) {
			cipherTextString += cipherText.get(i);
			if (i != cipherText.size()-1) cipherTextString += "|";
		}
		return new String(Base64.getEncoder().encode(cipherTextString.getBytes()));
	}
	
	// Returns ArrayList of decrypted cipher text
	public static String decryptText(String cipherText, String privateKey) {
		// Decrypt cipher text, M = C^D modN
		
		ArrayList<String> decodedPrivateKey = new ArrayList<String>(Arrays.asList(new String(Base64.getDecoder().decode(privateKey.getBytes())).split("\\|")));
		int N = Integer.parseInt(decodedPrivateKey.get(0));
		int D = Integer.parseInt(decodedPrivateKey.get(1));
		
		ArrayList<String> cipherTextFormatted = new ArrayList<String>(Arrays.asList(new String(Base64.getDecoder().decode(cipherText.getBytes())).split("\\|")));
		ArrayList<Integer> decryptedTextCode = new ArrayList<Integer>();
		ArrayList<String> decryptedText = new ArrayList<String>();
		
		for (int i = 0; i < cipherTextFormatted.size(); i++) {
			BigInteger encryptLetter = BigInteger.valueOf(Integer.parseInt(cipherTextFormatted.get(i)));
			BigInteger n = BigInteger.valueOf(N);
			BigInteger decryptedLetter = (encryptLetter.pow(D)).mod(n);
			decryptedTextCode.add(decryptedLetter.intValue());
		}
		
		for (int i = 0; i < decryptedTextCode.size(); i++) {
			decryptedText.add(Character.toString((char) Integer.parseInt(String.valueOf(decryptedTextCode.get(i)))));
		}
		
		String decodedText = "";
		for (int i = 0; i < decryptedText.size(); i++) {
			decodedText += decryptedText.get(i);
		}
		
		return decodedText;
	}
	
	public static ArrayList<String> generateKeys() {	
		// two prime numbers
		List<Integer> primesList = sieveOfEratosthenes(800);
		int P = randomPrime(primesList);
		int Q = randomPrime(primesList);
		
		int N = P * Q;
		int φ = (P - 1) * (Q - 1);
		
		// getting encryption number
		int E = 0;
		for (E = 2; E < φ; E++) {
			if (gcd(E, φ) == 1) {
				break;
			}
		}
		
		// getting decryption number
		int D = 0;
		while (true) {
			if (((D * E) % φ) == 1) {
				break;
			}
			D += 1;
		}
		
		ArrayList<String> keys = new ArrayList<String>();
		
		// getting public (encryption) key
		String publicKeyText = N+"|"+E;
		String publicKey = new String(Base64.getEncoder().encode(publicKeyText.getBytes()));
		keys.add(publicKey);
		
		// getting private (decryption) key
		String privateKeyText = N+"|"+D;
		String privateKey = new String(Base64.getEncoder().encode(privateKeyText.getBytes()));
		keys.add(privateKey);
		
		return keys;
		
		// Private: P, Q, Ø, D
		// Public : N, E
	}

}
