package rpg;
import java.util.Random;

/**
 * Represents the computer player and holds its units in this role-playing game.
 */
public class ComputerPlayer {

  /**
   * Computer Unit 1: Criati
   */
  Unit criati;

  /**
   * Computer Unit 2: Ledde
   */
  Unit ledde;

  /**
   * Computer Unit 3: Tyllion
   */
  Unit tyllion;

  /**
   * A random number generator to be used for returning random levels and jobs.
   */
  Random random = new Random();

  /**
   * Constructs the computer player.
   */
  public ComputerPlayer(){
    this.criati = new Unit("Criati", this.generateLevel(), this.generateJob());
    this.ledde = new Unit("Ledde", this.generateLevel(), this.generateJob());
    this.tyllion = new Unit("Tyllion", this.generateLevel(), this.generateJob());
  }

  // Getters and Setters

  /**
   * Returns the criati Unit.
   * Note: This method does not take any parameters.
   * @return criati
   */
  public Unit getCriati() {
    return this.criati;
  }

  /**
   * Returns the ledde Unit.
   * Note: This method does not take any parameters.
   * @return ledde
   */
  public Unit getLedde() {
    return this.ledde;
  }

  /**
   * Returns the tyllion Unit.
   * Note: This method does not take any parameters.
   * @return tyllion
   */
  public Unit getTyllion() {
    return this.tyllion;
  }

  /**
   * Randomly chooses a string representing the level of a unit by generating a random integer.
   * There are three possible levels: low, medium, high.
   * Note: This method does not take any parameters.
   * @return String of the generated level of a computer's unit
   */
  private String generateLevel(){
    String generatedLevel;

    // generate a random integer from 0 to 2
    int randomInt = this.random.nextInt(3);

    // assign generatedLevel a level based on randomInt's value
    if(randomInt == 0){
      generatedLevel = "low";
    }
    else if(randomInt == 1){
      generatedLevel = "medium";
    }
    else{
      generatedLevel = "high";
    }

    return generatedLevel;
  }

  /**
   * Randomly chooses a string representing the job of a unit by generating a random integer.
   * There are three possible jobs: mage, knight, archer.
   * Note: This method does not take any parameters.
   * @return String of the generated job a computer's unit will take on
   */
  private String generateJob(){
    String generatedJob;

    // generate a random integer from 0 to 2
    int randomInt = this.random.nextInt(3);

    // assign generatedJob a level based on randomInt's value
    if(randomInt == 0){
      generatedJob = "mage";
    }
    else if(randomInt == 1){
      generatedJob = "knight";
    }
    else{
      generatedJob = "archer";
    }

    return generatedJob;
  }

  /**
   * Determines the strength of the attacker by comparing the attacker's job and the job of the target.
   * Mages are strong against knights, but weak against archers. Knights are strong against archers, but weak against mages.
   * There are three possible attacker strengths: same, strong, weak.
   * @param attacker Unit belonging to computer that is attacking the target
   * @param target Unit being attacked by the computer
   * @return String representing the strength of the attacker relative to the target
   */
  public String determineAttackerStrength(Unit attacker, Unit target){
    String determinedStrength;

    // assigns determinedStrength by comparing job of attacker with job of the target
    if(attacker.getJob().equalsIgnoreCase(target.getJob())){
      determinedStrength = "same";
    }
    else if((attacker.getJob().equalsIgnoreCase("knight") && target.getJob().equalsIgnoreCase("archer")) ||
            (attacker.getJob().equalsIgnoreCase("archer") && target.getJob().equalsIgnoreCase("mage")) ||
            (attacker.getJob().equalsIgnoreCase("mage") && target.getJob().equalsIgnoreCase("knight"))){
      determinedStrength = "strong";
    }
    else{
      determinedStrength = "weak";
    }

    return determinedStrength;
  }

