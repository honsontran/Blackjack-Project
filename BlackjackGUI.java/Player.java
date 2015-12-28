// This class represents one blackjack player (or the dealer)
public class Player
{
	// define fields here
	//define fields
	private String name;
	private double money;
	private double wager;
	public boolean dealer;
	private int handWin; // win = 1, lose = 0, push = -1
	private int splitWin; // win = 1, lose = 0, push = -1
	private Hand hand;
	private boolean canSplit; //has the potential to split.
	private boolean yesSplit; //player's decision to split.
	private Hand splitHand; //split hand only applies when boolean split is true.
	private double splitWager;
	private double insurance;
	private boolean canInsure = false; //Insurance is false until dealer gets an Ace.
	private boolean yesInsure; //boolean to keep track if player committed to insuring.
	private boolean confirmInsurance; //boolean to use if dealer does hit BlackJack
	private boolean playing; // use when trying to find out if player is still playing or not.
	
	static int counter = 0; //for method counter and Hit. Static because it's one counter in game.

	// This constructor creates a player.
	// If isDealer is true, this Player object represents the dealer.
	public Player(String playerName, boolean isDealer, int money) {
		this.name = playerName;
		this.money = money;
		this.wager = 0;
		this.dealer = isDealer;
		this.handWin = 0;
		this.splitWin = 0;
		this.hand = new Hand();
		this.canSplit = false;
		this.yesSplit = false;
		this.splitHand = new Hand();
		this.splitWager = 0;
		this.yesInsure = false;
		this.insurance = 0;
		this.playing = true;
	}

	//create a method to fetch their name.
	public String getName(){
		return this.name;
	}
	
	//create a method to see if they are the dealer.
	public boolean dealer(){
		return this.dealer;
	}
	
	//create a method to set the dealer.
	public void setDealer(){
		this.dealer = true;
	}
	
	//create a method to get player's money left.
	public double getMoney(){
		return this.money;
	}
	
	//create a method that will display the player's wager.
	public double getWager(){
		return this.wager;
	}
	
	//create a method that will display the player's split wager.
	public double getSplitWager(){
		return this.splitWager;
	}
	
	//create a method that will return a player's hand.
	public Hand getHand(){
		return this.hand;
	}
	
	//Create a method to return a hand's win/loss.
	public int gethandWin(){
		return this.handWin;
	}
	
	//Create a method to return a split hand's win/loss.
	public int getsplitWin(){
		return this.splitWin;
	}
	
	//Create a method to set if the player is still playing or not.
	public void setPlaying(boolean playing){
		this.playing = playing;
	}
	
	//Create a method to see if the player is still playing or not.
	public boolean getPlaying(){
		return this.playing;
	}
	
	
	// This method deals two cards to the player (one face down if this is the dealer).
	// The window input should be used to redraw the window whenever a card is dealt.
	public void startRound(Deck deck, BlackjackWindow window){
		//If this player is the dealer, deal 2 cards, one face up and one down.
		//add these cards to his hand.
		if (this.dealer == true){
			Card c1 = deck.drawCard();
			c1.turnFaceUp();
			this.hand.addCard(c1);
			counter(c1);
			window.redraw();
			
			Card c2 = deck.drawCard();
			c2.turnFaceDown();
			this.hand.addCard(c2);
			window.redraw();

			
			//set the boolean to allow players to purchase insurance if there's an ace.
			if (c1.getFace() == 1){
				GIO.displayMessage("Dealer has an ace. Players can purchase insurance.");
				this.canInsure = true;
			}
		}
		
		//If the player is a dealer, proceed with betting and etc.
		else if (this.dealer == false) {
			GIO.displayMessage(this.name + "'s turn.");
			this.wager = GIO.readInt(this.name + ", place your bet: ");
			
			//add cards and split add them into the player's hand
			Card c1 = deck.drawCard();
			c1.turnFaceUp();
			this.hand.addCard(c1);
			counter(c1);
			window.redraw();
			
			Card c2 = deck.drawCard();
			c2.turnFaceUp();
			this.hand.addCard(c2);
			counter(c2);
			window.redraw();
			
			if (c1.getFace() == c2.getFace()) {
				this.canSplit = true;
				GIO.displayMessage("Note: you have the potential to split!");
			}
		}
	}

