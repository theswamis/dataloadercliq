package com.salesforce.cliq.ui;

import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfig;
import com.salesforce.cliq.cliconfig.CliConfigDml;
import com.salesforce.cliq.cliconfig.CliConfigExport;
import com.salesforce.cliq.cliconfig.CliConfigFactory;
import com.salesforce.dataloader.config.Config;

import com.nexes.wizard.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.*;

public class OperationPanelDescriptor extends WizardPanelDescriptor 
		implements ActionListener,DocumentListener {
    
    public static final String IDENTIFIER = "OPERATION_PANEL";
    
    OperationPanel operationPanel;

    private CliConfig myConfig;
    
    public OperationPanelDescriptor() {
        
    	operationPanel = new OperationPanel();
    	operationPanel.addRadioActionListener(this);
    	operationPanel.addDocumentListener(this);
    	
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(operationPanel);
        
    }
    
    public void insertUpdate(DocumentEvent e) {
    	setNextButtonAccordingToRadio();
    }
    
    public void removeUpdate(DocumentEvent e) {
    	setNextButtonAccordingToRadio();
    }
    
    public void changedUpdate(DocumentEvent e) {
    	setNextButtonAccordingToRadio();
    }
    
    public void actionPerformed(ActionEvent e) {
    	setNextButtonAccordingToRadio();
    }
    
    public void aboutToDisplayPanel() {
    	setNextButtonAccordingToRadio();
    	
    	if (myConfig == null) {
    		myConfig = DataLoaderCliq.getCliConfig();
    	}
    }
    
    public void aboutToHidePanel() {
    	String dlHome;
    	File dlHomeDir = new File(System.getProperty("user.dir"));
        dlHome = dlHomeDir.getParent();
    
        myConfig = CliConfigFactory.getCliConfig(
        		operationPanel.getRadioButtonSelected(), 
        		dlHome, 
        		operationPanel.getProcess());
        
        DataLoaderCliq.setCliConfig(myConfig);
        
        operationPanel.setEndpoint(myConfig.getConfigValue(Config.ENDPOINT));
    }
     
    public Object getNextPanelDescriptor() {    	
        return LoginPanelDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }
    
    private void setNextButtonAccordingToRadio() {
        if (operationPanel.isRadioButtonSelected() && operationPanel.isProcessValid())
           getWizard().setNextFinishButtonEnabled(true);
        else
           getWizard().setNextFinishButtonEnabled(false);
    }
    
}
