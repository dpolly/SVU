package SOTIReports;

/**
 * Searches store list and returns highest value of particular field. 
 * 
 * 
 * @author Hhdmp1
 * @version Created 09-24-2014 Last Modified 11-06-2014 </br>
 * 
 * 			09-24-2014 DMP: Created Method storeSearch </br>
 * 			11-06-2014 DMP: Cleaned up extra checking that is no longer needed with SOTI 11 Reports Method:storeSearch</br> 
 *			02-18-2015 DMP: Added methods to compare SVMobile version and hardware by each number Method: compareVersions, storeSearchHardware, storeSearchVersion  	
 *			02-18-2015 DMP: Removed storeSearch as replaced by new method storeSearchVersions Method:storeSearch
 *			02-18-2014 DMP: Renamed Class from DataExtractSVMobileVersion to DataExtractStoreSearch
 */
public class DataExtractStoreSearch 
{
	//CLASS Variables
	
	private String [][] reportData;
	
	//Constructors
	/**
	 * Constructor used for searching for SVMobile version in Report DAta
	 * 	
	 * @param reportData	the report data to be searched for SVMobile version in the form of 2D Array
	 */
	public DataExtractStoreSearch(String reportData[][])
	{
		this.reportData=reportData;
	}

	//methods		
	/**
	 * Compares two versions of SVMobile and determines if one version is greater/newer than the other.
	 * If verA is greater then A True returned
	 * If verB is greater then B False returned
	 * If verA and verB same then False returned
	 * 
	 * @param 	verA	the version of SVMobile that is being evaluated in form of string
	 * @param 	verB	the version of SVMobile that is being compared in form of string
	 * @return			the results of the comparison in form of boolean response.    
	 */
	static Boolean compareVersions(String verA, String verB)
	{

		//parse verA and B
			String [] splitVerA = verA.replace(".", " ").split(" ");
			String [] splitVerB = verB.replace(".", " ").split(" ");
			boolean results = false;

			for (int i = 0; i <= splitVerA.length -1; i++)
			{
				if (Integer.parseInt(splitVerA[i]) > Integer.parseInt(splitVerB[i])) return true;
				else if (Integer.parseInt(splitVerA[i]) < Integer.parseInt(splitVerB[i])) return false;
			}
			return results;
	}
	
	/**
	 * Searches for all instances of a store number in report data listing, evaluates the SVMobile version and returns the highest value.  
	 * 
	 * @param storeNumber		the store number to search for in the form a of a string 
	 * @param storeNumColumn	the number of the column in the 2D array where the store number can be found, is the form of an int. 
	 * @param svMobileVerColumn	the number of the column in the 2d array where the SVMobile version can be found, is the form of an int.
	 * @return					the highest version of SVMobile for a given store in form of String  
	 */
	public String storeSearchVersion (String storeNumber, int storeNumColumn, int svMobileVerColumn )
	{
		String results="0";
		
		for (int i = 1; i < reportData.length; i++)
		{
			try
			{
				if (reportData[i][storeNumColumn] != null && reportData[i][storeNumColumn] !="000000")
				{
					if (storeNumber.compareTo(reportData[i][storeNumColumn]) == 0 && reportData[i][svMobileVerColumn] != null) 
		
					{
						String version = reportData[i][svMobileVerColumn];
						if (compareVersions(version,results)) results=version;
					}
				}
			}
			catch(NumberFormatException e)
			{
		
			}
			catch (NullPointerException e)
			{
				
			}
		}	
		return results;
	}
	
	/**
	 * Searches for all instances of a store number in report data listing, evaluates the hardware version and returns the highest value.  
	 * 
	 * @param storeNumber		the store number to search for in the form a of a string 
	 * @param storeNumColumn	the number of the column in the 2D array where the store number can be found, is the form of an int. 
	 * @param hardwareColumn	the number of the column in the 2d array where the hardware version can be found, is the form of an int.
	 * @return					the highest version of SVMobile for a given store in form of String  
	 */	
	public String storeSearchHardware (String storeNumber, int storeNumColumn, int hardwareColumn )
	{
		String results="0";
		for (int i = 1; i < reportData.length; i++)
		{
			try
			{
				if (reportData[i][storeNumColumn] != null && reportData[i][storeNumColumn] !="000000")
				{
					if (storeNumber.compareTo(reportData[i][storeNumColumn]) == 0 && reportData[i][hardwareColumn] != null) 
		
					{
						String version = reportData[i][hardwareColumn].replace("MC", "");
						version = version.replace("N0", "00");
						if (compareVersions(version,results)) results=reportData[i][hardwareColumn];
					}
				}
			}
			catch(NumberFormatException e)
			{
		
			}
			catch (NullPointerException e)
			{
				
			}
		}	
		return results;
	}
}