	// This method executes gameplay for one player.
	// If this player is the dealer:
	//	- hits until score is at least 17
	// If this is an ordinary player:
	//	- repeatedly asks the user if they want to hit (draw another card)
	//	  until either the player wants to stand (not take any more cards) or
	//	  his/her score exceeds 21 (busts).
	// The window input should be used to redraw the window whenever a card is dealt or turned over.
	public void playRound(Deck deck, BlackjackWindow window){
		int total = this.hand.getScore();
		
		//Setting up dealer's turn.
		if (this.dealer == true){
			GIO.displayMessage("Dealer's turn: " + this.name);
			this.hand.getCard(1).turnFaceUp();
			window.redraw();
			//implement counter to facedown card
			
			//Run this initially first to check if the hand at this state (2 cards) is BlackJack.
			int countHand = this.hand.getScore(); //this is to hold a value of both cards to see if they're 21.
			if (countHand == 21){
				GIO.displayMessage("Dealer hit BlackJack! All players insured are safe.");
				confirmInsurance = true;
			}
			
			else{
				while (total <= 17){
					GIO.displayMessage("Time for dealer to hit:");
					Card c1 = deck.drawCard();
					c1.turnFaceUp();
					this.hand.addCard(c1);
					counter(c1);
					window.redraw();
					
					if ((this.hand.getScore() >= 17) && (this.hand.getScore() < 21)) {
						GIO.displayMessage("Dealer stands. Current Value: " + this.hand.getScore());
						total = this.hand.getScore();
					}
					
					if (this.hand.getScore() == 21){
						GIO.displayMessage("Dealer has BlackJack :D!");
						total = this.hand.getScore();
					}
					
					if (this.hand.getScore() > 21){
						GIO.displayMessage("Dealer busted. Current Value: " + this.hand.getScore());
						total = this.hand.getScore();
					}
				}
			} //ends the else if dealer doesn't have BlackJack
		} //ends if this.dealer == true
		
		//Setting up the dealer's turn.
		//Sets up the hit and stand system.
		if (this.dealer == false){
			boolean doubledown = false;
			
			GIO.displayMessage(this.name + ", it's your turn.");

			//Conditions if player commits/doesn't commit to insuring.
			if (canInsure == true){
				this.yesInsure = GIO.readBoolean("Would you like to buy insurance?");
				
				if (yesInsure == true){
					this.insurance = this.wager/2;
				}
			}
			
			//Round without splitting but option to double down.
			if (this.canSplit == false){
				doubledown = GIO.readBoolean("Would you like to double down?");
				
				if (doubledown == false) {
					Hit(deck, this.hand, window); //refer to hit method
				}
			
				//If doubledown is true. Same code as if doubledown == false, but doubles bet.
				if (doubledown == true){
					Doubledown(deck, this.hand);
					Hit(deck, this.hand, window); //refer to hit method
				}
			} //ends canSplit == false
			
			//Create a round where it is possible for the player to split.
			if (canSplit == true){
				yesSplit = GIO.readBoolean("You are able to split. Do you want to?");
				
				//Go through each hand as normal rounds if player splits.
				if (yesSplit == true){
					Split(deck, this.hand, this.splitHand, window);
										
					//Round for first hand
					doubledown = GIO.readBoolean("Do you want to double down on your first hand?");
					
					if (doubledown == false) {
						Hit(deck, this.hand, window); //refer to hit method
					}
					
					if (doubledown == true){
						Doubledown(deck, this.hand); //refer to Doubledown method
						Hit(deck, this.hand, window); //refer to hit method
					}
					
					//Round for the second hand
					doubledown = GIO.readBoolean("Do you want to double down on your split hand?");
					
					if (doubledown == false) {
						Hit(deck, this.splitHand, window); //refer to hit method
					}
					
					if (doubledown == true){
						Doubledown(deck, this.splitHand); //refer to Doubledown method
						Hit(deck, this.splitHand, window); //refer to hit method
					}
				} // ends yesSplit == true
				
				//If the player decides not to split.
				if (yesSplit == false){
					doubledown = GIO.readBoolean("Would you like to double down?");
					
					if (doubledown == false) {
						Hit(deck, this.hand, window); //refer to hit method
					}
				
					//If doubledown is true. Same code as if doubledown == false, but doubles bet.
					if (doubledown == true){
						Doubledown(deck, this.hand);
						Hit(deck, this.hand, window); //refer to hit method
					}				
				}
			} // ends canSplit == true
		} // ends dealer == false
	}

