import java.sql.*;

public class Test {
	public void dothis() {
	try {
		Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			System.err.println("JDBC-ajurin lataus epäonnistui");
			System.exit(-1);  // lopetus heti virheen vuoksi
			//Tietokantaoperaatioissa on useita virhemahdollisuuksia, ja niihin on varauduttavatry/catch-lohkoin (poikkeuksen tyyppinä SQLException).
			//4.Muodosta yhteys tietokantaan
		
	}
	ResultSet rs = null;
	final String URL = "jdbc:mysql://localhost:2206/otptest";
	final String USERNAME = "timo";//timo tai taneli tai pasi
	final String PASSWORD = "12345678";
	try {
		Connection conn =DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Statement stmt =conn.createStatement();
		String query = "SELECT * FROM users";
		rs =stmt.executeQuery(query);
		try {
			while (rs.next()) {
			    String testName = rs.getString("username");
			    
			    System.out.println(testName );
			}
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}catch(SQLException e2 ) {
        e2.printStackTrace();;
    }
	
	}
	public static void main(String[] args) {
		Test test = new Test();
		test.dothis();
	}
}

