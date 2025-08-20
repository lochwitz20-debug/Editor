package SSEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MenuButtonRunner {

	public static void main(String[] args) {
		JFrame choiceJFrame = new JFrame();
		choiceJFrame.setSize(450, 300);
		choiceJFrame.setTitle("test MenuButton");

		JPanel panel = new JPanel();
		JMenuBar bar = new JMenuBar();
		bar.setOpaque(true);

		JMenu menu = new JMenu("search_Mode");

		JMenuItem sNormal = new JMenuItem("normal search");
		sNormal.setPreferredSize(new Dimension(90, 20));
		JMenuItem sRegex = new JMenuItem("regex search");
		sRegex.setPreferredSize(new Dimension(90, 20));
		menu.add(sNormal);
		menu.add(sRegex);

		bar.add(menu);
		panel.add(bar);

		choiceJFrame.add(panel);
		choiceJFrame.setVisible(true);

		sNormal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent setDefaults) {
				System.out.println("normal search mode");
			}
		});

		sRegex.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent setDefaults) {
				System.out.println("regex search mode");
			}
		});

	}

}
