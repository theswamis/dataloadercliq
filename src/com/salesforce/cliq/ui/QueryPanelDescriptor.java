package com.salesforce.cliq.ui;

import com.nexes.wizard.*;
import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfigExport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.*;

public class QueryPanelDescriptor extends WizardPanelDescriptor 
		implements ActionListener,DocumentListener {
    
    public static final String IDENTIFIER = "QUERY_PANEL";
    
    private CliConfigExport myConfig;    
    QueryPanel queryPanel;  
    
    public QueryPanelDescriptor() {
        
    	queryPanel = new QueryPanel();
    	queryPanel.addVerifyActionListener(this);    	
    	queryPanel.addDocumentListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(queryPanel);
    }
    
    public void insertUpdate(DocumentEvent e) {
        displayEditInfo(e);
    }
    
    public void removeUpdate(DocumentEvent e) {
        displayEditInfo(e);
    }
    
    public void changedUpdate(DocumentEvent e) {
        displayEditInfo(e);
    }
    
    private void displayEditInfo(DocumentEvent e) {
    
    }    
    
    public void actionPerformed(ActionEvent e) {    	
    	
    	try {
    		myConfig.setQueryAndEntity(queryPanel.getQuery());
    		queryPanel.setMessage("Query is valid.");
    	} catch (Exception ex) {
    	   	queryPanel.setMessage(ex.getMessage());
    	}
    	
    	setNextButtonAccordingToQuery();
    }
    
    public void aboutToDisplayPanel() {
    	myConfig = (CliConfigExport)DataLoaderCliq.getCliConfig();
    	
    	setNextButtonAccordingToQuery();
    }   
    
    public Object getNextPanelDescriptor() {
        return ResultPanelDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return LoginPanelDescriptor.IDENTIFIER;
    }
    
    private void setNextButtonAccordingToQuery() {
        if (myConfig.isQueryValid())
           getWizard().setNextFinishButtonEnabled(true);
        else
           getWizard().setNextFinishButtonEnabled(false);
    }    
    
}
