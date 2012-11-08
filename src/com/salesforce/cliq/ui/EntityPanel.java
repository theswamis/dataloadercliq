package com.salesforce.cliq.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

import net.miginfocom.swing.MigLayout;

public class EntityPanel extends CliqWizardPanel {        
    private javax.swing.JTextField entityField = new JTextField(40);
    
    private boolean showExternalIdTextInput;
    private javax.swing.JTextField externalIdField = new JTextField(40);
    
    //private javax.swing.JLabel resultMessage = new javax.swing.JLabel();
    private JTextArea resultMessage = new JTextArea();
    private javax.swing.JLabel entityLabel = new javax.swing.JLabel();  
    private javax.swing.JLabel externalIdLabel = new javax.swing.JLabel();
    
    private JButton verifyButton = new JButton();
    
    private String operationName;
    
    public EntityPanel() {
    	super("Salesforce Entity (Object)");
    	
        verifyButton = new JButton();
    	addContentPanel(getContentPanel());	
    }
    
    public String getEntityName() {
        return entityField.getText();
    }
    
    public String getExternalIdField() {
    	return externalIdField.getText();
    }
    
    public void setOperationName(String name) {
    	operationName = name;
    	
    	addContentPanel(getContentPanel());
    }
    
    public String getOperationName() {
    	return operationName;
    }
    
    public void addVerifyActionListener(ActionListener l) {
        verifyButton.addActionListener(l);        
    }
    
    public void setStatus (String s) {
    	resultMessage.setText(s);
    }
    
    public void setShowExternalIdTextInput(boolean shouldShowExternalIdTextInput) {
    	showExternalIdTextInput = shouldShowExternalIdTextInput;
    	
    	//Refresh the content to update the display of external id field
    	addContentPanel(getContentPanel());	
    }
    
    protected JPanel getContentPanel() {
    	JPanel contentPanel1 = new JPanel();
        
    	JPanel jPanel1 = new javax.swing.JPanel();
        JPanel entityPanel = new JPanel();
        
        contentPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new MigLayout("wrap 1"));

        /* User Panel */        
        entityPanel.setLayout(new MigLayout("wrap 2,w 400"));
        entityPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Salesforce Entity (Object) to " + getOperationName(),        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));  

        entityLabel.setText("Entity Name:");
        entityPanel.add(entityLabel);        
        entityPanel.add(entityField);
        
        if (showExternalIdTextInput) {
	        externalIdLabel.setText("External Id (optional):");
	        entityPanel.add(externalIdLabel);
	        entityPanel.add(externalIdField);
        }
        
        verifyButton.setText("Verify");
        entityPanel.add(verifyButton);
        
        jPanel1.add(entityPanel);
        
        /* Result Panel */
        resultMessage = createResultMessage();        
        jPanel1.add(createResultPanel(resultMessage));
        
        /* End */
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        return contentPanel1;
        
    }
}