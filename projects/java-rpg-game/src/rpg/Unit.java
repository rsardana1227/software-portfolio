package rpg;
import java.util.Random;

/**
 * Represents a unit that can belong to either the computer or human player.
 */
public class Unit {

  /**
   * The unit's name.
   */
  String name;

  /**
   * The unit's level, which is randomly generated based on a given level range.
   */
  public int level = 0;

  /**
   * The unit's job, such as mage, knight or archer.
   */
  private String job;

  /**
   * The unit's current health points or HP.
   */
  private int hp = 0;

  /**
   * The unit's attack stat, which is determined by level and impacts damage dealt to a target.
   */
  public int attack = 0;

  /**
   * The unit's defense stat, which is determined by level and impacts damage received by this unit.
   */
  private int defense = 0;

  /**
   * The unit's temporary defense stat, which is granted by the "block" move.
   */
  public int temporaryDefense = 0;

  /**
   * The unit's evasion stat, which is determined by level and impacts whether this unit dodges the incoming attack.
   */
  private int evasion = 0;

  /**
   * A random number generator to be used in this class.
   */
  Random random = new Random();

  /**
   * Constructs a unit by assigning the given name and job as well as calculating other stats.
   * The level must be randomly generated given the level range that is passed.
   * @param name String representing the name of this unit
   * @param levelRange String representing the level range of this unit, such as low, medium or high
   * @param job String representing the job of this unit
   */
  public Unit(String name, String levelRange, String job){

    //assign name and job to this unit based on given parameters
	  this.name = name;
	  this.job = job;
    /*
     * randomly assign this unit's level based on the given levelRange
     *   - "low" means the level can be set to an int from 1 to 3
     *   - "medium" means the level can bes et to an int from 4 to 6
     *   - "high" means the level can be set to an int from 7 to 10
     */
	  if (levelRange.equalsIgnoreCase("low")) {
		  this.level = this.random.nextInt(3) + 1;
		} else if (levelRange.equalsIgnoreCase("medium")) {
		  this.level = this.random.nextInt(3) + 4;
		} else {
		  this.level = this.random.nextInt(4) + 7;
		}
    /*
     * calculate and assign this unit's stats of hp, attack, defense and evasion.
     *  Calculate and set the multiplier variable to a tenth of the level. Ex: (this.level / 10.0)
     *  Multiply the calculated multiplier with the stats' specific max value, which are given below.
     *  - max HP value: 100
     *  - max attack value: 20
     *  - max defense: 20
     *  - max evasion: 5
     * Hint: avoid integer division by using floats and round the final value to an integer
     * and set the rounded value to this unit's stat (Ex: this.hp)
     */
	  double multiplier = this.level / 10.0;

	  this.hp = (int) Math.round(multiplier * 100);
	  this.attack = (int) Math.round(multiplier * 20);
	  this.defense = (int) Math.round(multiplier * 20);
	  this.evasion = (int) Math.round(multiplier * 5);

  }

  // Getters and Setters

  /**
   * Returns this unit's level.
   * Note: This method does not take any parameters.
   * @return level
   */
  public int getLevel() {
//return unit level
	  return this.level;

  }

  /**
   * Returns this unit's job.
   * Note: This method does not take any parameters.
   * @return job
   */
  public String getJob() {
//return unit job
    return this.job;

  }

  /**
   * Returns this unit's hp.
   * Note: This method does not take any parameters.
   * @return hp
   */
  public int getHp() {
//replace current hp
	  return this.hp;
  }

  /**
   * Sets this unit's hp stat to the given hp.
   * Note: This method does not return anything.
   * @param hp int representing given health points value
   */
  public void setHp(int hp) {
//update hp
	  this.hp = hp;
    
  }

  /**
   * Sets this unit's temporary defense stat to the given temporary defense.
   * Note: This method does not return anything.
   * @param temporaryDefense int representing given temporary defense value
   */
  public void setTemporaryDefense(int temporaryDefense) {
//set temporary defense value
	  this.temporaryDefense = temporaryDefense;
  }

