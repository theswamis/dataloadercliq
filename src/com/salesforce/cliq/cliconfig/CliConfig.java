package com.salesforce.cliq.cliconfig;

import java.io.*;
import java.util.*;
import java.security.*;

import com.sforce.soap.partner.*;
import com.sforce.soap.partner.fault.*;

import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;

import com.salesforce.cliq.DataLoaderCliq;
import com.salesforce.dataloader.client.ClientBase;
import com.salesforce.dataloader.config.*;
import com.salesforce.dataloader.controller.*;
import com.salesforce.dataloader.security.*;

/**
 * Abstract class for all CLI configurations.
 * 
 * @author vswamidass
 *
 */
public abstract class CliConfig {

	public static File HOME_DIR;		// The Data Loader Directory
	public static File SCRIPT_DIR;		// processname directory
	public static File WRITE_DIR;		// processname/write directory
	public static File READ_DIR;		// processname/read directory
	public static File LOG_DIR;			// processname/log directory
	public static File CONFIG_DIR;		// processname/config directory	
	public static File OUTPUT_DIR;		// location of CLIq output files/scripts
	
	public static String INSTALL_DIR = "/cliq/";	//The cliq home directory
	
	public static String WINDOWS_JAVA_DIR = "Java";  //for windows, the location of the JRE in the DL Directory
	
	/**
	 *  This is the data loader process-conf.xml setting
	 */
	public static String endpoint;
	
	public static final List<String> allowedCliqProperties = Arrays.asList(
			Config.USERNAME,
			Config.PASSWORD,
			Config.ENDPOINT,			
			Config.PROXY_HOST,
			Config.PROXY_PORT,
			Config.PROXY_NTLM_DOMAIN,
			Config.PROXY_USERNAME,
			Config.PROXY_PASSWORD
	);
	
	/*
	 * Defines the type of operation
	 */
	public enum DataLoaderOperation {
		EXTRACT("Export"),
		EXTRACT_ALL("Export All (Include Deleted)"),
		INSERT,
		UPDATE,
		UPSERT,
		DELETE,
		HARD_DELETE("Hard Delete");
		
		private String displayName;
		
		private DataLoaderOperation() {
			String enumName = this.toString().toLowerCase();		
			displayName = enumName.substring(0,1).toUpperCase() + enumName.substring(1);
		}
		
		private DataLoaderOperation(String name) {
			displayName = name;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}
	
	/*
	 * This should be implemented by subclass enums
	 */
	interface Operation {
		 public DataLoaderOperation toDataLoaderOperation();		 
	}
	
	public DataLoaderOperation operation;
			
	public static String PROCESS_NAME;	// The user specified Process Name

	public static String PROCESS_DIR = "cliq_process";	// The user specified Process Name
	
	/* The system newline character used to create the CLI files */
	public static final String NEWLINE = System.getProperty("line.separator");
		
	/**
	 * The unencrypted password, stored temporarily only,
	 * so we can login to Salesforce  
	 */
	private String password;

	/**
	 * The unencrypted password, stored temporarily only,
	 * so we can login to Salesforce
	 */
	private String proxyPassword;
	
	/**
	 * Map of process-conf.xml property to value
	 */
	protected Map<String,String> configMap = new TreeMap<String,String>();
	
	/**
	 * Salesfore API
	 */
	//protected SoapBindingStub binding = null;
    protected ConnectorConfig config = new ConnectorConfig();
 	protected PartnerConnection connection;	
 	
 	/** Main **/
 	

	
	
	/**
	 * Constructor
	 * 
	 * @param dir		The home directory for Data Loader
	 * @param name		The process name used to name the files
	 * 					and identify the process in process-conf.xml.
	 */
	public CliConfig (DataLoaderOperation op, String dir, String name) {
		this(op,dir,dir + "/" + PROCESS_DIR,name);
	}	
	
