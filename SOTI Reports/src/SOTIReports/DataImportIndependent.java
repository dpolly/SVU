package SOTIReports;

import java.io.*;
/**
 * Extracts data from Independent SOTI Report and records the following information on each device
 * Current Store Status, Region, Store Number, TimeZone, Device count, Last Connection and eOrderVersion
 * 
 * @author Hhdmp1</br>
 * @Version Created 06-20-2014 Last Modified 09-22-2014
 * 			</br></br>
 * 			06-20-2014 DMP: updated Regions to reflect East/West Method: srRegionAssign</br>
 * 			09-22-2014 DMP: added call to DataSort2D class Method: importIndependentData</br>
 */
public class DataImportIndependent extends ReportProcessing
{
	//VARIABLES
	private LogIt logs = new LogIt("SOTIReports.log", "Data Import Independent");
	private static String[][] reportData;
	private String sourceDirectory;
	private static String outputDirectory ="C:\\SUPERVALU\\SOTIReports\\Output\\"; 
	private String timeZone, region, storeNumber, deviceCount;
	private int rowCount=0;
	
	//CONSTRUCTORS
	/**
	 * Constructor that is used to by the "Silent Mode"
	 */
	public DataImportIndependent()
	{
		super(outputDirectory, reportData);
		sourceDirectory = "C:\\SUPERVALU\\SOTIReports\\Source\\";
	}

	/**
	 * Constructor that is used by the "User Interface Mode"
	 * 
	 * @param souceDirectory 	the source directory for import file in form of a string
	 */
	public DataImportIndependent(String sourceDirectory) //Runs reports from GUI
	{
		super(outputDirectory, reportData);
		this.sourceDirectory = sourceDirectory;
	}

	//METHODS
	
	/**
	 * Reads the source file and identifies the information contained in each line, then calls the appropriate method
	 * to extract the data from the line and add to the reportData. Method will validate report being read is the correct report.
	 * 
	 * @param 	fileName	the source file that data will be extracted from in the form of a string
	 * @return				the extracted data in the form of a 2d array
	 */
	public String [][] importIndependentData (String fileName)
	{
		String line = null; 
		int totalLineCount = lineCount(fileName);
		String [][] temp2DArray = new String [totalLineCount + 1][12];
		reportData=temp2DArray;
			
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			line=reader.readLine(); 
			String [] tempArray = line.split(",");
			
			if (tempArray[8].indexOf("Retail Independents") >= 0 && line.indexOf("Custom Data Report") >= 0) //validates correct report
			{
				reportData[0][0] = "Independent"; //used later to validate reportData is for the correct customer type
				while ((line = reader.readLine()) != null ) //each of the lines are evaluated to determine what type of data they contain then appropriate method is called to process. 
				{
					if (line.indexOf("Independent") >= 0) srExtractTimeZoneInd(line);
					else if (line.indexOf("Last Connected on") >= 0) srExtractDeviceInd(line);
					else if (line.indexOf("C & K") >= 0) srExtractTimeZoneInd(line);
					else if (line.indexOf("Cub Franchise  (" ) >= 0) timeZone="Virtual"; //these are data from virtual folders, this data is pulled from another report 
					else if (line.indexOf("CUB Franchise MC9090") >= 0) timeZone="Virtual";//the virtual designation is used to skip this data during extraction
					else if (line.indexOf("Niemanns  (") >= 0) timeZone="Virtual";
					else if (line.indexOf("_Niemanns MC9090") >= 0) timeZone="Virtual";
					else srExtractFolderInd(line);
				}
				reportData[0][1] = "0"; //updating the status so this can be returned to calling method
			}
			else
			{
				logs.logIt("Error!  Sorry the source report selected incorrect format!", true);
				reportData[0][1] = "1";
			}
			reader.close();
			DataSort2D reportDataSort = new DataSort2D (reportData);  
			reportData = reportDataSort.sortInt(reportData,2);

		}
		catch (FileNotFoundException e)
		{
			reportData[0][1] = "201";
			logs.logIt("Critical Error!   Status Code=201", false);
			logs.logIt("Critical Error!   File Not Found=" + fileName, true);
		}
		catch (IOException e) 
		{
			reportData[0][1] = "200";
			logs.logIt("Critical Error!   Status Code=200", true);
			logs.logIt("Critical Error!   " + e, false);
		}
	
