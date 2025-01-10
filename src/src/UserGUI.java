package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.Timer;

public class UserGUI extends JFrame implements ActionListener {
	private Controller appController = new Controller();
	JPasswordField passwordField = new JPasswordField();
	JButton createButton = new JButton("Create password");
	JLabel label = new JLabel();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panelBottom = new JPanel();
	JButton helpButton = new JButton();
	private Timer timer;
	
	public UserGUI() {
		
		
		Container pane = this.getContentPane();
		this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		label.setText("Enter your simple password");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(300, 50));
		
		ImageIcon helpIcon = new ImageIcon(this.getClass().getResource("question-mark-line-icon.png"));
		helpButton.setIcon(helpIcon);
		helpButton.setFocusable(false);
		helpButton.setBorder(BorderFactory.createBevelBorder(0));
		helpButton.addActionListener(this);
		panelBottom.add(helpButton, BorderLayout.WEST);
	
		
		pane.add(label);
		
		panel1.setAlignmentX(CENTER_ALIGNMENT);
		panel1.setPreferredSize(new Dimension(300, 50));
		pane.add(panel1, Component.CENTER_ALIGNMENT);
		
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.setPreferredSize(new Dimension(250, 40));
		panel1.add(passwordField);
		
		createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createButton.setPreferredSize(new Dimension(150, 50));
		createButton.addActionListener(this);
		createButton.setBorder(BorderFactory.createSoftBevelBorder(0));
		panelBottom.add(createButton);
		
		
		
		panelBottom.setPreferredSize(new Dimension(380,50));
		pane.add(panelBottom);
		
		panel2.setBorder(BorderFactory.createEmptyBorder());
		pane.add(panel2);
		
		
		
		//Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("padlock-icon.png"));
		this.setIconImage(icon.getImage());
		this.setTitle("Password creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 200);
		this.setResizable(false);
			
		/*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
		
		int x = (int) rect.getMaxX() - this.getWidth();
		int y = (int) rect.getMaxY() - this.getHeight() - 25;
		
		this.setLocation(new Point(x, y));*/
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton) {
			String simplePassword = String.valueOf(passwordField.getPassword());
			boolean codeOk = appController.codePassword(simplePassword);
			if(!codeOk) {
				label.setText("The password entered is empty");
				label.setForeground(Color.red);
				timer = new Timer(2000, x -> {
					label.setText("Enter your simple password");
					label.setForeground(Color.black);
				});
				timer.setRepeats(false);
				timer.start();
				
				
			}else {
				label.setText("The generated password has been copied to your clipboard");
				label.setForeground(new Color(3, 105, 27));
				timer = new Timer(3000, x -> {
					label.setText("Enter your simple password");
					label.setForeground(Color.black);
				});
				timer.setRepeats(false);
				timer.start();
			}
			
		}
		
		if(e.getSource() == helpButton) {
			JOptionPane.showMessageDialog(this, "¿What is this application for?\n"
					+ "The purpose of this application is to have easy access to a more complicated password without the need to store it anywhere.\n"
					+ "¿Have you ever wanted to have more secure passwords but you can't remember them and don't want the saved somwhere other than your mind? With this app you can access to them from an easy to remember password!"
					+ "\n\n¿How do i use it?\nTo get a safer password you will need to enter a simple password/key of your choice (only lowercase and uppercase letters and numbers) which will be converted into a more safe password to use wherever you want."
					+ "\nIF YOU ENTER SPACES OR SPECIAL CHARACTERS THE APP WILL IGNORE THEM AND GENERATE A PASSWORD WITH THE CHARACTERS SUPPORTED (LOWERCASE AND UPPERCASE LETTERS, NUMBERS)"
					+ "\n\n¿How it works?\nEvery time you enter a simple password the app will convert all the characters in it into a more safe password thanks to conversion keys stored in your Documents Folder."
					+ "\n\nI don't like the password the app gave me ¿Can i change it?\nYes! To do so you need to go to your Documents directory, look for a folder named \"Password Creator Keys\" an deleting the two txt files inside it, then when you reopen the app it will generate new conversion keys,"
					+ "\nNOTE THAT IF YOU ARE ALREADY USING ANOTHER GENERATED PASSWORD WITH THIS SET OF KEYS YOU WILL NEED TO EITHER:"
					+ "\nSAVE THE COMPLICATED PASSWORD SOMEWHERE OR CHANGE TO A PASSWORD GENERATED WITH THE NEW KEYS IN WHATEVER ACCOUNTS YOU USED IT BEFORE."
					+ "\n\nIm changing PC ¿How do i gets access the same passwords i got in my previous pc?"
					+ "\nYou can copy the conversion keys from the old pc into the new one.\nGo into your Documents directory, then look for a folder named \"Password Creator Keys\" and then copy and replace the txt files into the new pc in the same location,\n"
					+ "NOTE THAT TO HAVE THE FOLDER GENERATED IN YOUR NEW PC YOU WILL HAVE TO OPEN THE APP IN THE NEW PC ATLEAST ONCE.","Common Questions", JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	
}
