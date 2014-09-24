package SOTIReports;
 
import java.io.*;

/**
 * This is the launching program for the SOTI Reports application, currently supporting reports for Independent and Virtual customers.
 * If you want to run in Silent mode use -a as a command line argument.  Silent mode assumes source files are 
 * in the default directories of C:\SUPERVALU\SOTIReports\Source and using standard naming conversions of independenteorder.csv
 * and virtualeorder.csv and generates all reports.
 * 
 * If you want to run with GUI mode use either -g at command line or no command line, the GUI mode will let you select location of 
 * source files and which reports you want to run.  
 *  * 
 * @author hhdmp1 </br>
 * @version Created 05-01-2014 Last Modified 07-17-2014
 * 			</br></br>
 * 			05-07-2014 DMP: Final version for class completed</br>
 * 			07-17-2014 DMP: added Retail option, but did not add actions Method: Main</br>
 */

public class SOTI_Reports
{
	//VARIABLES
	private static LogIt logs = new LogIt("SOTIReports.log", "SOTI_Reports"); 
	private static String sourceDirectory ="C:\\SUPERVALU\\SOTIReports\\Source\\";
	private static String [][] reportData;
	
	//MAIN
	public static void main (String [] args)
	{
		//opens up a log using LogIt class
		
		logs.logIt("**********     SOTI REPORTS LAUNCHED     **********", false);
		
		if (args.length > 0)
		{
			args[0]=args[0].toLowerCase();
		
			//AutoRun All Reports if default source data exist.
			if (args[0].indexOf("a") >=0) 
			{
				if (getSourceStatus("independent")) 
				{
					DataImportIndependent report = new DataImportIndependent();
					logs.logIt("Started! Running of reports for Independents", false);
					reportData=report.importIndependentData(sourceDirectory + "independenteorder.csv");
					if(reportData[0][1] == "0") logs.logIt("Imported data from:" + sourceDirectory + "independenteorder.csv", false);
					if(report.runAllReports(reportData) == 0) logs.logIt("Completed! Running of reports for Independents", false); 
	
				}
				if (getSourceStatus("virtual"))
				{
					DataImportVirtual report = new DataImportVirtual();
					logs.logIt("Started! Running of reports for Virtual", false);
					reportData=report.importVirtualData(sourceDirectory + "virtualeorder.csv");
					if(reportData[0][1] == "0") logs.logIt("Imported data from:" + sourceDirectory + "virtualeorder.csv", false);
					if(report.runAllReports(reportData) == 0) logs.logIt("Completed! Running of reports for Virtual", false); 
	
				}
				if (getSourceStatus("retail"))
				{
					
				}
			}
			
			//Launch GUI if g command or no command is generated.
			else if (args[0].indexOf("g") >=0)
			{
				ReportGUI guisession = new ReportGUI();
			}
			else logs.logIt("Invalid command line argument given", true);
		}
		else //if no command line runs GUI
		{
			ReportGUI guisession = new ReportGUI();
		}
	}
	
	//METHODS
	/**
	 * Validates that the source files exist for the auto generation of reports, if they do not exist will not attempt to run reports.
	 * Reports should be in default directory and follow standard naming conventions of C:\SUPERVALU\SOTIReports\custTypeeorder.csv
	 * 
	 * @param 	custType	the customer type of Independent or Virtual
	 * @return				the boolean response of true if exist and false if does not exist
	 */
	private static boolean getSourceStatus (String custType)
	{
		boolean status = false;
		if (new File(sourceDirectory + custType + "eorder.csv").exists()) status=true;
		logs.logIt("Error!    No source data found for " + custType, false);
		return status;
	}
	
}			
		
