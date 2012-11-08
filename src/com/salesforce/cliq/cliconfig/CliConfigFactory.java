package com.salesforce.cliq.cliconfig;

import com.salesforce.cliq.DataLoaderCliq;
import com.salesforce.dataloader.config.Config;

/*
 * Based on the CliConfig.DataLoaderOperation, constructs a CliConfig object and returns it
 */
public class CliConfigFactory {
	
	public static CliConfig getCliConfig(CliConfig.DataLoaderOperation op, String dir, String processName, String endPoint) {
		CliConfig myConfig;
		
        switch (op) {
	        case EXTRACT:
	        	myConfig = new CliConfigExport(CliConfigExport.ExportOperation.EXTRACT, dir, processName);                  
	            break;
	        case EXTRACT_ALL:
	        	myConfig = new CliConfigExport(CliConfigExport.ExportOperation.EXTRACT_ALL, dir, processName);                  
	            break;    
	        case INSERT:
	        	myConfig = new CliConfigDml(CliConfigDml.DmlOperation.INSERT, dir, processName);                  
	            break;   
	        case UPDATE:
	        	myConfig = new CliConfigDml(CliConfigDml.DmlOperation.UPDATE, dir, processName);                  
	            break; 
	        case UPSERT:
	        	myConfig = new CliConfigDml(CliConfigDml.DmlOperation.UPSERT, dir, processName);                  
	            break; 
	        case DELETE:
	        	myConfig = new CliConfigDml(CliConfigDml.DmlOperation.DELETE, dir, processName);                  
	            break; 
	        case HARD_DELETE:
	        	myConfig = new CliConfigDml(CliConfigDml.DmlOperation.HARD_DELETE, dir, processName);                  
	            break;
	        default:
	        	myConfig = null; //this should never be hit
	    }
        
        myConfig.setConfigValue(Config.ENDPOINT,endPoint);
        
        return myConfig;		
	}
	
	public static CliConfig getCliConfig(CliConfig.DataLoaderOperation op, String dir, String processName) {
		return CliConfigFactory.getCliConfig(op,dir,processName,null);
	}
}
