package SOTIReports;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

/**
 * Displays a GUI for the end user to select location of source data and which reports they wish to run for a given customer.
 * 
 * @author 	Hhdmp1</br>
 * @version Created 05-01-2014 Last Modified 7-17-2014
 * 			</br></br>
 * 			07-17-2014 DMP: added "Retail Stores" button and actions, added "Help" and "Select Output Directory" buttons</br>
 */

public class ReportGUI extends JFrame
{
	//VARIABLES
	private LogIt logs = new LogIt("SOTIReports.log", "Report GUI");//opens up logging tool for use
	private static String [][] reportData;
	private String [] parameters ={"false", "false", "false", "false", "false", "false", "false", "false","false","false"}; //used to track parameters selected by End User
	private JRadioButton rdbtnCustTypeIndependent;
	private final ButtonGroup buttonGroupCustType = new ButtonGroup();
	private File sourceDirectory;
	private JFileChooser source = new JFileChooser("C:\\SUPERVALU\\SOTIReports\\Source");

	//CONSTRUCTORS
	/**
	 * Constructor that activates the GUI, no calling parameters
	 */
	public ReportGUI() 
	{
		setupActions();
		setupCustomerType();
		setupReportOptionsArea();
		setupFrame();
	}
	
	//METHODS
	/**
	 * Executed when end user selects "Run Reports" validates source file has been selected and runs reports. 
	 */
	private void runReports()
	{
		if ( parameters[1] != "false" && parameters[0] !="false")
		{
			if (parameters[0].indexOf("Independent") == 0) 
			{
				DataImportIndependent report = new DataImportIndependent(parameters[1]);
				logs.logIt("Started! Running selected reports for " + parameters[0], false);
				reportData=report.importIndependentData(parameters[1]);
				if(reportData[0][1] == "0") logs.logIt("Imported data from:" + parameters[1], false);
				if(report.runSelectReports(reportData, parameters) == 0) logs.logIt("Completed! Run of selected reports for " + parameters[0], true);
			}
			if (parameters[0].indexOf("Virtual") == 0) 
			{
				DataImportVirtual report = new DataImportVirtual (parameters[1]);
				logs.logIt("Started! Running selected reports for " + parameters[0], false);
				reportData=report.importVirtualData(parameters[1]);
				if(reportData[0][1] == "0") logs.logIt("Imported data from:" + parameters[1], false);
				if(report.runSelectReports(reportData, parameters) == 0) logs.logIt("Completed! Run of selected reports for " + parameters[0], true);
			}
		}
		
		else 
		{
				logs.logIt("Error!    There was an issue with running requested reports, please validate selected options and try again!", true);
		}
	}

	/**
	 * Sets up the Actions section of the GUI Interface
	 */
 	private void setupActions()
	{
		JPanel jpActions = new JPanel();
		jpActions.setBackground(Color.WHITE);
		jpActions.setLayout(new GridLayout(4, 1, 0, 0));
		getContentPane().add(jpActions, BorderLayout.EAST);
		
		JButton btnRunReports = new JButton("Run Reports");
		btnRunReports.setBackground(UIManager.getColor("Button.background"));
		btnRunReports.setActionCommand("Run");
		btnRunReports.addActionListener(new ActionListenerButtons());
		
		JButton btnSelectFileSource = new JButton("Select File Source");
		btnSelectFileSource.setBackground(UIManager.getColor("Button.background"));
		jpActions.add(btnSelectFileSource);
		btnSelectFileSource.setActionCommand("File");
		btnSelectFileSource.addActionListener(new ActionListenerButtons());
		jpActions.add(btnRunReports);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBackground(UIManager.getColor("Button.background"));
		btnExit.addActionListener(new ActionListenerButtons());
		
		JButton btnSelectOutputDirectory = new JButton("Select Output Directory");
		jpActions.add(btnSelectOutputDirectory);
		jpActions.add(btnExit);
	}
	
