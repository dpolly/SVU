package SOTIReports;

import java.util.Arrays;
/**
 * Takes the data from a two dimensional array and sorts it.
 * 
 * @author Hhdmp1</br>
 * @version Created 09-12-2014 Last Modified 09-22-2014
 * 			</br></br>
 * 			09-13-2014 DMP: updated sortInt method to skip rows with null in the compare column Method: SortInt.</br>
 * 			09-22-2014 DMP: updated sortInt method to throw exception for invalid number Method: SortInt
 */
public class DataSort2D 
{

	//VARIABLES
	private LogIt logs = new LogIt("SOTIReports.log", "Data Sort 2D Array");
	private static String[][] reportData;
	
	
	//CONSTRUCTORS
	public DataSort2D (String [][] reportData)
	{
		this.reportData=reportData;
		
	}
	
	//METHODS
	/**
	 * Takes a two dimensional string array and sorts the array by specified column.  The data in the specified column is assumed to be convertible to integers.
	 * 
	 * Sort Method: bubble sort (aka sinking sort)
	 * 
	 * @param sortArray		the two dimensional array with data in a string format to be sorted
	 * @param column		the column used to determine sort order  NOTE: Rows with "null" in the column are ignored and data must be convertible to integers.   
	 * @return
	 */
	public String[][] sortInt(String[][] sortArray, int column) {
		boolean swapped = false;
		do
		{
			swapped = false;
			for (int i = 0; i < sortArray.length; i++)
			{
				if (sortArray[i][column] != null && sortArray[i + 1][column] != null)
				{
					try
					{
							int current = Integer.parseInt(sortArray[i][column]);
							int next = Integer.parseInt(sortArray[i + 1][column]);
							if (current > next)
							{
								String [] hold = sortArray [i];
								sortArray[i] = sortArray[i + 1];
								sortArray[i +1] = hold;
								swapped = true;
							}
					}
					catch(NumberFormatException e)
					{
						// turn on for debugging only logs.logIt(e, false);
					}
				}
			}
		} while (swapped == true);
		return sortArray;	
	}
}
