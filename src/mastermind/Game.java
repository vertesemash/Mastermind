
package mastermind;

//******************************************************************************
//  Game class
//  Runs the various aspects of the game (validating input, comparing input to the code, etc).
//  Date Created: July 10th 2017
//  Last Updated: July 11th 2017
//******************************************************************************

//Import ArrayList, Scanner, Random, and WordUtils
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import org.apache.commons.text.WordUtils;


public class Game 
{
    //Attempts tracks the guesses, finisher is an int which determines how a game ends
    private int attempts, finisher;
    private Scanner userInput;
    private Random theRandom;
    //gameAttempts contains all of the attempts in a given game
    private ArrayList<Attempt> gameAttempts;
    //This is the secret code. Shh! Don't tell anyone!
    private int[] code;
    //This boolean is used to track whether a user wants to play again or not
    private boolean reset;
    
    public Game()
    {
        //Initialize variables
        initialize();
        //Game description
        System.out.println(WordUtils.wrap("Welcome to Cows and Bulls! In this game, "
                + "a 4 digit code is randomly generated (digits are between 1 and 8) , "
                + "and you have 10 attempts to crack the code."
                + " You determine what the code is using Cows and Bulls. If you get a Cow, "
                + "that means you have the right number in the right place. If you "
                + "get a bull, you have the right number in the wrong place. "
                + "Dupilcate numbers ARE allowed, and one instance of a number in a code with multiple instances"
                + "of that number will register bulls for each instance.\n", 100));
    }
    
    //Controls the flow of the game
    public void runGame()
    {
       //While our reset control variable is true, run the game
       while (reset == true)
       {
           reset = false;
           //While we are not in a winning or losing state, execute a round of the game 
           while(finisher == 0)
           {
               //If user input is valid, validate their code against our code and increase attempts counter
               
               if(getUserInput())
               {
                   //Get most recent attempt and check it
                   validate(gameAttempts.get(attempts));
                   
                   //If user guessed all 4 digits correctly, we execute win state
                   //Otherwise, we check and see if that was their last attempt. If so, they lose.
                   if(gameAttempts.get(attempts).getCows() == 4)
                        finisher = 1;
                   else if(attempts == 9)
                       finisher = -1;
               }
               attempts++;
               //Increment attempts

           }
           //Once we are in a winning or losing state, run our end game
           endGame();
       }
       
       
    }
    
    //Get input from the user (either a code or menu options). Could probably be improved.
    private boolean getUserInput()
    {
        //Declare and initialize variables
        int[] userGuess = new int[4];
        String inputLine;
        boolean isValidInput = false;
        
        //While our input is not valid, try to get valid input
        while(isValidInput == false)
        {
            isValidInput = true;
            //Output rules for input
            System.out.println("Please input your guess (4 numbers from 1 to 8), separated by commas, on one line. \n"
                + "If you would like to end the game, please input 9. \n"
                + "If you would like to see all previous attempts, please input 10\n");
            //Try to parse user input. If the input is not numeric, error is thrown and user has to input data again
            try
            {
                //We grab input by line because it allows us to see if input is single digit
                //And a line of input can be turned into an array easily
                inputLine = userInput.nextLine();
                //If our input only has a single digit, check if we're ending the game or doing something else
                if(!inputLine.contains(","))
                {
                    //9 ends game, 10 outputs attempts. Other options can be added
                    switch(Integer.parseInt(inputLine))
                    {
                        case 9:
                            System.exit(0);
                        case 10:
                            if(!gameAttempts.isEmpty())
                                printAttempts();
                            else
                                System.out.println("Can't do that, sorry!\n");
                            isValidInput = false;
                            break;
                        default:
                           isValidInput = false;
                           break;
                    }   
                }
                else
                {
                    //Split input by commas
                    String[] inputAsString = inputLine.split(",");
                    int i = 0;
                    //Get each element from the String array and convert to Integer
                    for(String element: inputAsString)
                    {
                        //Debug printout
                        //System.out.println(element);
                        userGuess[i] = Integer.parseInt(element);
                        i++;
                    }
                    //Check each element in the array
                    for(int element: userGuess)
                    {
                        if((element > 8 || element < 1))
                        {
                            System.out.println("One of your numbers was outside of the number range (1-8)\n");
                            isValidInput = false;
                        }

                    }
                }
            }
            catch(Exception e)
            {
                //e.printStackTrace();
                //System.out.println(e.getMessage());
                System.out.println("Invalid data was inserted. Come on now!\n");
                isValidInput = false;    
            }    
        }
        gameAttempts.add(new Attempt(userGuess));
        //System.out.println("Guess added");
        return true;
    }
    //Allows user to play the game again if they so choose
    private void reset()
    {
        boolean validInput = false;
        String response;
        while(validInput == false)
        {
            validInput = true;
            try
            {
                System.out.println("Would you like to reset the game? (Y/N)\n");
                response = userInput.nextLine();
                switch(response.toLowerCase().charAt(0))
                {
                    case 'y':
                        initialize();
                        break;
                    case 'n':
                        System.out.println("Quitting game...");
                        break;
                    default:
                        validInput = false;
                        System.out.println("Invalid input, try again!\n");
                        break;
                }
            }
            catch(Exception e)
            {
                validInput = false;
                System.out.println("Invalid input, try again!\n");
            }
        }    
        
        
    }
    //Execute end game scenarios depending on if player won, lost, or just doesn't wanna play
    private void endGame()
    {
        switch(finisher)
        {
            case 1:
                System.out.println("\n-------------------------------------------------------------\nYou won! Here are all your guesses!");
                printAttempts();
                reset();
                break;
            case -1:
                System.out.println("\n-------------------------------------------------------------\nYou lost! Loser! Here is the code!");
                for(int element: code)
                    System.out.print(element + ",");
                System.out.println("\n-------------------------------------------------------------\nHere are your guesses!");
                printAttempts();
                reset();
                break;
            default:
                System.out.println("\n-------------------------------------------------------------\nNot entirely sure how it ended up here, but game will finish executing.");
                printAttempts();
                break;
        }

    }
    //Compare the player's guess with the generated code
    private void validate(Attempt guess)
    {
        boolean[] shouldNotLookAt = new boolean[4];
        int cows = 0, bulls = 0;
        
        for(int i = 0; i < 4; i++)
        {
            if(code[i] == guess.getPlayerGuess()[i])
            {
                cows++;
                shouldNotLookAt[i] = true;
            }
        }
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if( !(i==j) && shouldNotLookAt[j] == false && code[j] == guess.getPlayerGuess()[i])
                {
                    bulls++;
                    shouldNotLookAt[j] = true;
                }
            }
        }
        
        guess.setCows(cows);
        guess.setBulls(bulls);
        printAttempts();
    }
    //Print out a nice summary of a player's past attempts
    private void printAttempts()
    {
       System.out.println("Attempts:" + "\n--------------------------------------------------------------");
        for(Attempt userGuess: gameAttempts)
           System.out.println("Attempt " + (gameAttempts.indexOf(userGuess) + 1) +": " + userGuess.toString());
       System.out.print("\n");
    }
    private void initialize()
    {
        //initialize variables
        userInput = new Scanner(System.in);
        attempts = 0;
        theRandom = new Random();
        code = new int[4];
        gameAttempts = new ArrayList();
        reset = true;
        finisher = 0;
        //generate our code
        for(int i = 0; i < 4; i++)
        {
            code[i] = theRandom.nextInt(8) + 1;
        }
        
        /*for(int number: code)
            System.out.println(number);*/
    }
}
