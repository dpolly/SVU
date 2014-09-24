package SOTIReports;

import java.io.*;

import javax.swing.*;
/**
 * The report creation module of the SOTI-Reports application, is called by either the "Silent Mode" or "User Interface Mode" to
 * create the device and store reports for all customer types.   Options for reports are All, Active and/or Inactive devices and/or stores.
 * 
 * @author Hhdmp1</br>
 * @version	Created 05-07-2014 Last Modified 09-24-2014
 * 			</br></br>
 * 			09-13-2014 DMP: Changed store number validation in Method:reportStoreListing </br>
 * 			09-24-2014 DMP: Added call to DataExtractSVMobile Version Method: reportStoreListing </br>
 */
public class ReportProcessing 
{
	//VARIABLES
	private LogIt logs = new LogIt("SOTIReports.log", "Report Processing"); 
	private String outputDirectory;

	//CONSTRUCTORS
	/**
	 * Constructor for class, the report Data Layout is as follows
	 * [Row][0] store status [1] region [2] store number [3] time zone [4] device count [5] last connect date [6] eOrder Version
	 * 
	 * @param 	outputDirectory	the directory to output the reports to in the form of a string. 
	 * @param 	reportData		the report data in the form of a 2d array.  
	 */	
	public ReportProcessing(String outputDirectory, String [][] reportData)
	{
		this.outputDirectory = outputDirectory;
	}
	
	//METHODS
	/**
	 * Creates a directory if it does not exist.
	 * 
	 * @param path		the directory to be created in form of string 
	 */
 	private void createDir (String path)
	{
		do
		{
			File file = new File (path);
			file.mkdirs();
		}
		while(!(new File(path).exists()));
	}
	