	/**
	 * Constructor
	 * 
	 * @param homeDir	The home directory for Data Loader
	 * @param outputDir	The output directory for CLIq files
	 * @param name		The process name used to name the files
	 * 					and identify the process in process-conf.xml.
	 */
	public CliConfig (DataLoaderOperation op, String homeDir, String outputDir, String name) {
		operation = op;
		
		HOME_DIR = new File(homeDir);
		
		/* Set a default Output Directory */
		OUTPUT_DIR = new File(outputDir);
		
		PROCESS_NAME = name.replaceAll("[\\W|\\d]+","_");				
		SCRIPT_DIR = new File(OUTPUT_DIR.getAbsolutePath() + "/" + PROCESS_NAME);		
		WRITE_DIR = new File(SCRIPT_DIR.getAbsolutePath() + "/write");
		READ_DIR = new File(SCRIPT_DIR.getAbsolutePath() + "/read");
		LOG_DIR = new File(SCRIPT_DIR.getAbsolutePath() + "/log");
		CONFIG_DIR = new File(SCRIPT_DIR.getAbsolutePath() + "/config");
		
    	try {
			connection =  Connector.newConnection(config);
    	} catch (Exception e) {
    		//this is expected, but we are trying a hack to get the endpoint and version from Dataloader.jar
    	}
    	String defaultEndpoint = config.getAuthEndpoint();
    	String fixedUpEndpoint = "https://www.salesforce.com/" + defaultEndpoint.substring(defaultEndpoint.indexOf("services"));
    	setConfigValue(Config.ENDPOINT,fixedUpEndpoint);

    	//On Windows, we pass in the value of the java directory location
    	String javaDirProperty = System.getProperty("java.dir");
    	if (javaDirProperty != null) {
    		WINDOWS_JAVA_DIR = javaDirProperty;
    	}

		setConfigDefaults();
	}	
	
	/**
	 * Sets default values for process-conf.xml
	 */
	protected void setConfigDefaults() {
		setConfigValue(Config.ENDPOINT,endpoint);
		
        setConfigValue(Config.READ_UTF8,"true");
		setConfigValue(Config.WRITE_UTF8,"true");		
		setConfigValue(Config.ENABLE_EXTRACT_STATUS_OUTPUT,"true");
		setConfigValue(Config.ENABLE_LAST_RUN_OUTPUT,"true");
		setConfigValue(Config.LAST_RUN_OUTPUT_DIR,LOG_DIR.getAbsolutePath());
		setConfigValue(Config.OUTPUT_STATUS_DIR,LOG_DIR.getAbsolutePath());
		setConfigValue(Config.BULK_API_CHECK_STATUS_INTERVAL,"5000");
		setConfigValue(Config.BULK_API_SERIAL_MODE,"5000");
		setConfigValue(Config.DEBUG_MESSAGES,"false");
		setConfigValue(Config.ENABLE_RETRIES,"true");
		setConfigValue(Config.EXTRACT_REQUEST_SIZE,"500");
		setConfigValue(Config.INSERT_NULLS,"false");
		setConfigValue(Config.LOAD_BATCH_SIZE,"100");
		setConfigValue(Config.MAX_RETRIES,"3");
		setConfigValue(Config.MIN_RETRY_SLEEP_SECS,"2");
		setConfigValue(Config.NO_COMPRESSION,"false");
		setConfigValue(Config.TIMEOUT_SECS,"60");
		setConfigValue(Config.BULK_API_ENABLED,"false");
		setConfigValue(Config.OPERATION,operation.toString().toLowerCase());	
	
		/* Load the settings from the properties file */
    	try {
    	    Properties cliqProperties = new Properties();
    	    cliqProperties.load(new FileInputStream("cliq.properties"));
        	
    	    for (String property : allowedCliqProperties) {
    	    	String propertyValue = cliqProperties.getProperty(property);
    	    	if (propertyValue != null) {
    	    		if (property == Config.PASSWORD) { //Password must be encrypted
    	    			setPassword(propertyValue);
    	    		} else {
        	    		setConfigValue(property, propertyValue);    	    			
    	    		}
    	    	}
    	    }            
    	} catch (IOException e) {
    		System.out.println("WARNING: unable to read cliq.properties.");
    	}

	}
	
	/**
	 * Sets a key/value pair for process-conf.xml
	 * The list of constants are in com.salesforce.dataloader.config.Config
	 * 
	 * @param key		The process-conf.xml key 
	 * @param value		The process-conf.xml value
	 */
	public void setConfigValue(String key, String value) {
		if (key == Config.ENDPOINT) {
			if (value != null) { 
				configMap.put(key,value);
				endpoint = value;
			}
		} else if (key == Config.PROXY_PASSWORD) {
			proxyPassword = value;
			configMap.put(key,value);			
		} else {
			configMap.put(key,value);
		}
	}
	
	public String getConfigValue(String key) {
		return configMap.get(key);
	}
	
