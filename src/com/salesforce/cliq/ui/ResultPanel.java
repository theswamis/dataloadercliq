package com.salesforce.cliq.ui;
import net.miginfocom.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

public class ResultPanel extends CliqWizardPanel {

	public static final String SHOW_FILES_ACTION_COMMAND = "ShowFilesActionCommand";
	public static final String CREATE_FILES_ACTION_COMMAND = "CreateFilesActionCommand";
	
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JPanel resultPanel;
    
    private javax.swing.JTextArea summaryField;   
    private JButton createFilesButton;
    private JButton showFilesButton;
    private javax.swing.JTextArea statusField;   

    public ResultPanel() {
    	super("Generate Files");
    	addContentPanel(getContentPanel());	
    }
    
    public void addCreateFilesActionListener(ActionListener l) {
    	createFilesButton.addActionListener(l);        
    }
    
    public void addShowFilesActionListener(ActionListener l) {
    	showFilesButton.addActionListener(l);        
    }   
    
    public void setEnabledShowFiles(boolean b) {
    	showFilesButton.setEnabled(b);
    }    
    
    public void clearStatus() {
    	statusField.setText("");
    }    
    
    public void addStatus(String s) {
    	statusField.append(s);
    }

    public void clearSummary() {
    	statusField.setText("");
    }
    
    public void addSummaryItem(String s) {
    	summaryField.append(s);
    }
    
    private JPanel getContentPanel() {
    	
        JPanel contentPanel1 = new JPanel();
        summaryPanel = new JPanel();
        resultPanel = new JPanel();  
                
        jPanel1 = new javax.swing.JPanel();                    
        createFilesButton = new JButton();
        showFilesButton = new JButton();
        
        contentPanel1.setLayout(new java.awt.BorderLayout());        
        jPanel1.setLayout(new MigLayout("wrap 1"));
        
        /* Summary */
        summaryPanel.setLayout(new MigLayout("wrap 1,w 400"));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Summary",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));        
      
        summaryField = new JTextArea("",10,30); 
        summaryField.setLineWrap(true);
        summaryField.setWrapStyleWord(true);
        summaryField.setEditable(false);
        JScrollPane summaryScrollPane = new JScrollPane(summaryField);
        summaryScrollPane.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS & JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        summaryPanel.add(summaryScrollPane);
        
        createFilesButton.setText("Create Data Loader CLI Files");
        createFilesButton.setActionCommand(CREATE_FILES_ACTION_COMMAND);
        summaryPanel.add(createFilesButton,"");
        jPanel1.add(summaryPanel);
        
        /* Results */
        resultPanel.setLayout(new MigLayout("wrap 1,w 400"));
        resultPanel.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createEtchedBorder(),
        		"Results",        		
        		TitledBorder.DEFAULT_JUSTIFICATION,
        		TitledBorder.DEFAULT_POSITION,
        		new Font("Courier", Font.BOLD,14)
        		));        
                
        statusField = new JTextArea("",6,30); 
        statusField.setLineWrap(true);
        statusField.setWrapStyleWord(true);
        statusField.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(statusField);
        messageScrollPane.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS & JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultPanel.add(messageScrollPane);
        
        /* Not supported by Java 1.5
        showFilesButton.setText("Show Files");
        showFilesButton.setActionCommand(SHOW_FILES_ACTION_COMMAND);
        showFilesButton.setEnabled(false);
        resultPanel.add(showFilesButton,"");  
        */
        
        jPanel1.add(resultPanel);
        
        /* End */
        contentPanel1.add(jPanel1, java.awt.BorderLayout.CENTER);
        
        return contentPanel1;
        
    }
}