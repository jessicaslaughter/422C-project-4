package assignment4;

/**
 * This Critter maintains an age count, increasing every time step. Once the Critter is over 60 steps old,
 * it will only walk during time steps and fights. Otherwise, if their age is an even number, it 
 * will run, and if their age is an odd number, it will reproduce. Then, it will change its direction
 * based on its age to one of the diagonals. It will always fight if it is 60 or younger.
 * @author jessicaslaughter
 *
 */
public class Critter2 extends Critter {
	
	@Override
	public String toString() { return "2"; }
	
	private int dir;
	private int age;
	
	@Override
	public void doTimeStep() {
		if (age > 60) {
			walk(dir);
		}
		else if (age % 2 == 0) {
			run(dir);
		}
		else {
			Critter2 baby = new Critter2();
			reproduce(baby, Critter.getRandomInt(age % 8));
		}
		
		switch (age % 4) {
			case 0: dir = 1;
			case 1: dir = 3;
			case 2: dir = 5;
			case 3: dir = 7;
		}
		age++;
	}

	@Override
	public boolean fight(String opponent) {
		if (age > 60) {
			walk(dir);
			return false; // gettin old, don't want to fight
		}
		return true;
	}
	
	public static void runStats(java.util.List<Critter> twos) {
		int avgAge = 0;
		int maxAge = 0;
		int minAge = -1;
		for (Object obj : twos) {
			Critter2 c = (Critter2) obj;
			if (minAge == -1) {
				minAge = c.age;
			}
			avgAge += c.age;
			if (c.age < minAge) {
				minAge = c.age;
			}
			if (c.age > maxAge) {
				maxAge = c.age;
			}
		}
		avgAge /= twos.size();
		System.out.print("" + twos.size() + " total Critter2s    ");
		System.out.print("Average age: " + avgAge + "    ");
		System.out.print("Max age: " + maxAge + "    ");
		System.out.println("Min age: " + minAge + "    ");
	}

}