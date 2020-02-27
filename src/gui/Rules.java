package gui;


import java.awt.*;
import java.io.*;
import javax.swing.*;

/**
 * Frame that shows rules of the game, as of now needs rules
 * to be isnerted, this currently holds empty pictures.
 * @author AevanDino, Oscar Strandmark
 *
 */
public class Rules extends JPanel {
    
	/**
	 * Tabs to show in ui, easy to add more tabs if needed
	 *
	 */
	public Rules() {
		new GridLayout(1, 1);
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(450, 600));

		JComponent pnl1 = loadGameInfo("Intro.txt");
		tabbedPane.addTab("Intro", pnl1);

		JComponent pnl2 = loadGameInfo("Kartbitar.txt");
		tabbedPane.addTab("Kartbitar", pnl2);

		JComponent pnl3 = loadGameInfo("Skjuta.txt");
		tabbedPane.addTab("Skjuta", pnl3);

		JComponent pnl4 = loadGameInfo("Underlag.txt");
		tabbedPane.addTab("Underlag", pnl4);
		
		JComponent pnl5 = loadGameInfo("Fly.txt");
		tabbedPane.addTab("Fly", pnl5);
		add(tabbedPane);
	}

	protected JComponent createPaneWithImage(String imgPath) {
		JPanel pnl = new JPanel(false);
		// Raden nedan skalar om bilden till 450, 600
		ImageIcon imgTab = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH));
		JLabel lblContent = new JLabel(imgTab);
		lblContent.setHorizontalAlignment(JLabel.CENTER);
		pnl.setLayout(new GridLayout(1, 1));
		pnl.add(lblContent);
		return pnl;
	}

	/**
	 * Creates and fills textcomponent with text from textfile
	 * @param infoName Name of textfile
	 * @return Panel containing text from file
	 * @author André Möller, Rubem Midhall
 	 */

	protected JComponent loadGameInfo(String infoName) {
		JPanel pnl = new JPanel(false);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("files/"+infoName));
			String tempLine = reader.readLine();
			String line = tempLine;
			while (tempLine != null) {
				tempLine = reader.readLine();
				if(tempLine != null) {
					line += "<br>" + tempLine;
				}
			}
			reader.close();

			JEditorPane textAreaContent = new JEditorPane("text/html", "");
			textAreaContent.setText(line);
			textAreaContent.setEditable(false);
			pnl.setLayout(new GridLayout(1, 1));
			pnl.add(textAreaContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pnl;
	}

	/**
	 * Create the GUI and show it. Method creates panel and frame
	 * to show the user.
	 */
	private static void createAndShowFrame() {
		JFrame frame = new JFrame("Rules");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(new Rules(), BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * This method is called every time
	 * the user presses "View Rules" button.
	 */
	public void showRules() {
		createAndShowFrame();
	}

}