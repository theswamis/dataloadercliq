package com.salesforce.cliq.ui;
import net.miginfocom.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

public class LoginPanel extends CliqWizardPanel {
	
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel userPanel;
    
    private JTextArea resultMessage;
    
    private javax.swing.JLabel welcomeTitle;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPasswordField passwordField;
    private JButton loginButton = new JButton();
    private javax.swing.JLabel messageLabel = new javax.swing.JLabel();  

    public LoginPanel() {
    	super("Salesforce Login");
    	addContentPanel(getContentPanel());
    }
    
    public String getUsername() {
        return usernameField.getText();
    }
    
    public String getPassword() {
        return String.valueOf(passwordField.getPassword());
    }
    
    public void addLoginActionListener(ActionListener l) {
        loginButton.addActionListener(l);        
    }
    
    public void setMessage(String e) {
    	resultMessage.setText(e);    	
    }

    public void setUsername(String username) {
    	usernameField.setText(username);    	
    }

    public void setPassword(String password) {
    	passwordField.setText(password);    	
    }
    
    protected JPanel getContentPanel() {
        userPanel = new JPanel();
        
        welcomeTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        
        passwordLabel = new javax.swing.JLabel();
        passwordField = new JPasswordField(20);
        usernameLabel = new javax.swing.JLabel();
        usernameField = new JTextField(20);        
        
        messageLabel = new javax.swing.JLabel();   
    	
        JPanel contentPanel1 = new JPanel();            
        
        contentPanel1.setLayout(new java.awt.BorderLayout());
        
        welcomeTitle = new javax.swing.JLabel();
        contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new MigLayout("wrap 1"));

        /* User Panel */        
        userPanel.setLayout(new MigLayout("wrap 2,w 400"));
        userPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Login to Salesforce",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));
        usernameLabel.setText("Username:");
        userPanel.add(usernameLabel);
        userPanel.add(usernameField);        

        passwordLabel.setText("Password:");
        userPanel.add(passwordLabel);
        userPanel.add(passwordField);
        
        loginButton = new JButton();
        loginButton.setText("Verify Username and Password");
        userPanel.add(loginButton,"wrap,span 2");
        
        jPanel1.add(userPanel);

        /* Result Panel */
        resultMessage = createResultMessage();        
        jPanel1.add(createResultPanel(resultMessage));
        
        /* End */        
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        return contentPanel1;        
    }   
}