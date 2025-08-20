package SSEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class SQL_Statement_Editor_replace_create {

	public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException {

		// layout for connection window:
		JFrame choiceJFrame = new JFrame();
		choiceJFrame.setSize(450, 300);
		choiceJFrame.setTitle("Set KB properties");

		JPanel choicePanelMain = new JPanel();
		JPanel choicePanelClassification = new JPanel();
		JPanel choicePanelCountry = new JPanel();

		choicePanelCountry.setLayout(new BoxLayout(choicePanelCountry, BoxLayout.Y_AXIS));
		choicePanelClassification.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));

		JLabel chooseCountryLabel = new JLabel(" Country: ");
		choicePanelMain.add(chooseCountryLabel);

		JLabel chooseClassificationLabel = new JLabel(" classification: ");
		choicePanelClassification.add(chooseClassificationLabel);

		JRadioButton checkSpain = new JRadioButton("spain");
		JRadioButton checkSwitzerland = new JRadioButton("switzerland");
		JRadioButton checkGermany = new JRadioButton("germany");

		ButtonGroup checkCountryGruppe = new ButtonGroup();
		checkCountryGruppe.add(checkSpain);
		checkCountryGruppe.add(checkSwitzerland);
		checkCountryGruppe.add(checkGermany);

		JRadioButton checkProcedures = new JRadioButton("procedures");
		JRadioButton checkDiagnoses = new JRadioButton("diagnoses");
		JRadioButton checkTraining = new JRadioButton("training");

		ButtonGroup checkClassificationGroup = new ButtonGroup();
		checkClassificationGroup.add(checkProcedures);
		checkClassificationGroup.add(checkDiagnoses);
		checkClassificationGroup.add(checkTraining);

		choicePanelCountry.add(checkSpain);
		choicePanelCountry.add(checkSwitzerland);
		choicePanelCountry.add(checkGermany);

		choicePanelClassification.add(checkProcedures);
		choicePanelClassification.add(checkDiagnoses);
		choicePanelClassification.add(checkTraining);

		GroupLayout choiceClassificationLayout = new GroupLayout(choicePanelClassification);
		choicePanelClassification.setLayout(choiceClassificationLayout);
		choiceClassificationLayout.setAutoCreateGaps(true);

		choiceClassificationLayout.setHorizontalGroup(
				choiceClassificationLayout.createSequentialGroup().addComponent(chooseClassificationLabel)
						.addGroup(choiceClassificationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(checkProcedures).addComponent(checkDiagnoses)
								.addComponent(checkTraining)));

		choiceClassificationLayout.setVerticalGroup(
				choiceClassificationLayout.createSequentialGroup().addComponent(chooseClassificationLabel)
						.addComponent(checkProcedures).addComponent(checkDiagnoses).addComponent(checkTraining));

		JLabel labelUsername = new JLabel();
		labelUsername.setText(" Username: ");
		choicePanelMain.add(labelUsername);

		JLabel labelPassword = new JLabel();
		labelPassword.setText(" Password: ");
		choicePanelMain.add(labelPassword);

		JButton OpenEditorButton = new JButton();
		OpenEditorButton.setText("Open Editor");
		choicePanelMain.add(OpenEditorButton);

		JButton setDefaultButton = new JButton();
		setDefaultButton.setText("Set as default");
		choicePanelMain.add(setDefaultButton);

		JTextField textUsername = new JTextField(10);
		choicePanelMain.add(textUsername);

		JPasswordField textPassword = new JPasswordField("123456");
		textPassword.setEchoChar('*');
		choicePanelMain.add(textPassword);

		// preparation of configuration file with connection parameters:
		// initialize file in same folder as jar
		Properties appProperties = new Properties();
		InputStream stream = new FileInputStream(new File("defaultEntryParameters.txt").getCanonicalPath());
		appProperties.load(stream);

		// check default parameter
		if (appProperties.getProperty("username").isEmpty()) {
			System.out.println("no defaults given");
		} else {

			// credentials
			textUsername.setText(appProperties.getProperty("username"));
			choicePanelMain.add(textUsername);

			textPassword.setText(appProperties.getProperty("password"));
			textPassword.setEchoChar('*');

			// mode and country
			String mode = appProperties.getProperty("mode");
			if (mode.contentEquals("diagnoses")) {
				checkDiagnoses.setSelected(true);
			} else if (mode.contentEquals("procedures")) {
				checkProcedures.setSelected(true);
			} else {
				checkTraining.setSelected(true);
			}

			if (appProperties.getProperty("country").equals("spain")) {
				checkSpain.setSelected(true);
			} else if (appProperties.getProperty("country").equals("switzerland")) {
				checkSwitzerland.setSelected(true);
			} else {
				checkGermany.setSelected(true);
			}
		}

		// continue layout for connection window using the default parameters:
		GroupLayout choiceLayout = new GroupLayout(choicePanelMain);
		choicePanelMain.setLayout(choiceLayout);
		choiceLayout.setAutoCreateGaps(true);

		choiceLayout.setHorizontalGroup(choiceLayout.createSequentialGroup()
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(chooseCountryLabel).addComponent(labelUsername).addComponent(labelPassword))
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(choiceLayout.createSequentialGroup().addComponent(choicePanelCountry)
								.addComponent(choicePanelClassification))
						.addComponent(textUsername).addComponent(textPassword)
						.addGroup(choiceLayout.createSequentialGroup().addComponent(OpenEditorButton)
								.addComponent(setDefaultButton))));

		choiceLayout.setVerticalGroup(choiceLayout.createSequentialGroup()
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(chooseCountryLabel).addComponent(choicePanelCountry)
						.addComponent(choicePanelClassification))
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labelUsername)
						.addComponent(textUsername))
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(labelPassword)
						.addComponent(textPassword))
				.addGroup(choiceLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(OpenEditorButton).addComponent(setDefaultButton)));

		choiceJFrame.add(choicePanelMain);
		choiceJFrame.setVisible(true);

		// methods for buttons in connection window:
		// set new defaults
		setDefaultButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent setDefaults) {

				Properties appProperties = new Properties();
				try {
					InputStream stream = new FileInputStream(new File("defaultEntryParameters.txt").getCanonicalPath());
					appProperties.load(stream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (textUsername.getText().toString().length() > 0 && textUsername.getText().toString().length() > 0) {

					appProperties.setProperty("username", textUsername.getText());
					appProperties.setProperty("password", textPassword.getText());

					if (checkDiagnoses.isSelected()) {
						appProperties.setProperty("mode", "diagnoses");
					} else if (checkProcedures.isSelected()) {
						appProperties.setProperty("mode", "procedures");
					} else {
						appProperties.setProperty("mode", "training");
					}

					if (checkSpain.isSelected()) {
						appProperties.setProperty("country", "spain");
					} else if (checkSwitzerland.isSelected()) {
						appProperties.setProperty("country", "switzerland");
					} else {
						appProperties.setProperty("country", "germany");
					}

					try {
						FileOutputStream streamOut = new FileOutputStream(
								new File("defaultEntryParameters.txt").getCanonicalPath());
						appProperties.store(streamOut, null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (textUsername.getText().toString().length() < 1) {
						JOptionPane.showMessageDialog(null, "Please insert a username");
					} else {
						JOptionPane.showMessageDialog(null, "Please insert a password");
					}

				}

			}
		});

		// open the editor
		OpenEditorButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent act) {

				String username = textUsername.getText();
				String password = textPassword.getPassword().toString();
				String mode = "";
				String marksTableName = "";
				String markid = "";
				String schema = "";

				if (username.length() > 1 && password.length() > 1) {

					if (checkProcedures.isSelected()) {
						mode = "op";
						schema = "op";
					} else if (checkDiagnoses.isSelected()) {
						mode = "dg";
						schema = "kbfiles";
					} else if (checkTraining.isSelected()) {
						mode = "op_training";
						schema = "op";
					} else {
						JOptionPane.showMessageDialog(null,
								"No classification was selected, default mode is 'procedures'");
					}

					String country = "";

					if (checkSpain.isSelected()) {
						country = "spain";
					} else if (checkSwitzerland.isSelected()) {
						country = "switzerland";
					} else if (checkGermany.isSelected()) {
						country = "germany";
					}

					if (country.equals("germany") && mode.equals("op")) {
						marksTableName = "ops_marks";
						markid = "markid";
					} else if (mode.equals("op_training")) {
						marksTableName = "marks_training";
						markid = "id";
					} else {
						marksTableName = "marks";
						markid = "id";
					}

					String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + country;

					// layout for editor window:
					JFrame window = new JFrame("VanTEd, connected to: " + url + "#" + mode);
					JPanel panelMain = new JPanel();
					window.setPreferredSize(new Dimension(800, 800));

					// add components to
					// main panel
					JLabel labelInput = new JLabel();
					labelInput.setText("Input: ");
					panelMain.add(labelInput);

					JTextArea inputFromClassification = new JTextArea(40, 30);

					JScrollPane scrollInput = new JScrollPane(inputFromClassification);
					scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					panelMain.add(scrollInput);

					JLabel labelFinalStatement = new JLabel();
					labelFinalStatement.setText("Final Statement: ");
					panelMain.add(labelFinalStatement);

					JButton execute_button = new JButton();
					execute_button.setText("execute");
					panelMain.add(execute_button);

					JButton reconnect_button = new JButton();
					reconnect_button.setText("reconnect");
					panelMain.add(reconnect_button);

					JTextArea finalStatement = new JTextArea(40, 30);

					JScrollPane scrollOutput = new JScrollPane(finalStatement);
					scrollOutput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
					scrollOutput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					panelMain.add(scrollOutput);

					JTabbedPane tabpane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

					JPanel panelReplace = new JPanel();
					JPanel panelCreate = new JPanel();
					JPanel panelMarks = new JPanel();

					// replace tab
					JButton replace_button = new JButton();
					replace_button.setText("replace");
					panelReplace.add(replace_button);

					JLabel labelReplacement = new JLabel();
					labelReplacement.setText("Replacement: ");
					panelReplace.add(labelReplacement);

					JTextField textReplacement = new JTextField(400);
					panelReplace.add(textReplacement);

					JLabel labelToReplace = new JLabel();
					labelToReplace.setText("To replace: ");
					panelReplace.add(labelToReplace);

					JTextField textToReplace = new JTextField(40);
					panelReplace.add(textToReplace);

					// create tab
					JButton create_button = new JButton();
					create_button.setText("create");
					panelCreate.add(create_button);

					JMenuBar search_button = new JMenuBar();
					// search_button.setPreferredSize(new Dimension(90,
					// search_button.getPreferredSize().height));

					search_button.setOpaque(true);
					JMenu menu = new JMenu("Search Mode");
					JMenuItem sNormal = new JMenuItem("code");
					JMenuItem sRegex = new JMenuItem("regex");

					MouseListener showMenu = new MouseListener() {
						public void mouseClicked(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseReleased(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}

						public void mouseEntered(MouseEvent e) {
							((JMenu) e.getSource()).doClick();
						}
					};

					menu.addMouseListener(showMenu);

					menu.add(sNormal);
					menu.add(sRegex);
					search_button.add(menu);
					panelCreate.add(search_button);

					JLabel labelCode_range = new JLabel();
					labelCode_range.setText("Code(range) or text: ");
					panelCreate.add(labelCode_range);

					JTextField textSearch = new JTextField(100);
					panelCreate.add(textSearch);

					JLabel labelSubchapter = new JLabel();
					labelSubchapter.setText("Subchapter: ");
					panelCreate.add(labelSubchapter);

					JTextField textSubchapter = new JTextField(50);
					if (window.getTitle().contains("germany#op")) {
						textSubchapter.setText("5");
					}
					panelCreate.add(textSubchapter);

					// marks tab
					// questions
//					JButton replace_underscores_button = new JButton();
//					replace_underscores_button.setText("repl_");
//					panelMarks.add(replace_underscores_button);

					JLabel labelQuestionsToProceed = new JLabel();
					labelQuestionsToProceed.setText("Question to proceed: ");
					panelMarks.add(labelQuestionsToProceed);

					JTextField questionToProceed = new JTextField(400);
					panelMarks.add(questionToProceed);
					// read number of last question of table:
					connectionToPostgreSQL nextQuestionNumber = new connectionToPostgreSQL();
					String newQuestion = nextQuestionNumber.readQuestionNumber("mark", country, mode, marksTableName,
							markid);
					questionToProceed.setText(newQuestion);

					JButton createQuestion_button = new JButton();
					createQuestion_button.setText("create question");
					panelMarks.add(createQuestion_button);

					// IC
					JLabel labelICToProceed = new JLabel();
					labelICToProceed.setText("IC to proceed: ");
					panelMarks.add(labelICToProceed);

					JTextField ICToProceed = new JTextField(400);
					panelMarks.add(ICToProceed);

					JLabel labelICAnnahme = new JLabel();
					labelICAnnahme.setText("IC Annahme: ");
					panelMarks.add(labelICAnnahme);

					JTextField ICAnnahme = new JTextField(400);
					panelMarks.add(ICAnnahme);

					// read number of last IC of table:
					connectionToPostgreSQL nextICNumber = new connectionToPostgreSQL();
					String newIC = nextICNumber.readICNumber("mark", country, mode, marksTableName, markid);
					ICToProceed.setText(newIC);

					JButton createIC_button = new JButton();
					createIC_button.setText("create new IC");
					panelMarks.add(createIC_button);

					JButton convertQuestionToIC_button = new JButton();
					convertQuestionToIC_button.setText("convert to IC");
					panelMarks.add(convertQuestionToIC_button);

					// set layout for tabs and main panel
					// create tab:
					GroupLayout layoutCreateTab = new GroupLayout(panelCreate);
					panelCreate.setLayout(layoutCreateTab);
					layoutCreateTab.setAutoCreateGaps(true);

					layoutCreateTab.setHorizontalGroup(layoutCreateTab.createSequentialGroup()
							.addGroup(layoutCreateTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(labelCode_range).addComponent(labelSubchapter))
							.addGroup(layoutCreateTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(textSearch).addComponent(textSubchapter))
							.addGroup(layoutCreateTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(search_button).addComponent(create_button)));

					layoutCreateTab.setVerticalGroup(layoutCreateTab.createSequentialGroup()
							.addGroup(layoutCreateTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelCode_range).addComponent(textSearch).addComponent(search_button))
							.addGroup(layoutCreateTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelSubchapter).addComponent(textSubchapter)
									.addComponent(create_button)));

					// replace tab
					GroupLayout layoutReplaceTab = new GroupLayout(panelReplace);
					panelReplace.setLayout(layoutReplaceTab);
					layoutReplaceTab.setAutoCreateGaps(true);

					layoutReplaceTab.setHorizontalGroup(layoutReplaceTab.createSequentialGroup()
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(labelToReplace).addComponent(labelReplacement))
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(textToReplace).addComponent(textReplacement))
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(replace_button)));

					layoutReplaceTab.setVerticalGroup(layoutReplaceTab.createSequentialGroup()
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.BASELINE))
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelToReplace).addComponent(textToReplace))
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelReplacement).addComponent(textReplacement))
							.addGroup(layoutReplaceTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(replace_button)));

					// marks tab
					GroupLayout layoutMarksTab = new GroupLayout(panelMarks);
					panelMarks.setLayout(layoutMarksTab);
					layoutMarksTab.setAutoCreateGaps(true);

					layoutMarksTab.setHorizontalGroup(layoutMarksTab.createSequentialGroup()
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(labelQuestionsToProceed).addComponent(labelICToProceed)
									.addComponent(labelICAnnahme))
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(questionToProceed).addComponent(ICToProceed).addComponent(ICAnnahme))
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(createQuestion_button).addComponent(convertQuestionToIC_button)
									.addComponent(createIC_button))
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.LEADING)));

					// .addComponent(replace_underscores_button)));

					layoutMarksTab.setVerticalGroup(layoutMarksTab.createSequentialGroup()
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelQuestionsToProceed).addComponent(questionToProceed)
									.addComponent(createQuestion_button))
							// .addComponent(replace_underscores_button))
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelICToProceed).addComponent(ICToProceed)
									.addComponent(convertQuestionToIC_button))
							.addGroup(layoutMarksTab.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelICAnnahme).addComponent(ICAnnahme)
									.addComponent(createIC_button)));

					// main panel

					tabpane.addTab("replace", panelReplace);
					tabpane.addTab("create", panelCreate);
					tabpane.addTab("edit marks", panelMarks);

					GroupLayout layout = new GroupLayout(panelMain);
					panelMain.setLayout(layout);
					layout.setAutoCreateGaps(true);

					layout.setHorizontalGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(labelInput)
									.addComponent(labelFinalStatement))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(scrollInput).addComponent(tabpane).addComponent(scrollOutput))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(reconnect_button).addComponent(execute_button)));

					layout.setVerticalGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelInput).addComponent(scrollInput).addComponent(reconnect_button))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(tabpane))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(labelFinalStatement).addComponent(scrollOutput)
									.addGroup(layout.createSequentialGroup().addComponent(execute_button))));

					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.getContentPane().add(panelMain);
					window.pack();

					if (checkSpain.isSelected()) {
						choiceJFrame.setVisible(false);
						window.setVisible(true);
					} else if (checkSwitzerland.isSelected()) {
						choiceJFrame.setVisible(false);
						window.setVisible(true);
					} else if (checkGermany.isSelected()) {
						choiceJFrame.setVisible(false);
						window.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Please choose a country");
					}

					// methods of the editor:
					// replace tab:
					replace_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent ev) {

							String line = inputFromClassification.getText();
							String toReplace = textToReplace.getText();
							String replacement = textReplacement.getText();
							String output = "";

							final Pattern PATTERN_toReplace = Pattern.compile(toReplace);

							try {

								Matcher matcherToReplace = PATTERN_toReplace.matcher(line);
								output = matcherToReplace.replaceAll(replacement);

								finalStatement.setText(output);
								inputFromClassification.setText(output);

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					});

					// "create" tab:
					// search for a range of code or some concept/text in the classification to show
					// and edit them in the
					// input field
					sNormal.addActionListener(new ActionListener() {

						@SuppressWarnings("resource")
						@Override
						public void actionPerformed(ActionEvent search) {
							inputFromClassification.setText("");
							String country = "";
							String title = window.getTitle();
							String mode = "";

							if (title.contains("dg")) {
								mode = "dg";
							} else {
								mode = "op";
							}

							System.out.println("mode test: " + mode);
							String subchapter = textSearch.getText().substring(0, 1);

							textSubchapter.setText(subchapter);

							if (title.contains("spain")) {
								country = "es";
							} else if (title.contains("switzerland")) {
								country = "ch";
							} else if (title.contains("germany")) {
								country = "de";
							}

							try {
								String filePath = "./classification/" + country + "_" + mode;
								String toSearchFor = textSearch.getText();

								Scanner searchInClassification;
								searchInClassification = new Scanner(new File(filePath));

								while (searchInClassification.hasNext()) {
									String line = searchInClassification.nextLine().toString();
									if (line.startsWith(toSearchFor)) {
										inputFromClassification.append(line + "\n");
									}
								}

							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

						}
					});

					sRegex.addActionListener(new ActionListener() {

						@SuppressWarnings("resource")
						@Override
						public void actionPerformed(ActionEvent setDefaults) {
							inputFromClassification.setText("");
							String country = "";
							String title = window.getTitle();
							String mode = "";

							if (title.contains("dg")) {
								mode = "dg";
							} else {
								mode = "op";
							}
							String subchapter = textSearch.getText().substring(0, 1);

							textSubchapter.setText(subchapter);

							if (title.contains("spain")) {
								country = "es";
							} else if (title.contains("switzerland")) {
								country = "ch";
							} else if (title.contains("germany")) {
								country = "de";
							}

							try {
								String filePath = "./classification/" + country + "_" + mode;
								String toSearchFor = textSearch.getText();

								Scanner searchInClassification;
								searchInClassification = new Scanner(new File(filePath));

								while (searchInClassification.hasNext()) {
									String line = searchInClassification.nextLine().toString();
									if (line.matches(toSearchFor) == true) {
										inputFromClassification.append(line + "\n");
									}
								}

							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

						}
					});

					// define the coding table name and generate an INSERT statement for the input
					// field data to be inserted
					create_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent create) {

							String codingTable = "";
							String title = window.getTitle();
							String atomsIfColumnName = "";
							String atomsIfNotColumnName = "";
							String marksValue = "";
							String subchapter = "";

							if (textSubchapter.getText().length() > 0) {
								subchapter = textSubchapter.getText();
							} else {
								subchapter = textSearch.getText().substring(0, 1);
							}

							if (title.contains("germany#op")) {
								codingTable = "op.ops_coding_table";
								atomsIfColumnName = "atoms_if";
								atomsIfNotColumnName = "atoms_if_not";
							} else if (title.contains("training")) {
								codingTable = "op.coding_training";
								atomsIfColumnName = "atoms_if";
								atomsIfNotColumnName = "atoms_if_not";
							} else if (title.contains("switzerland#op")) {
								codingTable = "op.coding";
								atomsIfColumnName = "atoms_if";
								atomsIfNotColumnName = "atoms_if_not";
							} else {
								codingTable = "kbfiles.vantage";
								atomsIfColumnName = "p_atoms";
								atomsIfNotColumnName = "n_atoms";
							}

							ArrayList<String> lines = new ArrayList<String>();

							for (String line : inputFromClassification.getText().split("\\n")) {

								lines.add(line);

							}
							System.out.println(lines.get(lines.size() - 1));

							if (lines.get(lines.size() - 1).contains("&")) {
								finalStatement.setText("INSERT INTO " + codingTable + " (chap,code," + atomsIfColumnName
										+ "," + atomsIfNotColumnName + ",marks,added_by) VALUES \n");
							} else {
								finalStatement.setText("INSERT INTO " + codingTable + " (chap,code," + atomsIfColumnName
										+ "," + atomsIfNotColumnName + ",added_by) VALUES \n");
							}

							for (int i = 0; i < lines.size() - 1; i++) {
								String code = lines.get(i).substring(0, lines.get(i).indexOf(";"));
								String atoms_if = lines.get(i).substring(lines.get(i).indexOf(";") + 1);

								finalStatement.append("('" + subchapter + "','" + code + "','" + atoms_if + "',NULL,'"
										+ username + "')," + "\n");

							}

							String code = lines.get(lines.size() - 1).substring(0,
									lines.get(lines.size() - 1).indexOf(";"));

							if (lines.get(lines.size() - 1).contains("&")) {

								String atoms_if = lines.get(lines.size() - 1).substring(
										lines.get(lines.size() - 1).indexOf(";") + 1,
										lines.get(lines.size() - 1).indexOf("&"));

								marksValue = lines.get(lines.size() - 1).substring(
										lines.get(lines.size() - 1).indexOf("&") + 1,
										lines.get(lines.size() - 1).length());

								finalStatement.append("('" + subchapter + "','" + code + "','" + atoms_if + "','"
										+ marksValue + ",'" + username + "');");

							} else {

								String atoms_if = lines.get(lines.size() - 1)
										.substring(lines.get(lines.size() - 1).indexOf(";") + 1);

								finalStatement.append("('" + subchapter + "','" + code + "','" + atoms_if + "',NULL,'"
										+ username + "');");
							}
						}

					});

					// removing underscores from answer concepts in marks table to stay close to
					// natural language
