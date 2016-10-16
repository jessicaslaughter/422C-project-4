package assignment4;

/**
 * This Critter always eats algae, but will only fight other Critters if it has
 * energy greater than 100. If it's close to dying (energy < 10), it will run for food.
 * Otherwise, it will either walk (energy < 100) or reproduce if it has a lot
 * of energy (energy >= 100). After each time step, this Critter will change direction
 * by one counterclockwise turn.
 * 
 * @author jessicaslaughter
 *
 */
public class Critter1 extends Critter {
	
	@Override
	public String toString() { return "1"; }
	
	private int dir;
	
	public Critter1() {
		dir = Critter.getRandomInt(8);
	}

	@Override
	public void doTimeStep() {
		if (getEnergy() < 10) {
			run(dir); // need some food
		}
		else if (getEnergy() >= 10 && getEnergy() < 100) {
			walk(dir); // not in a huge rush to get food
		}
		else {
			walk(dir);
			Critter1 baby = new Critter1();
			reproduce(baby, Critter.getRandomInt(8));
		}
		
		// new direction that loops counterclockwise
		if (dir + 1 > 7) {
			dir = 0;
		}
		else {
			dir++;
		}
	}

	@Override
	public boolean fight(String opponent) {
		if (opponent.equals("@")) {
			return true; // always eat algae
		}
		if (getEnergy() > 100) {
			return true; // enough energy, fight
		}
		else if (getEnergy() > 30) {
			run(dir); // not a ton of energy, run away
			return false;
		}
		else {
			walk(dir); // dyin, try walking away
		}
		return true;
	}
	
}
