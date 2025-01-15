package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.Timer;

public class UserGUI extends JFrame implements ActionListener {
	
	private Controller appController = new Controller();
	private JPasswordField passwordField = new JPasswordField();
	
	private JLabel label = new JLabel();
	private JLabel label2 = new JLabel();
	private JLabel passwordLabel = new JLabel();
	
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panelBottom = new JPanel();
	private JPanel safePasswordPanel = new JPanel();
	
	private JButton createButton = new JButton("Create password");
	private JButton helpButton = new JButton();
	private JButton resetKeysButton = new JButton();
	private JButton exportButton = new JButton();
	private JButton copyButton = new JButton();
	
	private JFileChooser fc = new JFileChooser();
	private Timer timer;
	
	public UserGUI() {
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		Container pane = this.getContentPane();
		this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
	
		
		label.setText("Enter your simple password");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(380, 50));
		
		ImageIcon helpIcon = new ImageIcon(this.getClass().getResource("question-mark-icon.png"));
		helpButton.setIcon(helpIcon);
		helpButton.setFocusable(false);
		helpButton.setBorder(BorderFactory.createBevelBorder(0));
		helpButton.addActionListener(this);
		helpButton.setToolTipText("Common Questions");
		panelBottom.add(helpButton, BorderLayout.WEST);
	
		ImageIcon resetIcon = new ImageIcon(this.getClass().getResource("undo-arrow-icon.png"));
		resetKeysButton.setIcon(resetIcon);
		resetKeysButton.setFocusable(false);
		resetKeysButton.setBorder(BorderFactory.createBevelBorder(0));
		resetKeysButton.addActionListener(this);
		resetKeysButton.setToolTipText("Reset encryption keys");
		panelBottom.add(resetKeysButton, BorderLayout.EAST);
		
		
		pane.add(label);
		
		safePasswordPanel.setPreferredSize(new Dimension(380,50));
		pane.add(safePasswordPanel);
		
		label2.setText("");
		label2.setPreferredSize(new Dimension(100, 50));
		safePasswordPanel.add(label2, BorderLayout.WEST);
		
		passwordLabel.setText("");
		passwordLabel.setPreferredSize(new Dimension(150, 50));
		safePasswordPanel.add(passwordLabel, BorderLayout.CENTER);
		
		
		copyButton.setText("Copy");
		copyButton.setPreferredSize(new Dimension(80, 37));
		copyButton.addActionListener(this);
		copyButton.setBorder(BorderFactory.createSoftBevelBorder(0));
		copyButton.setVisible(false);
		safePasswordPanel.add(copyButton, BorderLayout.EAST);
		
		panel1.setAlignmentX(CENTER_ALIGNMENT);
		panel1.setPreferredSize(new Dimension(300, 50));
		pane.add(panel1, Component.CENTER_ALIGNMENT);
		
		passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordField.setPreferredSize(new Dimension(250, 40));
		panel1.add(passwordField);
		
		createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		createButton.setPreferredSize(new Dimension(150, 37));
		createButton.addActionListener(this);
		createButton.setBorder(BorderFactory.createSoftBevelBorder(0));
		panelBottom.add(createButton, BorderLayout.CENTER);
		
		
		ImageIcon exportIcon = new ImageIcon(this.getClass().getResource("document-export-icon.png"));
		exportButton.setIcon(exportIcon);
		exportButton.setFocusable(false);
		exportButton.setBorder(BorderFactory.createBevelBorder(0));
		exportButton.addActionListener(this);
		exportButton.setToolTipText("Export encryption keys");
		panelBottom.add(exportButton);
		
		
		panelBottom.setPreferredSize(new Dimension(380,50));
		pane.add(panelBottom);
		
		panel2.setBorder(BorderFactory.createEmptyBorder());
		pane.add(panel2);
		
		
		
