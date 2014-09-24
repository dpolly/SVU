package SOTIReports;
import java.util.Arrays;

/**
 * Debugging tools for project
 * 
 * @author Hhdmp1</br>
 * @version Created 09-12-2014 Last Modified 09-13-2014
 * 		</br></br>
 * 			09-12-2014 DMP: Added Method:compareString</br>
 * 			09-13-2014 DMP: Added Method:print2DArray</br>
 */
public class Debug 
{
	//VARIABLES
	//CONSTRUCTORS
	/**
	 * Default constructor
	 */
	public Debug ()
	{
		
	}
	
	/**
	 * Constructor that includes two dimensional array used for debugging
	 */
	public Debug (String [][] TwoDArray)
	{
		
	}
	
	//METHODS
	
	/**
	 * A debugging tool that will compare two strings and print out if they match or not
	 * 
	 * @param stringA	the first string to be compared
	 * @param stringB	the second string to be compared
	 */
	public static void compareString (String stringA, String stringB)
	{
		if (stringA !=null && stringB != null)
		{
			if (stringA.equalsIgnoreCase(stringB)) System.out.println("Match");
			else System.out.println("No Match");
		}
	}
	
	/**
	 *  A debugging tool that will print out the contents of a 2DArray to the console.
	 *
	 * 	Constructor: Debug tempArray = new Debug (sortArray);
	 * 
	 * @param printArray	the two dimensional array to be printed to the console
	 */
	public static void print2DArray (String [][] printArray)
	{
		for (int i = 0; i < printArray.length; i++ )
		{
			System.out.println(Arrays.toString(printArray[i]));
		}
	}
	
	
}
