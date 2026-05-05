package rpg;

/**
 * This is a simplified version of a role-playing game.
 */
public class GameControl {

  /**
   * Creates a human player to play the game.
   */
  HumanPlayer human = new HumanPlayer();

  /**
   * Creates a computer player to play the game.
   */
  ComputerPlayer computer = new ComputerPlayer();

  /**
   * Prints the game's context and rules.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void printInstructions(){
    System.out.println();
    System.out.println("Welcome to the final battle! You will be facing off against the computer.");
    System.out.println("Each of you will have 3 units with randomly generated jobs and levels.");
    System.out.println("The jobs are: mage, knight, and archer. Archers are strong against mages, but weak against knights.");
    System.out.println("Mages are strong against knights, but weak against archers. Knights are strong against archers, but weak against mages.");
    System.out.println("There are two moves: attack (deal damage to one target) and block (temporarily increase defense).");
    System.out.println("Combat is turn based; all your live units will take a turn and then all the computer's live units will take a turn.");
    System.out.println("You have 10 turns to defeat the computer. If both players still have units standing, you only win ");
    System.out.println("if the combined HP of your units exceeds the computer's.");
    System.out.println();
  }
  
  /**
   * Prints the current status of all human units and all computer units.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void printStatus(){
    System.out.println();
    System.out.println("Your units:");
    this.human.getFalia().printCurrentStatus();
    this.human.getErom().printCurrentStatus();
    this.human.getAma().printCurrentStatus();
    System.out.println();
    System.out.println("Computer units:");
    this.computer.getCriati().printCurrentStatus();
    this.computer.getLedde().printCurrentStatus();
    this.computer.getTyllion().printCurrentStatus();
    System.out.println();
  }

  /**
   * Takes the human player's turn by calling moveUnit on each of the human player's three units: Falia, Erom, and Ama.
   * Prints the unit's job and level before moving it. Checks if there is no winner before proceeding to the next move.
   * If there is a winner between the first and second unit's turn or between the second and third unit's turn,
   * then return out of the method to end the human turn.
   * Resets any computer temporary defense after all human units have made their move.
   * Note: This method does not return anything.
   * @param round int representing the current round that the game is on.
   */
  public void takeHumanTurn(int round){

	  System.out.println("====================Human Turn====================");

	  // falia moves first
	  System.out.println(human.getFalia().getJob().toUpperCase() + " Falia (Lv: " + human.getFalia().getLevel() + ") is ready to act:");
	  human.moveUnit(human.getFalia(), computer);

	  // check for winner after Falia
	  if (getWinner(round) != null) return;

	  // erom moves second
	  System.out.println();
	  System.out.println(human.getErom().getJob().toUpperCase() + " Erom (Lv: " + human.getErom().getLevel() + ") is ready to act:");
	  human.moveUnit(human.getErom(), computer);

	  // check for winner after Erom
	  if (getWinner(round) != null) return;

	  // ama moves last
	  System.out.println();
	  System.out.println(human.getAma().getJob().toUpperCase() + " Ama (Lv: " + human.getAma().getLevel() + ") is ready to act:");
	  human.moveUnit(human.getAma(), computer);

	  // after all the human moves, reset computer temporary defense
	  computer.resetTemporaryDefense();

  }

  /**
   * Takes the computer player's turn and resets any human temporary defense after the computer has made its moves.
   * Note: This method does not take any parameters and does not return anything.
   */
  public void takeComputerTurn(){

	  System.out.println("====================Computer Turn====================");
//	  computer player takes turn
	  computer.strategy(human.getFalia(), human.getErom(), human.getAma());

	  // after computer makes move, remove any block bonuses on the human side
	  human.resetTemporaryDefense();

  }

  /**
   * Gets the winner of the game based on the round parameter and whether one of the players has been knocked out.
   * If the round is less than 11, return null if both players are alive, otherwise return the winner if the opposing player is knocked out.
   * If both players still have living units after 10 rounds, then the player with the greatest sum of HP wins, otherwise it is a tie.
   * @param round int representing the current round that the game is on.
   * @return String representing who won the game ("human" or "computer") or "tie" if there is a tie.
   * Return null if both players are still alive and the current round is less than 11.
   */
  public String getWinner(int round){
	  
	// in the first 10 rounds, only check if one side is fully defeated
	  if (round < 11) {

	    if (computer.isKnockedOut()) return "human";
	    if (human.isKnockedOut()) return "computer";

	    // no winner yet
	    return null;
	  }

	  // after 10 rounds, decide winner based on total remaining HP

	  int humanHp = human.getFalia().getHp() + human.getErom().getHp() + human.getAma().getHp();
	  int computerHp = computer.getCriati().getHp() + computer.getLedde().getHp() + computer.getTyllion().getHp();

	  if (humanHp > computerHp) return "human";
	  if (computerHp > humanHp) return "computer";

	  // same HP on both sides
	  return "tie";

  }

  /**
   * Creates an instance of GameControl and contains the flow of this role-playing game.
   * Note: This method does not return anything nor does it take any parameters.
   * @param args Not used.
   */
  public void runGameLoop(){

    //print the game instructions
	  printInstructions();


    //initialize a String variable to keep track of the winner
	  String winner = null;
    //initialize a boolean variable to keep track of whether someone has won within 10 rounds
	  boolean hasWinner = false;
			  
    /*
     *  Create a loop that runs 10 times or exits if there is a winner. In each iteration:
     *    - print the current round number
     *    - print the current status of all units. Hint: printStatus() is given to you in this class
     *    - take the human player's turn
     *    - check for a winner and update your String variable and boolean variable accordingly
     *    - print the current status of all units. Hint: printStatus() is given to you in this class
     *    - take the computer player's turn
     *    - check for a winner and update your String variable and boolean variable accordingly
     */
	  for (int round = 1; round <= 10 && !hasWinner; round++) {
		  System.out.println("Round " + round);
		  
//		  status at start of round
		  printStatus();
		  
//		  human turn
		  takeHumanTurn(round);
		  
//		  checking for winner
		  winner = getWinner(round);
		  	if (winner != null) {
		  		hasWinner = true;
		  		break;
		    }
//		  printing current status
		  printStatus();
		  
//		  computer turn
		  takeComputerTurn();
		  
//		  checking for winner
		  winner = getWinner(round);
		  	if (winner != null) {
		  		hasWinner = true;
		  		break;
	  }}

    //close the scanner used for human input
	  human.closeScanner();
    /*
     * If time has run out (all 10 rounds have been complete) without a winner, call getWinner to 
     * determine the winner based on combined HP of all units.
     */
	  if (winner == null) {
		  winner = getWinner(11);
	  }
		  
    //Print the end result of the game based on the winner
	  System.out.println("====================Results====================");
	  if (winner != null && winner.equalsIgnoreCase("human")) {
	    System.out.println("You've defeated the enemy!");
	  } else if (winner != null && winner.equalsIgnoreCase("computer")) {
	    System.out.println("All your heroes have been defeated, enemy forces have won!");
	  } else {
	    System.out.println("Nobody wins!");
	  }
  }

  /**
   * Runs the game
   * Note: This method does not return anything.
   * @param args Not used.
   */
  public static void main(String[] args){
    
    new GameControl().runGameLoop();

  }
}
