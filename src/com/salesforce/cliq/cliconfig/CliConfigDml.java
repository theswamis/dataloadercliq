package com.salesforce.cliq.cliconfig;

import java.io.File;
import java.io.FileWriter;

import com.salesforce.cliq.cliconfig.CliConfig.DataLoaderOperation;
import com.salesforce.cliq.cliconfig.CliConfig.Operation;
import com.salesforce.cliq.cliconfig.CliConfigExport.ExportOperation;
import com.salesforce.dataloader.config.Config;
import com.sforce.soap.partner.fault.LoginFault;
import com.sforce.soap.partner.fault.UnexpectedErrorFault;

/**
 * Extends CliConfig with specific implementation for DML operations
 * INSERT, UPDATE, UPSERT, DELETE
 * 
 * @author vswamidass
 *
 */
public class CliConfigDml extends CliConfig {
	
	public static final String EXTERNAL_ID_PROPERTY = "sfdc.externalIdField";
	
	public enum DmlOperation implements Operation {
		INSERT(false), UPDATE(false), UPSERT(true), DELETE(false), HARD_DELETE(false);

		public DataLoaderOperation toDataLoaderOperation() {
			switch (this) {
			case INSERT:
				return CliConfig.DataLoaderOperation.INSERT;
			case UPDATE:
				return CliConfig.DataLoaderOperation.UPDATE;
			case UPSERT:
				return CliConfig.DataLoaderOperation.UPSERT;
			case DELETE:
				return CliConfig.DataLoaderOperation.DELETE;	
			case HARD_DELETE:
				return CliConfig.DataLoaderOperation.HARD_DELETE;	
			default:
				return null; //this should never be hit
			}
		}

		private boolean canUseExternalId;

		private DmlOperation(boolean usesExternalId) {
			canUseExternalId = usesExternalId;
		}
		
		public boolean isExternalIdOperation() {
			return canUseExternalId;
		}
	}
	DmlOperation dmlOperation;
		
	/**
	 * Constructor
	 * Sets the process-conf.xml process.operation to the operation parameter.
	 * 
	 * @param dir			Data Loader Directory
	 * @param name			Process Name
	 * @param operation		DML Operation (enum Operations)
	 */
	public CliConfigDml(DmlOperation operation, String dir, String name) {
		super(operation.toDataLoaderOperation(), dir, name);
		//setEndpoint(endPoint);
		
		dmlOperation = operation;	
	}
	
	/**
	 * Overrides CliConfig.setConfigDefaults
	 * 
	 * Sets specific process-conf.xml parameters for DML operations.
	 */
	protected void setConfigDefaults() {
		setConfigValue(Config.MAPPING_FILE,getSdlFile().getAbsolutePath());	
		setConfigValue(Config.DAO_NAME,getInputFile().getAbsolutePath());
		setConfigValue(Config.DAO_TYPE,"csvRead");
		
		super.setConfigDefaults();
	}
	
	/**
	 * Overrides CliConfig.createFiles to add files for DML operations.
	 */
	public void createFiles() throws Exception {
		super.createFiles();
		createSdl();
		createInputFile();		
	}

	protected File getSdlFile() {
		return new File(CONFIG_DIR,PROCESS_NAME + ".sdl");
	}
	
	protected File getInputFile() {
		return new File(READ_DIR,PROCESS_NAME + ".csv");
	}
	
	/**
	 * Creates a placeholder sdl mapping file
	 * 
	 * @throws Exception
	 */
	protected void createSdl() throws Exception {
		File sdl = getSdlFile();

		sdl.createNewFile();

		FileWriter fw = new FileWriter(sdl);
		fw.write("# Created by Dataloader Cliq" + NEWLINE);
		fw.write("#" + NEWLINE);
		fw.write("# Create your field mapping list in the following format" + NEWLINE);		
		fw.write("# <Your CSV Field1>=<Salesforce Field in " + getConfigValue(Config.ENTITY) + ">" + NEWLINE);
		fw.write("# NOTE: Salesforce Fields are case-sensitive. " + NEWLINE);
		fw.write(NEWLINE);
		fw.write("# Example: " + NEWLINE);
		fw.write("ID=Id" + NEWLINE);
		    		
		fw.flush();
		fw.close();
	};
	
	/**
	 * Creates a placeHolder CSV file in the read directory
	 * 
	 * @throws Exception
	 */
	protected void createInputFile() throws Exception {
		File inputFile = getInputFile();
    	
		inputFile.createNewFile();
		
		FileWriter fw = new FileWriter(inputFile);
		fw.write("Replace this file with your csv.  The first row should have the field names.");
		fw.write("Make sure to update the sdl file also " + getSdlFile().getAbsolutePath());
		fw.flush();
		fw.close();
	};		

	public void setExternalIdField(String entity, String externalIdField)
			throws Exception {

		if (this.isFieldExternalId(entity, externalIdField)) {
			this.setConfigValue(EXTERNAL_ID_PROPERTY, externalIdField);
		} else {
			this.setConfigValue(EXTERNAL_ID_PROPERTY, null);
			throw new Exception("External Id not valid for " + entity);
		}
	}
	
	public String getExternalIdField() {
		return getConfigValue(EXTERNAL_ID_PROPERTY);
	}
	
	public boolean isExternalIdOperation() {
		return dmlOperation.isExternalIdOperation();
	}
	
	public boolean isExternalIdValid() {
		try {
			return this.isFieldExternalId(this.getConfigValue(Config.ENTITY), this.getExternalIdField());
		} catch (Exception e) {
			return false;
		}
	}
	
	public void setDmlOperation(DmlOperation op) {
		dmlOperation = op;
		setDataLoaderOperation(op.toDataLoaderOperation());
	}

	public DmlOperation getDmlOperation() {
		return dmlOperation;
	}
}
