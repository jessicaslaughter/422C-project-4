/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Gina Lu
 * gbl286
 * 16480
 * Jessica Slaughter
 * jts3329
 * 16470
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

/**
 * This Critter likes to make friends and not fight. 
 * If it has energy > 50, it'll walk. Otherwise it'll only walk when it makes a friend
 * When this Critter stumbles into another Critter, it doesn't fight
 * @author ginalu
 *
 */
public class Critter4 extends Critter {
	
	@Override
	public String toString() { return "4"; }
	private int dir;
	private int datesFour;
	private int datesElse;
	
	public Critter4() {
		dir = Critter.getRandomInt(8);
		datesFour = 0;
		datesElse = 0;
	}

	@Override
	public void doTimeStep() {
		dir = getRandomInt(8);
		if(this.getEnergy() > 50){
			walk(dir);
		}
	}

	public boolean fight(String opponent) {
		if(opponent == "4"){
			datesFour+=1;
			walk(dir);
			return false;
		}
		else if(opponent != "@"){
			datesElse+=1;
			walk(dir);
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void runStats(java.util.List<Critter> four) {
		int avgDateFour = 0;
		int totalDateFour = 0;
		int avgDateElse = 0;
		int totalDateElse = 0;
		int totalDates = 0;
		for (Object obj : four) {
			Critter4 c = (Critter4) obj;
			totalDateElse += c.datesElse;
			totalDateFour += c.datesFour;
			totalDates += (c.datesElse + c.datesFour);
		}
		if(four.size() != 0){
			avgDateFour = totalDateFour / four.size();
			avgDateElse = totalDateElse / four.size();
		}
		System.out.print("" + four.size() + " total Critter4s    ");
		System.out.print("Total Dates: " + totalDates + "    ");
		System.out.print("Average Dates with other Critter4s: " + avgDateFour + "    ");
		System.out.println("Average Dates with other Critters: " + avgDateElse + "    ");
	}
	
}