		ImageIcon icon = new ImageIcon(this.getClass().getResource("padlock-icon.png"));
		this.setIconImage(icon.getImage());
		this.setTitle("Password creator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 250);
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
			String code = appController.codePassword(simplePassword);
			if(code.equals("")) {
				label.setText("The password entered is empty");
				label.setForeground(Color.red);
				timer = new Timer(2000, x -> {
					label.setText("Enter your simple password");
					label.setForeground(Color.black);
				});
				timer.setRepeats(false);
				timer.start();
				
				
			}else {
				label2.setText("New Password:");
				passwordLabel.setText(code);
				label2.setForeground(new Color(3, 105, 27));
				passwordLabel.setForeground(new Color(3, 105, 27));
				copyButton.setVisible(true);
			}
			
		}
		
		if(e.getSource() == copyButton) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(passwordLabel.getText()), null);
		}
		
		
		if(e.getSource() == helpButton) {
			JOptionPane.showMessageDialog(this, "¿What is this application for?\n"
					+ "The purpose of this application is to have easy access to a more complicated password without the need to store it anywhere.\n"
					+ "¿Have you ever wanted to have more secure passwords but you can't remember them and don't want the saved somewhere other than your mind? With this app you can access to them from an easy to remember password!"
					+ "\n\n¿How do i use it?\nTo get a safer password you will need to enter a simple password/key of your choice (only lowercase and uppercase letters and numbers) which will be converted into a more safe password to use wherever you want."
					+ "\nIF YOU ENTER SPACES OR SPECIAL CHARACTERS THE APP WILL IGNORE THEM AND GENERATE A PASSWORD WITH THE CHARACTERS SUPPORTED (LOWERCASE AND UPPERCASE LETTERS, NUMBERS)"
					+ "\n\n¿How it works?\nEvery time you enter a simple password the app will convert it into a more safe password thanks to encryption keys generated on the first time you open the app."
					+ "\n\nI don't like the password the app gave me ¿Can i change it?\nYes! To do so you need to press the \"Reset Encryption Keys\" button(The spinning arrow) and it will generate new encryption keys."
					+ "\nNOTE THAT IF YOU ARE ALREADY USING ANOTHER GENERATED PASSWORD WITH THIS SET OF KEYS YOU WILL NEED TO EITHER:"
					+ "\nSAVE THE COMPLICATED PASSWORD SOMEWHERE OR CHANGE TO A PASSWORD GENERATED WITH THE NEW KEYS IN WHATEVER ACCOUNTS YOU USED IT BEFORE."
					+ "\n\nIm changing PC ¿How do i get access to the same passwords i got in my previous pc?"
					+ "\nYou can copy the encryption keys from the old pc into the new one.\nPress the \"Export encryption keys\" button(The file with an Upwards arrow) and select a folder where the keys will be exported to,"
					+ "\nthen copy and replace both encryption keys into \"Documents/Password Creator Keys/\" on your new PC.\n"
					+ "NOTE THAT TO HAVE THE FOLDER GENERATED IN YOUR NEW PC YOU WILL HAVE TO OPEN THE APP IN THE NEW PC ATLEAST ONCE.","Common Questions", JOptionPane.PLAIN_MESSAGE);
		}
		
		
		if(e.getSource() == resetKeysButton) {
			int n = JOptionPane.showConfirmDialog(this, "Reseting encryption keys will give you access to different safe passwords than before."
					+ "\nIt is recommended to save any previous safe passwords that may have been"
					+ "\n used in services."
					+ "\n¿Would you like to reset encryption keys?", "Reset encryption keys", JOptionPane.YES_NO_OPTION);
			if(n == 0) {
				appController.resetKeys();
				JOptionPane.showMessageDialog(this, "Encryption keys reseted succesfully.", "Operation done", JOptionPane.PLAIN_MESSAGE);	
			}
		}
		
		
		
		if(e.getSource() == exportButton) {
			JOptionPane.showMessageDialog(this, "Select the directory where the keys will be exported to", "Select the directory", JOptionPane.PLAIN_MESSAGE);
			int n = fc.showSaveDialog(this);
			
			if(n == JFileChooser.APPROVE_OPTION) {
				File fileSelected = fc.getSelectedFile();
				boolean saveOk = appController.saveKeysTo(fileSelected);
				
				if(saveOk == true) {
					JOptionPane.showMessageDialog(this, "Keys exported succesfully", "Keys exported", JOptionPane.PLAIN_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(this, "An error has ocurred", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
	}
	
}