  /**
   * DO NOT MODIFY, REQUIRED FOR TESTING
   *
   * Returns this unit's defense stat.
   * Note: This method does not take any parameters.
   * @return defense
   */
  public int getDefense() {
    return this.defense;
  }

  /**
   * DO NOT MODIFY, REQUIRED FOR TESTING
   *
   * Sets this unit's evasion stat to the given evasion value.
   * Note: This method does not return anything.
   * @param evasion int representing given value for evasion
   */
  public void setEvasion(int evasion) {
    this.evasion = evasion;
  }

  /**
   * Prints the unit's name, level, job, and remaining HP.
   * If the unit has no remaining hp, prints that this unit is knocked out.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void printCurrentStatus(){

	// print status if alive (hp>0), otherwise show knocked out
	  if (this.hp > 0) {
	    System.out.println(this.name + " is a level " + this.level + " " + this.job + " with " + this.hp + " HP remaining.");
	  } else {
	    System.out.println(this.name + " is knocked out and cannot move.");
	  }

  }

  /**
   * Calculates damage based on this unit's attack stat, maximum attack, and attacker strength relative to target.
   * @param attackerStrength String representing the attacker's strength relative to the target
   * @return int representing the total damage this unit will deal when attacking
   */
  public int attack(String attackerStrength){

	  double multiplier = 1.0;
    /*
     * check the value of attackerStrength to determine the multiplier which will be applied to the damage
     *  later in this method
     *   - "same" results in an unchanged multiplier of 1.0
     *   - "strong" results in a multiplier that will increase the damage by 20%
     *   - "weak" results in a multiplier that halves damage or decreases by 50%
     */
	  if (attackerStrength.equalsIgnoreCase("strong")) {
		  multiplier = 1.2;
		} else if (attackerStrength.equalsIgnoreCase("weak")) {
		  multiplier = 0.5;
		}

    //assign attackMax variable to the maximum attack value of 50.0
    //get the raw damage by dividing this unit's attack stat by 30.0 then multiplying this by the max attack value
	  double attackMax = 50.0;
	  double rawDamage = (this.attack / 30.0) * attackMax;
	  
    //multiply the raw damage by the multiplier variable you assigned above
    // Hint: make sure to round and convert to an integer before returning the result
	  return (int) Math.round(rawDamage * multiplier);

  }

  /**
   * Provides temporary defensive buff to reduce damage taken during the current turn.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void block(){
	// blocking increases defense temporarily
	  this.temporaryDefense += 2;

  }

  /**
   * Uses this unit's evasion, temporaryDefense, and defense stats to either dodge the attack or adjust damage.
   * If the attack is not dodged, applies the adjusted damage to this unit's remaining HP.
   * Prints a message containing the damage received and the remaining HP.
   * Note: This method does not return anything.
   * @param damage int representing the incoming damage from an opposing unit
   */
  public void receiveDamage(int damage){

    
    /*
     * perform evasion check to determine whether this unit will take damage:
     *   - generate a random number between 0 and 20
     *   - if the number is less than or equal to this unit's evasion stat, the unit will dodge the attack
     *   - if the attack is dodged, print a message and return out of this method
     */
	  
	  // only roll for dodge if evasion is > 0
	  if (this.evasion > 0) {
		    int roll = this.random.nextInt(21);

		    if (roll <= this.evasion) {
		      System.out.println(this.name + " dodged!");
		      return;
		    }
		  }
    //calculate the defense adjustment by adding this unit's defensive stats (temporary and normal defense)
    // divide this sum by 10.0
	  
	  double defenseAdjustment = (this.temporaryDefense + this.defense) / 10.0;


    //calculate the actual damage received by dividing the given damage value by the calculated defense adjustment
	  int damageReceived = (int) Math.round(damage / defenseAdjustment);

    //update this unit's HP stat by subtracting the damage
	  this.hp -= damageReceived;

    //set this unit's HP to 0 if the unit's remaining HP has dropped below 0
	  if (this.hp < 0) {
		  this.hp = 0;
		}
    //print how much damage was received and the remaning HP
	  System.out.println(this.name + " takes " + damageReceived + " damage. Remaining HP: " + this.hp);

  }
}