/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Gina Lu
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * Jessica Slaughter
 * jts3329
 * 16470
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	static List<Integer> moved = new java.util.ArrayList<Integer>();
	
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;

	/**
	 * This private method moves a critter in a specified direction
	 * for a specified number of steps.
	 * @param direction is the direction to be moved in (0-7)
	 * @param numSteps is the number of steps to take in the direction
	 */
	private final void moveCritter(int direction, int numSteps) {
		switch (direction) {
			case 0: this.x_coord = this.x_coord + numSteps;
					break;
			case 1: this.x_coord = this.x_coord + numSteps;
					this.y_coord = this.y_coord - numSteps;
					break;
			case 2: this.y_coord = this.y_coord - numSteps;
					break;
			case 3: this.x_coord = this.x_coord - numSteps;
					this.y_coord = this.y_coord - numSteps;
					break;
			case 4: this.x_coord = this.x_coord - numSteps;
					break;
			case 5: this.x_coord = this.x_coord - numSteps;
					this.y_coord = this.y_coord + numSteps;
					break;
			case 6: this.y_coord = this.y_coord + numSteps;
					break;
			case 7: this.x_coord = this.x_coord + numSteps;
					this.y_coord = this.y_coord + numSteps;
					break;
			default: break;
		}
		
		if (this.x_coord > Params.world_width - 1) { // relocate to left side
			this.x_coord = this.x_coord - Params.world_width;
		}
		else if (this.x_coord < 0) { // relocate to right side
			this.x_coord = this.x_coord + Params.world_width;
		}
		if (this.y_coord > Params.world_height - 1) { // relocate to top
			this.y_coord = this.y_coord - Params.world_height;
		}
		else if (this.y_coord < 0) { // relocate to bottom
			this.y_coord = this.y_coord + Params.world_height;
		}
	}
	
	private boolean hasMoved; // true if Critter has moved in this time step
	
	/**
	 * This method moves a Critter by one position in a specified direction.
	 * @param direction is the direction to move the Critter in
	 */
	protected final void walk(int direction) {
		this.energy = this.energy - Params.walk_energy_cost;
		if (hasMoved) {
			return;
		}
		this.moveCritter(direction, 1);
		hasMoved = true;
	}
	
	/**
	 * This method moves a Critter by two positions in a specified direction.
	 * @param direction is the direction to move the Critter in
	 */
	protected final void run(int direction) {
		this.energy = this.energy - Params.run_energy_cost;
		if (hasMoved) {
			return;
		}
		this.moveCritter(direction, 2);
		hasMoved = true;
	}
	
	/**
	 * This method determines whether a parent reproduces,
	 * and if so, sets the offspring's energy and position
	 * and halves the parent's energy.
	 * @param offspring is the new Critter
	 * @param direction is the direction the offspring should be placed from the parent
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy) {
			return; // not enough energy
		}
		offspring.energy = this.energy / 2; // round down
		this.energy = this.energy / 2 + this.energy % 2; // round up
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.moveCritter(direction, 1); // move offspring adjacent to parent
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try{
			//need to check input first
			Critter c = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
			//add to the collection
			Critter.population.add(c);
			//energy
			c.energy=Params.start_energy;
			//random position
			c.x_coord = Critter.getRandomInt(Params.world_width);
			c.y_coord = Critter.getRandomInt(Params.world_height);
		}
		catch(ClassNotFoundException|InstantiationException|IllegalAccessException exception){
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> critClass = null;
		try {
			critClass = Class.forName(myPackage + "." + critter_class_name);
		}
		catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		for (Critter crit : population) {
			if (critClass.isInstance(crit)) {
				result.add(crit);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
	}
	
	public static void worldTimeStep() {
		for(int crit = 0; crit < population.size(); crit+=1){
			population.get(crit).doTimeStep();	//do time step for all critters
		}
		for(int crit = 0; crit < population.size(); crit+=1){
			population.get(crit).energy -= Params.rest_energy_cost;
			if(population.get(crit).energy <= 0){
				population.remove(crit);		//delete dead critters
			}
		}
		moved.clear();
		for(int crit = 0; crit < population.size()-1; crit+=1){
			int Ax=population.get(crit).x_coord;
			int Ay=population.get(crit).y_coord;
			for(int compare = crit+1; compare < population.size(); compare+=1){
				int Bx=population.get(compare).x_coord;
				int By=population.get(compare).y_coord;
				if((Ax == Bx) && (Ay == By)){
					Boolean Afight = population.get(crit).fight(population.get(compare).toString());
					Boolean Bfight = population.get(compare).fight(population.get(crit).toString());
					if(!((Ax ==population.get(crit).x_coord)&&(Ay ==population.get(crit).y_coord)
							&&(Bx ==population.get(compare).x_coord)&&(By ==population.get(compare).y_coord))){		//someone's position changed
						if(!(Ax == population.get(crit).x_coord)&&(Ay == population.get(crit).y_coord)){		//A changed
							moved.add(crit);
							break;		//go to the next critter and add to the moved critters to check later
						}
						if(!(Bx == population.get(compare).x_coord)&&(By == population.get(compare).y_coord)){		//B changed
							moved.add(compare);
						}
					}
					else{		//still in the same position so they need to fight
						Critter dead = encounter(population.get(crit), Afight, population.get(compare), Bfight);
						if(dead == population.get(crit)){population.remove(crit);}
						else{population.remove(compare);}
					}
				}
			}
		}
		int movedIncr = 0;
		while(moved.size() > movedIncr){
			int Ax=population.get(moved.get(movedIncr)).x_coord;
			int Ay=population.get(moved.get(movedIncr)).y_coord;
			for(int compare = 0; compare < population.size(); compare+=1){
				if(compare!=moved.get(movedIncr)){		//compare with all the other critters
					int Bx=population.get(compare).x_coord;
					int By=population.get(compare).y_coord;
					if((Ax == Bx) && (Ay == By)){		//if A and B are in the same space, then invoke fight()
						Boolean Afight = population.get(moved.get(movedIncr)).fight(population.get(compare).toString());
						Boolean Bfight = population.get(compare).fight(population.get(moved.get(movedIncr)).toString());
						if(!((Ax ==population.get(moved.get(movedIncr)).x_coord)&&(Ay ==population.get(moved.get(movedIncr)).y_coord)
								&&(Bx ==population.get(compare).x_coord)&&(By ==population.get(compare).y_coord))){		//someone's position changed
							if(!(Ax == population.get(moved.get(movedIncr)).x_coord)&&(Ay == population.get(moved.get(movedIncr)).y_coord)){		//A changed
								moved.add(moved.get(movedIncr));
								break;		//go to the next critter and add to the moved critters to check later
							}
							if(!(Bx == population.get(compare).x_coord)&&(By == population.get(compare).y_coord)){		//B changed
								moved.add(compare);
							}
						}
						else{		//still in the same position so they need to fight
							Critter dead = encounter(population.get(moved.get(movedIncr)), Afight, population.get(compare), Bfight);
							if(dead == population.get(moved.get(movedIncr))){population.remove(moved.get(movedIncr));}
							else{population.remove(compare);}
						}
					}
				}
			}
			movedIncr+=1;
		}
		population.addAll(babies);
		babies.clear();
		for (Critter crit : population) {
			crit.hasMoved = false; // reset movement tracker
		}
	}
	
	/**
	 * This method prints out a 2-D grid of the simulated world.
	 */
	public static void displayWorld() {
		// top border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		
		// rows
		for (int y = 0; y < Params.world_height; y++) {
			System.out.print("|");
			for (int x = 0; x < Params.world_width; x++) {
				String space = " ";
				for (Critter crit : population) {
					if (crit.x_coord == x && crit.y_coord == y) { // position occupied
						System.out.print(crit.toString());
						space = "";
						break;
					}
				}
				System.out.print(space);
			}
			System.out.println("|");
		}
		
		// bottom border
		System.out.print("+");
		for (int i = 0; i < Params.world_width; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
	
	/**
	 * This method happens when A and B are still in the same place after fight()
	 */
	public static Critter encounter(Critter A, Boolean Afight, Critter B, Boolean Bfight){		//A is either from query or slower index and return the dead
		int[] result = new int[2];
		if(!Afight){		//A doesn't want to fight
			result[0] = 0;
		}
		else{		//A wants to fight, get random number
			result[0] = rand.nextInt(A.energy);
		}
		if(!Bfight){		//B doesn't want to fight
			result[1] = 0;
		}
		else{		//B wants to fight, get random number
			result[1] = rand.nextInt(B.energy);
		}
		if(result[0] > result[1]){		//A wins
			A.energy += (B.energy/2);
			return B;
		}
		else{		//B wins
			B.energy += (A.energy/2);
			return A;
		}
	}
	
}
