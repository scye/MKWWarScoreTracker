package ScoreTrack;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.io.*;
import java.text.ParseException;

public class Window extends JFrame implements ItemListener, ActionListener
{
	private static final long serialVersionUID = 7291851636580461072L;
	
	private MKWST myMain;
	
	// Components
	private JLabel[] lbPlace = new JLabel[12];
	private JLabel[] lbRace = new JLabel[12];
	private JLabel[] lbRaceResult = new JLabel[12];
	private JLabel lbOveralResult;
	
	private JCheckBox[][] cbPositions = new JCheckBox[12][12];
	
	private JCheckBox cbAuto;
	private JButton btSend;
	private JTextField tfFormat;
	private JButton btDefault;
	private JButton btReset;
	private JCheckBox cbServer;
	private JFormattedTextField tfPort;
	private JButton btFolder;
	private JTextField tfPath;
	
	// Resources
	private Color colCheckbox[] = {
		new Color(75,175,75),
		new Color(75,175,175),
		new Color(175,75,75)
	};;
	private Color colLabels = new Color(255, 237, 57);
	
	private Font ftLabels = new Font("Calibri", Font.BOLD, 20);
	
	private ImageIcon[] imgPlace = new ImageIcon[12];
	private ImageIcon[] imgCheckbox = new ImageIcon[2];
	private ImageIcon[] imgAuto = new ImageIcon[2];
	private ImageIcon imgSend;
	private ImageIcon imgDefault;
	private ImageIcon imgReset;
	private ImageIcon imgFolder;
	private ImageIcon[] imgServer = new ImageIcon[2];
	
	public Window(MKWST mkwst, boolean auto, String format, String path, int port)
	{
		myMain = mkwst;
		
		createResources();
		initializeWindow();
		
		cbAuto.setSelected(auto);
		tfFormat.setText(format);
		tfPath.setText(path);
		tfPort.setText("" + port);
	}
	
	private void createResources()
	{
		// Create images
		for (int i = 0; i < 12; i++)
		{
			imgPlace[i] = new ImageIcon(getClass().getResource("/img/place" + (i + 1) + ".png"));
		}
		
		imgCheckbox[0] = new ImageIcon(getClass().getResource("/img/checkboxUnchecked.png"));
		imgCheckbox[1] = new ImageIcon(getClass().getResource("/img/checkboxChecked.png"));
		
		imgAuto[0] = new ImageIcon(getClass().getResource("/img/autoUnselected.png"));
		imgAuto[1] = new ImageIcon(getClass().getResource("/img/autoSelected.png"));
		
		imgSend = new ImageIcon(getClass().getResource("/img/send.png"));
		imgDefault = new ImageIcon(getClass().getResource("/img/default.png"));
		imgReset = new ImageIcon(getClass().getResource("/img/reset.png"));
		
		imgFolder = new ImageIcon(getClass().getResource("/img/folder.png"));
		imgServer[0] = new ImageIcon(getClass().getResource("/img/serverUnselected.png"));
		imgServer[1] = new ImageIcon(getClass().getResource("/img/serverSelected.png"));
	}
	
