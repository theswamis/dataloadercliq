package com.salesforce.cliq.ui;

import java.awt.*;

import java.awt.event.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

import com.salesforce.cliq.cliconfig.CliConfig;

import net.miginfocom.swing.MigLayout;

public abstract class CliqWizardPanel extends JPanel implements ComponentListener {
	
	private static final String LOGO_URL = "http://code.google.com/p/dataloadercliq/logo?logo_id=1258144218";
	private static final String DEFAULT_TITLE = "Data Loader CLIq";
	
	private static final String WELCOME_MESSAGE = "Welcome to CLIq";
	
	private static String currentEndpoint;
	
    private JLabel iconLabel;
    private JSeparator separator;
    private JLabel textLabel;
    private JLabel versionLabel;
    private JPanel titlePanel; 
    
    private JPanel contentContainer;
    private JPanel contentPanel;
    
    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {
    	doDisplayEndpoint();
    }
    
    private void doDisplayEndpoint() {
    	if (currentEndpoint == null) {
    		versionLabel.setText(WELCOME_MESSAGE);
    	} else {
    		versionLabel.setText(currentEndpoint);
    	}
    }
    
    public CliqWizardPanel() {
    	this(DEFAULT_TITLE);
    }
    
    public void setEndpoint(String e) {
    	currentEndpoint = e;
    }

	public CliqWizardPanel(String title) {
        super();
        
        addComponentListener(this);
               
        titlePanel = new javax.swing.JPanel();
        textLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        iconLabel = new javax.swing.JLabel();
        separator = new javax.swing.JSeparator();

        setLayout(new java.awt.BorderLayout());

        titlePanel.setLayout(new java.awt.BorderLayout());
        titlePanel.setBackground(Color.white);
        
        try {
        	URL logoUrl = new URL(LOGO_URL);
        	Icon icon = new ImageIcon(logoUrl);        	
        	textLabel.setIcon(icon);
        } catch (Exception e) {
        	//do something later
        }
      
        textLabel.setBackground(Color.white);
        textLabel.setIconTextGap(100);
        textLabel.setFont(new Font("MS Sans Serif", Font.BOLD, 18));
        textLabel.setText(title);
        textLabel.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        textLabel.setOpaque(true);

        versionLabel.setBackground(Color.gray);
        versionLabel.setIconTextGap(100);
        versionLabel.setFont(new Font("MS Sans Serif", Font.PLAIN, 10));
        doDisplayEndpoint();
        versionLabel.setForeground(Color.white);
        versionLabel.setBorder(new EmptyBorder(new Insets(3, 3, 3, 3)));
        versionLabel.setOpaque(true);
        
        titlePanel.add(textLabel,BorderLayout.CENTER);
        titlePanel.add(versionLabel,BorderLayout.SOUTH);

        titlePanel.add(iconLabel, BorderLayout.EAST);
        //versionLabel.add(separator, BorderLayout.SOUTH);
        
    	contentContainer = new JPanel();
    	contentContainer.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10))); 
    	
        add(contentContainer, BorderLayout.WEST);       
        add(titlePanel, BorderLayout.NORTH);  
	}
	
    public void addContentPanel(JPanel newContentPanel) {
    	removeContentPanel();
    	
    	contentPanel = newContentPanel;	
    	contentContainer.add(contentPanel, BorderLayout.NORTH); 
    }
    
    public void removeContentPanel() {
    	if (contentPanel != null)
    		contentContainer.remove(contentPanel);
    }
    
    public JPanel createResultPanel(JTextArea message) {
    	JPanel resultPanel = new JPanel(); 
	    resultPanel.setLayout(new MigLayout("wrap 2,w 400"));
	    resultPanel.setBorder(BorderFactory.createTitledBorder(
	    		BorderFactory.createEtchedBorder(),
	    		"Result",        		
	    		TitledBorder.DEFAULT_JUSTIFICATION,
	    		TitledBorder.DEFAULT_POSITION,
	    		new Font("Courier", Font.BOLD,14)
	    		));
	        
	    JScrollPane messageScrollPane = new JScrollPane(message);
	    messageScrollPane.setPreferredSize(new Dimension(500, 120));
	    messageScrollPane.setVerticalScrollBarPolicy(
	    		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS & JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    resultPanel.add(messageScrollPane);
	    return resultPanel;
    }
    
    public JTextArea createResultMessage() {
        JTextArea resultMessage = new JTextArea();   
	    resultMessage.setFont(new Font("Arial", Font.ITALIC,12));
	    resultMessage.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));  
	    resultMessage.setLineWrap(true);
	    return resultMessage;
    }
    
}