  /**
   * Determines which alive human unit is the best to attack based on determined strengths and their current HP.
   * @param falia Unit belonging to the human player
   * @param erom Unit belonging to the human player
   * @param ama Unit belonging to the human player
   * @param unit Unit belonging to the computer that is currently taking its turn
   * @return Unit representing the human unit that is the optimal target
   */
  public Unit selectOptimalTarget(Unit falia, Unit erom, Unit ama, Unit unit){

    // store attacker strengths relative to potential human targets
    // these will help to determine the most optimal target for the given computer unit
    String attackerStrength1 = determineAttackerStrength(unit, falia);
    String attackerStrength2 = determineAttackerStrength(unit, erom);
    String attackerStrength3 = determineAttackerStrength(unit, ama);

    int bestHp = 0; // holds the best opponent HP in context of given attacker-target relationship
    Unit optimalTarget = null;

    // strong relationships: check which of the alive human units has the highest HP for the computer unit to attack
    if(attackerStrength1.equalsIgnoreCase("strong") && falia.getHp() > 0){
      bestHp = falia.getHp();
      optimalTarget = falia;
    }
    if(attackerStrength2.equalsIgnoreCase("strong") && erom.getHp() > bestHp && erom.getHp() > 0){
      bestHp = erom.getHp();
      optimalTarget = erom;
    }
    if(attackerStrength3.equalsIgnoreCase("strong") && ama.getHp() > bestHp && ama.getHp() > 0){
      bestHp = ama.getHp();
      optimalTarget = ama;
    }

    // return optimal target if the bestHp is greater than 0 when the attacking computer unit is strong
    // compared to one or more alive human units
    if(bestHp > 0){
      return optimalTarget;
    }

    // same relationships: check which of the alive human units has the highest HP
    // in this case, computer unit is the same against any alive human units
    if(attackerStrength1.equalsIgnoreCase("same") && falia.getHp() > 0){
      bestHp = falia.getHp();
      optimalTarget = falia;
    }
    if(attackerStrength2.equalsIgnoreCase("same") && erom.getHp() > bestHp && erom.getHp() > 0){
      bestHp = erom.getHp();
      optimalTarget = erom;
    }
    if(attackerStrength3.equalsIgnoreCase("same") && ama.getHp() > bestHp && ama.getHp() > 0){
      bestHp = ama.getHp();
      optimalTarget = ama;
    }

    // return optimal target if the bestHp is greater than 0 when the attacking computer unit's strength is the
    // same when compared to one or more alive human units
    if(bestHp > 0){
      return optimalTarget;
    }

    // weak relationships: check which of the alive human units has the lowest HP for the computer unit to attack
    // in this case, computer unit is weak against any alive human units

    bestHp = 100; // set boundary; all human units will have HP lower than or equal to 100

    if(attackerStrength1.equalsIgnoreCase("weak") && falia.getHp() > 0){
      bestHp = falia.getHp();
      optimalTarget = falia;
    }
    if(attackerStrength2.equalsIgnoreCase("weak") && erom.getHp() < bestHp && erom.getHp() > 0){
      bestHp = erom.getHp();
      optimalTarget = erom;
    }
    if(attackerStrength3.equalsIgnoreCase("weak") && ama.getHp() < bestHp && ama.getHp() > 0){
      optimalTarget = ama;
    }

    return optimalTarget;
  }

  /**
   * Counts the number of computer units that are currently alive (above 0 HP).
   * Note: This method does not take any parameters.
   * @return int representing the number of alive units
   */
  public int countAliveUnits(){
    int count = 0;

    // increment count for every unit with HP greater than 0
    if(this.criati.getHp() > 0){
      count++;
    }
    if(this.ledde.getHp() > 0){
      count++;
    }
    if(this.tyllion.getHp() > 0){
      count++;
    }

    return count;
  }

  /**
   * Calls unit's move based on the given move, unit playing and the unit being targeted.
   * The unit must either attack the target by dealing damage or block itself.
   * Note: This method does not return anything.
   * @param move String of either "attack" or "block"
   * @param unit Unit belonging to the computer that is taking its turn
   * @param target Unit belonging to human that the computer unit is targeting
   * @param attackerStrength String representing strength of unit relative to target
   */
  public void moveUnit(String move, Unit unit, Unit target, String attackerStrength){

    // call the correct move on the unit based on given move
    if(move.equalsIgnoreCase("attack")){

      // obtain damage to be dealt to target
      int damage = unit.attack(attackerStrength);

      // deal damage to target
      target.receiveDamage(damage);
    }else unit.block();
  }