 	/**
 	 * Creates a file if it does not exist.
 	 * 
 	 * @param 	fileName 	the name of file to be created in form of a string
 	 */
	private void createFile (String fileName)
	{
		try
		{
			do
			{
				File file = new File(fileName);
				file.createNewFile();
			}
			while(!(new File(fileName).exists()));
		}
		
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,  "Error! unable to create requested file" + e);
		}
	}

	/**
	 * Generates a csv file containing the Region StoreNumber TimeZone DeviceCount LastConnect and eOrderVersion for each device.
	 * 
	 * @param 	reportData		the data structure used to store device data in the form of 2d Array
	 * @param 	reportType		the type of report to generate, options Active Devices, Inactive Devices or all devices in the form of a string.
	 * @return					the status in the form of an int, with 0 being returned on successful report creation. 
	 */
	private int reportDeviceListing(String [][] reportData, String reportType)
	{
		int status = -1;
		
		//Validate file and directory exist, if not create
		String fileName = outputDirectory + reportData[0][0] + "-" + reportType + " devices.csv";
		if (!(new File(fileName).exists()))
		{
			createDir(outputDirectory);
			createFile(fileName);
		}

		//Write Report
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
			//header setup, checks of report data for easily adding customer types
			if (reportData[0][0].indexOf("Independent") >= 0 || reportData[0][0].indexOf("Virtual") >= 0) writer.write("Region,StoreNumber,TimeZone,DeviceCount,LastConnect,eOrderVersion\n");
	
			for (int i = 1; i < reportData.length; i++)
			{
				if (reportType == reportData[i][0] || reportType == "All")
				{
						if((reportData[i][1]) != null) writer.write(reportData[i][1] + ","); else writer.write(",");
						if((reportData[i][2]) != null) writer.write(reportData[i][2] + ","); else writer.write(",");
						if((reportData[i][3]) != null) writer.write(reportData[i][3] + ","); else writer.write(",");
						if((reportData[i][4]) != null) writer.write(reportData[i][4] + ","); else writer.write(",");
						if((reportData[i][5]) != null) writer.write(reportData[i][5] + ","); else writer.write(",");
 						if((reportData[i][6]) != null) writer.write(reportData[i][6]); else writer.write("");
						writer.write("\n");
				}
			}
			writer.close();
			logs.logIt("Successfully generated " + reportData[0][0] + "-" + reportType + " devices.csv", false);
			status=0;
		} 
		catch (FileNotFoundException e)
		{
			status=201;
			logs.logIt("Critical Error!   Status Code=" + status, false);
			logs.logIt("Critical Error!   File Not Found or Already in Use=" + fileName, true);
	
		}
		catch (IOException e) 
		{
			status=200;
			logs.logIt("Critical Error!   Status Code=" + status, true);
			logs.logIt("Critical Error!   " + e, false);
		}
		
		return status;
	}

	/**
	 * Generates a csv file containing the Region StoreNumber TimeZone DeviceCount and eOrderVersion for each store.
	 * 
	 * @param 	reportData		the data structure used to store device data in the form of 2d Array
	 * @param 	reportType		the type of report to generate, options Active Devices, Inactive Devices or all devices in the form of a string.
	 * @return					the status in the form of an int, with 0 being returned on successful report creation. 
	 */
	private int reportStoreListing (String [][] reportData, String reportType)
	{
		//reportType = active, inactive or all
		int status = -1;
		
		DataExtractSVMobileVersion versionTable = new DataExtractSVMobileVersion(reportData);
		
		//Validate file and directory exist, if not create
		String fileName = outputDirectory + reportData[0][0] + "-" + reportType + " stores.csv";
		if (!(new File(fileName).exists()))
		{
			createDir(outputDirectory);
			createFile(fileName);
		}

		//Write Report
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
			//header setup, checks of report data for easily adding customer types
			if (reportData[0][0].indexOf("Independent") >= 0 || reportData[0][0].indexOf("Virtual") >= 0) writer.write("Region,StoreNumber,TimeZone,DeviceCount,eOrderVersion\n");
			for (int i = 1; i < reportData.length; i++)
			{
				if (reportData[i][2] != null && reportData[i -1][2] != null && reportData[i][2].equalsIgnoreCase(reportData[i -1][2]) == false)
				{
					if (reportType == reportData[i][0] || reportType == "All")
					{

						if((reportData[i][1]) != null) writer.write(reportData[i][1] + ","); else writer.write(",");
						if((reportData[i][2]) != null) writer.write(reportData[i][2] + ","); else writer.write(",");
						if((reportData[i][3]) != null) writer.write(reportData[i][3] + ","); else writer.write(",");
						if((reportData[i][4]) != null) writer.write(reportData[i][4] + ","); else writer.write(",");
						//runs a search to determine highest version for store and reports that in the report, ignores null or bad version data.   
						if(Integer.parseInt(reportData[i][4]) >= 1) writer.write(Double.toString(versionTable.storeSearch(reportData[i][2], 2, 6))); else writer.write("");
						writer.write("\n");
					}
				}
			}
			writer.close();
			logs.logIt("Successfully generated" + reportData[0][0] + "-" + reportType + " stores.csv", false);
			status=0;
		} 
		catch (FileNotFoundException e)
		{
			status=201;
			logs.logIt("Critical Error!   Status Code=" + status, false);
			logs.logIt("Critical Error!   File Not Found or Already in Use=" + fileName, true);
	
		}
		catch (IOException e) 
		{
			status=200;
			logs.logIt("Critical Error!   Status Code=" + status, true);
			logs.logIt("Critical Error!   " + e, false);
		}
		
		return status;
	}
	
	/**
	 * Automatically generates all available reports for given customer type used in the "Silent Mode".
	 * 
	 * @param 	reportData		the data structure used to store device data in the form of 2d Array
	 * @return					the status in the form of an int, with 0 being returned on successful report creation.
	 */
	public int runAllReports(String [][] reportData)
	{
		int status = -1;
		
		String [] tempArray = {"All", "Active", "Inactive"};
			for (int i = 0; i < tempArray.length; i++) 
			{
				status=reportDeviceListing(reportData, tempArray[i]);
				if (status != 0) return status;
			}
			for (int i = 0; i < tempArray.length; i++)
			{
				status=reportStoreListing(reportData, tempArray[i]);
				if (status != 0) return status;
			}
	
		return status;
	
	}

	/**
	 * Runs through the selected reports in the "User Interface Mode" and generates the corresponding reports.
	 * 
	 * @param 	reportData		the data structure used to store device data in the form of 2d Array
	 * @param 	selectReports	the data structure used to store the parameters selected in the GUI in the form of array
	 * @return					the status in the form of an int, with 0 being returned on successful report creation.
	 */
	public int runSelectReports(String [][] reportData, String [] selectReports)
	{
		int status = -1;
		if(selectReports[2].indexOf("true") >= 0 && selectReports[5].indexOf("true") >= 0) reportDeviceListing(reportData, "All");
		if(selectReports[2].indexOf("true") >= 0 && selectReports[6].indexOf("true") >= 0) reportDeviceListing(reportData, "Active");
		if(selectReports[2].indexOf("true") >= 0 && selectReports[7].indexOf("true") >= 0) reportDeviceListing(reportData, "Inactive");
		if(selectReports[3].indexOf("true") >= 0 && selectReports[5].indexOf("true") >= 0) reportStoreListing(reportData, "All");
		if(selectReports[3].indexOf("true") >= 0 && selectReports[6].indexOf("true") >= 0) reportStoreListing(reportData, "Active");
		if(selectReports[3].indexOf("true") >= 0 && selectReports[7].indexOf("true") >= 0) reportStoreListing(reportData, "Inactive");
		status =0;
		return status;	
	}
}

	
	