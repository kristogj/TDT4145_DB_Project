
public class Exercise {
	
	private String name, description, machine, machineDescription;
	private int kg,sets;
	
	
	
	public Exercise(String name, int kg, int sets) {
		this.name = name;
		this.kg = kg;
		this.sets = sets;
	}
	
	public Exercise(String name, String beskrivelse) {
		this.name = name;
		this.description = beskrivelse;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setMachine(String machine) {
		this.machine = machine;
	}
	
	public void setMachineDescription(String machineDescription) {
		this.machineDescription = machineDescription;
	}
	
	

}
