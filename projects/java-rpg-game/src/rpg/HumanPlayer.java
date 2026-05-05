package rpg;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents the human player and holds their units in this role-playing game.
 */
public class HumanPlayer {

  /**
   * Human Unit 1: Falia
   */
  Unit falia;

  /**
   * Human Unit 2: Erom
   */
  Unit erom;

  /**
   * Human Unit 3: Ama
   */
  Unit ama;

  /**
   * A random number generator to be used for returning random levels and jobs.
   */
  Random random = new Random();

  /**
   * A scanner to be used for selecting moves and targets.
   */
  Scanner scan = new Scanner(System.in);

  /**
   * Constructs a human player.
   */
  public HumanPlayer(){
    this.falia = new Unit("Falia", this.generateLevel(), this.generateJob());
    this.erom = new Unit("Erom", this.generateLevel(), this.generateJob());
    this.ama = new Unit("Ama", this.generateLevel(), this.generateJob());
  }

  // Getters and Setters

  /**
   * Returns the falia Unit.
   * Note: This method does not take any parameters.
   * @return falia
   */
  public Unit getFalia(){
    return this.falia;
  }

  /**
   * Returns the erom Unit.
   * Note: This method does not take any parameters.
   * @return erom
   */
  public Unit getErom() {
    return this.erom;
  }

  /**
   * Returns the ama Unit.
   * Note: This method does not take any parameters.
   * @return ama
   */
  public Unit getAma() {
    return this.ama;
  }

  /**
   * Closes the scanner
   */
  public void closeScanner(){
    this.scan.close();
  }

  /**
   * Randomly chooses a string representing the level of a unit by generating a random integer.
   * There are three possible levels: low, medium, high.
   * Note: This method does not take any parameters.
   * @return String of the generated level of a human's unit
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
   * @return String of the generated job a human's unit will take on
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
   * Checks if the user entered a valid move string, meaning it begins with one of the following letters: 'a' 'A' 'b' 'B'
   * Prints a friendly message to enter a valid input and returns null if the string is invalid. Does not reprompt is move is invalid.
   * @param move String representing the move to be performed by a human unit, for example, "attack" or "block"
   * @return String of "attack" or "block" or null
   */
  public String validateMove(String move){

	// make sure move isn't null or empty
	  if (move == null || move.length() == 0) {
	    System.out.println("please enter a valid move.");
	    return null;
	  }

	  // ignore uppercase vs lowercase
	  move = move.toLowerCase();

	  // check first letter
	  if (move.charAt(0) == 'a') return "attack";
	  if (move.charAt(0) == 'b') return "block";

	  // anything else is invalid
	  System.out.println("please enter a valid move.");
	  return null;

  }

  /**
   * Checks if the computer target selected by the human is alive and returns said target if it exists.
   * If the target with the given name is not alive or does not exist, print a message saying so and return null.
   * @param targetName String that should be the name of a computer unit
   * @param computer ComputerPlayer that the human is currently playing against
   * @return Unit representing the target belonging to the computer or null
   */
  public Unit selectTarget(String targetName, ComputerPlayer computer){

	// make sure input isn't null
	  if (targetName == null) {
	    System.out.println("please choose a valid target.");
	    return null;
	  }

	  targetName = targetName.toLowerCase();

	  // check criati
	  if (targetName.equals("criati")) {
	    if (computer.getCriati().getHp() > 0) return computer.getCriati();
	    System.out.println("that unit is already defeated.");
	    return null;
	  }

	  // check ledde
	  if (targetName.equals("ledde")) {
	    if (computer.getLedde().getHp() > 0) return computer.getLedde();
	    System.out.println("that unit is already defeated.");
	    return null;
	  }

	  // check tyllion
	  if (targetName.equals("tyllion")) {
	    if (computer.getTyllion().getHp() > 0) return computer.getTyllion();
	    System.out.println("that unit is already defeated.");
	    return null;
	  }

	  // invalid name
	  System.out.println("please pick criati, ledde, or tyllion.");
	  return null;
  }

  /**
   * Determines the strength of the attacker by comparing the attacker's job and the job of the target.
   * Mages are strong against knights, but weak against archers. Knights are strong against archers, but weak against mages.
   * There are three possible attacker strengths: same, strong, weak.
   * @param attacker Unit belonging to human that is attacking the target
   * @param target Unit belonging to computer that is being attacked by the human
   * @return String representing the strength of the attacker relative to the target
   */
  public String determineAttackerStrength(Unit attacker, Unit target){
    String determinedStrength;

    // assign determinedStrength by comparing job of attacker with job of the target
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
   * For the given unit if it is alive, allow human player to pick between attacking a target of their choosing or blocking.
   * This human unit will carry out the selected move during its turn.
   * Note: This method does not return anything.
   * @param unit Unit that is currently taking a turn
   * @param computer ComputerPlayer that human is playing against
   */
  public void moveUnit(Unit unit, ComputerPlayer computer){

	// print status before prompting
	  unit.printCurrentStatus();

	  // if unit is knocked out, it can't move
	  if (unit.getHp() <= 0) {
	    return;
	  }

	  // keep asking until a valid move is entered
	  String move = null;
	  while (move == null) {
	    System.out.println("choose a move (attack/block):");
	    String input = this.scan.nextLine();
	    move = validateMove(input);
	  }

	  // if player chose attack
	  if (move.equals("attack")) {

	    Unit target = null;

	    // keep asking until valid target is picked
	    while (target == null) {
	      System.out.println("choose a target (criati/ledde/tyllion):");
	      String targetInput = this.scan.nextLine();
	      target = selectTarget(targetInput, computer);
	    }

	    String attackerStrength = determineAttackerStrength(unit, target);
	    int damage = unit.attack(attackerStrength);
	    target.receiveDamage(damage);

	  } else {
	    unit.block();
	  }

  }

  /**
   * Resets temporary defensive buff of each human unit by setting temporaryDefense back to 0.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void resetTemporaryDefense(){
    this.erom.setTemporaryDefense(0);
    this.falia.setTemporaryDefense(0);
    this.ama.setTemporaryDefense(0);
  }

  /**
   * Determines if human player has lost or is knocked out.
   * This is done by checking if all of its three units are knocked out.
   * Note: This method does not take any parameters.
   * @return boolean true if human has no units left or false
   */
  public boolean isKnockedOut(){

    // return true if all human units have 0 HP or less
    return this.falia.getHp() <= 0 && this.erom.getHp() <= 0 && this.ama.getHp() <= 0;
  }
}