	// This method informs the player about whether they won, lost, or pushed.
	// It also discards the player's cards to prepare for the next round.
	// The window input should be used to redraw the window after cards are discarded.
	public void finishRound(int dealerScore, BlackjackWindow window){
		//These calculations apply to the players only.
		if (this.dealer == false){
			GIO.displayMessage("Total score for: " + this.name);
			
			if (yesSplit == false){
				compareScore(this.hand.getScore(), dealerScore);
			}
			
			if (yesSplit == true){
				GIO.displayMessage("Calculations for first hand: ");
				compareScore(this.hand.getScore(), dealerScore);
				
				GIO.displayMessage("Calculations for the second hand: ");
				compareScore(this.splitHand.getScore(), dealerScore);
			}
		} //ends this.dealer == false
		this.hand.discardAll();
		this.splitHand.discardAll();
		window.redraw();
	} //ends endRound
	
	//Create a method to let the dealer cash everyone out when necessary.
	public void dealerRound(double allWagers){
		this.money += allWagers;
		GIO.displayMessage("Dealer's current cash: " + this.money);
	}
	
	
	/* The methods below are used to clear up some duplicate masses of code
	 * in the startRound and endRound method. Since there were multiple cases of Hit,
	 * Double down, and Split, I thought it would be better to just make
	 * them as methods to be called.
	 */
	
	//Make a method to ask/let the player hit.
	public void Hit(Deck deck, Hand hand, BlackjackWindow window){
		//ask if they want a hint first.
		boolean hint = GIO.readBoolean("Would you like a hint?");
		
		if (hint == true)
			Hint();
		
		boolean hit = GIO.readBoolean(this.name + ", would you like to hit?");
		
		while (hit == true) {
			Card c2 = deck.drawCard();
			c2.turnFaceUp();
			hand.addCard(c2);
			counter(c2);
			window.redraw();

			if (hand.getScore() == 21){
				GIO.displayMessage("You hit 21!");
				return;
			}
			
			if (hand.getScore() > 21) {
				GIO.displayMessage(this.name + " busted.");
				return;
			}
			
			hit = GIO.readBoolean("Would you like to hit again?");	
		}
		
	}

	//This method splits the hand when called.
	public void Split(Deck deck, Hand hand, Hand splitHand, BlackjackWindow window){
		splitWager = this.wager;
		splitHand.addCard(hand.getCard(1));
		Card temp = hand.getCard(0);
		hand.discardAll();
		hand.addCard(temp);
		window.redraw();
	}
	
	//This method proceeds with the double down process.
	public void Doubledown(Deck deck, Hand hand){
		this.wager += this.wager; //doubles your wager
		GIO.displayMessage("Your wager is now $" + wager);
	}
	
