import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExerciseGroup {
	
	private String name;
	private List<Exercise> exercises;
	
	public ExerciseGroup(String name,Collection<Exercise> exercises) {
		this.name = name;
		this.exercises = new ArrayList<Exercise>(exercises);
	}
	
	public String getName() {
		return name;
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}
}
