package com.salesforce.cliq.ui;

import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfigDml;
import com.salesforce.dataloader.config.Config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.nexes.wizard.*;

public class EntityPanelDescriptor extends WizardPanelDescriptor implements ActionListener {
    
    public static final String IDENTIFIER = "ENTITY_PANEL";
    
    private CliConfigDml myConfig;    
    EntityPanel entityPanel;
    
    public EntityPanelDescriptor() {
        
    	entityPanel = new EntityPanel();
    	entityPanel.addVerifyActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(entityPanel);
    }
    
    public void actionPerformed(ActionEvent e) {    		
    	try {
	    	myConfig.setEntityName(entityPanel.getEntityName());
	    	String externalId = entityPanel.getExternalIdField();
	    	if (externalId != null && externalId.length() > 0) {
	    		myConfig.setExternalIdField(entityPanel.getEntityName(),entityPanel.getExternalIdField());
	    	}
    		entityPanel.setStatus("Configuration is valid");      		
    	} catch (Exception ex) {
    		entityPanel.setStatus(ex.getMessage());    		
    	}
    	
    	setNextButtonAccordingToQuery();
    }
    
    public void aboutToDisplayPanel() {
    	myConfig = (CliConfigDml)DataLoaderCliq.getCliConfig();
    	
    	entityPanel.setShowExternalIdTextInput(myConfig.isExternalIdOperation());
    	entityPanel.setOperationName(myConfig.getConfigValue(Config.OPERATION));
    	
    	setNextButtonAccordingToQuery();
    }   
    
    public Object getNextPanelDescriptor() {
        return ResultPanelDescriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return LoginPanelDescriptor.IDENTIFIER;
    }
    
    //TODO Recheck the external id field and refresh if the user hit the back button
	private void setNextButtonAccordingToQuery() {
		if (myConfig.isEntityNameValid()) {
			if (myConfig.getDmlOperation() == CliConfigDml.DmlOperation.UPSERT) {
				if (entityPanel.getExternalIdField().length() > 0) { //a value was entered
					if (myConfig.isExternalIdValid()) { //is it valid?
						getWizard().setNextFinishButtonEnabled(true);
					} else {
						getWizard().setNextFinishButtonEnabled(false);
					}
				} else {
					getWizard().setNextFinishButtonEnabled(true); //external id not provided
				}
			} else {	
				getWizard().setNextFinishButtonEnabled(true);
			}
		} else {
			getWizard().setNextFinishButtonEnabled(false);
		}
	}
    
    
}