	//This method compares the scores between the players and the dealer.
	public void compareScore(int playerScore, int dealerScore){
		if (this.yesInsure == true){ //Use this loop if the player insured.
			if (confirmInsurance == true){
				GIO.displayMessage("Looks like your insurance helped out!");
				handWin = -1;
				splitWin = -1;
				GIO.displayMessage("Your remaining cash: " + this.money);
			}
		}
		
		else { //if the player did not insure.
			if (this.hand.getScore() > 21){
				GIO.displayMessage("You lost because you busted.");
				this.money -= this.wager; //subtract wager from your bank.
				GIO.displayMessage("Your remaining cash: " + this.money);
				splitWin = 0;
			}
			
			else if (this.hand.getScore() < dealerScore){
				GIO.displayMessage("You lost because the dealer scored higher.");
				this.money -= this.wager; //subtract wager from your bank.
				GIO.displayMessage("Your remaining cash: " + this.money);
				handWin = 0;
			}
			
			else if (dealerScore > 21) {
				GIO.displayMessage("The dealer busted. You win!");
				this.money -= this.wager; //subtract wager from your bank.
				GIO.displayMessage("Your remaining cash: " + this.money);
				handWin = 1;
			}
			
			else if ((playerScore > dealerScore) && (playerScore <= 21)){
				System.out.println("You beat the dealer. You win!");
				this.money += this.wager; //subtract wager from your bank.
				GIO.displayMessage("Your remaining cash: " + this.money);
				handWin = 1;
			}
			
			else if ((playerScore == dealerScore) && (dealerScore <= 21 && playerScore <= 21)){
				GIO.displayMessage("You pushed with the dealer. It's a tie.");
				GIO.displayMessage("Your remaining cash: " + this.money);
				handWin = -1;
			}
			
			//Make a compare score for splits.
			if (yesSplit == true){
				if (this.hand.getScore() > 21){
					GIO.displayMessage("You lost because you busted.");
					this.money -= this.wager; //subtract wager from your bank.
					GIO.displayMessage("Your remaining cash: " + this.money);
					splitWin = 0;
				}
				
				else if (this.hand.getScore() < dealerScore){
					GIO.displayMessage("You lost because the dealer scored higher.");
					this.money -= this.wager; //subtract wager from your bank.
					GIO.displayMessage("Your remaining cash: " + this.money);
					handWin = 0;
				}
				
				else if (dealerScore > 21) {
					GIO.displayMessage("The dealer busted. You win!");
					this.money -= this.wager; //subtract wager from your bank.
					GIO.displayMessage("Your remaining cash: " + this.money);
					handWin = 1;
				}
				
				else if ((playerScore > dealerScore) && (playerScore <= 21)){
					System.out.println("You beat the dealer. You win!");
					this.money += this.wager; //subtract wager from your bank.
					GIO.displayMessage("Your remaining cash: " + this.money);
					handWin = 1;
				}
				
				else if ((playerScore == dealerScore) && (dealerScore <= 21 && playerScore <= 21)){
					GIO.displayMessage("You pushed with the dealer. It's a tie.");
					GIO.displayMessage("Your remaining cash: " + this.money);
					handWin = -1;
				}
			}
		}//end yesSplit = true
	}
	
	//Create a method to set up insurance if the dealer shows an ace.
	public void Insurance(){
		boolean insure = GIO.readBoolean("Would you like to buy insurance?");
		
		if (insure == true) { 
			insurance = this.wager / 2; //insurance is half of your bet.
		}
	}
	
	//Create a method that will keep track of a "counter" that counts the probability of the value of a card.
	// +1 for 2-6, 0 for 7-9, and -1 point for 10-A
	public void counter(Card card){
		int face = card.getFace();
		
		//Keeping track of the counters
		if(face >= 2 && face <= 6)
			counter++;
		
		if(face == 1 || face >= 10)
			counter--;
	}
	
	//Create a method to clear the counter.
	public static void clearCounter(){
		counter = 0;
	}
	
	public void Hint() {
		//Display the hints based on counter
		
		if (counter == 0)
			GIO.displayMessage("Since the counter is 0, the chances of your next card could be anything.");
		
		if (counter < 0)
			GIO.displayMessage("The counter is " + counter + ". The greater the negative number, the more likely your next card is a higher card.");
		
		if (counter > 0)
			GIO.displayMessage("The counter is " + counter + ". The greater the positive number, the more likely your next card is a lower card.");
			
	}

}