	private void initializeWindow()
	{
		// Set up frame
		setSize(580,532);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)screenSize.getWidth()/2 - 290, (int)screenSize.getHeight()/2 - 250);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent event) {
		        dispose();
		        myMain.closeApp();
		    }
		});
		
		setTitle("War Score Tracker");
		setIconImage(imgCheckbox[1].getImage());
		
		getContentPane().setBackground(new Color(40, 40, 40));
		GridBagLayout myLayout = new GridBagLayout();
		myLayout.columnWidths = new int[] {56, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 160};
		myLayout.rowHeights = new int[]   {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
		setLayout(myLayout);
		
		//Create position labels
		for (int ix = 0; ix < 12; ix++)
		{
			lbPlace[ix] = new JLabel(imgPlace[ix]);
			lbPlace[ix].setBorder(null);
			add(lbPlace[ix], new GridBagConstraints(ix + 1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
					
		for (int iy = 0; iy < 12; iy++)
		{			
			// Create label saying "R #"
			lbRace[iy] = new JLabel("R" + (iy + 1));
			lbRace[iy].setFont(ftLabels);
			lbRace[iy].setHorizontalAlignment(SwingConstants.RIGHT);
			lbRace[iy].setVerticalAlignment(SwingConstants.BOTTOM);
			lbRace[iy].setForeground(colLabels);
			lbRace[iy].setBorder(null);
			add(lbRace[iy], new GridBagConstraints(0, iy + 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 4), 0, 0));
			
			// Create 12 check boxes for spots
			for (int ix = 0; ix < 12; ix++)
			{
				cbPositions[iy][ix] = new JCheckBox(imgCheckbox[0]);
				cbPositions[iy][ix].setBorder(null);
				cbPositions[iy][ix].setBackground(colCheckbox[(int)Math.floor(ix/4)]);
				
				cbPositions[iy][ix].addItemListener(this);
				add(cbPositions[iy][ix], new GridBagConstraints(ix + 1, iy + 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			
			// Create label for displaying result
			lbRaceResult[iy] = new JLabel("0 - 0 (±0)");
			lbRaceResult[iy].setFont(ftLabels);
			lbRaceResult[iy].setVerticalAlignment(SwingConstants.BOTTOM);
			lbRaceResult[iy].setForeground(colLabels);
			add(lbRaceResult[iy], new GridBagConstraints(13, iy + 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 0), 0, 0));
		}
		
		// Create controls
		cbAuto = new JCheckBox(imgAuto[0]);
		cbAuto.setBorder(null);
		cbAuto.setBackground(null);
		cbAuto.setToolTipText("If enabled score updates always update the text file");
		
		cbAuto.addItemListener(this);
		add(cbAuto, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 24, 0, 0), 0, 0));
		
		btSend = new JButton(imgSend);
		btSend.setBorder(null);
		btSend.setToolTipText("Send current war score to text file.");
		btSend.addActionListener(this);
		add(btSend, new GridBagConstraints(1, 13, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		tfFormat = new JTextField();
		tfFormat.setBorder(null);
		tfFormat.setToolTipText("Text file format. This will be written into the text file on export. {1} will be replaced with your team's score, {2} will be replaced with the opponent team's score, {d} will be replaced with the difference (signs included) and {r} will be replaced with the number of races left to play.");
		add(tfFormat, new GridBagConstraints(3, 13, 8, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		btDefault = new JButton(imgDefault);
		btDefault.setBorder(null);
		btDefault.setToolTipText("Set text file format to default.");
		btDefault.addActionListener(this);
		add(btDefault, new GridBagConstraints(11, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		btReset = new JButton(imgReset);
		btReset.setBorder(null);
		btReset.setToolTipText("Reset scores");
		btReset.addActionListener(this);
		add(btReset, new GridBagConstraints(12, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		lbOveralResult = new JLabel("0 - 0 (±0)");
		lbOveralResult.setFont(ftLabels);
		lbOveralResult.setVerticalAlignment(SwingConstants.BOTTOM);
		lbOveralResult.setForeground(colLabels);
		lbOveralResult.setBorder(null);
		add(lbOveralResult, new GridBagConstraints(13, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 0), 0, 0));

		cbServer = new JCheckBox(imgServer[0]);
		cbServer.setBorder(null);
		cbServer.setBackground(null);
		cbServer.setToolTipText("Turns local file server on/off.");
		
		cbServer.addItemListener(this);
		add(cbServer, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 24, 0, 0), 0, 0));
		
		tfPort = new JFormattedTextField("8081");
		try {
			MaskFormatter mfPort = new MaskFormatter("#####");
			mfPort.install(tfPort);
		} catch (ParseException ex) {}
		tfPort.setBorder(null);
		tfPort.setToolTipText("Whole number from 0 to 65535. Local file server will be accessible under 'http://localhost:[this number]'.");
		add(tfPort, new GridBagConstraints(1, 14, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		tfPath = new JTextField();
		tfPath.setBorder(null);
		tfPath.setToolTipText("Location of 'warScore.txt' and 'score.html'.");
		tfPath.setEnabled(false);
		add(tfPath, new GridBagConstraints(3, 14, 8, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		btFolder = new JButton(imgFolder);
		btFolder.setBorder(null);
		btFolder.setToolTipText("Locate folder.");
		btFolder.addActionListener(this);
		add(btFolder, new GridBagConstraints(11, 14, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		
		// Enable Frame
		setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() == cbAuto)
		{
			// Change image of check box
			((JCheckBox) e.getSource()).setIcon(imgAuto[e.getStateChange() % 2]);
		}
		else if (e.getSource() == cbServer)
		{
			// Change image of check box
			((JCheckBox) e.getSource()).setIcon(imgServer[e.getStateChange() % 2]);
			
			// Toggle server
			if (cbServer.isSelected()) 
			{
				myMain.createServer(Integer.parseInt(tfPort.getText().replace(" ","")));
				// Lock port/folder setting
				tfPort.setEnabled(false);
				btFolder.setEnabled(false);
			}
			else
			{
				myMain.closeServer();
				// Unlock port/folder setting
				tfPort.setEnabled(true);
				btFolder.setEnabled(true);
			}
		}
		else
		{
			// Change image of check box
			((JCheckBox) e.getSource()).setIcon(imgCheckbox[e.getStateChange() % 2]);
			
			// Identify check box
			int currentRace = -1;
			
			for (int iy = 0; iy < 12 && currentRace == -1; iy++)
			{
				for (int ix = 0; ix < 12; ix++)
				{
					if (e.getSource() == cbPositions[iy][ix])
					{
						currentRace = iy;
						break;
					}
				}
			}
			
			// Get data from check boxes
			boolean checks[] = new boolean[12];
			for (int ix = 0; ix < 12; ix++)
			{
				checks[ix] = cbPositions[currentRace][ix].isSelected();
			}
			
			// Update race
			myMain.updateRace(currentRace, checks);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btSend)
		{
			myMain.sendResult(tfFormat.getText());
		}
		else if (e.getSource() == btDefault)
		{
			// Reset text in text field
			tfFormat.setText("A {1} - {2} B ({d})");
		}
		else if (e.getSource() == btReset)
		{
			// Reset all check boxes
			for (int iy = 0; iy < 12; iy++)
			{			
				for (int ix = 0; ix < 12; ix++)
				{
					cbPositions[iy][ix].setSelected(false);
				}
			}
		}
		else if (e.getSource() == btFolder)
		{
			// Open folder chooser prompt
			JFileChooser chooser = new JFileChooser();
			File currentPath = new File(tfPath.getText());
			chooser.setCurrentDirectory(currentPath);
			chooser.setDialogTitle("Select fileserver folder...");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			String newPath;
			
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{ 
				newPath = chooser.getSelectedFile().toString();
			}
			else
			{
			    newPath = currentPath.toString();
			}
			
			tfPath.setText(newPath);
			File testPath = new File(tfPath.getText());
			if (testPath.exists()) tfPath.setBackground(Color.white);
			else tfPath.setBackground(Color.red);
			
			// Update program setting
			myMain.setFolder(newPath);
		}
	}
	
	protected void setScore(int race, int home, int away)
	{
		String difSign = "";
		
		if (home - away == 0) difSign = "±";
		else if (home - away > 0) difSign = "+";
		
		String labelText = home + " - " + away + " (" + difSign + (home - away) + ")";
				
		// Update score label (race 13 means overall score)
		if (race < 12)
		{
			lbRaceResult[race].setText(labelText);
		}
		else
		{
			lbOveralResult.setText(labelText);
		}
		
		// Relay send text file command in auto mode
		if (cbAuto.isSelected() && race == 12) myMain.sendResult(tfFormat.getText());
	}
}