	/**
	 * Sets up the Top or Title bar section of the GUI Interface
	 */
	private void setupCustomerType()
	{
		
		JButton btnHelp = new JButton("Help");
		getContentPane().add(btnHelp, BorderLayout.SOUTH);
		JPanel jpTitleBar_Main = new JPanel();
		jpTitleBar_Main.setBackground(new Color(255, 255, 255));
		jpTitleBar_Main.setSize(new Dimension(350, 50));
		jpTitleBar_Main.setMinimumSize(new Dimension(350, 50));
		getContentPane().add(jpTitleBar_Main, BorderLayout.NORTH);
		jpTitleBar_Main.setLayout(new GridLayout(0, 1, 0, 1));
		
		JPanel jpTitleBar = new JPanel();
		jpTitleBar.setBackground(new Color(220, 20, 60));
		jpTitleBar_Main.add(jpTitleBar);
		
		JLabel lblTitle = new JLabel("SOTI Reports");
		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setFont(new Font("Impact", Font.PLAIN, 20));
		jpTitleBar.add(lblTitle);
		
		JPanel jpCustType = new JPanel();
		jpCustType.setBackground(Color.WHITE);
		jpTitleBar_Main.add(jpCustType);
		jpCustType.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		rdbtnCustTypeIndependent = new JRadioButton("Independent Stores");
		buttonGroupCustType.add(rdbtnCustTypeIndependent);
		rdbtnCustTypeIndependent.setBackground(new Color(255, 255, 255));
		rdbtnCustTypeIndependent.addActionListener(new ActionListenerOptions());
		jpCustType.add(rdbtnCustTypeIndependent);
		
		JRadioButton rdbtnCustTypeVirtual = new JRadioButton("Virtual Stores");
		buttonGroupCustType.add(rdbtnCustTypeVirtual);
		rdbtnCustTypeVirtual.setBackground(new Color(255, 255, 255));
		rdbtnCustTypeVirtual.addActionListener(new ActionListenerOptions());
		jpCustType.add(rdbtnCustTypeVirtual);
		
		JRadioButton rdbtnCustTypeRetail = new JRadioButton("Retail Stores");
		buttonGroupCustType.add(rdbtnCustTypeRetail);
		rdbtnCustTypeVirtual.setBackground(new Color(255, 255, 255));
		rdbtnCustTypeVirtual.addActionListener(new ActionListenerOptions());
		jpCustType.add(rdbtnCustTypeRetail);
	}
 	
	/**
	 * Sets up the frame for the GUI Interface
	 */
	private void setupFrame ()
	{
		setLocationByPlatform(true);
		setLocation(new Point(100, 100));
		setSize(new Dimension(421, 407));
		setMinimumSize(new Dimension(300, 400));
		setBackground(new Color(230, 230, 250));
		getContentPane().setFont(new Font("Arial Narrow", Font.PLAIN, 16));
		getContentPane().setBackground(new Color(255, 255, 255));
		setVisible(true);
		
	}
	
