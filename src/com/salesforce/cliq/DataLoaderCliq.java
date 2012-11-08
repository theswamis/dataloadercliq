package com.salesforce.cliq;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardPanelDescriptor;
import com.salesforce.cliq.*;
import com.salesforce.cliq.cliconfig.CliConfig;
import com.salesforce.cliq.cliconfig.CliConfigDml;
import com.salesforce.cliq.cliconfig.CliConfigExport;
import com.salesforce.cliq.cliconfig.CliConfigFactory;
import com.salesforce.cliq.ui.EntityPanelDescriptor;
import com.salesforce.cliq.ui.LoginPanelDescriptor;
import com.salesforce.cliq.ui.OperationPanelDescriptor;
import com.salesforce.cliq.ui.QueryPanelDescriptor;
import com.salesforce.cliq.ui.ResultPanelDescriptor;

import com.salesforce.dataloader.config.*;

import java.io.*;
import java.net.*;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Properties;

/**
 * DataLoaderCliq is the main class for Data Loader CLIq.
 * It provides both a Text and Graphical UI for the CliConfig
 * Classes. On execution of main, it will check for newer
 * versions in the public SVN repository.
 * 
 * @author      Vijay Swamidass 
 * 
 */
public class DataLoaderCliq {
	
	public static final String VERSION_URL = "http://dataloadercliq.googlecode.com/svn/trunk/src/version.properties";
	public static final String DOWNLOAD_URL = "http://code.google.com/p/dataloadercliq/";

	public static boolean debugMode = false;
	
	/* Used to read user input */
    static BufferedReader rdr = new BufferedReader(
            new java.io.InputStreamReader(System.in));
	
    /**
     *  A shared instance of CliConfig used by the various
     *  Wizard panels  
     */
    static CliConfig theConfig;  
    
    /**
     * Used by the Wizard to set the shared instance of CliConfig
     * 
     * @param c	An instance of CliConfig
     */    
    public static void setCliConfig(CliConfig c) {
    	theConfig = c;
    }

    /**
     * Used by the Wizard to get the shared instance of CliConfig 
     * 
     * @param c	An instance of CliConfig
     */   
    public static CliConfig getCliConfig() {
    	return theConfig;
    }
    
    public static boolean isDebugMode() {
    	return debugMode;
    }
    
    public static void log(String m) {
    	log(m,null);
    }
    
    public static void log(String m,Exception e) {
    	if (DataLoaderCliq.isDebugMode()) {
    		if (e != null) {
    			e.printStackTrace();
    		}
    		System.out.println("log: " + m);
    		
    	}
    }
    
