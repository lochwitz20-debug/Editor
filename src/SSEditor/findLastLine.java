package SSEditor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class findLastLine {

	public static String addLineWithQuestion(JTextArea s, JTextField questionToProceed, String mode)
			throws BadLocationException {

		String lastLine;
		int lines = s.getLineCount();
		String line;
		List<String> listOfLines = new ArrayList<String>();

		for (int i = 0; i < lines; i++) {
			int start = s.getLineStartOffset(i);
			int end = s.getLineEndOffset(i);
			if (s.getText(start, end - start).length() > 1) {
				listOfLines.add(s.getText(start, end - start));
			}
		}

		s.setText("");
		for (int i = 0; i < listOfLines.size(); i++) {
			s.append(listOfLines.get(i));
		}

		lastLine = listOfLines.get(listOfLines.size() - 1);
		String lastLineSub1 = lastLine.substring(0, lastLine.indexOf(";"));
		String lastLineSub2 = lastLine.substring(lastLine.indexOf(";"), lastLine.lastIndexOf("|"));
		Integer question = Integer.valueOf(questionToProceed.getText());
		String lastLineWithQuestion = "";

		if (mode == "op") {
			lastLineWithQuestion = lastLineSub1 + "|F" + question.toString() + lastLineSub2;
		} else {
			lastLineWithQuestion = lastLine.substring(0, lastLine.lastIndexOf("|")) + "&F" + question;
		}
		return lastLineWithQuestion;

	}

	public static String addLineWithIC(JTextArea s, JTextField ICToProceed, String mode) throws BadLocationException {

		String firstLine;
		int lines = s.getLineCount();
		String line;
		List<String> listOfLines = new ArrayList<String>();

		for (int i = 0; i < lines; i++) {
			int start = s.getLineStartOffset(i);
			int end = s.getLineEndOffset(i);
			if (s.getText(start, end - start).length() > 1) {
				listOfLines.add(s.getText(start, end - start));
			}
		}

		s.setText("");
		for (int i = 0; i < listOfLines.size(); i++) {
			s.append(listOfLines.get(i));
		}

		firstLine = listOfLines.get(0);
		String lastLineSub1 = firstLine.substring(0, firstLine.indexOf(";"));
		String lastLineSub2 = firstLine.substring(firstLine.indexOf(";"), firstLine.lastIndexOf("|"));
		Integer IC = Integer.valueOf(ICToProceed.getText());
		String lastLineWithIC = "";

		if (mode == "op") {
			lastLineWithIC = lastLineSub1 + "|X" + IC.toString() + lastLineSub2;
		} else {
			lastLineWithIC = firstLine.substring(0, firstLine.lastIndexOf("|")) + "&X" + IC;
		}
		return lastLineWithIC;

	}

}
