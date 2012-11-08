package com.salesforce.cliq.ui;

import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfig;
import com.salesforce.cliq.cliconfig.CliConfigExport;
import com.salesforce.dataloader.config.Config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.nexes.wizard.*;

public class ResultPanelDescriptor extends WizardPanelDescriptor implements ActionListener {
    
    public static final String IDENTIFIER = "RESULT_PANEL";
    
    private CliConfig myConfig;    
    ResultPanel resultPanel;    
    private boolean isSuccess = false;    
    
    public ResultPanelDescriptor() {
        
    	resultPanel = new ResultPanel();   
    	resultPanel.addCreateFilesActionListener(this);
    	//resultPanel.addShowFilesActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(resultPanel);
    }
    
    public void actionPerformed(ActionEvent e) {
    	//Ignoring Show files for now since it is not widely supported
        if (e.getActionCommand().equals(ResultPanel.SHOW_FILES_ACTION_COMMAND)) {        	
        	Desktop dt = Desktop.getDesktop();
        	try {
        		dt.open(CliConfig.SCRIPT_DIR);        		
        	} catch (Exception ex) {
        		System.out.println("Unable to display directory.");
        	}        	 
        } else if (e.getActionCommand().equals(ResultPanel.CREATE_FILES_ACTION_COMMAND)) {
        	doCreateCliConfig();
        }
    }
    
    private void doCreateCliConfig() {
    	resultPanel.clearStatus();
    	try {
    		myConfig.createCliConfig();    		
    		resultPanel.addStatus("Files created successfully!" + "\n\n");
    		resultPanel.addStatus("Your files are in: \n" + CliConfig.SCRIPT_DIR);
    		
    		isSuccess = true;
    		//resultPanel.setEnabledShowFiles(true);
    	} catch (Exception e) {
    		resultPanel.addStatus(e.getMessage());
    		
    		isSuccess = false;
    		//resultPanel.setEnabledShowFiles(false);
    	}
    	
    	setNextButtonAccordingToResult();
    }
    
    public void aboutToDisplayPanel() {
   		myConfig = DataLoaderCliq.getCliConfig(); 
   		
   		resultPanel.clearSummary();
   		resultPanel.addSummaryItem("Directory: " + CliConfig.SCRIPT_DIR + "\n\n");
   		resultPanel.addSummaryItem("Entity (Object): " + myConfig.getConfigValue(Config.ENTITY) + "\n\n");
   		resultPanel.addSummaryItem("Username: " + myConfig.getConfigValue(Config.USERNAME) + "\n\n");
   		resultPanel.addSummaryItem("Operation: " + myConfig.getConfigValue(Config.OPERATION) + "\n\n");
   		
   		setNextButtonAccordingToResult();
    }
    
    public void aboutToHidePanel() {

    }        
    
    public Object getNextPanelDescriptor() {
    	return FINISH;
    }
    
    public Object getBackPanelDescriptor() {
    	if (myConfig instanceof CliConfigExport) {
    		return QueryPanelDescriptor.IDENTIFIER;
    	} else {
    		return EntityPanelDescriptor.IDENTIFIER;
    	}
    }
    
    private void setNextButtonAccordingToResult() {
        if (isSuccess) {
           getWizard().setNextFinishButtonEnabled(true);
        } else {
           getWizard().setNextFinishButtonEnabled(false);
        }
    }      
    
}