    /**
     * Shows a text prompt, reads the user input, and returns the input.
     * 
     * @param prompt 	String to show the user
     * @return 			The user input 
     */
    static String getUserInput(String prompt) {
        System.out.print(prompt);
        try {
            return rdr.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Shows a simple text menu
     */
    private static void displayMenu() {
        // set up a simple menu system
    	Integer operationNumber = 1;
    	for (CliConfig.DataLoaderOperation operation : CliConfig.DataLoaderOperation.values()) {
            System.out.println(operationNumber++ + ". " + operation.toString());
    	}
    }
    
    /**
     * Runs the GUI using the Sun Wizard Class
     */
    private static void runGui() {
        Wizard wizard = new Wizard();
        wizard.getDialog().setTitle("Data Loader CLIq");
        
        WizardPanelDescriptor operationDescriptor = new OperationPanelDescriptor();
        wizard.registerWizardPanel(OperationPanelDescriptor.IDENTIFIER, operationDescriptor);

        WizardPanelDescriptor loginDescriptor = new LoginPanelDescriptor();
        wizard.registerWizardPanel(LoginPanelDescriptor.IDENTIFIER, loginDescriptor);
        
        WizardPanelDescriptor queryDescriptor = new QueryPanelDescriptor();
        wizard.registerWizardPanel(QueryPanelDescriptor.IDENTIFIER, queryDescriptor);
        
        WizardPanelDescriptor entityDescriptor = new EntityPanelDescriptor();
        wizard.registerWizardPanel(EntityPanelDescriptor.IDENTIFIER, entityDescriptor);                

        WizardPanelDescriptor resultDescriptor = new ResultPanelDescriptor();
        wizard.registerWizardPanel(ResultPanelDescriptor.IDENTIFIER, resultDescriptor);  
        
        wizard.setCurrentPanel(OperationPanelDescriptor.IDENTIFIER);
        
        int ret = wizard.showModalDialog();  
    }
    
    private static void showError(String message) {
		System.out.println("=========================================================");
		System.out.println("## " + message);
		System.out.println("=========================================================");      
    }
    
    private static void showStepSeparator(Integer stepNumber, String title) {
    	System.out.println();
		System.out.println("#########################################################");
		System.out.println("Step " + stepNumber);
		System.out.println(title);		
		System.out.println("#########################################################");     
    }
    
    /**
     * Run the Text-based UI
     */
    private static void runTextUi() {
    	
    	String username;
    	String processName;
    	String password;    	
    	String dlHome;
    	
    	showStepSeparator(1, "Choose Operation");
    	displayMenu();
    	Integer menuOption = Integer.valueOf(getUserInput("Operation number? "));
        while (menuOption < 1 && menuOption > CliConfig.DataLoaderOperation.values().length) {  //Get the count of options
        	System.out.println("Please enter a valid choice: ");
        	displayMenu();
        }

    	System.out.println("Each process needs a unique indentifier.  Use word characters only."); 
    	processName = getUserInput("Enter the name of your process: ");
        
        File dlHomeDir = new File(System.getProperty("user.dir"));
        dlHome = dlHomeDir.getParent();
     
        CliConfig.DataLoaderOperation operation = CliConfig.DataLoaderOperation.values()[menuOption-1];
        
        System.out.println("Configuring for operation: " + operation.toString());
        theConfig = CliConfigFactory.getCliConfig(operation, dlHome, processName);

    	showStepSeparator(2, "Verify Salesforce Login");
        System.out.println("Using endpoint: " + theConfig.getConfigValue(Config.ENDPOINT));
  
    	try {
    		if (theConfig.getConfigValue(Config.USERNAME).length() > 0) {
    			System.out.println("Logging in to Salesforce...");
    			theConfig.doSalesforceLogin();
    		}
    	} catch (Exception e) {
    		showError("Error logging in to Salesforce with cliq.properties: " + e.getMessage()); 
    	}
        
    	while (!theConfig.isLoggedIn()) {
        	username = getUserInput("Enter your username: ");
            password = getUserInput("Enter your password: ");
            
        	theConfig.setConfigValue(Config.USERNAME,username);
        	theConfig.setPassword(password);
        	
        	try {
    			System.out.println("Logging in to Salesforce...");
        		theConfig.doSalesforceLogin();
        	} catch (Exception e) {
        		showError("Error logging in to Salesforce: " + e.getMessage()); 
        	}
        }

        
        if (theConfig instanceof CliConfigExport) {
        	String query;
        	showStepSeparator(3, "Configure Export");        	
        	while (!((CliConfigExport)theConfig).isQueryValid()) {
        		query = getUserInput("Enter your SOQL query: ");
	        	try {
	        		((CliConfigExport)theConfig).setQueryAndEntity(query);
	        	} catch (Exception e) {
	        		showError(e.getMessage());
	        	}
        	}
        } else {
        	String entity;
        	showStepSeparator(3, "Validate Salesforce Entity");           	
        	while (!theConfig.isEntityNameValid()) {
        		entity = getUserInput("Enter the Object name: ");
	        	try {
	        		theConfig.setEntityName(entity);
	        	} catch (Exception e) {
	        		showError("Invalid Object - Please try again.");
	        	}
        	}
        }
        
    	showStepSeparator(4, "Create Scripts");   
    	System.out.println("Creating files...");
    	try {
    		theConfig.createCliConfig();
    		System.out.println("CLIq completed successfully!");
    		System.out.println("Your files are in: " + theConfig.SCRIPT_DIR);
    	} catch (Exception e) {
    		showError("Error creating configuration files: " + e.getMessage());
    	}
    }

    /**
     * Checks if newer version is available and pauses for user response if so.
     * If an argument is provided (any argument), run the Text UI, otherwise
     * run the GUI.
     * 
     * @param args		Any single arg will launch the Text UI
     */
    public static void main(String[] args) {
    	
    	DataLoaderCliq dlc = new DataLoaderCliq();

		System.out.println("You can download the latest version of CLIq at:");
		System.out.println(DOWNLOAD_URL);
    	
		boolean textMode = false;
		
		for (String param : args) {
			if (param.contains("-d")) {
				System.out.println("Enabling debug mode.");
				debugMode = true;
			} else if (param.contains("-t")) {
				textMode = true;
			}
		}
		
    	if (textMode) {
    		runTextUi();
    	} else {
    		System.out.println("Loading GUI...");
    		runGui();
    	}
    }
    
}