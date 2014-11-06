package SOTIReports;

/**
 * Extracts the highest version of SVMobile for all listed devices with in a store and returns as a double to calling object.
 * 
 * 
 * @author Hhdmp1
 * @version Created 09-24-2014 Last Modified 11-06-2014 </br>
 * 
 * 			09-24-2014 DMP: Created Method storeSearch </br>
 * 			11-06-2014 DMP: Cleaned up extra checking that is no longer needed with SOTI 11 Reports Method:storeSearch</br> 
 */
public class DataExtractSVMobileVersion 
{
	//CLASS Variables
	
	private String [][] reportData;
	
	//Constructors
	/**
	 * Constructor used for searching for SVMobile version in Report DAta
	 * 	
	 * @param reportData	the report data to be searched for SVMobile version in the form of 2D Array
	 */
	public DataExtractSVMobileVersion(String reportData[][])
	{
		this.reportData=reportData;
	}
		
	//methods
	/**
	 * Searches for all instances of a store number in report data listing, evaluates the SVMobile version and returns the highest value.  
	 * 
	 * @param storeNumber		the store number to search for in the form a of a string 
	 * @param storeNumColumn	the number of the column in the 2D array where the store number can be found, is the form of an int. 
	 * @param svMobileVerColumn	the number of the column in the 2d array where the SVMobile version can be found, is the form of an int.
	 * @return					the highest version of SVMobile for a given store.  
	 */
	public Double storeSearch (String storeNumber, int storeNumColumn, int svMobileVerColumn )
	{
		double results=0;
		
		for (int i = 1; i < reportData.length; i++)
		{
			try
			{
				if (reportData[i][storeNumColumn] != null && reportData[i][storeNumColumn] !="000000")
				{
					if (storeNumber.compareTo(reportData[i][storeNumColumn]) == 0 && reportData[i][svMobileVerColumn] != null) 
		
					{
						Double version = Double.parseDouble((reportData[i][6]).substring(0,3));
						if (results < version) results=version;			
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

