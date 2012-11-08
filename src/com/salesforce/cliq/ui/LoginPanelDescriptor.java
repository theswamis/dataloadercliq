package com.salesforce.cliq.ui;

import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfig;
import com.salesforce.cliq.cliconfig.CliConfigExport;
import com.salesforce.dataloader.config.Config;

import com.nexes.wizard.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanelDescriptor extends WizardPanelDescriptor implements ActionListener {
    
    public static final String IDENTIFIER = "LOGIN_PANEL";
    
    LoginPanel loginPanel;
    
    private CliConfig myConfig;
    
    public LoginPanelDescriptor() {        
    	loginPanel = new LoginPanel();    
    	loginPanel.addLoginActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(loginPanel);
    }
    
    public void actionPerformed(ActionEvent e) {    	
    	
    	myConfig.setConfigValue(Config.USERNAME,loginPanel.getUsername());
    	myConfig.setPassword(loginPanel.getPassword());
    	
    	try {
    		loginPanel.setMessage("Logging in...");        	
    		myConfig.doSalesforceLogin();
    		loginPanel.setMessage("Login Successful.");
    	} catch (Exception ex) {
       		DataLoaderCliq.log("Exception during login: " + ex.getMessage(), ex);
    		loginPanel.setMessage(ex.getMessage());	
    	}
    	
    	loginPanel.setEndpoint(myConfig.getConfigValue(Config.ENDPOINT));
    	    	
    	setNextButtonAccordingToLogin();
    }
    
    public void aboutToDisplayPanel() {
    	myConfig = DataLoaderCliq.getCliConfig();
    	
    	loginPanel.setUsername(myConfig.getConfigValue(Config.USERNAME));
    	loginPanel.setPassword(myConfig.getPlainPassword());
    	
    	setNextButtonAccordingToLogin();
    }
    
    public void aboutToHidePanel() {

    }    
    
    public Object getNextPanelDescriptor() {
    	if (myConfig instanceof CliConfigExport) {
    		return QueryPanelDescriptor.IDENTIFIER;
    	} else {
    		return EntityPanelDescriptor.IDENTIFIER;
    	}
    }
    
    public Object getBackPanelDescriptor() {
    	return OperationPanelDescriptor.IDENTIFIER;
    }
    
    private void setNextButtonAccordingToLogin() {
        if (myConfig.isLoggedIn())
           getWizard().setNextFinishButtonEnabled(true);
        else
           getWizard().setNextFinishButtonEnabled(false);           
   
   }    
    
}
