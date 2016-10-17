package assignment4;

/**
 * When faced with food, it eats it.
 * When faced with another Critter 3, it reproduces with the Critter 3 and runs away
 * When faced with another critter, it just runs away
 * @author ginalu
 *
 */
public class Critter3 extends Critter {
	
	@Override
	public String toString() { return "3"; }
	private int dir;
	private int reproducedNum;
	
	public Critter3() {
		dir = Critter.getRandomInt(8);
		reproducedNum = 0;
	}

	@Override
	public void doTimeStep() {
		dir = getRandomInt(8);
	}

	public boolean fight(String opponent) {
		if(opponent == "@"){
			return true;
		}
		if(opponent == "3"){
			Critter3 baby = new Critter3();
			reproduce(baby, Critter.getRandomInt(8));
			reproducedNum+=1;
		}
		run(dir);
		return false;
	}
	
	public static void runStats(java.util.List<Critter> three) {
		int maxReproduce = 0;
		int avgReproduce = 0;
		int totalReproduce = 0;
		int minReproduce = ((Critter3)(three.get(0))).reproducedNum;
		for (Object obj : three) {
			Critter3 c = (Critter3) obj;
			if (c.reproducedNum > maxReproduce) {
				maxReproduce = c.reproducedNum;
			}
			if(c.reproducedNum < minReproduce){
				minReproduce = c.reproducedNum;
			}
			totalReproduce += c.reproducedNum;
		}
		if(three.size() != 0){
			avgReproduce = totalReproduce / three.size();
		}
		
		System.out.print("" + three.size() + " total Critter3s    ");
		System.out.print("Average Reproductions per Critter: " + avgReproduce + "    ");
		System.out.print("Max Reproductions per Critter: " + maxReproduce + "    ");
		System.out.println("Min Reproductions per Critter: " + minReproduce + "    ");
	}
	
}