package ScoreTrack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MKWST
{
	Window myWn;
	FileServer myFs;

	// Score data
	private int[][] scores = new int[13][2];
	private final int placeScore[] = {15, 12, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	
	// Settings
	private final String settingsPath = "settings.mkwst";
	private final String settingOutput = "warScore.txt";
	
	private boolean settingAuto;
	private String settingFormat;
	private String settingFolder;
	private int settingPort;
	
	public static void main(String[] args)
	{
		new MKWST();
	}
	
	public MKWST()
	{
		loadSettings();
		
		// Create window
		System.out.println("Creating window");
		myWn = new Window(this, settingAuto, settingFormat, settingFolder, settingPort);
		
		// Initialize scores
		System.out.println("Initializing spots");
		for (int i = 0; i < 12; i++)
		{
			for (int t = 0; t < 2; t++)
			{
				scores[i][t] = 0;
			}
		}
	}
	
	private void loadSettings()
	{
		System.out.println("Loading settings");
		
		// Check if settings file exists
		File settingsFile = new File(settingsPath);
		
		// Create settings file with default settings if it doesn't exist
		if (!settingsFile.exists())
		{
			System.out.println("No settings file found. Creating new one");
			try
			{
				PrintWriter pw = new PrintWriter(settingsPath);
				
				pw.println("0");
				pw.println("A {1} - {2} B ({d})");
				pw.println(new File(".").getCanonicalPath());
				pw.println("8081");
				
				pw.close();
			} catch (IOException ex) {ex.printStackTrace();}
		}
		
		settingAuto = false;
		settingFormat = "A {1} - {2} B ({d})";
		settingFolder = "/";
		settingPort = 8081;
		
		try
		{
			System.out.println("Loading settings");
			BufferedReader reader = new BufferedReader(new FileReader(settingsPath));
			
			settingAuto = reader.readLine().equals("1");
			settingFormat = reader.readLine();
			settingFolder = reader.readLine();
			settingPort = Integer.parseInt(reader.readLine());
			
			reader.close();
			
		} catch (IOException ex) {ex.printStackTrace();}
	}
	
	private void saveSettings()
	{
		try
		{
			System.out.println("Saving settings");
			PrintWriter pw = new PrintWriter(settingsPath);
			
			if (settingAuto) pw.println("1");
			else pw.println("1");
			
			pw.println(settingFormat);
			pw.println(settingFolder);
			pw.println("" + settingPort);
			
			pw.close();
		} catch (IOException ex) {ex.printStackTrace();}
	}
	
	protected void setFolder (String path)
	{
		System.out.println("Update folder setting");
		settingFolder = path;
	}
	
	protected void createServer(int port)
	{
		// Update setting
		settingPort = port;
		
		myFs = new FileServer(settingFolder, settingPort);
	}
	
	protected void closeServer()
	{
		if (myFs != null)
		{
			myFs.closeServer();
			myFs = null;
		}
	}
	
	protected void updateRace(int race, boolean checks[])
	{
		System.out.println("Updating race " + race);
		// Reset race's score
		scores[race][0] = 0;
		scores[race][1] = 0;
		
		// Check if given score is valid (6 checks)
		int inputs = 0;
		for (int ix = 0; ix < 12; ix++) if (checks[ix]) inputs++;
					
		if (inputs == 6)
		{
			for (int ix = 0; ix < 12; ix++)
			{
				if (checks[ix]) scores[race][0] += placeScore[ix];	// Add score to home team if flagged
				else scores[race][1] += placeScore[ix];				// Add to opponent otherwise
			}
		}
		
		System.out.println("Score: " + scores[race][0] + " : " + scores[race][1]);
		
		// Update window label
		myWn.setScore(race, scores[race][0], scores[race][1]);
		
		// Update total scores
		updateOverall();
	}
	
	private void updateOverall()
	{
		int overall[] = {0, 0};
		
		// Calculate overall scores
		for (int it = 0; it < 2; it++)
		{
			for (int iy = 0; iy < 12; iy++)
			{
				// Add race score to total
				overall[it] += scores[iy][it];
			}
			
			// Store in global array
			scores[12][it] = overall[it];
		}	
		
		System.out.println("Overall score: " + overall[0] + " : " + overall[1]);
		
		// Update window label
		myWn.setScore(12, overall[0], overall[1]);
	}
	
	protected void sendResult(String textFormat)
	{
		// Update setting
		settingFormat = textFormat;
				
		// Replace keys
		textFormat = textFormat.replace("{1}", "" + scores[12][0]);
		textFormat = textFormat.replace("{2}", "" + scores[12][1]);
		String difSign = "";
		if (scores[12][0] - scores[12][1] == 0) difSign = "±";
		else if (scores[12][0] - scores[12][1] > 0) difSign = "+";
		
		textFormat = textFormat.replace("{d}", difSign + (scores[12][0] - scores[12][1]));
		
		int racesLeft = 0;
		for (int i = 0; i < 12; i++) if (scores[i][0] == 0) racesLeft++;
		
		textFormat = textFormat.replace("{r}", "" + racesLeft);
		
		try {
			PrintWriter pw = new PrintWriter(settingFolder + "/" + settingOutput);
			pw.println(textFormat);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void closeApp()
	{
		closeServer();
		saveSettings();
		System.out.println("Closing");
		System.exit(0);
	}
}