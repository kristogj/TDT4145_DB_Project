import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController{
	
	
	
	
	///////////////////////////Kravspesifikasjon 1///////////////////////////
	
	public static void insertWorkout(Connection myConn, Date date, Time time, int duration, int personligForm, int prestasjon, String notat )throws SQLException{
		String preQueryStatement = "INSERT INTO workout (DATO, TIDSPUNT, VARIGHET, PERSONLIGFORM, PRESTASJON, NOTAT) VALUES (?,?,?,?,?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		
		prepStat.setDate(1,date);
		prepStat.setTime(2, time);
		prepStat.setInt(3, duration);
		prepStat.setInt(4, personligForm);
		prepStat.setInt(5, prestasjon);
		prepStat.setString(6, notat);
		prepStat.execute();
		
	}
	
	public static void insertExerciseGroup(Connection myConn, String navn) throws SQLException {
		String preQueryStatement = "INSERT INTO exercisegroup (NAVN) VALUES (?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setString(1, navn);
		prepStat.execute();
	}
	
	
	public static void insertExercise(Connection myConn, String navn, String beskrivelse) throws SQLException {
		String preQueryStatement = "INSERT INTO exercise (NAVN, BESKRIVELSE) VALUES (?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setString(1, navn);
		prepStat.setString(2, beskrivelse);
		prepStat.execute();
	}
	
	public static void insertMachine(Connection myConn, String navn, String beskrivelse) throws SQLException{
		String preQueryStatement = "INSERT INTO machine (NAVN, BESKRIVELSE) VALUES (?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setString(1, navn);
		prepStat.setString(2, beskrivelse);
		prepStat.execute();
	}
	
	public static void insertExerciseOnMachine(Connection myConn,String exerciseName,String machineName) throws SQLException{
		//Begge er fremmednøkkler til sin entitet
		String preQueryStatement = "INSERT INTO exerciseonmachine (EXERCISENAME, MACHINENAME) VALUES (?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setString(1, exerciseName);
		prepStat.setString(2, machineName);
		prepStat.execute();
	}
	
	public static void insertGroupContainsExercise(Connection myConn,String groupName,String exerciseName) throws SQLException{
		//Begge er fremmednøkkler til sin entitet
		String preQueryStatement = "INSERT INTO groupcontainsexersice (GRUPPENAVN, EXERSICENAME) VALUES (?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setString(1, groupName);
		prepStat.setString(2, exerciseName);
		prepStat.execute();
	}
	
	public static void insertWorkoutContainsExercise(Connection myConn,Date workoutDate,String exerciseName,int antallKilo, int antallSet) throws SQLException{
		//Begge er fremmednøkkler til sin entitet
		String preQueryStatement = "INSERT INTO workoutcontainsexercise (DATO, NAVN, KILO, SETT) VALUES (?,?,?,?)";
		PreparedStatement prepStat = myConn.prepareStatement(preQueryStatement);
		prepStat.setDate(1, workoutDate);
		prepStat.setString(2, exerciseName);
		prepStat.setInt(3, antallKilo); //Hvis disse verdiene ikke er relevante settes de til null
		prepStat.setInt(4, antallSet); //Hvis disse verdiene ikke er relevante settes de til null -  feks på øvelser som ikke er på apparat
		prepStat.execute();
	}
	
	
	
	
///////////////////////////Kravspesifikasjon 2///////////////////////////
	
	
	
	//Hente de n siste Workout med all informasjon
	public static List<Workout> getNWorkouts(Connection conn, int n) throws SQLException{
		List<Workout> workouts = new ArrayList<Workout>();
		
		//First find a list over the N last workouts
		String stmt = "select * from workout order by dato desc limit ?";
		PreparedStatement prepStat = conn.prepareStatement(stmt);
		prepStat.setInt(1, n);
		ResultSet rs = prepStat.executeQuery();
		while(rs.next()) {
			Workout w = new Workout(rs.getDate("dato"), rs.getTime("tidspunt"), rs.getInt("varighet"), rs.getInt("personligForm"), rs.getInt("prestasjon"), rs.getString("notat"));
			workouts.add(w);
		}
		
		//Then for every workout get all the exercises
		for (Workout w : workouts) {
			Date id = w.getDato();
			stmt = "select * from workoutcontainsexercise where dato = ?";
			prepStat = conn.prepareStatement(stmt);
			prepStat.setDate(1, id);
			while(rs.next()) {
				Exercise e = new Exercise(rs.getString("exersicename"),rs.getInt("kilo"),rs.getInt("sett"));
				
				//Hen exercise beskrivelse
				String tmpStmt = "Select * from exercise where navn = ?";
				PreparedStatement pr = conn.prepareStatement(tmpStmt);
				pr.setString(1, rs.getString("exersicename"));
				ResultSet tmpRes = pr.executeQuery();
				e.setDescription(tmpRes.getString("beskrivelse"));
				
				//Hvis øvelsen gjøres på en maskin, hent apparatinfo
				//hmm
				
				//Legg til øvelse i workout
				w.addExercise(e);
			}
			
		}
		return workouts;
	}
		
	
	///////////////////////////Kravspesifikasjon 3///////////////////////////
	public static String getExerciseResult(Connection myConn, Date dateStart,Date dateEnd) throws SQLException{
		//TODO - BETWEEN FUNKER IKKE HER MED DATOENE...
		dateStart.setYear(dateStart.getYear()-1900);
		dateEnd.setYear(dateEnd.getYear()-1900);
        String query = "SELECT PERSONLIGFORM, VARIGHET FROM workout WHERE DATO BETWEEN ? AND ?";
        //SELECT PERSONLIGFORM, VARIGHET FROM workout WHERE DATO BETWEEN '3000-01-01' AND '4000-01-01'
        PreparedStatement preparedStatement = myConn.prepareStatement(query);
        preparedStatement.setDate(1, dateStart);
        preparedStatement.setDate(2, dateEnd);
        System.out.println(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        int index =1;
        double antallTimer = 0;
        double antallPersonligeForm = 0;
        while (resultSet.next()) {
            System.out.println("INNNE I LOOPEN");
            index++;
            antallTimer += resultSet.getInt("VARIGHET");
            antallPersonligeForm += resultSet.getInt("PERSONLIGFORM");
        }
        double personligFormSnitt = antallPersonligeForm/index;
        double varighetSnitt = antallTimer/index;
        
        String report = "I løpet av perioden på "+ index + "dager, trente du "+ antallTimer + " timer."+ " Gjennomsnittsøkten var på " +varighetSnitt +" timer, med et gjennomsnittlig perosnlig form på "+personligFormSnitt +".";

        return report;
    }
	
	
	///////////////////////////Kravspesifikasjon 4///////////////////////////
	
	
	public static List<ExerciseGroup> getExerciseGroups(Connection conn) throws SQLException{
		List<ExerciseGroup> groups = new ArrayList<ExerciseGroup>();
		
		//Spørr om alle koblingene mellom en exercise og en gruppe
		String stmt = "Select * from groupcontainsexersice";
		PreparedStatement prepStat = conn.prepareStatement(stmt);
		ResultSet rs = prepStat.executeQuery();
		
		//Lag en map hvor gruppenavn er key og ArrayList med exercisenavn er value
		Map<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		while(rs.next()) {
			if(map.containsKey(rs.getString("gruppenavn"))) {
				map.get(rs.getString("gruppenavn"));
			}
			else {
				map.put(rs.getString("gruppenavn"), new ArrayList<String>(Arrays.asList(rs.getString("exersicename"))));
			}
		}
		
		//Lag objekter ut av mappen
		for(String gruppenavn : map.keySet()) {
			List<Exercise> exercises = new ArrayList<Exercise>();
			for(String exercisename : map.get(gruppenavn)) {
				//Hen exercise beskrivelse
				String tmpStmt = "Select * from exercise where navn = ?";
				PreparedStatement pr = conn.prepareStatement(tmpStmt);
				pr.setString(1, exercisename);
				ResultSet tmpRes = pr.executeQuery();
				if(tmpRes.next()) {
					Exercise e = new Exercise(exercisename,tmpRes.getString("beskrivelse"));
					exercises.add(e);
				}
				
			}
			groups.add(new ExerciseGroup(gruppenavn,exercises));
		}
		return groups;
	}
	
	
	///////////////////////////Kravspesifikasjon 5///////////////////////////
	
	
	//Skal hente ut hvor mange treningsøkter en har hatt totalt
	public static int getTotalWorkouts(Connection conn) throws SQLException {
		String stmt  = "select count(dato) as total from workout";
		PreparedStatement pr = conn.prepareStatement(stmt);
		ResultSet rs = pr.executeQuery();
		return rs.next() ? rs.getInt("total") : 0;
	}
	
	
	
}
