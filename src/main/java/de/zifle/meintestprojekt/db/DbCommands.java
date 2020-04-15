package de.zifle.meintestprojekt.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCommands {

	public static void dropAndCreateYarbDB() throws SQLException, IOException {
		Connection dbConnection = DbConnector.getInstance().getConnection();

		Statement statement = dbConnection.createStatement();

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(DbCommands.class.getResourceAsStream("/db-scripts/dropAndCreateYarbDB.sql")));
		String line = bufferedReader.readLine();

		while (line != null) {
			statement.addBatch(line);
			line = bufferedReader.readLine();
		}

		statement.executeBatch();
	}
	

}
