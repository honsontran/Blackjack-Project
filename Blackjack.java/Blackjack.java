public class Blackjack {

	public static void main (String[] args) {
		System.out.println("Welcome to BlackJack!");
		System.out.println("Created by Honson Tran for CS111 @ Rutgers University!");
		System.out.println("--------------------------------------------------------");
		
		//ask for the amount of players w/ error checking for usable amt and then create an array of that size
		int numPlayers;
		do {
			System.out.println("");
			System.out.println("How many players (including dealer) are going to be playing?");
			System.out.println("You are able to have 6 players max.");
			numPlayers = IO.readInt();
		} while (numPlayers > 6 || numPlayers < 1); 
		Player[] players = new Player[numPlayers];
		
		// declare the starting amount for every player
		System.out.println("--------------------------------------------------");
		System.out.println("Enter the starting amount for every player:");
		double starting = IO.readDouble();
		System.out.println("--------------------------------------------------");
		
		//declare dealer first and then store it at the end of players[].
		System.out.println("Who is the dealer? Enter your name first");
		String name = IO.readString();
		Player dealer = new Player(name, true, starting);
		players[players.length-1] = dealer;
		System.out.println("--------------------------------------------------");
		System.out.println("Great! Let's enter the player names now.");
		System.out.println("");
		
		BlackjackWindow GUI = new BlackjackWindow(players);
		
		// create name for person and creates object of person
		for (int i = 0; i < players.length-1; i++) {
			System.out.println("Player " + (i+1) + ", enter your name.");
			name = IO.readString();
			System.out.println("--------------------------------------------------");
			Player person = new Player(name, false, starting);
			players[i] = person;
		}
		
		//Create & Shuffle the deck
		Deck deck = new Deck();
				
		//Sys.out to announce start of game.
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		System.out.println("LET THE GAMES BEGIN.");
		System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		System.out.println("");
		
		//Loop through this whole loop as long as there are 2 players in the game.
		boolean repeat = true;
		
		while (repeat == true){
			//Go through each player to setup the round.
			//run through the setup round for each player.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].setupRound(deck);
					System.out.println("--------------------------------------------------");
				}
			}
			
			//run through the round for each player.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].startRound(deck);
				}
			}
			
			//Get the dealer's score for the endRound method.
			int dealerScore = 0;
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					if (players[i].dealer == true){
						dealerScore = players[i].getHand().addScore();
					}
				}
			}
			
			//Cash everyone out according to dealer score.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].endRound(dealerScore);
					Cashout(players[i].gethandWin(), players[i].getsplitWin(), players);
				}
			}
	
			//Ask everyone if they still want to play.
			//for loop is already implemented into these methods. Another loop not required.
			checkPlaying(players);
			repeat = stillInGame(players);
			
			//Special Case: If the dealer runs out of money before all players. All active players win.
			if (players[players.length-1].getMoney() <= 0) {
				System.out.println("Congratulations to the following players: ");
				
				//Loop to run through the names of players still in the game.
				for (int i = 0; i < players.length-1; i++){
					if (players[i].getPlaying() == true){
						System.out.println(players[i].getName());
					}
				}
				
				System.out.println("You guys beat the dealer!");
				System.out.println("");
				System.out.println("Here are each of your current amounts of cash:");
				
				//Loop to display each player's amount of money won/left.
				for (int i = 0; i < players.length-1; i++){
					if (players[i].getPlaying() == true){
						System.out.println(players[i].getName() + "'s Money: " + players[i].getMoney());
					}
				}
				
				//Time to close the program since the casino ran out of money. GG. :D
				System.exit(0);
			}
		}
		
		//Now, if there is one player remaining, announce their name and their value. Congrats!
		if (repeat == false){
			System.out.println("Congratulations " + players[1].getName() + ", you won!");
			System.out.println("Total Cash Remaining: " + players[1].getMoney());
			System.out.println("");
			System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
			System.out.println("THANKS FOR PLAYING BLACKJACK!");
			System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
		}
	} //ends main(String[] args)
	
	/* The methods were created to simplify the code inside main (String[] args).
	 * Any methods regarding rounds are found here.
	 */
	
	//Create a method that will calculate the bank amount after all bets have been taken.
	public static void Cashout(int handWin, int splitWin, Player[] players){
		double totalMoney = 0;
		
		for (int i = 0; i < players.length; i++){
			if (players[i].gethandWin() == 1){
				totalMoney -= players[i].getWager();
			}
			
			if (players[i].gethandWin() == 0){
				totalMoney += players[i].getWager();
			}
			
			if (players[i].getsplitWin() == 1){
				totalMoney -= players[i].getSplitWager();
			}
			
			if (players[i].getsplitWin() == 0){
				totalMoney += players[i].getSplitWager();
			}
		}
	}
	
	//Create a method that will remove a player if they don't want to play.
	public static void checkPlaying(Player[] players){
		//Ask all the players if they're playing or not.
		for (int i = 0; i < players.length-1; i++){
			if (players[i].getMoney() > 0){
				System.out.println(players[i].getName() + ", would you like to play again? (Y/N)");
				players[i].setPlaying(IO.readBoolean());
			}
			
			else {
				System.out.println(players[i].getName() + ",you ran out of money! You are disqualified.");
				players[i].setPlaying(false);
			}
		}
	}
	
	//This method checks if there is only 1 player left in the game.
	public static boolean stillInGame(Player[] players) {
		boolean stillPlaying = false;
		int playersLeft = 0;
		
		//Check if there's only one person left in the game. If there is, return false to stop BlackJack game.
		for (int i = 0; i < players.length; i++) {
			if (players[i].getPlaying() == true){
				playersLeft++;
			}
		}
		
		if (playersLeft == 1){
			stillPlaying = false;
			
		}
		else {
			for (int i = 0; i < players.length; i++) {
				if (players[i].getPlaying() == true){
					stillPlaying = true;
				}
			}
		}
		return stillPlaying;
	}

} //ends BlackJack class