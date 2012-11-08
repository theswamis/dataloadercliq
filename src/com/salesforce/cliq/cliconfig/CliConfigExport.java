package com.salesforce.cliq.cliconfig;

import com.salesforce.cliq.cliconfig.CliConfig.Operation;
import com.salesforce.dataloader.config.Config;
import com.sforce.soap.partner.fault.*;

import java.io.File;
import java.util.regex.*;

/**
 * Extends CliConfig with specific implementation for Export(Extract) operations
 * 
 * @author vswamidass
 * 
 */
public class CliConfigExport extends CliConfig {

	public enum ExportOperation implements Operation {
		EXTRACT, EXTRACT_ALL;

		public DataLoaderOperation toDataLoaderOperation() {
			switch (this) {
			case EXTRACT_ALL:
				return CliConfig.DataLoaderOperation.EXTRACT_ALL;
			default:
				return CliConfig.DataLoaderOperation.EXTRACT;
			}
		}
	}
	ExportOperation exportOperation;

	/* Indicates if the current query has been validated with Salesforce */
	private boolean queryValid = false;

	private String queryOriginal = null;

	/**
	 * Constructor
	 * 
	 * @param dir
	 *            Data Loader home dir
	 * @param name
	 *            The Process name
	 */
	public CliConfigExport(ExportOperation operation, String dir, String processName) {
		super(operation.toDataLoaderOperation(), dir, processName);	
		exportOperation = operation;
	}

	/**
	 * Sets specific process-conf.xml parameters for Export operations.
	 */
	@Override
	protected void setConfigDefaults() {
		setConfigValue(Config.OPERATION, "extract");
		setConfigValue(Config.DAO_TYPE, "csvWrite");

		File outputFile = new File(WRITE_DIR, PROCESS_NAME + ".csv");
		setConfigValue(Config.DAO_NAME, outputFile.getAbsolutePath());

		super.setConfigDefaults();
	}

	/**
	 * Sets the process-conf.xml extractionSOQL and entity key/values - Verifies
	 * that the query is valid with Salesforce - Parses out the entity from the
	 * query
	 * 
	 * @param q
	 *            SOQL Query to Export
	 * @throws Exception
	 *             Salesforce reported an error with the query
	 */
	public void setQueryAndEntity(String q) throws Exception {

		String xmlCompatibleQuery = new String();

		/* Set the variables */
		queryOriginal = q;
		setQueryValid(false);

		/* Fix > */
		Pattern greaterThan = Pattern.compile(">");
		xmlCompatibleQuery = greaterThan.matcher(getQueryOriginal())
				.replaceAll("&gt;");

		/* Fix < */
		Pattern lessThan = Pattern.compile("<");
		xmlCompatibleQuery = lessThan.matcher(xmlCompatibleQuery).replaceAll(
				"&lt;");

		setConfigValue(Config.EXTRACT_SOQL, xmlCompatibleQuery);

		/* Run the query and verify */
		try {
			connection.query(getQueryOriginal());
			setQueryValid(true);
		} catch (UnexpectedErrorFault uef) {
			throw new Exception(uef.getExceptionMessage());
		} catch (MalformedQueryFault mqf) {
			throw new Exception(mqf.getExceptionMessage());
		} catch (InvalidFieldFault iff) {
			throw new Exception(iff.getExceptionMessage());
		} catch (InvalidSObjectFault isf) {
			throw new Exception(isf.getExceptionMessage());
		} catch (Exception e) {
			throw new Exception("Unknown error in query.");
		}

		/* TODO need to validate */
		Pattern p = Pattern.compile("FROM (\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(this.getQueryOriginal());
		m.find();
		setEntityName(m.group(1));

	}

	public String getQueryOriginal() {
		return queryOriginal;
	}

	public void setQueryValid(boolean v) {
		queryValid = v;
	}

	public boolean isQueryValid() {
		return queryValid;
	}

	public void setExportOperation(ExportOperation op) {
		exportOperation = op;
		setDataLoaderOperation(op.toDataLoaderOperation());
	}

	public ExportOperation getExportOperation() {
		return exportOperation;
	}
}