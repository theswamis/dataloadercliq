package com.salesforce.cliq.test;

import java.io.*;
import java.util.Properties;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert.*;
import org.junit.*;


import com.salesforce.cliq.cliconfig.*;
import com.salesforce.dataloader.config.Config;

public class TestExtract {
	
	static final String USERNAME = "username";
	static final String PASSWORD = "password";

	/* The properties file */
	Properties p;
	
	CliConfigExport configExport;
	
	
    @Before 
    public void setUp() { 
		p = new Properties();
		try {
			InputStream in = this.getClass().getResourceAsStream("/test.properties");
			p.load(in);
			
		    configExport = new CliConfigExport(
					CliConfigExport.ExportOperation.EXTRACT, "/tmp", "test_export");
			
			configExport.setConfigValue(Config.USERNAME,p.getProperty(USERNAME));
			configExport.setPassword(p.getProperty(PASSWORD));
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
    }
    
    @Test
    public void testLogin() {
		try  {
			configExport.doSalesforceLogin();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
    }
    
	@Test
	public void testValidQuery() {

		if (!configExport.isLoggedIn()) {
			testLogin();
		}
		
		//Test a valid login and query
		try {
			configExport.setQueryAndEntity("SELECT id FROM account WHERE createddate > LAST_YEAR");
			
			//Make sure the entity is parsed out correctly
			Assert.assertEquals(configExport.getConfigValue(Config.ENTITY).toLowerCase(),"account");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		
		try {
			configExport.setQueryAndEntity("SELECT id FROM accounts");
			Assert.fail("Invalid query not detected.");
		} catch (Exception e) {
			//The invalid query should throw an exception
		}
	}
	
	@Test
	public void testInvalidQuery() {
		
		if (!configExport.isLoggedIn()) {
			testLogin();
		}
		
		try {
			configExport.setQueryAndEntity("SELECT id FROM accounts");
			Assert.fail("Invalid query not detected.");
		} catch (Exception e) {
			//This is good - The invalid query should throw an exception and be invalid
			Assert.assertFalse(configExport.isQueryValid());
		}
	}
}