package dastrangeboi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.Border;

public class Window {
	
	public static void createWindow() {
		JFrame frame = new JFrame("RSA Encryption");
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		Border border = BorderFactory.createLineBorder(Color.black);
		
		// [Encrypt]
		JPanel encryptPanel = new JPanel();
		encryptPanel.setLayout(null);
		tabbedPane.addTab("Encrypt", encryptPanel);
		
		// [Encrypt] Button
		JButton encryptText = new JButton();
		encryptText.setText("Encrypt Text");
		encryptText.setFocusPainted(false);
		encryptText.setContentAreaFilled(false);
		encryptText.setOpaque(true);
		encryptText.setBounds(200, 20, 120, 30);
		encryptText.setBackground(Color.white);
		encryptPanel.add(encryptText);
		
		// [Encrypt] Public Key Text Box
		JTextArea publicKeyInsetion = new JTextArea();
		publicKeyInsetion.setBorder(border);
		publicKeyInsetion.setBounds(50, 100, 200, 30);
		publicKeyInsetion.setLineWrap(true);
		encryptPanel.add(publicKeyInsetion);
		
		// [Encrypt] Public Key Text Box Label
		JLabel publicKeyInsetionLabel = new JLabel();
		publicKeyInsetionLabel.setBounds(50, 80, 200, 20);
		publicKeyInsetionLabel.setText("Public Key");
		encryptPanel.add(publicKeyInsetionLabel);
		
		// [Encrypt] Message to encrypt Text Box
		JTextArea messageToEncryptText = new JTextArea();
		messageToEncryptText.setBorder(border);
		messageToEncryptText.setBounds(50, 150, 200, 100);
		messageToEncryptText.setLineWrap(true);
		messageToEncryptText.setWrapStyleWord(true);
		encryptPanel.add(messageToEncryptText);
		
		// [Encrypt] Message to encrypt Text Box Label
		JLabel messageToEncryptTextLabel = new JLabel();
		messageToEncryptTextLabel.setBounds(50, 130, 200, 20);
		messageToEncryptTextLabel.setText("Message to encrypt (Plain Text)");
		encryptPanel.add(messageToEncryptTextLabel);
		
		// [Encrypt] Encrypted Text, Text Area
		JTextArea encryptedTextBox = new JTextArea();
		encryptedTextBox.setBorder(border);
		encryptedTextBox.setBounds(285, 100, 200, 150);
		encryptedTextBox.setEditable(false);
		encryptedTextBox.setLineWrap(true);
		encryptedTextBox.setAutoscrolls(true);
		encryptPanel.add(encryptedTextBox);
		
		// [Encrypt] Encrypted Text, Text Area Label
		JLabel encryptedTextBoxLabel = new JLabel();
		encryptedTextBoxLabel.setBounds(285, 80, 200, 20);
		encryptedTextBoxLabel.setText("Encrypted Text");
		encryptPanel.add(encryptedTextBoxLabel);
		
		// [Encrypt] Button on click event
		encryptText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputtedPublicKey = publicKeyInsetion.getText();
				String inputtedMessage = messageToEncryptText.getText();
				if (!inputtedMessage.isEmpty() && !inputtedPublicKey.isEmpty()) {
					String encryptedTextFromAlg = RSA.encryptText(inputtedMessage, inputtedPublicKey);
					encryptedTextBox.setText(encryptedTextFromAlg);
				}
			}
		});
		
		// [Decrypt]
		JPanel decryptPanel = new JPanel();
		decryptPanel.setLayout(null);
		tabbedPane.addTab("Decrypt", decryptPanel);
		
		// [Decrypt] Button
		JButton decryptText = new JButton();
		decryptText.setText("Decrypt Text");
		decryptText.setBounds(200, 20, 120, 30);
		decryptText.setFocusPainted(false);
		decryptText.setContentAreaFilled(false);
		decryptText.setOpaque(true);
		decryptText.setBackground(Color.white);
		decryptPanel.add(decryptText);
		
		// [Decrypt] Private Key Text Box
		JTextArea privateKeyInsetion = new JTextArea();
		privateKeyInsetion.setBorder(border);
		privateKeyInsetion.setBounds(50, 100, 200, 30);
		privateKeyInsetion.setLineWrap(true);
		decryptPanel.add(privateKeyInsetion);
		
		// [Decrypt] Private Key Text Box Label
		JLabel privateKeyInsetionLabel = new JLabel();
		privateKeyInsetionLabel.setBounds(50, 80, 200, 20);
		privateKeyInsetionLabel.setText("Private Key");
		decryptPanel.add(privateKeyInsetionLabel);
		
		// [Decrypt] Message to decrypt Text Box
		JTextArea messageToDecryptText = new JTextArea();
		messageToDecryptText.setBorder(border);
		messageToDecryptText.setBounds(50, 150, 200, 100);
		messageToDecryptText.setLineWrap(true);
		decryptPanel.add(messageToDecryptText);
		
		// [Decrypt] Message to decrypt Text Box Label
		JLabel messageToDecryptTextLabel = new JLabel();
		messageToDecryptTextLabel.setBounds(50, 130, 200, 20);
		messageToDecryptTextLabel.setText("Message to decrypt (Base64)");
		decryptPanel.add(messageToDecryptTextLabel);
		
		// [Decrypt] Decrypted Text, Text Area
		JTextArea decryptedTextBox = new JTextArea();
		decryptedTextBox.setBorder(border);
		decryptedTextBox.setBounds(285, 100, 200, 150);
		decryptedTextBox.setEditable(false);
		decryptedTextBox.setLineWrap(true);
		decryptedTextBox.setWrapStyleWord(true);
		decryptPanel.add(decryptedTextBox);
		
		// [Decrypt] Decrypted Text, Text Area Label
		JLabel decryptedTextBoxLabel = new JLabel();
		decryptedTextBoxLabel.setBounds(285, 80, 200, 20);
		decryptedTextBoxLabel.setText("Decrypted Text");
		decryptPanel.add(decryptedTextBoxLabel);
		
		// [Decrypt] Button on click event
		decryptText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputtedPrivateKey = privateKeyInsetion.getText();
				String inputtedMessage = messageToDecryptText.getText();
				String decryptedTextFromAlg = RSA.decryptText(inputtedMessage, inputtedPrivateKey);
				decryptedTextBox.setText(decryptedTextFromAlg);
			}
		});
		
		// [Generate Keys]
		JPanel genKeysPanel = new JPanel();
		genKeysPanel.setLayout(null);
		tabbedPane.addTab("Generate Keys", genKeysPanel);
		
		// [Generate Keys] Button
		JButton genKeys = new JButton();
		genKeys.setText("Generate");
		genKeys.setBounds(200, 20, 120, 30);
		genKeys.setFocusPainted(false);
		genKeys.setContentAreaFilled(false);
		genKeys.setBackground(Color.white);
		genKeys.setOpaque(true);
		genKeysPanel.add(genKeys);
		
		// [Generate Keys] Public Text Area
		JTextArea publicKeyBox = new JTextArea();
		publicKeyBox.setBorder(border);
		publicKeyBox.setBounds(50, 125, 200, 50);
		publicKeyBox.setEditable(false);
		publicKeyBox.setLineWrap(true);
		genKeysPanel.add(publicKeyBox);
		
		// [Generate Keys] Public Label
		JLabel publicKeyBoxLabel = new JLabel();
		publicKeyBoxLabel.setBounds(50, 105, 200, 20);
		publicKeyBoxLabel.setText("Public Key");
		genKeysPanel.add(publicKeyBoxLabel);
		
		// [Generate Keys] Private Text Area
		JTextArea privateKeyBox = new JTextArea();
		privateKeyBox.setBorder(border);
		privateKeyBox.setBounds(285, 125, 200, 50);
		privateKeyBox.setEditable(false);
		privateKeyBox.setLineWrap(true);
		genKeysPanel.add(privateKeyBox);
		
		// [Generate Keys] Private Label
		JLabel privateKeyBoxLabel = new JLabel();
		privateKeyBoxLabel.setBounds(285, 105, 200, 20);
		privateKeyBoxLabel.setText("Private Key");
		genKeysPanel.add(privateKeyBoxLabel);
		
		// [Generate Keys] Generated by label
		JLabel generatedKeyByText = new JLabel();
		JLabel generatedKeyByDate = new JLabel();
		generatedKeyByText.setBounds(20, 230, 150, 50);
		generatedKeyByDate.setBounds(135, 230, 275, 50);
		generatedKeyByText.setText("Keys generated at: ");
		generatedKeyByDate.setForeground(Color.blue);
		genKeysPanel.add(generatedKeyByText);
		genKeysPanel.add(generatedKeyByDate);
		
		// [Generate Keys] Display keys
		try (InputStream input = new FileInputStream("keys.properties")) {
			
			Properties prop = new Properties();
			
			prop.load(input);
			
			publicKeyBox.setText(prop.getProperty("db.public"));
			privateKeyBox.setText(prop.getProperty("db.private"));
			generatedKeyByDate.setText(prop.getProperty("db.generatedAt"));
			
		} catch (IOException io) {
			io.printStackTrace();
		}
		
		// [Generate Keys] Button on click event
		genKeys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> genertedKeys = RSA.generateKeys();
				String publicKey = genertedKeys.get(0);
				String privateKey = genertedKeys.get(1);
				Date date = new Date();
				publicKeyBox.setText(publicKey);
				privateKeyBox.setText(privateKey);
				generatedKeyByDate.setText(date.toString());
				
				try (OutputStream output = new FileOutputStream("keys.properties")) {
					
					Properties prop = new Properties();
					
					prop.setProperty("db.public", publicKey);
					prop.setProperty("db.private", privateKey);
					prop.setProperty("db.generatedAt", date.toString());
					
					prop.store(output, null);
					
					
				} catch (IOException io) {
					io.printStackTrace();
				}
				
			}
		});
		
		// frame
		frame.add(tabbedPane);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(550, 350);
	    frame.setLocationByPlatform(true);
	    frame.setVisible(true);
	}

	public static void main(String[] args) {
		createWindow();
	}

}
