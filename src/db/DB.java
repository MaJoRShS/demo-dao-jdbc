package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	private static Connection conn = null;

	/*
	 * Aqui está o método que vai abrir a coneção com o banco, ele primeiro verifica
	 * se a coneção não existe e ai ele começa a instanciala, ele atribui as
	 * propriedades direto em variaveis do tipo properties mesmo e ai usa esse tal
	 * de DRIVEMANAGER para abrir a coneção;
	 */
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}
	
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			}catch (SQLException e){
				throw new DbException(e.getMessage());
			}
			
		}
	}

	/*
	 * Carrega as propriedades do arquivo db.properties e trás para ca para poder
	 * abrir a coneção
	 */
	private static Properties loadProperties() {
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw  new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw  new DbException(e.getMessage());
			}
		}
	}
}