//					replace_underscores_button.addActionListener(new ActionListener() {
//
//						@Override
//						public void actionPerformed(ActionEvent underscores) {
//
//							String user = textUsername.getText();
//							String countryURL = "";
//							String password = textPassword.getText();
//							String mode = "";
//							String marksTableName = "";
//							String title = window.getTitle();
//
//							if (checkProcedures.isSelected()) {
//								mode = "op";
//							} else {
//								mode = "";
//							}
//
//							if (checkSpain.isSelected()) {
//								countryURL = "spain";
//							} else if (checkSwitzerland.isSelected()) {
//								countryURL = "switzerland";
//							} else if (checkGermany.isSelected()) {
//								countryURL = "germany";
//							}
//
//							if (title.contains("germany#op")) {
//								marksTableName = "ops_marks";
//							} else {
//								marksTableName = "marks";
//							}
//
//							String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + countryURL;
//
//							try (Connection conn = DriverManager.getConnection(url, user, password)) {
//								if (conn != null) {
//									System.out.println("Connection to the PostgreSQL server successful.");
//									String SQL_update;
//									String SQL_select;
//									Statement stmt = conn.createStatement();
//									SQL_update = "UPDATE " + mode + "." + marksTableName
//											+ " SET text = replace(text, '_', ' ') WHERE mark like '&F"
//											+ questionToProceed.getText() + "';";
//									SQL_select = "SELECT text FROM " + mode + "." + marksTableName
//											+ " WHERE mark like '&F" + questionToProceed.getText() + "';";
//									stmt.execute(SQL_update);
//
//									ResultSet result;
//									result = stmt.executeQuery(SQL_select);
//									while (result.next()) {
//										finalStatement.setText(result.getString(1));
//									}
//									stmt.close();
//								}
//
//							} catch (SQLException e) {
//								System.out.println(e.getMessage());
//								finalStatement.setText(e.getMessage());
//							}
//						}
//
//					});

					// create question as list of last concepts in input field (CAVE: defined
					// delimiter is "|")
					createQuestion_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String marksTableName = "";
							String title = window.getTitle();
							String mode = "";
							String countryURL = "";
							String markid = "";

							if (checkSpain.isSelected()) {
								countryURL = "spain";
							} else if (checkSwitzerland.isSelected()) {
								countryURL = "switzerland";
							} else if (checkGermany.isSelected()) {
								countryURL = "germany";
							}

							if (checkProcedures.isSelected()) {
								mode = "op";
							} else if (checkTraining.isSelected()) {
								mode = "op";
							} else {
								mode = "kbfiles";
							}

							if (title.contains("germany#op")) {
								marksTableName = "ops_marks";
								markid = "markid";
							} else if (title.contains("training")) {
								marksTableName = "marks_training";
								markid = "id";
							} else {
								marksTableName = "marks";
								markid = "id";
							}

							connectionToPostgreSQL nextQuestionNumber = new connectionToPostgreSQL();
							String newQuestion = nextQuestionNumber.readQuestionNumber("mark", countryURL, mode,
									marksTableName, markid);
							System.out.println("new Question: " + newQuestion);
							questionToProceed.setText(newQuestion);

							if (inputFromClassification.getText().contains("|")) {
								String options = "";
								finalStatement.setText(
										"INSERT INTO " + mode + "." + marksTableName + " (text,mark) VALUES \n");
								ArrayList<String> lines = new ArrayList<String>();

								for (String line : inputFromClassification.getText().split("\\n")) {

									lines.add(line);

								}

								for (int i = 0; i < lines.size(); i++) {
									int index = 0;
									for (int k = 0; k < lines.get(i).length(); k++) {
										if (lines.get(i).charAt(k) == '|') {
											index = k;
										}
									}

									if (options == "") {
										options = lines.get(i).substring(index + 1);
									} else {
										options = options + "," + lines.get(i).substring(index + 1);
									}

								}
								finalStatement.append("('" + options + "','&F" + questionToProceed.getText() + "');");

							} else {
								finalStatement.setText("INSERT INTO " + mode + "." + marksTableName
										+ " (text,mark) VALUES \n('" + inputFromClassification.getText() + "','&F"
										+ questionToProceed.getText() + "');");
							}

							findLastLine lastLine = new findLastLine();
							try {
								String newLastLine = lastLine.addLineWithQuestion(inputFromClassification,
										questionToProceed, mode);
								inputFromClassification.append(newLastLine);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}

						}

					});

					// create IC as list of last concepts in input field (CAVE: defined
					// delimiter is "|")
					createIC_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String marksTableName = "";
							String title = window.getTitle();
							String mode = "";
							String countryURL = "";
							String markid = "";

							if (checkSpain.isSelected()) {
								countryURL = "spain";
							} else if (checkSwitzerland.isSelected()) {
								countryURL = "switzerland";
							} else if (checkGermany.isSelected()) {
								countryURL = "germany";
							}

							if (checkProcedures.isSelected()) {
								mode = "op";
							} else if (checkTraining.isSelected()) {
								mode = "op";
							} else {
								mode = "kbfiles";
							}

							if (title.contains("germany#op")) {
								marksTableName = "ops_marks";
								markid = "markid";
							} else if (title.contains("training")) {
								marksTableName = "marks_training";
								markid = "id";
							} else {
								marksTableName = "marks";
								markid = "id";
							}

							connectionToPostgreSQL nextICNumber = new connectionToPostgreSQL();
							String newIC = nextICNumber.readICNumber("mark", countryURL, mode, marksTableName, markid);
							// System.out.println("new IC: " + newIC);
							ICToProceed.setText(newIC);

							String annahmeText = ICAnnahme.getText();
							String defaultAnswer;

							if (inputFromClassification.getText().contains("|")) {

								String options = "";
								finalStatement.setText(
										"INSERT INTO " + mode + "." + marksTableName + " (text,mark) VALUES \n");
								ArrayList<String> lines = new ArrayList<String>();

								for (String line : inputFromClassification.getText().split("\\n")) {

									lines.add(line);

								}

								for (int i = 1; i < lines.size(); i++) {
									int index = 0;
									for (int k = 0; k < lines.get(i).length(); k++) {
										if (lines.get(i).charAt(k) == '|') {
											index = k;
										}
									}

									// check if IC_Annahme is given
									if (annahmeText.length() > 0) {
										options = "IC_Annahme: " + annahmeText + ". Falls {"
												+ lines.get(0).substring(index + 1) + "},{"
												+ lines.get(i).substring(index + 1);
										annahmeText = "";
									} else if (options.length() < 1) {
										options = "IC_Annahme: " + lines.get(0).substring(index + 1) + ". Falls {"
												+ lines.get(i).substring(index + 1);
									} else {
										options = options + "},{" + lines.get(i).substring(index + 1);
									}

								}
								finalStatement
										.append("('" + options + "} auswählen.','&X" + ICToProceed.getText() + "');");

							} else {
								finalStatement.setText("INSERT INTO " + mode + "." + marksTableName
										+ " (text,mark) VALUES \n('" + inputFromClassification.getText() + "','&X"
										+ ICToProceed.getText() + "');");
							}

							findLastLine lastLine = new findLastLine();
							try {
								String newLastLine = lastLine.addLineWithIC(inputFromClassification, ICToProceed, mode);
								inputFromClassification.append(newLastLine);
							} catch (BadLocationException e1) {
								e1.printStackTrace();
							}

						}

					});

					// convert Question to IC
					convertQuestionToIC_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent underscores) {

							String user = textUsername.getText();
							String countryURL = "";
							String password = textPassword.getText();
							String mode = "";
							String marksTableName = "";
							String title = window.getTitle();

							if (checkProcedures.isSelected()) {
								mode = "op";
							} else if (checkTraining.isSelected()) {
								mode = "op";
							} else {
								mode = "kbfiles";
							}

							if (checkSpain.isSelected()) {
								countryURL = "spain";
							} else if (checkSwitzerland.isSelected()) {
								countryURL = "switzerland";
							} else if (checkGermany.isSelected()) {
								countryURL = "germany";
							}

							if (title.contains("germany#op")) {
								marksTableName = "ops_marks";
							} else if (title.contains("training")) {
								marksTableName = "marks_training";
							} else {
								marksTableName = "marks";
							}

							String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + countryURL;

							try (Connection conn = DriverManager.getConnection(url, user, password)) {
								if (conn != null) {
									System.out.println("Connection to the PostgreSQL server successful.");
									String SQL_updateText;
									String SQL_updateMarknumber;
									String SQL_select;
									Statement stmt = conn.createStatement();
									SQL_select = "SELECT text FROM " + mode + "." + marksTableName
											+ " WHERE mark like '&F" + questionToProceed.getText() + "';";

									ResultSet result;
									result = stmt.executeQuery(SQL_select);

									String answers = "";

									while (result.next()) {
										answers = answers + result.getString(1);
									}

									List<String> answersList = Stream.of(answers.split(",", -1))
											.collect(Collectors.toList());
									String ICoptions = "";

									for (int i = 0; i < answersList.size(); i++) {
										ICoptions = ICoptions + "{" + answersList.get(i) + "},";
									}

									SQL_updateText = "UPDATE " + mode + "." + marksTableName
											+ " SET text = 'IC_Annahme: " + ICAnnahme.getText() + ICoptions
											+ "auswählen.' WHERE mark like '&F" + questionToProceed.getText() + "';";

									SQL_updateMarknumber = "UPDATE " + mode + "." + marksTableName + " SET mark = '&X"
											+ ICToProceed.getText() + "' WHERE mark like '&F"
											+ questionToProceed.getText() + "';";

									finalStatement.setText(SQL_updateText + "\n" + SQL_updateMarknumber);
									stmt.close();
								}

							} catch (SQLException e) {
								System.out.println(e.getMessage());
								finalStatement.setText(e.getMessage());
							}
						}

					});

					// main panel
					// execute statement in "final statement" field
					execute_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent ae) {

							String user = textUsername.getText();
							String countryURL = "";
							String password = textPassword.getText();
							String checkQuestion = "";

							if (finalStatement.getText().contains("ops_marks")) {
								Integer m = Integer.valueOf(questionToProceed.getText()) + 1;
								checkQuestion = m.toString();
								questionToProceed.setText(checkQuestion);
							} else {
								checkQuestion = "no question";

							}

							if (checkSpain.isSelected()) {
								countryURL = "spain";
							} else if (checkSwitzerland.isSelected()) {
								countryURL = "switzerland";
							} else if (checkGermany.isSelected()) {
								countryURL = "germany";
							}

							String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + countryURL;

							try (Connection conn = DriverManager.getConnection(url, user, password)) {
								if (conn != null) {
									System.out.println("Connection to the PostgreSQL server successful.");
									String SQL;
									Statement stmt = conn.createStatement();
									SQL = finalStatement.getText();
									stmt.execute(SQL);
									stmt.close();
									finalStatement.setText("");
								}

							} catch (SQLException e) {
								System.out.println(e.getMessage());
								finalStatement.setText(e.getMessage());
							}

						}

					});

					// go back to connection window (to connect to other knowledgebase)
					reconnect_button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent reconn) {
							window.setVisible(false);
							choiceJFrame.setVisible(true);
						}
					});
				} else

				{
					if (username.length() < 1) {
						JOptionPane.showMessageDialog(null, "Please insert a username");
					} else {
						JOptionPane.showMessageDialog(null, "Please insert a password");
					}
				}

			}

		});

	}

}
