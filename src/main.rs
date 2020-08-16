extern crate rand;
extern crate base64;
extern crate colored;
extern crate num;

use rand::Rng;
use colored::*;
use num::bigint::ToBigInt;
use std::io;
use std::str;
use std::char;

// Pause until user is ready
fn get_console_pause() {
    let mut ignored = String::new();
    io::stdin().read_line(&mut ignored).expect("Couldn't read line");
}

// Get user input
fn get_user_input() -> String {
    let mut user_input = String::new();
    io::stdin().read_line(&mut user_input).expect("Couldn't read line");
    user_input = user_input.replace("\r\n", "");
    return user_input;
}

// Returns gcd (greatest common divisor)
fn gcd(a: usize, b: usize) -> usize {
    if b == 0 {
        return a;
    }
    return gcd(b, a % b);
}

// Returns lcm (lowest common multiple)
fn lcm(a: usize, b: usize) -> usize {
    return (a * b) / gcd(a, b);
}

// Sieve of eratosthenes (Algorithm to get prime numbers)
fn sieve_of_eratosthenes(n: usize) -> Vec<usize> {
    let mut prime;
    prime = vec![true; n+1];

    let mut p = 2;
    while p * p <= n {
        if prime[p] {
            let mut i = p * 2;
            while i <= n {
                prime[i] = false;
                i += p;
            }
        }
        p += 1;
    }

    let mut prime_numbers = Vec::new();
    let mut i: usize = 2;
    while i <= n {
        if prime[i] {
            prime_numbers.push(i as usize);
        }
        i += 1;
    }
    return prime_numbers;
}

// Random prime number
fn random_prime(primes: &Vec<usize>) -> usize {
    let number_of_primes = primes.len();
    return primes[rand::thread_rng().gen_range(number_of_primes / 2, number_of_primes)]
}

fn generate_keys(security_size: usize) {
    let list_of_primes = sieve_of_eratosthenes(security_size);
    let p = random_prime(&list_of_primes);
    let q = random_prime(&list_of_primes);

    let n = p * q;
    let y = lcm(p - 1, q - 1);

    let mut e = 2;
    loop {
        if e < y {
            if gcd(e, y) == 1 {
                break;
            }
        } else {
            break;
        }
        e += 1;
    }

    let mut d = 0;
    loop {
        if ((d * e) % y) == 1 {
            break;
        }
        d += 1;
    }

    // getting public (encryption) key
    let mut public_key_text = String::from(n.to_string());
    public_key_text.push('|');
    public_key_text.push_str(&e.to_string());
    let e_bytes = base64::encode(public_key_text);

    // getting private (decryption) key
    let mut private_key_text = String::from(n.to_string());
    private_key_text.push('|');
    private_key_text.push_str(&d.to_string());
    let d_bytes = base64::encode(private_key_text);

    // present
    println!("\n{}", "Press ENTER when ready to continue".bold());
    println!("{}: {}", "Public Key".blue(), e_bytes);
    println!("{}: {}", "Private Key".blue(), d_bytes);
    get_console_pause();
}

fn encrypt_text() {
    println!("\n{}", "Enter other user's public key:".yellow().bold());
    let public_key = get_user_input();

    println!("{}", "Enter text to encrypt:".yellow().bold());
    let text_to_encrypt = get_user_input();

    let temp_public_key_bytes = &base64::decode(public_key).unwrap();
    let decode_public_key = String::from_utf8_lossy(temp_public_key_bytes);
    let decode_public_key_splitted = decode_public_key.split("|");
    let vec: Vec<&str> = decode_public_key_splitted.collect();
    let n: usize = vec[0].parse().unwrap();
    let e: usize = vec[1].parse().unwrap();

    // encrypt each letter
    let mut cipher_text: Vec<usize> = Vec::new();
    for c in text_to_encrypt.chars() {
        let letter_code = c as usize;
        let encrypted_letter = num::pow(letter_code.to_bigint().unwrap(), e) % n;
        cipher_text.push(encrypted_letter.to_string().parse().unwrap());
    }

    // format encrypted letters
    let mut cipher_text_string = String::new();
    for (i, encrypt) in cipher_text.iter().enumerate() {
        cipher_text_string.push_str(&encrypt.to_string());
        if i != cipher_text.len()-1 {
            cipher_text_string.push('|');
        }
    }

    // present
    let encoded_final_text = base64::encode(cipher_text_string);
    println!("\n{}", "Press ENTER when ready to continue".bold());
    println!("{}: {}\n", "Encrypted Text".blue().bold(), encoded_final_text);
    get_console_pause();
}

fn decrypt_text() {
    println!("\n{}", "Enter cipher text:".yellow().bold());
    let cipher_text = get_user_input();

    println!("{}", "Enter your private key:".yellow().bold());
    let private_key = get_user_input();

    // Get N & D keys from private keys
    let temp_private_key_bytes = &base64::decode(private_key).unwrap();
    let decode_private_key = String::from_utf8_lossy(temp_private_key_bytes);
    let decode_private_key_splitted = decode_private_key.split("|");
    let vec: Vec<&str> = decode_private_key_splitted.collect();
    let n: usize = vec[0].parse().unwrap();
    let d: usize = vec[1].parse().unwrap();

    // un-format cipher text
    let cipher_text_formatted = &base64::decode(cipher_text).unwrap();
    let f = String::from_utf8_lossy(cipher_text_formatted);
    let ff = f.split("|");
    let vec2: Vec<&str> = ff.collect();

    // convert encrypted letters to decrypted letters
    let mut decrypted_text_code: Vec<usize> = Vec::new();
    let mut i = 0;
    while i < vec2.len() {
        let encrypt_letter: usize = vec2[i].parse().unwrap();
        let decrypted_letter = num::pow(encrypt_letter.to_bigint().unwrap(), d) % n;
        decrypted_text_code.push(decrypted_letter.to_string().parse().unwrap());
        i += 1;
    }

    // format decrypted text
    let mut decrypted_text = String::from("");
    i = 0;
    while i < decrypted_text_code.len() {
        let formatted_de_text_code = decrypted_text_code[i] as u8;
        decrypted_text.push(formatted_de_text_code as char);
        i += 1;
    }

    // present
    println!("\n{}", "Press ENTER when ready to continue".bold());
    println!("{}: {}\n", "Decrypted Text".blue().bold(), decrypted_text);
    get_console_pause();
}

fn change_number_of_prime_numbers_generated() -> usize {
    println!("\n{}", "Enter number of prime numbers generated (Increases security, but takes longer) [1000] :".yellow().bold());
    let number = get_user_input();
    return number.parse().unwrap();
}

fn main() {
    let mut number_of_primes: usize = 1000;
    loop {
        println!("1. {}", "Generate Keys".green().bold());
        println!("2. {}", "Encrypt Text".green().bold());
        println!("3. {}", "Decrypt Text".green().bold());
        println!("4. {}", "Settings".green().bold());
        println!("5. {}\n", "Exit".green().bold());

        let mut input = String::new();
        io::stdin().read_line(&mut input).expect("Couldn't read line");
        input = input.replace("\r\n", "");

        if input == "1" {
            generate_keys(number_of_primes);
        } else if input == "2" {
            encrypt_text();
        } else if input == "3" {
            decrypt_text();
        } else if input == "4" {
            number_of_primes = change_number_of_prime_numbers_generated();
        } else if input == "5" {
            break;
        } else {
            println!("{}", "\n--Invalid input--".red().bold());
        }
    }
}