package com.salesforce.cliq.ui;
import java.awt.*;
import java.util.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentListener;
import com.salesforce.cliq.cliconfig.*;

import net.miginfocom.swing.MigLayout;

public class OperationPanel extends CliqWizardPanel {
	
    private javax.swing.ButtonGroup operationGroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel operationPanel;
    private javax.swing.JPanel processPanel;
    
    private javax.swing.JTextField processField;       

    public OperationPanel() {
    	super("Select Operation");    	
    	addContentPanel(getContentPanel());	
    }
    
    public CliConfig.DataLoaderOperation getRadioButtonSelected() {
    	if (operationGroup.getSelection() != null) {
    		return Enum.valueOf(CliConfig.DataLoaderOperation.class,operationGroup.getSelection().getActionCommand());
    	} else {
    		return null;
    	}
    }
    
    public String getProcess() {
    	return processField.getText();
    }
    
    public boolean isProcessValid() {
    	//Word Chars, > 0 length, etc.    	
        return (processField.getText().length() > 0) ? true : false;
    }
    
    public Boolean isRadioButtonSelected() {
        return getRadioButtonSelected() != null ? true : false;
    }
    
    public void addDocumentListener(DocumentListener l) {
        processField.getDocument().addDocumentListener(l);        
    }     
    
    public void addRadioActionListener(ActionListener l) {
        Enumeration e = operationGroup.getElements();

        while (e.hasMoreElements()) {
    		JRadioButton button = (JRadioButton)e.nextElement();
    		button.addActionListener(l);
        }
    }
    
    private JPanel getContentPanel() {
    	
        JPanel contentPanel1 = new JPanel();
        
        operationGroup = new javax.swing.ButtonGroup();
        
        jPanel1 = new javax.swing.JPanel();
        operationPanel = new javax.swing.JPanel();
        processPanel = new javax.swing.JPanel();

        processField = new JTextField(20);  
        
        contentPanel1.setLayout(new java.awt.BorderLayout());        
        jPanel1.setLayout(new MigLayout("wrap 1"));
        
        /* Operation Choice */
        operationPanel.setLayout(new MigLayout("wrap 1,w 300"));
        operationPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Select Operation",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));
        jPanel1.add(operationPanel);
        
        //Add all of the operations to the panel
        for (CliConfig.DataLoaderOperation op : CliConfig.DataLoaderOperation.values()) {
        	JRadioButton radioButton = new javax.swing.JRadioButton();
        	radioButton.setActionCommand(op.toString());
        	
            radioButton.setText(op.getDisplayName());
            operationGroup.add(radioButton);
            operationPanel.add(radioButton);
        }
        
        /* Process Name */
        processPanel.setLayout(new MigLayout("wrap 1,w 300"));
        processPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Enter Process Name (Letters Only)",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));
        processPanel.add(processField);
        jPanel1.add(processPanel);

        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        return contentPanel1;
        
    }
   
   
}