	//	reports=reportData;
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
	 * Extracts data from lines designated as "device" these lines contain contain specific details about a device.
	 * In addition logs the general data about each device that was pulled from other lines.   
	 * DataReport fields logged: [0] store status [1] region [2] store number [3] time zone [4] device count
	 * DataReport fields extracted and logged: [5] last connect date [6] eOrder Version
	 * 
	 * @param 	line	the data that will be evaluated in the form of a string 	
	 */
	private void srExtractDeviceInd (String line)
	{
		//First Step: remove " and split line into Temp Array 
		String [] tempArray;
		line=line.replace("\"", "");
		tempArray = line.split(",");
		
		if (timeZone.compareTo("Virtual") != 0) 
		{ /*NOTE:
		   *The "Independent" report contains "Virtual" folders however
		   * its incomplete so its skipped here and pulled from another report. 
		   */

     			//Second Step: Pulling needed information
				rowCount++; //adds data to next line in array
				reportData[rowCount][0] = "Active"; //only active stores will have devices listed.
				reportData[rowCount][1] = region; //pulled from folder line
				reportData[rowCount][2] = storeNumber; //pulled from folder line
				reportData[rowCount][3] = timeZone; //pulled from timeZone line
				reportData[rowCount][4] = deviceCount; //pulled from folder line
				/*NOTE: 
				 * This report requires several pieces of data to be pulled from previous lines
				 * which is temporarily stored as variable then associated with the appropriate device here.
				 */
		
				/*Last Connect Date added to Report Data array*/
				String tempStringa = tempArray [7].replace("Last Connected on", "");
				String tempStringb = tempArray [8].trim();
				tempStringb=tempStringb.substring(0, 4);//extract year
				reportData[rowCount][5] = tempStringa + " " + tempStringb; //concat parts of date and add to reportData
				
				/* eOrder Version added to Report Data
				 * If data is missing from report skips
				 */
				if (tempArray.length >= 10) reportData[rowCount][6] = tempArray[9].replace("eOrderConfig: version=", ""); //trim to version and add to reportData
			}
	}
	
	/**
	 * Extracts store number data from lines designated as "folders", then uses store number to assign a region by calling the srRegionAssign method.
	 * Then logs detail on "inactive" stores, as there will not be any additional data to add.  
	 *  
	 * 	@param 	line	the data that will be evaluated in the form of a string
	 */
	private void srExtractFolderInd (String line)
	{
		//Splitting line
		String [] tempArray;
		line=line.replace("\"", "");
		line=line.replace("(", ",");
		line=line.replace(")", "");
		tempArray = line.split(",");

		//pulling data from split
		storeNumber=tempArray[9].trim();
		deviceCount=tempArray[10];
		region=srRegionAssign(storeNumber);
		tempArray=deviceCount.split(" ");
		deviceCount=tempArray[0];

		//Adding inActive Folders to Array
		if (Integer.parseInt(deviceCount) == 0)
		{
			rowCount++;
			reportData[rowCount][0] = "Inactive";
			reportData[rowCount][1] = region;
			reportData[rowCount][2] = storeNumber;
			reportData[rowCount][3] = timeZone;
			reportData[rowCount][4] = "0";
		}
	}
	
	/**
	 * Extracts time zone information from lines designated as "TimeZone" and stores for future use.
	 * 
	 * @param 	line	the data that will be evaluated in the form of a string 
	 */
	private void srExtractTimeZoneInd (String line)
	{
		String [] tempArray;
		tempArray = line.split(",");
		line = tempArray[8].replace("(", ",");
		tempArray = line.split(",");
		timeZone=tempArray[0].replace("\"", "").trim();
	}
	
	/**
	 * Evaluates a given store number and returns the associated region with that store number
	 * 
	 * @param 	storeNumber		the store number to be evaluated in the form of a string
	 * @return					the region the store is assigned to in the form of a string
	 */
	private String srRegionAssign(String storeNumber)
		{
			int distCenter = Integer.parseInt(storeNumber.substring(0,2));
			switch (distCenter)
			{
				case 9:
				case 11:
				case 12:
				case 14:
				case 15:
				case 18:
				case 23:
				case 61:
				case 63:
				case 73:
				case 75:
				case 76:
				case 84: return "West";
				case 21:
				case 44:
				case 45:
				case 49:
				case 62:
				case 71:
				case 80:
				case 86: return "East";
				default: return "Unknown";
			}	
		}
}

