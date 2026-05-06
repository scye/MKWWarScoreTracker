# MKWWarScoreTracker

Lets you keep scores of a 6v6 war (MK8dx or MKWorld) by clicking on the positions your team got each race.
Also features a function to write the result into a text file which can be used to display the score on OBS for streaming or recording or lets you create a local file server for the text file to be fetched by a java script.
I also added an html file with a java script inside to animate the text.

<img width="888" height="542" alt="image" src="https://github.com/user-attachments/assets/3cf998ff-f4e7-443e-a6ed-02f33443f85d" />

# How to use
<b>1. <a href="https://github.com/scye/MKWWarScoreTracker/releases">Download the .jar file</a></b><br>
The program will create an additional file "settings.mkwst" where it stores it's settings so you might want it in it's own folder. 

<b>2a. Optional: Set up the file server and java script</b><br>
<b>2a.1. File server</b><br>
If you want to use the program in conjunction with the .html file to get animated text updates in OBS you need to start a local file server. And of course download the .html file if you want to use that one.
First, select the folder in which the .html file is located (the .txt file will be created in there too).
Secondly, set the port for the file server. It has to match the port set inside the java script. If you use the .html file provided in the release here the port is going to be <b><i>8083</i></b>.
Then, turn the file server on by pressing on the little power button.
<img width="396" height="36" alt="image" src="https://github.com/user-attachments/assets/5b077f1c-e941-4187-8680-dce0231a515a" /><br>
<b>2a.2. In OBS</b><br>
To now access the score updates in OBS you need to create a browser source. Despite the file being on your local system do not select "local File" here. Instead type in the URL <i>http://localhost:[port]/score.html</i>.
<img width="276" height="73" alt="image" src="https://github.com/user-attachments/assets/5edaeb91-8976-46fa-a547-9d5e09fba862" /><br>

<b>2b. Optional: Local text file</b><br>
The program creates a file called <i>warScore.txt</i> in the selected folder. If you don't need your text to be animated you don't need to create a file server and just create a text source in OBS which reads this file.

<b>3. Noting down scores</b><br>
To enter scores, simply check the spots that your team got for the respective race. Once there are 6 boxes checked the program will automatically update the race's and the overall result.
<img width="530" height="68" alt="image" src="https://github.com/user-attachments/assets/32078007-35ff-4f3b-8f4f-c55cf8a275d8" />

<b>4.1. Updating the stream source</b><br>
To update the <i>warScore.txt</i> file you just click on the "Send current war score to text file"-button.
<img width="68" height="35" alt="image" src="https://github.com/user-attachments/assets/616ad381-091d-4e27-b1a5-d50891c307f0" />
To do this automatically press the [A]-button.<br>
<b>4.2. Optional: Set the text format</b><br>
You can also change the text that's being sent to the file. This is useful to add team tags or whatever you like. Simply write the text that should be sent to the file in the text box below the check boxes and write <i>{1}</i> for your own team's score, <i>{2}</i> for the opponent's score, <i>{d}</i> for the difference and <i>{r}</i> for the races remaining.

The file server closes automatically when you close this program so you don't need to worry about closing it at the end of your session.

<i>I don't know about licencing stuff but I don't mind anyone using and/or changing this. If you like to credit this, just link this github page :3</i>
