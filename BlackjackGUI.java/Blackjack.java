// This is the main program for the blackjack game.
public class Blackjack {
	// The main method should:
	//	- Ask the user how many people want to play (up to 3, not including the dealer).
	//	- Create an array of players.
	//	- Create a Blackjack window.
	// 	- Play rounds until the players want to quit the game.
	//	- Close the window.
	public static void main(String[] args){
		GIO.displayMessage("Welcome to BlackjackGUI");
		
		//define field for Player array creation
		int numPlayers;
		
		//ask for the amount of players w/ error checking. If it's valid, proceed to make the array of player objects.
		do { //error checking
			numPlayers = GIO.readInt("How many players are there (including the dealer, you can have 4 total)");
		} while (numPlayers > 4 || numPlayers < 2);
		
		Player[] players = new Player[numPlayers]; //Make the array of players
		
		//Enter everyone's starting money.
		int starting = GIO.readInt("Enter the starting amount for every player:");
		
		//Create the dealer
		String name = GIO.readString("Who is the dealer? Enter your name first:");
		Player dealer = new Player(name, true, starting);
		players[players.length-1] = dealer;
		
		//Fill in the info for the rest of the players.
		for (int i = 0; i < players.length-1; i++){
			name = GIO.readString("Player" + (i+1) + ", enter your name:");
			players[i] = new Player(name, false, starting);
		}

		//Create the window after the players are made.
		BlackjackWindow window = new BlackjackWindow(players);
		window.setVisible(true);
		
		//Go through the round after the window has been created.
		Blackjack.playRound(players, window);
	}

	// This method executes an single round of play (for all players).  It should:
	//	- Create and shuffle a deck of cards.
	//	- Start the round (deal cards) for each player, then the dealer.
	//	- Allow each player to play, then the dealer.
	//	- Finish the round (announce results) for each player.
	public static void playRound(Player[] players, BlackjackWindow window){
		//Create the deck
		Deck deck = new Deck();
		
		//Loop through this whole loop as long as there are 2 players in the game.
		boolean repeat = true;
		
		while (repeat == true){
			//Go through each player to setup the round.
			//run through the setup round for each player.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].startRound(deck, window);
				}
			}
			
			//run through the round for each player.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].playRound(deck, window);
				}
			}
			
			//Get the dealer's score for the finishRound method.
			int dealerScore = 0;
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					if (players[i].dealer == true){
						dealerScore = players[i].getHand().getScore();
					}
				}
			}
			
			//Cash everyone out according to dealer score.
			for (int i = 0; i < players.length; i++){
				if (players[i].getPlaying() == true){
					players[i].finishRound(dealerScore, window);
					Cashout(players[i].gethandWin(), players[i].getsplitWin(), players);
				}
			}
	
			//Ask everyone if they still want to play.
			//for loop is already implemented into these methods. Another loop not required.
			checkPlaying(players);
			repeat = stillInGame(players);
			
			//Special Case: If the dealer runs out of money before all players. All active players win.
			if (players[players.length-1].getMoney() <= 0) {
				GIO.displayMessage("You guys beat the dealer! Congratulations to the following players: ");
				
				//Loop to run through the names of players still in the game.
				for (int i = 0; i < players.length-1; i++){
					if (players[i].getPlaying() == true){
						GIO.displayMessage(players[i].getName());
					}
				}
				
				GIO.displayMessage("Here are each of your current amounts of cash:");
				
				//Loop to display each player's amount of money won/left.
				for (int i = 0; i < players.length-1; i++){
					if (players[i].getPlaying() == true){
						GIO.displayMessage(players[i].getName() + "'s Money: " + players[i].getMoney());
					}
				}
				
				//Time to close the program since the casino ran out of money. GG. :D
				System.exit(0);
			}
		}
		
		//Now, if there is one player remaining, announce their name and their value. Congrats!
		if (repeat == false){
			GIO.displayMessage("Congratulations " + players[1].getName() + ", you won!");
			GIO.displayMessage("Total Cash Remaining: " + players[1].getMoney());
			GIO.displayMessage("THANKS FOR PLAYING BLACKJACK!");
			return;
		}
	}
	
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
				players[i].setPlaying(GIO.readBoolean(players[i].getName() + ", would you like to play again?"));
			}
			
			else {
				GIO.displayMessage(players[i].getName() + ",you ran out of money! You are disqualified.");
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

}
