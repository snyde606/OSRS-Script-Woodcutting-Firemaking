import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.osbot.rs07.api.map.Area;

@SuppressWarnings("unused")
public class GUI {
	JFrame gui;

	public static String stringTree = "Tree";
	public static boolean started = false;

	public GUI() {

	}

	public void createGUI() {

		started = false;

		final int GUI_WIDTH = 300, GUI_HEIGHT = 100;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		final int gX = (int) (screenSize.getWidth() / 2) - (GUI_WIDTH / 2);
		final int gY = (int) (screenSize.getHeight() / 2) - (GUI_HEIGHT / 2);

		gui = new JFrame("Jacobian's Woodcutter and Firemaker");
		gui.setBounds(gX, gY, GUI_WIDTH, GUI_HEIGHT);
		gui.setResizable(false);
		JPanel panel = new JPanel();

		gui.add(panel);

		final JComboBox<String> tree = new JComboBox<String>(
				new String[] { "Tree", "Oak", "Willow", "Maple", "Mahogany", "Yew", "Magic" });

		tree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stringTree = tree.getSelectedItem().toString();
			}
		});

		panel.add(tree);

		JButton startButton = new JButton("Start");

		startButton.addActionListener(e -> {
			started = true;
			gui.setVisible(false);
			gui.dispose();
		});

		panel.add(startButton);

		gui.setVisible(true);
	}

	public static String getStringTree() {
		if (stringTree != null && stringTree != "")
			return stringTree;
		else
			return "Error - no tree selected";
	}

	public static boolean isStarted() {
		return started;
	}

}
