//Hand.java represents each player's hand, including the dealer.

public class Hand {
	public static int value = 0;
	public static int softhand = 0;
	public Card[] hand;
	
	//Constructor to create a hand for each player.
	//Set every element in the array to null.
	//If it's null, it's not in the hand.
	public Hand() {
		this.hand = new Card[20]; //Hand value is generously big enough to hold cards.
		for (int i = 0; i < hand.length; i++) {
			hand[i] = null;
		}
	}
	
	//This method takes info of a card from a hand.
	public Card getCard(int index){
		return hand[index];
	}
	
	//creates the actual array with cards. Trims the nulls.
	public Card[] numHand(Card hand[]) {
		int numCard = 0;
		for (int i = 0; i < this.hand.length; i++) {
			if (hand != null) {
				numCard++;
			}
		}
		this.hand = new Card[numCard];
		return hand;
		
	}
	
	//Creates + overwrites the current array to make room for new card.
	public void addCard(Card newCard) {
		for(int i = 0; i < this.hand.length; i++){
			if(this.hand[i] == null){
				this.hand[i] = newCard;
				break;
			}
		}
	}
	
	//Returns your current hand.
	public String getHand(){
		return this.hand.toString();
	}
	
	//Calculates the current score of the hand.
	public int addScore(){
		int total = 0;
		
		//This adds the normal value without aces.
		for(int i = 0; i < this.hand.length; i++){
			if(hand[i] != null){
				total += hand[i].getValue();
			}
		}
		
		//Find aces. If there is, set a boolean to use the next loop.
		boolean ace = false;
		for(int i = 0; i < hand.length;i++){
			if(hand[i] != null){	
				if(hand[i].getFace() == 1){
					ace = true;
				}
			}
		}
		
		//If ace is true, display this value instead of the one without aces.
		if(ace == true && total + 10 <= 21){
			total += 10;
		}
		
		return total;
	}//end addScore()
	
	//returns the calculated score.
	public int getScore() {
		return value;
	}

	//Empty the player's hand for a new round.
	public void emptyHand(){
		for (int i = 0; i < hand.length; i++){
			this.hand[i] = null;
		}
	}
	
	//Convert this into English.
	public String toString(){
		String sentence = "";
		
		for(int i=0; i<this.hand.length;i++){
			if(hand[i] != null){
				sentence += this.hand[i].toString() + "\n";	
			}
		}
		return sentence;
	}
}