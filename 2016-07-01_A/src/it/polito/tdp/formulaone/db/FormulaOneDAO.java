package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;



public class FormulaOneDAO {

	Map<Integer,Driver> drivers= new HashMap<Integer,Driver>();
	
	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT DISTINCT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	
	public void getAllDrivers() {

		String sql = "SELECT * FROM drivers";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			Driver d=null;;
			while (rs.next()) {
				try {
				d = new Driver(rs.getInt("driverId"), rs.getString("driverRef"),rs.getInt("number"),
						rs.getString("code"),rs.getString("forename"),rs.getString("surname"),
						((rs.getDate("dob")).toLocalDate()), rs.getString("nationality"),rs.getString("url"));
				}catch(NullPointerException e) {}
				
				if (d!=null)
				{drivers.put(d.getDriverId(),d);}
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	
public List<Integer> getAllRacesFromYear(int year) {
		
		String sql = "SELECT races.raceId " + 
				"FROM races " + 
				"WHERE races.YEAR=? " ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, year);
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("raceId"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

public List<Driver> getAllDriversFromRace (int race) {
	
	String sql = "SELECT driverId " + 
			"FROM results " + 
			"WHERE results.raceId=? " + 
			"AND results.POSITION != '(NULL)' ";
	
	try {
		Connection conn = ConnectDB.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		st.setInt(1, race);
		
		ResultSet rs = st.executeQuery() ;
		
		List<Driver> list = new ArrayList<>() ;
		while(rs.next()) {
			list.add(drivers.get(rs.getInt("driverId")));
		}
		
		conn.close();
		return list ;
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("SQL Query Error");
	}
}

public int getWeight (int d1, int d2) {
	
	String sql = "SELECT COUNT(*) AS cnt " + 
			"FROM results r1, results r2 " + 
			"WHERE r1.raceId= r2.raceId " + 
			"AND r1.driverId=? " + 
			"AND r2.driverId=? " + 
			"AND r1.POSITION< r2.position ";
	
	try {
		Connection conn = ConnectDB.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		st.setInt(1, d1);
		st.setInt(2, d2);
		
		ResultSet rs = st.executeQuery() ;
		
		int peso;
		rs.next();
		peso= rs.getInt("cnt");
		
		conn.close();
		return peso;
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException("SQL Query Error");
	}
}

	
}
