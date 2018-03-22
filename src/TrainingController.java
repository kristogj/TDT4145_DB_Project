import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.html.HTMLDocument.RunElement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class TrainingController{
	
	@FXML
	Button registerWorkoutButton, registerExerciseButton, registerMachineButton, registerExcersiceGroupButton,
	registerExerciseInGroupButton, registerExerciseInWorkoutButton, registerExerciseOnMachineButton,
	getExerciseResultsButton;
	
	@FXML
	TextField registerWorkoutField, registerExerciseField, registerMachineField, registerExerciseGroupField,
	registerExerciseInGroupField,registerExerciseInWorkoutField, registerExerciseOnMachineField,
	getNLastWorkoutsField, getExerciseResultsField;
	
	@FXML
	TextArea textArea;
	
	Connection myConn;
	
	
	public void initialize() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		this.myConn = DBConnection.getDBConnection();
	}	
	
	
	//register workout to databse
	@FXML
	public void registerWorkout() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerWorkoutField.getText().split(","));
			List<String> dateString = Arrays.asList(input.get(0).split("-"));
			int year = Integer.parseInt(dateString.get(0));
			int month = Integer.parseInt(dateString.get(1));
			int day = Integer.parseInt(dateString.get(2));
			Date date = new Date(year - 1900, month - 1, day);
			Time time = Time.valueOf(input.get(1));
			int duration = Integer.parseInt(input.get(2));
			int personligForm = Integer.parseInt(input.get(3));
			int prestasjon = Integer.parseInt(input.get(4));
			String notat = input.get(5);
			AdminController.insertWorkout(myConn, date, time, duration, personligForm, prestasjon, notat);
			textArea.setText("Workout added");
		} catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
		
	
	}
	
	
	@FXML
	public void registerExercise() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerExerciseField.getText().split(","));
			String navn = input.get(0);
			String beskrivelse = input.get(1);
			AdminController.insertExercise(myConn, navn, beskrivelse);
			textArea.setText("Exercise added");
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
	}
	
	@FXML
	public void registerMachine() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerMachineField.getText().split(","));
			String navn = input.get(0);
			String beskrivelse = input.get(1);
			AdminController.insertMachine(myConn, navn, beskrivelse);
			textArea.setText("Machine added");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
		
		
	}
	
	
	@FXML
	public void registerExerciseGroup() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerExerciseGroupField.getText().split(","));
			String navn = input.get(0);
			AdminController.insertExerciseGroup(myConn, navn);
			textArea.setText("ExerciseGroup added");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
		
	}
	
	
	@FXML
	public void registerExerciseInGroup() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerExerciseInGroupField.getText().split(","));
			String groupName = input.get(0);
			String exerciseName = input.get(1);
			AdminController.insertGroupContainsExercise(myConn, groupName, exerciseName);
			textArea.setText("Exercise added to exercise group.");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
		
	}
	
	
	@FXML
	public void registerExerciseInWorkout() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerExerciseInWorkoutField.getText().split(","));
			List<String> dateString = Arrays.asList(input.get(0).split("-"));
			int year = Integer.parseInt(dateString.get(0));
			int month = Integer.parseInt(dateString.get(1));
			int day = Integer.parseInt(dateString.get(2));
			Date workoutDate = new Date(year - 1900, month - 1, day);
			String exerciseName = input.get(1).replaceAll("\\s", "");
			int antallKilo = Integer.parseInt(input.get(2));
			int antallSet = Integer.parseInt(input.get(3));
			AdminController.insertWorkoutContainsExercise(myConn, workoutDate, exerciseName, antallKilo, antallSet);
			textArea.setText("Exercise was added to this workout");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
	}
	
	
	@FXML
	public void registerExerciseOnMachine() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<String> input = Arrays.asList(registerExerciseOnMachineField.getText().split(","));
			String exerciseName = input.get(0);
			String machineName = input.get(1);
			AdminController.insertExerciseOnMachine(myConn, exerciseName, machineName);
			textArea.setText("Exercise was added to the machine");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
	}

	
	@FXML
	public void getExerciseGroups() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		try {
			List<ExerciseGroup> groups = AdminController.getExerciseGroups(myConn);
			String result = "Exercise Group \t Exercise\n";
			
			for(ExerciseGroup group : groups) {
				for(Exercise exercise : group.getExercises()) {
					result+= group.getName() + "\t\t\t" + exercise.getName() + "\n";
				}
			}
			textArea.setText(result);
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
	}
	
	
	@FXML
	public void getNLastWorkouts() throws NumberFormatException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		try {
			List<Workout> workouts = AdminController.getNWorkouts(myConn, Integer.parseInt(getNLastWorkoutsField.getText()));
			String result = "Date \t\t tidspunkt \t varighet \t Form \t Prestasjon \t Notat\n";
			
			for(Workout workout : workouts) {
				result += workout.getDato().toString() + "\t";
				result += workout.getTidspunkt().toString() + "            ";
				result += workout.getVarighet() + "\t\t  ";
				result += workout.getPersonligForm() + "\t\t ";
				result += workout.getPrestasjon() + "\t\t\t";
				result += workout.getNotat() + "\n";
			}
			
			textArea.setText(result);
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
		
	}
	
	
	public void getTotalWorkouts() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try {
			int totalWorkouts = AdminController.getTotalWorkouts(myConn);
			textArea.setText(totalWorkouts + "");
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
		
	}
	
	
	public void getExerciseResult() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try {
			List<String> input = Arrays.asList(getExerciseResultsField.getText().split(","));
			List<String> startDate = Arrays.asList(input.get(0).split("-"));
			List<String> endDate = Arrays.asList(input.get(1).split("-"));
			int startYear = Integer.parseInt(startDate.get(0));
			int startMonth = Integer.parseInt(startDate.get(1));
			int startDay = Integer.parseInt(startDate.get(2));
			Date dateStart = new Date(startYear, startMonth, startDay);
			int endYear = Integer.parseInt(endDate.get(0));
			int endMonth = Integer.parseInt(endDate.get(1));
			int endDay = Integer.parseInt(endDate.get(2));
			Date dateEnd = new Date(endYear, endMonth, endDay);
			String result = AdminController.getExerciseResult(myConn, dateStart, dateEnd);
			textArea.setText(result);
			
		}catch (RuntimeException e) {
			textArea.setText("Error: Key is already taken or you wrote unvalid data");
		}
	}
	
}