	/**
	 * Checks if the user has logged in to Salesforce.com
	 * 
	 * @return		true if the loginResult is not null 
	 * 				false if the loginResult is null (not logged in)
	 */	 	
	public boolean isLoggedIn() {
		DataLoaderCliq.log("Session Id: " + config.getSessionId());
		
		return (config.getSessionId() != null);
	}
	
	/**
	 * Gets the Unique Process Name for this instance of CLIq
	 * 
	 * @return String	ProcessName
	 */
	public String getProcess() {
		return PROCESS_NAME;
	}
	
	/**
	 * Sets the entity (Salesforce object)
	 * Queries Salesforce to verify that it is valid and to get 
	 * the right case 
	 * 
	 * @param name			The Entity (Object) name
	 * @throws Exception	Indicates a failure to verify the entity
	 */
	public void setEntityName(String name) throws Exception {
		DescribeSObjectResult describeSObjectResult;
		
		setConfigValue(Config.ENTITY,null);
        try {
        	describeSObjectResult = connection.describeSObject(name);
        } catch (ApiFault ex) {
        	throw new Exception(ex.getExceptionMessage());
        }
		
		if (describeSObjectResult != null) {
			setConfigValue(Config.ENTITY,describeSObjectResult.getName());
		}	
	}
	
	public boolean isEntityNameValid() {
		return getConfigValue(Config.ENTITY) != null;
	}
	
	public boolean isFieldExternalId(String entity,String field) throws Exception {
		DescribeSObjectResult describeSObjectResult;
		
        try {
        	describeSObjectResult = connection.describeSObject(entity);
        	for (Field f : describeSObjectResult.getFields()) {
        		if (f.getName().equals(field) && f.isExternalId()) {
        			return true;
        		}
         	}
        	return false; //we didn't find the external id
        } catch (Exception ex) {
        	return false;
        }
	}
	
	protected void setDataLoaderOperation(DataLoaderOperation op) {
		operation = op;
	}

	protected DataLoaderOperation getDataLoaderOperation() {
		return operation;
	}
	
	/**
	 * encryptPassword calls the EncryptionUtil Class
	 * from the DataLoader.jar library.
	 * 
	 * @param p		plain-text password
	 * @return		CLI compatible encrypted password string
	 */
	private String encryptPassword(String p) {	
		EncryptionUtil eu = new EncryptionUtil();
		try {
			return eu.encryptString(p);			
		} catch (GeneralSecurityException e) {
			return null;
		}		
	}
	
	/**
	 * Set the process-conf.xml password to the encrypted value.
	 * Saves the plain-text string for logging into salesforce. 
	 * 
	 * @param p		The plain-text password.
	 */
	public void setPassword(String p) {
		password = p; //save unencrypted pw for login
		setConfigValue(Config.PASSWORD,encryptPassword(p));
	}
	
	public String getPlainPassword() {
		return password;
	}		
	
	public String getPlainProxyPassword() {
		return proxyPassword;
	}	
	
	/**
	 * Main method to create directories and files for CLI configuation 
	 */
	public void createCliConfig() throws Exception {
		/* 
		 * These methods may be overridden by subclasses to 
		 * add new directories or files.
		 */ 
		createDirectories();
		createFiles();
	}
	
	/**
	 * Creates the Process directory and all subdirectories.
	 * 
	 * @throws Exception		If the Script Directory (Process Name) exists
	 */
	public void createDirectories() throws Exception {
		
		if (SCRIPT_DIR.isDirectory()) {
			throw new Exception("Directory already exists.  Please delete first.");
		}		
		
		OUTPUT_DIR.mkdirs();
		SCRIPT_DIR.mkdir();
		WRITE_DIR.mkdir();    	
    	READ_DIR.mkdir();  	
    	LOG_DIR.mkdir(); 
    	CONFIG_DIR.mkdir();
	}
	
	/**
	 * Creates all configuration and script files for running the CLI Data Loader.
	 * 
	 * @throws Exception
	 */
	public void createFiles() throws Exception {
		createProcessXml();
		createLogXml();
		createConfigProperties();
		createBatScript();
		createShScript();
	}	
	
