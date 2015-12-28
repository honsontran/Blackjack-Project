public class Player {
	
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
	
	//create a constructor to create a player.
	public Player(String name, boolean dealer, double money){
		this.name = name;
		this.money = money;
		this.wager = 0;
		this.dealer = dealer;
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
	
	//this method is to set up the round by giving each player two cards as well as the dealer.
	//anything in the hand is prepared as well as splitting before going into the round.
	public void setupRound (Deck deck){
		//If this player is the dealer, deal 2 cards, one face up and one down.
		//add these cards to his hand.
		if (this.dealer == true){
			Card c1 = deck.deal();
			c1.flipUp();
			this.hand.addCard(c1);
			counter(c1);
			
			Card c2 = deck.deal();
			c2.flipDown();
			this.hand.addCard(c2);
			
			System.out.println("The dealer's hand:");
			System.out.println(c1.toString() + " and a facedown card.");
			System.out.println("Current Shown Value: " + c1.getValue());
			
			//set the boolean to allow players to purchase insurance if there's an ace.
			if (c1.getFace() == 1){
				System.out.println("");
				System.out.println("Dealer has an ace. Players can purchase insurance.");
				this.canInsure = true;
			}
		}
		
		//If the player is a dealer, proceed with betting and etc.
		else if (this.dealer == false) {
			System.out.println("--------------------------------------------------");
			System.out.println(this.name + "'s turn.");
			System.out.println("");
			System.out.println("Place your bet: ");
			this.wager = IO.readDouble();
			System.out.println("");
			
			//add cards and split add them into the player's hand
			Card c1 = deck.deal();
			c1.flipUp();
			this.hand.addCard(c1);
			counter(c1);
			
			Card c2 = deck.deal();
			c2.flipUp();
			this.hand.addCard(c2);
			counter(c2);
			
			System.out.println("Here's your current hand:");
			System.out.println(this.hand);
			System.out.println("Current Value: " + this.hand.addScore());
			
			if (c1.getFace() == c2.getFace()) {
				this.canSplit = true;
				System.out.println("Note: you have the potential to split!");
			}
		}
	}
	
	//In this method, the player will be asked to split, make bets, and also to hit or not
	//This method will also manage the dealer's card management as well.
	public void startRound(Deck deck) {
		int total = this.hand.addScore();
				
		//Setting up dealer's turn.
		if (this.dealer == true){
			System.out.println("Dealer's turn: " + this.name);
			System.out.println("Time to show the facedown card.");
			System.out.println(this.hand);
			//implement counter to facedown card
			
			//Run this initially first to check if the hand at this state (2 cards) is BlackJack.
			int countHand = this.hand.addScore(); //this is to hold a value of both cards to see if they're 21.
			if (countHand == 21){
				System.out.println("Dealer hit BlackJack! All players insured are safe.");
				confirmInsurance = true;
			}
			
			else{
				while (total <= 17){
					Card c1 = deck.deal();
					c1.flipUp();
					this.hand.addCard(c1);
					counter(c1);
					
					System.out.println("Time for dealer to hit: ");
					System.out.println(this.name + " has drawn a " + c1.toString());
					System.out.println("Current Value: " + this.hand.addScore());
					
					if ((this.hand.addScore() >= 17) && (this.hand.addScore() < 21)) {
						System.out.println("");
						System.out.println("Dealer stands.");
						System.out.println("Current Value: " + this.hand.addScore());
						System.out.println("--------------------------------------------------");					
						break;
					}
					
					if (this.hand.addScore() == 21){
						System.out.println("Dealer has BlackJack :D!");
						System.out.println("--------------------------------------------------");
					}
					
					if (this.hand.addScore() > 21){
						System.out.println("Dealer busted.");
						System.out.println("Current Value: " + this.hand.addScore());
						System.out.println("--------------------------------------------------");
					}
				}
			} //ends the else if dealer doesn't have BlackJack
		}
		
		//Setting up the dealer's turn.
		//Sets up the hit and stand system.
		if (this.dealer == false){
			boolean doubledown = false;
			
			System.out.println(this.name + ", it's your turn.");
			System.out.println("Current Value: " + this.hand.addScore()); //outputs value and softhand is applicable.
			
			//Conditions if player commits/doesn't commit to insuring.
			if (canInsure == true){
				System.out.println("Would you like to buy insurance? (Y/N)");
				this.yesInsure = IO.readBoolean();
				
				if (yesInsure == true){
					this.insurance = this.wager/2;
				}
			}
			
			//Round without splitting but option to double down.
			if (this.canSplit == false){
				System.out.println("");
				System.out.println("Would you like to double down? (Y/N)");
				doubledown = IO.readBoolean();
				
				if (doubledown == false) {
					Hit(deck, this.hand); //refer to hit method
				}
			
				//If doubledown is true. Same code as if doubledown == false, but doubles bet.
				if (doubledown == true){
					System.out.println("");
					Doubledown(deck, this.hand);
					Hit(deck, this.hand); //refer to hit method
				}
			} //ends canSplit == false
			
			//Create a round where it is possible for the player to split.
			if (canSplit == true){
				System.out.println("You are able to split. Do you want to? (Y/N)");
				yesSplit = IO.readBoolean();
				
				//Go through each hand as normal rounds if player splits.
				if (yesSplit == true){
					Split(deck, this.hand, this.splitHand);
										
					//Round for first hand
					System.out.println("Do you want to double down on your first hand? (Y/N)");
					doubledown = IO.readBoolean();
					
					if (doubledown == false) {
						Hit(deck, this.hand); //refer to hit method
					}
					
					if (doubledown == true){
						Doubledown(deck, this.hand); //refer to Doubledown method
						Hit(deck, this.hand); //refer to hit method
					}
					
					//Round for the second hand
					System.out.println("Do you want to double down on your split hand? (Y/N)");
					doubledown = IO.readBoolean();
					
					if (doubledown == false) {
						Hit(deck, this.splitHand); //refer to hit method
					}
					
					if (doubledown == true){
						Doubledown(deck, this.splitHand); //refer to Doubledown method
						Hit(deck, this.splitHand); //refer to hit method
					}
				} // ends yesSplit == true
				
				//If the player decides not to split.
				if (yesSplit == false){
					System.out.println("Would you like to double down? (Y/N)");
					doubledown = IO.readBoolean();
					
					if (doubledown == false) {
						Hit(deck, this.hand); //refer to hit method
					}
				
					//If doubledown is true. Same code as if doubledown == false, but doubles bet.
					if (doubledown == true){
						Doubledown(deck, this.hand);
						Hit(deck, this.hand); //refer to hit method
					}				
				}
			} // ends canSplit == true
		} // ends dealer == false
	} //ends startRound
	
	//Create a method to calculate and inform player of all scores.
	//Discard all cards after calculations and cash out.
	public void endRound(int dealerScore){
		//These calculations apply to the players only.
		if (this.dealer == false){
			System.out.println("Total score for: " + this.name);
			System.out.println("");
			
			if (yesSplit == false){
				compareScore(this.hand.addScore(), dealerScore);
			}
			
			if (yesSplit == true){
				System.out.println("Calculations for first hand: ");
				compareScore(this.hand.addScore(), dealerScore);
				System.out.println("---------------------------------");
				
				System.out.println("Calculations for the second hand: ");
				compareScore(this.splitHand.addScore(), dealerScore);
			}
		} //ends this.dealer == false
		
		this.hand.emptyHand();
		this.splitHand.emptyHand();
	} //ends endRound
	
	//Create a method to let the dealer cash everyone out when neccessary.
	public void dealerRound(double allWagers){
		this.money += allWagers;
		System.out.println("Dealer's current cash: " + this.money);
	}
	
	
	/* The methods below are used to clear up some duplicate masses of code
	 * in the startRound and endRound method. Since there were multiple cases of Hit,
	 * Double down, and Split, I thought it would be better to just make
	 * them as methods to be called.
	 */
	
	//Make a method to ask/let the player hit.
	public void Hit(Deck deck, Hand hand){
		//ask if they want a hint first.
		System.out.println("Would you like a hint?");
		boolean hint = IO.readBoolean();
		System.out.println("");
		
		if (hint == true)
			Hint();
		
		System.out.println("--------------------------------------------------");
		System.out.println(this.name + ", would you like to hit? (Y/N)");
		boolean hit = IO.readBoolean();
		
		while (hit == true) {
			Card c2 = deck.deal();
			c2.flipUp();
			hand.addCard(c2);
			counter(c2);
			
			System.out.println("Hit: " + c2.toString());
			System.out.println("Current Value: " + hand.addScore());
			System.out.println("");
			
			if (hand.addScore() == 21){
				return;
			}
			
			if (hand.addScore() > 21) {
				System.out.println(this.name + " busted.");
				return;
			}
			
			System.out.println("Would you like to hit again? (Y/N)");
			hit = IO.readBoolean();	
		}
		
		System.out.println("--------------------------------------------------");
	}

	//This method splits the hand when called.
	public void Split(Deck deck, Hand hand, Hand splitHand){
		splitWager = this.wager;
		splitHand.addCard(hand.getCard(1));
		Card temp = hand.getCard(0);
		hand.emptyHand();
		hand.addCard(temp);
		
		//Display each hand and their value.
		System.out.println("Hand 1:");
		System.out.println(hand.toString());
		hand.addScore();
		System.out.println("--------------------------");
		
		System.out.println("Hand 2:");
		System.out.println(splitHand.toString());
		splitHand.addScore();
	}
	
	//This method proceeds with the double down process.
	public void Doubledown(Deck deck, Hand hand){
		this.wager += this.wager; //doubles your wager
		System.out.println("Your wager is now " + wager);
		System.out.println("");
		System.out.println("Here's your hand:");
		System.out.println(hand);
		System.out.println("");
		System.out.println("Current Value: " + hand.addScore());
	}
	
	//This method compares the scores between the players and the dealer.
	public void compareScore(int playerScore, int dealerScore){
		if (this.yesInsure == true){ //Use this loop if the player insured.
			if (confirmInsurance == true){
				System.out.println("Looks like your insurance helped out!");
				handWin = -1;
				splitWin = -1;
				System.out.println("Your remaining cash: " + this.money);
				System.out.println("--------------------------------------------------");
			}
		}
		
		else { //if the player did not insure.
			if (this.hand.addScore() > 21){
				System.out.println("");
				System.out.println("You lost because you busted.");
				this.money -= this.wager; //subtract wager from your bank.
				System.out.println("Your remaining cash: " + this.money);
				splitWin = 0;
				System.out.println("--------------------------------------------------");
			}
			
			else if (this.hand.addScore() < dealerScore){
				System.out.println("");
				System.out.println("You lost because the dealer scored higher.");
				System.out.println("Dealer Score: " + dealerScore);
				System.out.println(this.name + "'s Score: " + playerScore);
				this.money -= this.wager; //subtract wager from your bank.
				System.out.println("Your remaining cash: " + this.money);
				handWin = 0;
				System.out.println("--------------------------------------------------");
			}
			
			else if (dealerScore > 21) {
				System.out.println("");
				System.out.println("The dealer busted. You win!");
				System.out.println(this.name + "'s Score: " + playerScore);
				this.money -= this.wager; //subtract wager from your bank.
				System.out.println("Your remaining cash: " + this.money);
				handWin = 1;
				System.out.println("--------------------------------------------------");
			}
			
			else if ((playerScore > dealerScore) && (playerScore <= 21)){
				System.out.println("");
				System.out.println("You beat the dealer. You win!");
				System.out.println(this.name + "'s Score: " + playerScore);
				System.out.println("Dealer's score: " + dealerScore);
				this.money += this.wager; //subtract wager from your bank.
				System.out.println("Your remaining cash: " + this.money);
				handWin = 1;
				System.out.println("--------------------------------------------------");
			}
			
			else if ((playerScore == dealerScore) && (dealerScore <= 21 && playerScore <= 21)){
				System.out.println("");
				System.out.println("You pushed with the dealer. It's a tie.");
				System.out.println(this.name + "'s Score: " + playerScore);
				System.out.println("Dealer's score: " + dealerScore);
				System.out.println("Your remaining cash: " + this.money);
				handWin = -1;
				System.out.println("--------------------------------------------------");
			}
			
			//Make a compare score for splits.
			if (yesSplit == true){
				if (this.splitHand.addScore() > 21){
					System.out.println("");
					System.out.println("You lost because you busted.");
					this.money -= this.wager; //subtract wager from your bank.
					System.out.println("Your remaining cash: " + this.money);
					splitWin = 0;
					System.out.println("--------------------------------------------------");
				}
				
				else if (this.splitHand.addScore() < dealerScore){
					System.out.println("");
					System.out.println("You lost because the dealer scored higher.");
					System.out.println("Dealer Score: " + dealerScore);
					System.out.println(this.name + "'s Score: " + playerScore);
					this.money -= this.wager; //subtract wager from your bank.
					System.out.println("Your remaining cash: " + this.money);
					splitWin = 0;
					System.out.println("--------------------------------------------------");
				}
				
				else if (dealerScore > 21) {
					System.out.println("");
					System.out.println("The dealer busted. You win!");
					System.out.println(this.name + "'s Score: " + playerScore);
					this.money -= this.wager; //subtract wager from your bank.
					System.out.println("Your remaining cash: " + this.money);
					splitWin = 1;
					System.out.println("--------------------------------------------------");
				}
				
				else if ((playerScore > dealerScore) && (playerScore <= 21)){
					System.out.println("");
					System.out.println("You beat the dealer. You win!");
					System.out.println(this.name + "'s Score: " + playerScore);
					System.out.println("Dealer's score: " + dealerScore);
					this.money += this.wager; //subtract wager from your bank.
					System.out.println("Your remaining cash: " + this.money);
					splitWin = 1;
					System.out.println("--------------------------------------------------");
				}
				
				else if ((playerScore == dealerScore) && (dealerScore <= 21 && playerScore <= 21)){
					System.out.println("");
					System.out.println("You pushed with the dealer. It's a tie.");
					System.out.println(this.name + "'s Score: " + playerScore);
					System.out.println("Dealer's score: " + dealerScore);
					System.out.println("Your remaining cash: " + this.money);
					splitWin = -1;
					System.out.println("--------------------------------------------------");
				}
			}
		}//end yesSplit = true
	}
	
	//Create a method to set up insurance if the dealer shows an ace.
	public void Insurance(){
		System.out.println("Would you like to buy insurance? (Y/N)");
		boolean insure = IO.readBoolean();
		
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
		System.out.println("Current Counter: " + counter);
		
		if (counter == 0)
			System.out.println("Since the counter is 0, the chances of your next card could be anything.");
		
		if (counter < 0)
			System.out.println("The greater the negative number, the more likely your next card is a higher card.");
		
		if (counter > 0)
			System.out.println("The greater the positive number, the more likely your next card is a lower card.");
			
	}

}