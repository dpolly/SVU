package SOTIReports;

import java.io.*;
/**
 * Extracts data from Independent SOTI Report and records the following information on each device
 * Current Store Status, Region, Store Number, TimeZone, Device count, Last Connection and eOrderVersion
 * 
 * @author Hhdmp1</br>
 * @version Created 05-07-2014 Last Modified 09-19-2014 
 * 			</br></br>
 * 			09-12-2014 DMP: changed store number from "unknown" to 000000 if CID blank in Method:srExtractDeviceVirtual</br>
 * 			09-13-2014 DMP: added sort on store number to Method:importVirtualData </br>
 * 			09-18-2014 DMP: added device count Method:srCountDevice
 * 			09-19-2014 DMP: added call to srCountDevice Method:importVirtualData
 */
public class DataImportVirtual extends ReportProcessing
{
	//VARIABLE
	private LogIt logs = new LogIt("SOTIReports.log", "Data Import Independent");
	private static String[][] reportData;
	private String sourceDirectory;
	private static String outputDirectory ="C:\\SUPERVALU\\SOTIReports\\Output\\"; 
	private int rowCount=0;

	//CONSTRUCTORS
	/**
	 * Constructor that is used to by the "Silent Mode"
	 */
	public DataImportVirtual()//AutoRuns all reports from a default directory 
	{
		super(outputDirectory, reportData);
		sourceDirectory = "C:\\SUPERVALU\\SOTIReports\\Source\\";
	}

	/**
	 * Constructor that is by the "User Interface Mode
	 * 
	 * @param 	sourceDirectory		the location of the source file in the form of a string 
	 */
	public DataImportVirtual(String sourceDirectory) //Runs reports from GUI
	{
		super(outputDirectory, reportData);
		this.sourceDirectory=sourceDirectory;
	}
	
	//METHODS
	/**
	 * Reads the source file and identifies the information contained in each line, then calls the appropriate method
	 * to extract the data from the line and add to the reportData. Method will validate report being read is the correct report.
	 *  
	 * @param 	fileName	the source file that data will be extracted from in the form of a string
	 * @return				the extracted data in the form of a 2d array
	 */
	public String [][] importVirtualData (String fileName)
	{
		String line = null; 
		int totalLineCount = lineCount(fileName);
		String [][] temp2DArray = new String [totalLineCount + 1][12];
		reportData=temp2DArray;
		
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			line=reader.readLine(); 
			
			if (line.indexOf("Device Information  with Custom Data Report") >= 0) //verify correct report was selected and update header of ReportDatat 2D Table
			{
				reportData[0][0] = "Virtual";
				while ((line = reader.readLine()) != null )  
				{
					srExtractDeviceVirtual(line);
				}
				reportData[0][1] ="0";
			}
			else
			{
				logs.logIt("Error!  Sorry the source report selected incorrect format!", true);
				reportData[0][1] ="1";
			}
			reader.close();
			//sorts the data by store number
			DataSort2D reportDataSort = new DataSort2D (reportData);  
			reportData = reportDataSort.sortInt(reportData,2);
			reportData = srCountDevice(reportData);
			
		}
		catch (FileNotFoundException e)
		{
			reportData[0][1] ="201";
			logs.logIt("Critical Error!   Status Code=201", false);
			logs.logIt("Critical Error!   File Not Found=" + fileName, true);
		}
		catch (IOException e) 
		{
			reportData[0][1] ="200";
			logs.logIt("Critical Error!   Status Code=200", true);
			logs.logIt("Critical Error!   " + e, false);
		}
				
		return reportData;
	}
	
	/**
	 * Counts the number of lines in in a file
	 * 
	 * @param 	fileName	the file to be evaluated in the form of a string
	 * @return				the number of lines in the file in the form of an int
	 */
	private int lineCount (String fileName)
	{
		String line = null;
		int results=0;
	
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null )
			{
				results++;
			}
			reader.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Records the number of devices per store and updates column [4] in the report data table to reflect number of devices in store.   
	 * 
	 * @param reportData	the report data to be evaluated in the form of a 2d array
	 * @return				the report data updated with device count
	 */
	private String [][] srCountDevice (String [][] reportData)
	{

		int count=0;		//counts the number of devices for a given store and then is used to update the table
		int tempRow=0;		//used to update the count on the last row
		for (int i = 0; i < reportData.length ; i++)
		{
			if (reportData[i][2] != null && reportData [i][2] != "000000")
			{			
				int tempPrevious=Integer.parseInt(reportData[i -1][2]);
				int tempCurrent=Integer.parseInt(reportData[i][2]);
				if (tempPrevious == tempCurrent)
				{
					count++;
				}
				else
				{
					for(int j=count; j > 0; j--)
					{
						reportData[i-j][4]=Integer.toString(count);
					}
					count=1;
				}
				tempRow=i;
			}

		}
		reportData[tempRow][4]=Integer.toString(count);
		return reportData;
	}
	
	/**
	 * Extracts data from the lines in the report and adds the data to the reportData 2d array.     
	 * DataReport default fields for this customer type: [0] store status [1] region [3] time zone [4] device count
	 * DataReport fields extracted and logged: [2] store number [5] last connect date [6] eOrder Version
	 * 
	 * @param	line	the data that will be evaluated in the form of a string 	
	 */
	private void srExtractDeviceVirtual (String line)
	{
		//First Step: remove " and split line into Temp Array 
		String [] tempArray;
		line=line.replace("\"", "");
		tempArray = line.split(",");
//		
		//Second Step: Pull needed information about specific device
		rowCount++; //adds data to next line in reportData array
		reportData[rowCount][0] = "Active"; //only active stores will have devices listed.
		reportData[rowCount][1] = "West"; //Default is "West" for this report
		reportData[rowCount][3] = "Niemanns"; //Default is Niemanns for this report
		reportData[rowCount][4] = "n/a"; //default is 0

		/*Extract store number from CID ID Number, however if CID ID Number is blank will fill in 000000 */
		if (tempArray[41].indexOf("061") >= 0) reportData[rowCount][2]=tempArray[41].replace("061-", "61"); //remove dash and add to reportData
		else reportData[rowCount][2] = "000000"; //to deal with unexpected or unknown CID Numbers
		
		/*Extract Last Connection Date */
		if(tempArray[48] != "") 
		{
			tempArray[48] = tempArray[48].replace("Last Connected Time:", ""); //remove none date
			tempArray[48] = tempArray[48].substring(0,10);//extract date
			reportData[rowCount][5] = tempArray[48].trim();//trim out white space and add to reportData
		}
		
		/*Extract eOrder Version*/
		if (tempArray[42] != "") reportData[rowCount][6] = tempArray[42].replace("version=", "");//trim to version only and add to reportData
	}	
}

