import java.io.*;

/**
 * Your customised guessing player.
 * This player is for bonus task.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class CustomGuessPlayer implements Player
{

    // VARIABLES FOR USE;
    protected Character[] characters;
    protected Character chosen;	
    protected String[] attributes = {"hairLength", "glasses", "facialHair", "eyeColor", "pimples",
    		"hat", "hairColor", "noseShape", "faceShape"};

    // VARIABLES FOR GUESS;
    protected int[] guessed_attributes;
    protected String[] guessed_values;
    protected Guess guess1 = null;
    protected Guess guess2 = null;
    protected int characters_left;
    protected float attribute_chance;
    protected float person_chance;

 
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

    public CustomGuessPlayer(String gameFilename, String chosenName)
        throws IOException
    {

	// Load all the data;
	characters = Loader.LoadData(gameFilename);

	// Getting the character;
	for(int i = 0; i < characters.length; i++){

		// Singling out the chosen character;
		if((characters[i].get("name")).equals(chosenName)){
			chosen = characters[i];
			break;
		}
	}

	// Setting up data;
	guessed_attributes = new int[attributes.length];
        guessed_values = new String[attributes.length];

	// Initializing it;
	for(int i = 0; i < attributes.length; i++){
		guessed_attributes[i] = 0;
		guessed_values[i] = null;
	}

	// For later use;
	characters_left = characters.length + 1;

	
    } // end of CustomGuessPlayer()


    public Guess guess() {

	// Variables for use:
	Guess curr_guess;
	String[] attr_value = new String[2];	
	String person;
	int counter = 0;
	int last_loc = 0;

	// Finding the most common Attr-value pair;
	attr_value = findHighestAttrValuePair(attr_value);

	// Finding the best choice.
	findBestChoice();

	// Print statements for debug;
	System.out.println("[CUSTOM] Attribute:\t" + attribute_chance + "%.");
	System.out.println("[CUSTOM] Person:\t" + person_chance + "%.");

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
		return new Guess(Guess.GuessType.Person, "", characters[last_loc].get("name"));	
	}

	// If chance of "correctness" higher then the other,
	// go with that.
	if(person_chance > attribute_chance){

		curr_guess = guess2;
		guess2 = null;
		return curr_guess;

	}else if(attribute_chance >= person_chance){

	        return new Guess(Guess.GuessType.Attribute, attr_value[0], attr_value[1]);
	}

	return new Guess(Guess.GuessType.Attribute, attr_value[0], attr_value[1]);


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
						characters_left--;				
					}
				}
			}
		}else {

			// Checking all the stuff.
			for(int i = 0; i < characters.length; i++){

				if(!((characters[i].get(currGuess.getAttribute())).equals(currGuess.getValue()))){
			
					if(characters[i].isDown()){
				
						// Do nothing;

					}else {

						characters[i].setDown();
						characters_left--;				
					}
				}
			}

			// Finding the location of the attribute and setting it to guess.
			for(int i = 0; i < attributes.length; i++){
				if(attributes[i].equals(currGuess.getAttribute())){
					guessed_attributes[i] = 1;
					guessed_values[i] = currGuess.getValue();	
					break;
				}
			}

			guess2 = null;
		}


	}else {		
		if(answer == true){
			return true;
		}else {

			// Checking all the stuff.
			for(int i = 0; i < characters.length; i++){

				if((characters[i].get("name")).equals(currGuess.getValue())){
			
					characters[i].setDown();
					characters_left--;
					break;			
				}
			}
		}
	}

	return false;
    } // end of receiveAnswer()
	
    public boolean findBestChoice(){

	// Variables for use;	
	int outcast_counter[] = new int[characters.length];;
	int counter = 0;
	int array_counter = 0;
	int loc = 0;
	int max = 0;

	// Finding if they resemble closest to the guessed values;
	for(int i = 0; i < characters.length; i++){

		if(characters[i].isDown()){

			// Do nothing;	
			continue;	
		}else{

			
			// New counter;
			int c = 0;
		
			for(int k = 0; k < attributes.length; k++){

				if(guessed_values[k] == null){

					continue;
				}

				if(characters[i].get(attributes[k]).equals(guessed_values[k])){
					c++;
				}
			}

			outcast_counter[i] = c;	
		}
	}

	// Finding the highest and setting it the next guess;	
	for(int i = 0; i < characters.length; i++){
		if(outcast_counter[i] > max){
			max = outcast_counter[i];
			loc = i;
		}
	}

	// Setting the person chance;
	person_chance = ((max * 100) / attributes.length);
	

	// Setting the guess2;
	guess2 = new Guess(Guess.GuessType.Person, "", characters[loc].get("name"));

	return false;
    }

    public String[] findHighestAttrValuePair(String[] array){

	// Variables for use;
	int attribute = 0;
	int location = 0;
	int different;

	// Arrays for use;
	String[] attributes_color = new String[attributes.length];
	int attributes_num[] = new int[attributes.length];

	// Initialising arrays;	
	for(int i = 0; i < attributes.length; i++){

		attributes_num[i] = 0;
		attributes_color[i] = "null";
	}

	// Finding the most common attribute-value pair;
	for(int k = 0; k < attributes.length; k++){
	
		// Checking if the current attribute already guessed;
		if(guessed_attributes[k] == 1){
			continue;
		}
		
		// Looping thorugh all characters;
		for(int j = 0; j < characters.length; j++){

			// Varaibles for use;
			String value = "null";
			int num_commons = 1;

			// No point checking eliminated characters;	
			if(characters[j].isDown()){
				continue;
			}

			// Loop for other characters.
			for(int i = 0; i < characters.length; i++){

				// No point checking eliminated characters;	
				if(characters[i].isDown()){
					// Do nothing
					continue;
				}

				if(i == j){

					// Do nothing;					
				}else {

					// Assigning String:
					value = characters[j].get(attributes[k]);
	
					if((characters[j].get(attributes[k])).equals(characters[i].get(attributes[k]))){
				
						num_commons++;
					}
				}
			}

			// If current greatest, assign num and value;
			if(num_commons > attributes_num[k]){
				attributes_num[k] = num_commons;
				attributes_color[k] = value;
			}
		}
	}


	// Finidng the highest out of all atrributes;
	for(int i = 0; i < attributes.length; i++){

		if(attributes_num[i] > attribute){

			attribute = attributes_num[i];
			location = i;
		}

	}

	// Setting the attribute chance;
	attribute_chance = (attribute * 100) / (characters_left);
	
	// Return with 0;
	array[0] = attributes[location];
	array[1] = attributes_color[location];

	return array;

    }

 } // end of class CustomGuessPlayer