	public void createProcessXml() throws Exception {		
		File processXml = new File(CONFIG_DIR,"process-conf.xml");
		processXml.createNewFile();
		
		FileWriter fw = new FileWriter(processXml);
		fw.write("<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">" + NEWLINE);
		fw.write("<beans>" + NEWLINE);
		fw.write("\t<bean id=\"" + PROCESS_NAME + "\" class=\"com.salesforce.dataloader.process.ProcessRunner\" singleton=\"false\">" + NEWLINE);
		fw.write("\t\t<description>Created by Dataloader Cliq.</description>" + NEWLINE);
		fw.write("\t\t<property name=\"name\" value=\"" + PROCESS_NAME + "\"/>" + NEWLINE);    		
		fw.write("\t\t<property name=\"configOverrideMap\">" + NEWLINE);
		fw.write("\t\t\t<map>" + NEWLINE);
		
		//Loop through properties
		for (String k : configMap.keySet()) {
			fw.write("\t\t\t\t<entry key=\"" + k + "\" value=\"" + configMap.get(k) + "\"/>" + NEWLINE);			
		}
		
		fw.write("\t\t\t</map>" + NEWLINE);
		fw.write("\t\t</property>" + NEWLINE);
		fw.write("\t</bean>" + NEWLINE);
		fw.write("</beans>" + NEWLINE);
		
		fw.flush();
		fw.close();
	}
	
	public void createLogXml() throws Exception {		
		File logXml = new File(CONFIG_DIR,"log-conf.xml");    	
		logXml.createNewFile();
		
		FileWriter fw = new FileWriter(logXml);
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEWLINE);
		fw.write("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">" + NEWLINE);
		fw.write("<log4j:configuration>" + NEWLINE);
		
		fw.write("\t<appender name=\"fileAppender\" class=\"org.apache.log4j.RollingFileAppender\">" + NEWLINE);
		fw.write("\t\t<param name=\"File\"   value=\"log/sdl.log\" />" + NEWLINE);
		fw.write("\t\t<param name=\"Append\" value=\"true\" />" + NEWLINE);
		fw.write("\t\t<param name=\"MaxFileSize\" value=\"100KB\" />" + NEWLINE);
		fw.write("\t\t<param name=\"MaxBackupIndex\" value=\"1\" />" + NEWLINE);
		fw.write("\t\t<layout class=\"org.apache.log4j.PatternLayout\">" + NEWLINE);
		fw.write("\t\t\t<param name=\"ConversionPattern\" value=\"%d %-5p [%t] %C{2} %M (%F:%L) - %m%n\"/>" + NEWLINE);
		fw.write("\t\t</layout>" + NEWLINE);	    
		fw.write("\t</appender>" + NEWLINE);
		
		fw.write("\t<appender name=\"STDOUT\" class=\"org.apache.log4j.ConsoleAppender\">" + NEWLINE);
		fw.write("\t\t<layout class=\"org.apache.log4j.PatternLayout\">" + NEWLINE);
		fw.write("\t\t\t<param name=\"ConversionPattern\" value=\"%d %-5p [%t] %C{2} %M (%F:%L) - %m%n\"/>" + NEWLINE);
		fw.write("\t\t</layout>" + NEWLINE);	    
		fw.write("\t</appender>" + NEWLINE);
		
		fw.write("\t<category name=\"org.apache.log4j.xml\">" + NEWLINE);
		fw.write("\t\t<priority value=\"warn\" />" + NEWLINE);
		fw.write("\t\t<appender-ref ref=\"fileAppender\" />" + NEWLINE);
		fw.write("\t\t<appender-ref ref=\"STDOUT\" />" + NEWLINE);
		fw.write("\t</category>" + NEWLINE);
		
		fw.write("\t<logger name=\"org.apache\" >" + NEWLINE);
		fw.write("\t\t<level value =\"warn\" />" + NEWLINE);
		fw.write("\t</logger>" + NEWLINE);
		
		fw.write("\t<root>" + NEWLINE);
		fw.write("\t\t<priority value =\"info\" />" + NEWLINE);
		fw.write("\t\t<appender-ref ref=\"fileAppender\" />" + NEWLINE);
		fw.write("\t\t<appender-ref ref=\"STDOUT\" />" + NEWLINE);
		fw.write("\t</root>" + NEWLINE);
		
		fw.write("</log4j:configuration>" + NEWLINE);
  				
		fw.flush();
		fw.close();    		
	}
	
	public void createConfigProperties() {		
		File configProperties = new File(CONFIG_DIR,"config.properties");
    	try {
    		configProperties.createNewFile();
    	} catch (IOException ie) {
    		//Do Nothing
    	}		
	}	
	
	/**
	 * Creates .bat script for Windows
	 * 
	 * @throws Exception
	 */
	public void createBatScript() throws Exception {		
		File batScript = new File(SCRIPT_DIR,PROCESS_NAME + ".bat");
		
		batScript.createNewFile();

		FileWriter fw = new FileWriter(batScript);
		
		fw.write("SET DLPATH=\"" + HOME_DIR + "\"" + NEWLINE);
		fw.write("SET DLCONF=\"" + CONFIG_DIR + "\"" + NEWLINE);
		fw.write("SET DLDATA=\"" + WRITE_DIR + "\"" + NEWLINE);
		
		fw.write("call %DLPATH%\\" + WINDOWS_JAVA_DIR + "\\bin\\java.exe " +
				"-cp %DLPATH%\\* " +
				"-Dsalesforce.config.dir=%DLCONF% com.salesforce.dataloader.process.ProcessRunner " +
				"process.name=" + PROCESS_NAME + NEWLINE);
		
		fw.write("REM To rotate your export files, uncomment the line below" + NEWLINE);
		fw.write("REM copy %DLDATA%\\" + PROCESS_NAME + ".csv " + 
				"%DLDATA%\\%date:~10,4%%date:~7,2%%date:~4,2%-%time:~0,2%-" + PROCESS_NAME + ".csv" + NEWLINE);
		
		fw.flush();
		fw.close();		    		
	}
	
	/**
	 * Creates .sh script for UNIX
	 * 
	 * @throws Exception
	 */
	public void createShScript() throws Exception {		
		File batScript = new File(SCRIPT_DIR,PROCESS_NAME + ".sh");
		
		batScript.createNewFile();

		FileWriter fw = new FileWriter(batScript);
		
		fw.write("#!/bin/sh" + NEWLINE);
			
		fw.write("export DLPATH=\"" + HOME_DIR + "\"" + NEWLINE);
		fw.write("export DLCONF=\"" + CONFIG_DIR + "\"" + NEWLINE);
		
		fw.write("java -cp \"$DLPATH/*\" " +
				"-Dsalesforce.config.dir=$DLCONF " +
				"com.salesforce.dataloader.process.ProcessRunner " +
				"process.name=" + PROCESS_NAME + NEWLINE);
		fw.flush();
		fw.close();		    		
	}
	
	/**
	 * Verifies that the username and password are valid with Salesforce
	 * 
	 * setUsername and setPassword must be called before this method. 
	 */
	public void doSalesforceLogin() throws Exception {
        if (getConfigValue(Config.USERNAME).length() == 0 || getPlainPassword().length() == 0)
        	throw new Exception("Username and password cannot be blank.");  
        else {
       		DataLoaderCliq.log("Logging in to salesforce as " + getConfigValue(Config.USERNAME));
       		
            config.setUsername(getConfigValue(Config.USERNAME));
            config.setPassword(getPlainPassword());
            config.setAuthEndpoint(getConfigValue(Config.ENDPOINT));
            
            if (getConfigValue(Config.PROXY_HOST) != null && getConfigValue(Config.PROXY_HOST).length() > 0) {
            	System.out.println("Enabling proxy host: " + getConfigValue(Config.PROXY_HOST));            	
            	config.setProxy(getConfigValue(Config.PROXY_HOST), Integer.valueOf(getConfigValue(Config.PROXY_PORT)));
            }
            if (getConfigValue(Config.PROXY_USERNAME) != null && getConfigValue(Config.PROXY_USERNAME).length() > 0) {
            	config.setProxyUsername(getConfigValue(Config.PROXY_USERNAME));
            	config.setProxyPassword(getConfigValue(Config.PROXY_PASSWORD));
            }
            
            try {
            	connection = new PartnerConnection(config);
            	connection.login(getConfigValue(Config.USERNAME), getPlainPassword());

            } catch (LoginFault  ex) {
            	DataLoaderCliq.log("Login exception: " + ex.getFaultCode(), ex);
            	throw new Exception(ex.getFaultCode().getLocalPart() + " - " + ex.getExceptionMessage());            	
            } catch (ApiFault ex) {
            	DataLoaderCliq.log("API exception: " + ex.getCause(), ex);
            	throw new Exception(ex.getFaultCode().getLocalPart() + " - " + ex.getExceptionMessage());
            } catch (ConnectionException  ex) {
            	DataLoaderCliq.log("Connection exception: " + ex.getCause(), ex);
            	throw ex;            	
            }
        }
    }
}
