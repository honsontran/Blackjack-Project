// This class represents the set of cards held by one player (or the dealer).
public class Hand {
	
	// define fields here
	public static int value = 0;
	public static int softhand = 0;
	public Card[] hand;

	// This constructor builds a hand (with no cards, initially).
	public Hand() {
		this.hand = new Card[20]; //Hand value is generously big enough to hold cards.
		for (int i = 0; i < hand.length; i++) {
			hand[i] = null;
		}
	}
	
	// This method retrieves the size of this hand.
	public int getNumberOfCards(){
		int numCards = 0;
		
		for (int i = 0; i < this.hand.length; i++) {
			if (this.hand[i] != null) {
				numCards++;
			}
		}
		return numCards;
	}

	// This method retrieves a particular card in this hand.  The card number is zero-based.
	public Card getCard(int index){
		return this.hand[index];
	}

	// This method takes a card and places it into this hand.
	public void addCard(Card newcard){
		for(int i = 0; i < this.hand.length; i++){
			if(this.hand[i] == null){
				this.hand[i] = newcard;
				break;
			}
		}
	}

	// This method computes the score of this hand.
	public int getScore(){
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

	// This methods discards all cards in this hand.
	public void discardAll(){
		for (int i = 0; i < hand.length; i++){
			this.hand[i] = null;
		}
	}
}
