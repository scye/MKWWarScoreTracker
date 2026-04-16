package ScoreTrack;

public class Main
{
	public static void main(String[] args)
	{
		ScoreCalc sc = new ScoreCalc();
		Window wn = new Window();
		sc.wn = wn;
		wn.sc = sc;
	}
}