  /**
   * Computer picks and performs unit's move based on viable targets' hp and its own stats. Called in main.
   * The computer's strategy finds an optimal target, considers the attacker's strength and takes
   * into account the number of blocks available.
   * Note: This method does not return anything.
   * @param falia: human unit 1 that a computer unit can potentially target
   * @param erom: human unit 2 that a computer unit can potentially target
   * @param ama: human Unit 3 that a computer unit can potentially target
   */
  public void strategy(Unit falia, Unit erom, Unit ama){

    // computer should attack at least once during its turn
    // acts as maximum block() allowance
    int block = countAliveUnits() - 1;
    // counter for current number of block moves taken this turn
    int blockCount = 0;
    // holds target that will be attacked by a computer unit
    Unit target;

    // Strategy if Computer Unit 1 is still alive
    if(this.criati.getHp() > 0) {
      target = selectOptimalTarget(falia, erom, ama, this.criati);
      // handles possible null target if all human units are knocked out
      if (target == null){
        System.out.println("Falia, Erom, and Ama have all fallen. They cannot be targetted.");
        return;
      }

      // determines criati's strength compared to the selected target
      String attackerStrength = determineAttackerStrength(this.criati, target);

      if (attackerStrength.equalsIgnoreCase("weak") && blockCount < block) {
        System.out.println("Criati is blocking; their defense temporarily increases for the next turn!");
        moveUnit("block", this.criati, null, attackerStrength);
        blockCount++;
      } else{
        System.out.println("Criati attacks ");
        moveUnit("attack", this.criati, target, attackerStrength);
        if(target.getHp() < 0){
          target.setHp(0);
        }
        System.out.println(target.name + " has " + target.getHp() + " remaining.");
      }
    }

    // strategy if Computer Unit 2 is still alive, works the same as unit 1
    if(this.ledde.getHp() > 0){
      target = selectOptimalTarget(falia, erom, ama, this.ledde);
      if (target == null){
        System.out.println("Falia, Erom, and Ama have all fallen. They cannot be targetted.");
        return;
      }

      // determines ledde's strength compared to the selected target
      String attackerStrength = determineAttackerStrength(this.ledde, target);

      if(attackerStrength.equalsIgnoreCase("weak") && blockCount < block){
        System.out.println("Ledde is blocking; their defense temporarily increases for the next turn!");
        moveUnit("block", this.ledde, null, attackerStrength);
        if(target.getHp() < 0){
          target.setHp(0);
        }
        System.out.println(target.name + " has " + target.getHp() + " remaining.");
        blockCount++;
      }else {
        System.out.println("Ledde attacks ");
        moveUnit("attack", this.ledde, target, attackerStrength);
        if(target.getHp() < 0){
          target.setHp(0);
        }
        System.out.println(target.name + " has " + target.getHp() + " remaining.");
      }
    }

    // strategy if Computer Unit 3 is still alive, works the same as unit 1 and 2; count is not updated
    if(this.tyllion.getHp() > 0){
      target = selectOptimalTarget(falia, erom, ama, this.tyllion);
      if (target == null){
        System.out.println("Falia, Erom, and Ama have all fallen. They cannot be targetted.");
        return;
      }

      // determines tyllion's strength compared to the selected target
      String attackerStrength = determineAttackerStrength(this.tyllion, target);

      if(attackerStrength.equalsIgnoreCase("weak") && blockCount < block){
        System.out.println("Tyllion is blocking; their defense temporarily increases for the next turn!");
        moveUnit("block", this.tyllion,null, attackerStrength);
      }else{
        System.out.println("Tyllion attacks ");
        moveUnit("attack", this.tyllion, target, attackerStrength);
        if(target.getHp() < 0){
          target.setHp(0);
        }
        System.out.println(target.name + " has " + target.getHp() + " remaining.");
      }
    }
  }

  /**
   * Resets temporary defensive buff of each computer unit by setting temporaryDefense back to 0.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void resetTemporaryDefense(){
    this.ledde.setTemporaryDefense(0);
    this.criati.setTemporaryDefense(0);
    this.tyllion.setTemporaryDefense(0);
  }

  /**
   * Determines if computer player has lost or is knocked out.
   * This is done by checking if all of its three units are knocked out.
   * Note: This method does not take any parameters.
   * @return boolean true if computer has no units left or false
   */
  public boolean isKnockedOut(){

    // return true if all computer units have 0 HP or less
    return this.criati.getHp() <= 0 && this.ledde.getHp() <= 0 && this.tyllion.getHp() <= 0;
  }
}