	/**
	 * Sets up the ReportOptions section of the GUI Interface
	 */
	private void setupReportOptionsArea()
	{
		JPanel jpReportOptions = new JPanel();
		jpReportOptions.setBackground(Color.WHITE);
		getContentPane().add(jpReportOptions, BorderLayout.CENTER);
		jpReportOptions.setLayout(new CardLayout(0, 0));
		
		JPanel jpReportOptionsIndependent = new JPanel();
		jpReportOptionsIndependent.setBackground(Color.WHITE);
		jpReportOptions.add(jpReportOptionsIndependent, "name_232204135669685");
		jpReportOptionsIndependent.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel jpReportOptionsIndependentReportList = new JPanel();
		jpReportOptionsIndependentReportList.setBackground(Color.WHITE);
		jpReportOptionsIndependent.add(jpReportOptionsIndependentReportList);
		GridBagLayout gbl_jpReportOptionsIndependentReportList = new GridBagLayout();
		gbl_jpReportOptionsIndependentReportList.columnWidths = new int[]{227, 0};
		gbl_jpReportOptionsIndependentReportList.rowHeights = new int[]{27, 27, 27, 14, 0};
		gbl_jpReportOptionsIndependentReportList.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_jpReportOptionsIndependentReportList.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		jpReportOptionsIndependentReportList.setLayout(gbl_jpReportOptionsIndependentReportList);
		
		JLabel lblReportOptionsIndHeader1 = new JLabel("Report Selection");
		lblReportOptionsIndHeader1.setHorizontalAlignment(SwingConstants.CENTER);
		lblReportOptionsIndHeader1.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		GridBagConstraints gbc_lblReportOptionsIndHeader1 = new GridBagConstraints();
		gbc_lblReportOptionsIndHeader1.fill = GridBagConstraints.BOTH;
		gbc_lblReportOptionsIndHeader1.insets = new Insets(0, 0, 5, 0);
		gbc_lblReportOptionsIndHeader1.gridx = 0;
		gbc_lblReportOptionsIndHeader1.gridy = 0;
		jpReportOptionsIndependentReportList.add(lblReportOptionsIndHeader1, gbc_lblReportOptionsIndHeader1);
		
		JCheckBox chckbxDeviceListing = new JCheckBox("Device Listing");
		chckbxDeviceListing.setBackground(Color.WHITE);
		GridBagConstraints gbc_chckbxDeviceListing = new GridBagConstraints();
		gbc_chckbxDeviceListing.fill = GridBagConstraints.BOTH;
		gbc_chckbxDeviceListing.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDeviceListing.gridx = 0;
		gbc_chckbxDeviceListing.gridy = 1;
		chckbxDeviceListing.addActionListener(new ActionListenerOptions());
		jpReportOptionsIndependentReportList.add(chckbxDeviceListing, gbc_chckbxDeviceListing);
		
		JCheckBox chckbxStoreListing = new JCheckBox("Store Listing");
		chckbxStoreListing.setBackground(Color.WHITE);
		GridBagConstraints gbc_chckbxStoreListing = new GridBagConstraints();
		gbc_chckbxStoreListing.fill = GridBagConstraints.BOTH;
		gbc_chckbxStoreListing.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxStoreListing.gridx = 0;
		gbc_chckbxStoreListing.gridy = 2;
		chckbxStoreListing.addActionListener(new ActionListenerOptions());
		jpReportOptionsIndependentReportList.add(chckbxStoreListing, gbc_chckbxStoreListing);
		
		JLabel lblReportOptionsIndInst1 = new JLabel("Please select all reports you want generated.");
		lblReportOptionsIndInst1.setVerticalAlignment(SwingConstants.TOP);
		lblReportOptionsIndInst1.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblReportOptionsIndInst1 = new GridBagConstraints();
		gbc_lblReportOptionsIndInst1.gridx = 0;
		gbc_lblReportOptionsIndInst1.gridy = 3;
		jpReportOptionsIndependentReportList.add(lblReportOptionsIndInst1, gbc_lblReportOptionsIndInst1);
		
		JPanel jpRepotOptionsIndependentStoreStatus = new JPanel();
		jpRepotOptionsIndependentStoreStatus.setBackground(Color.WHITE);
		jpReportOptionsIndependent.add(jpRepotOptionsIndependentStoreStatus);
		jpRepotOptionsIndependentStoreStatus.setLayout(new GridLayout(4, 1, 0, 0));
		
		JLabel lblSelectStoreStatus = new JLabel("Select Store Status");
		lblSelectStoreStatus.setFont(new Font("Tahoma", Font.BOLD, 11));
		jpRepotOptionsIndependentStoreStatus.add(lblSelectStoreStatus);
		
		JCheckBox chckbxAllStores = new JCheckBox("All Stores");
		chckbxAllStores.setBackground(Color.WHITE);
		chckbxAllStores.addActionListener(new ActionListenerOptions());
		jpRepotOptionsIndependentStoreStatus.add(chckbxAllStores);
		
		JCheckBox chckbxInactiveStoresOnly = new JCheckBox("Inactive Stores Only");
		chckbxInactiveStoresOnly.setBackground(Color.WHITE);
		chckbxInactiveStoresOnly.addActionListener(new ActionListenerOptions());
		jpRepotOptionsIndependentStoreStatus.add(chckbxInactiveStoresOnly);
		
		JCheckBox chckbxActiveStoresOnly = new JCheckBox("Active Stores Only");
		chckbxActiveStoresOnly.setBackground(Color.WHITE);
		chckbxActiveStoresOnly.addActionListener(new ActionListenerOptions());
		jpRepotOptionsIndependentStoreStatus.add(chckbxActiveStoresOnly);
		
	}
	
	/**
	 * embedded class that listens for activity on the buttons and responds appropriately
	 * 
	 * @author Hhdmp1
	 *
	 */
	class ActionListenerButtons implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			if (e.getActionCommand().compareTo("Exit") == 0) //closes application
			{
				System.exit(0);
			}
			if (e.getActionCommand().compareTo("File") == 0) //selection of source files
			{
				//choose source file for selected customer type
				source.setFileSelectionMode(JFileChooser.FILES_ONLY);
				source.showOpenDialog(source);
				
				//add source file to parameters array for later retrieval
				sourceDirectory=source.getSelectedFile();
				parameters[1] = sourceDirectory.getAbsolutePath();
			}
			if (e.getActionCommand().compareTo("Run") == 0) //runs the reports
			{
				runReports();
			}
		}
	}
	
	/**
	 * embedded class that records the report parameters and adds them to a parameter database
	 * parameters [0] equals customer type [1] source directory 
	 * parameters [2] Device listing report [3] store listing report [4] reserved for future development
	 * parameters [5] all stores [6] active stores [7] inactive stores [8 thru 10] reserved for future development
	 * 
	 * @author Hhdmp1
	 *
	 */
	class ActionListenerOptions implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getActionCommand() == "Independent Stores" || e.getActionCommand() == "Virtual Stores" || e.getActionCommand() == "Retail Stores") parameters[0] =e.getActionCommand().replace(" Stores", "");
			if (e.getActionCommand() == "Device Listing") parameters[2]="true";
			if (e.getActionCommand() == "Store Listing") parameters[3]="true";
			if (e.getActionCommand() == "All Stores") parameters[5]="true";
			if (e.getActionCommand() == "Active Stores Only") parameters[6]="true";
			if (e.getActionCommand() == "Inactive Stores Only") parameters[7]="true";
		}
	}
	
}
