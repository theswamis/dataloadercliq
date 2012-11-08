package com.salesforce.cliq.ui;
import net.miginfocom.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class QueryPanel extends CliqWizardPanel {
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel welcomeTitle;
    private javax.swing.JPanel queryPanel;
    private javax.swing.JTextArea queryField;
    private javax.swing.JCheckBox includeDeletedRecords;

    private JTextArea resultMessage;
    
    private javax.swing.JTextArea messageField;    
    
    private JButton loginButton;

    public QueryPanel() {
    	super("Validate SOQL Query");      	
    	addContentPanel(getContentPanel());	
    }
    
    public String getQuery() {
        return queryField.getText();
    }
    
    public boolean isIncludeDeletedRecordsChecked() {
        return includeDeletedRecords.isSelected();
    }

    public void setMessage(String e) {
    	resultMessage.setText(e);    	
    }
    
    public void addVerifyActionListener(ActionListener l) {
        loginButton.addActionListener(l);        
    }
    
    public void addDocumentListener(DocumentListener l) {
        queryField.getDocument().addDocumentListener(l);        
    }
    
    private JPanel getContentPanel() {
    	
        JPanel contentPanel1 = new JPanel();
        
        loginButton = new JButton();
        
        welcomeTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        
        queryPanel = new javax.swing.JPanel();
        queryField = new JTextArea(5,50);     //change to text area  
        includeDeletedRecords = new javax.swing.JCheckBox();     
        
        contentPanel1.setLayout(new java.awt.BorderLayout());
        
        contentPanel1.add(welcomeTitle, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new MigLayout("wrap 1"));

        /* Query Panel */
        queryPanel.setLayout(new MigLayout("wrap 1,w 400"));
        queryPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Query",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));
        queryPanel.add(queryField);
        
        queryField.setFont(new Font("Courier", Font.PLAIN, 12));
        queryField.setLineWrap(true);
        queryField.setWrapStyleWord(true);
        JScrollPane queryScrollPane = new JScrollPane(queryField);
        queryScrollPane.setPreferredSize(new Dimension(500, 120));
        queryScrollPane.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS & JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);        
        queryPanel.add(queryScrollPane);        

        loginButton.setText("Verify");
        queryPanel.add(loginButton,"span 2,align 50% 50%");
        jPanel1.add(queryPanel);
        
        /* Result Panel */
        resultMessage = createResultMessage();        
        jPanel1.add(createResultPanel(resultMessage));
        
        /* End */                
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        
        
        return contentPanel1;
        
    }
}