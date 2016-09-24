import java.io.*;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player
{

    // VARIABLES FOR USE;
    protected Character[] characters;
    protected Character chosen;

    protected String[] attributes = {"hairLength", "glasses", "facialHair", "eyeColor", "pimples",
    		"hat", "hairColor", "noseShape", "faceShape"};

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *    Note you can handle IOException within the constructor and remove
     *    the "throws IOException" method specification, but make sure your
     *    implementation exits gracefully if an IOException is thrown.
     */
    public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException
    {
	
	// Load all the data;
	characters = Loader.LoadData(gameFilename);

	// Getting the character;
	for(int i = 0; i < characters.length; i++){

		if((characters[i].get("name")).equals(chosenName)){

			chosen = characters[i];
			break;
		}

	}	


    } // end of RandomGuessPlayer()


    public Guess guess() {

	// Variables for use;
	String atr_guess;
	String val_guess = null;
	int char_guess;  
	int counter = 0;
	int last_loc = 0;

	// Random for random choice;
	Random random = new Random();

	// Checking if there is only ony player left;
	// Getting the character;
	for(int i = 0; i < characters.length; i++){

		if(characters[i].isDown()){
			
			// Do nothing;

		}else {

			last_loc = i;
			counter++;
		}

	}	

	// If only one just go to guessing;
	if(counter == 1){
		
		System.out.println(characters[last_loc].get("name"));
		return new Guess(Guess.GuessType.Person, "", characters[last_loc].get("name"));	
	
	}


	while(true){

		// Assigning values;
		// Getting the attribute wanted.
		atr_guess = attributes[random.nextInt(attributes.length)];
		char_guess = random.nextInt(characters.length);
	
		// Check if the character is eliminated;
		if(characters[char_guess].isDown()){

			// Do nothing;
		}else{
			
			break;

		}
	}
	

	val_guess = characters[char_guess].get(atr_guess);
	return new Guess(Guess.GuessType.Attribute, atr_guess, val_guess);

    } // end of guess()


    public boolean answer(Guess currGuess) {

        // Checking the guess type;
	// If attribute guess;

	if((currGuess.getType()).equals(Guess.GuessType.Attribute)){

		if((currGuess.getValue()).equals(chosen.get(currGuess.getAttribute()))){
	
			return true;
				
		}else {

			return false;

		}

	}else {

		if((chosen.get("name")).equals(currGuess.getValue())){

			return true;
		
		}else {

			return false;
		}


	}

    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
	

	// Checking type of guess.
	if((currGuess.getType()).equals(Guess.GuessType.Attribute)){

		if(answer == false){

			// Checking all the stuff.
			for(int i = 0; i < characters.length; i++){

				if((characters[i].get(currGuess.getAttribute())).equals(currGuess.getValue())){
			
					if(characters[i].isDown()){
				
						// Do nothing;

					}else {

						characters[i].setDown();				
					}
				}

			}

		}

		return false;

	}else {
			
		if(answer == true){

			return true;

		}else {

			// Checking all the stuff.
			for(int i = 0; i < characters.length; i++){

				if((characters[i].get("name")).equals(currGuess.getValue())){
			
					if(characters[i].isDown()){
				
						// Do nothing;

					}else {

						characters[i].setDown();				
					}
				}

			}
		}

	}

	return false;
    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
