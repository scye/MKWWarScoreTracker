package ScoreTrack;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ScoreCalc
{
	public Window wn;
	
	private int[][] scores = new int[13][2];
	private final int raceScore[] = {15, 12, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
	
	public ScoreCalc()
	{
		// Initialize score
		for (int i = 0; i < 12; i++)
		{
			for (int t = 0; t < 2; t++)
			{
				scores[i][t] = 0;
			}
		}
	}
	
	protected void calculate(boolean[][] flag)
	{		
		// Reset war score
		scores[12][0] = 0;
		scores[12][1] = 0;
		
		for (int iy = 0; iy < 12; iy++)
		{
			// Reset race's score
			scores[iy][0] = 0;
			scores[iy][1] = 0;
			
			// Check if given score is valid (6 checks)
			int inputs = 0;
			for (int ix = 0; ix < 12; ix++) if (flag[iy][ix]) inputs++;
			
			if (inputs == 6)
			{
				for (int ix = 0; ix < 12; ix++)
				{
					if (flag[iy][ix]) scores[iy][0] += raceScore[ix]; // Add score to home team if flagged
					else scores[iy][1] += raceScore[ix]; // Add to opponent otherwise
				}
			}
			
			// Send to window
			wn.setScore(iy, scores[iy][0], scores[iy][1]);
			
			// Add to final result
			scores[12][0] += scores[iy][0];
			scores[12][1] += scores[iy][1];
		}
		
		// Send final result to window
		wn.setScore(12, scores[12][0], scores[12][1]);
	}
	
	protected void sendResult(String textFormat)
	{
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
			PrintWriter pw = new PrintWriter("warScore.txt");
			pw.println(textFormat);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}