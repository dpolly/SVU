package SOTIReports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

/**
 * Logging tool for Project
 * 
 * @author Hhdmp1</br>
 * @version Created 05-01-2014 Last Modified 05-07-2014
 * 			</br></br>
 * 			05-07-2014 DMP: Modified to include pop up message Method:logIt</br>
 */

public class LogIt 
{
	//VARIABLES
	private String logName; 
	private String sourceMessage;
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	
	//CONSTRUCTORS
	/**
	 * Constructor for LogIt class
	 * 
	 * @param 	logName 		the name of the log file to be updated.  
	 * @param 	sourceMessage	the name of the class logging the message
	 */
	public LogIt(String logName, String sourceMessage)
	{
		this.logName = logName;
		this.sourceMessage = sourceMessage;
	}

	//METHODS
	/**
	 * Creates a directory if it does not exist.
	 * 
	 * @param 	path	the directory to be created in form of string 
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
	 * Logs desired activity messages to a log in a default directory of C:\SUPERVALU\logs\SOTIReports.log". 
	 * If desired, logged message can be displayed in pop up message for end user.   
	 *  
	 * @param 	logMessage		the message to be logged in log and displayed on popup in the form of a string
	 * @param 	popUpMessage	the boolean response to using a popup message, true for yes pop up message or false for no pop up message
	 */
	public void logIt(String logMessage, boolean popUpMessage)
	{
		//Creates LogFile if one does not exist
		String fileName = "C:\\SUPERVALU\\Logs\\" + logName;
		if (!(new File(fileName).exists()))
		{
			createDir("C:\\SUPERVALU\\Logs\\");
			createFile(fileName);
		}
		
		//Logs error in logfile
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			Date currentDate = new Date();
			writer.write(dateFormat.format(currentDate) + " " + sourceMessage + " :    " + logMessage + "\n");
			writer.close();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null,  "Error!  There was an error logging to log file" + e);
		}
		
		//produces popup message for end user displaying message that was logged
		if (popUpMessage) JOptionPane.showMessageDialog(null,  logMessage);
	}
}
