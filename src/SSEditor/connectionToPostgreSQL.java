package SSEditor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connectionToPostgreSQL {

	public static String readQuestionNumber(String s, String country, String mode, String marksTableName,
			String markid) {

		String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + country;
		String newQuestion = "";
		String schema = "";

		if (mode == "op") {
			schema = "op";
		} else if (mode == "op_training") {
			schema = "op";
		} else {
			schema = "kbfiles";
		}

		try (Connection conn = DriverManager.getConnection(url, "gstiller", "123456")) {
			if (conn != null) {
				String SQL = "SELECT * FROM " + schema + "." + marksTableName + " WHERE mark LIKE '%F%' order by "
						+ markid + " DESC LIMIT 1;";
				Statement stmt = conn.createStatement();
				stmt.execute(SQL);
				ResultSet rs = stmt.executeQuery(SQL);
				while (rs.next()) {
					String nextQuestionNumber = rs.getString(s).substring(rs.getString(s).indexOf('F') + 1);
					Integer newQuestionNumber = Integer.valueOf(nextQuestionNumber) + 1;
					newQuestion = newQuestionNumber.toString();
				}
				stmt.close();

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return newQuestion;
	}

	public static String readICNumber(String s, String country, String mode, String marksTableName, String markid) {

		String url = "jdbc:postgresql://ukbrar825.3mhealth.com:5432/" + country;
		String newIC = "";
		String schema = "";

		if (mode == "op") {
			schema = "op";
		} else if (mode == "op_training") {
			schema = "op";
		} else {
			schema = "kbfiles";
		}

		try (Connection conn = DriverManager.getConnection(url, "gstiller", "123456")) {
			if (conn != null) {
				// System.out.println("Connection to the PostgreSQL server successful.");
				String SQL = "SELECT * FROM " + schema + "." + marksTableName + " WHERE mark LIKE '%X%' order by "
						+ markid + " DESC LIMIT 1;";
				Statement stmt = conn.createStatement();
				stmt.execute(SQL);
				ResultSet rs = stmt.executeQuery(SQL);
				while (rs.next()) {
					String nextICNumber = rs.getString(s).substring(rs.getString(s).indexOf('X') + 1);
					Integer newICNumber = Integer.valueOf(nextICNumber) + 1;
					newIC = newICNumber.toString();
				}
				stmt.close();

			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return newIC;
	}
}
