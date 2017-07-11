
package mastermind;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import org.apache.commons.lang3.text.WordUtils;


public class Game 
{
    //If finisher is 0, continue game. If it is -1, player lost. If 1, player won.
    private int attempts, finisher;
    private Scanner userInput;
    private Random theRandom;
    private ArrayList<Attempt> gameAttempts;
    private int[] code;
    private boolean reset;
    
    public Game()
    {
        userInput = new Scanner(System.in);
        attempts = 0;
        theRandom = new Random();
        code = new int[4];
        gameAttempts = new ArrayList();
        reset = true;
        finisher = 0;
        for(int i = 0; i < 4; i++)
        {
            code[i] = theRandom.nextInt(8) + 1;
        }
        
        /*for(int number: code)
            System.out.println(number);*/
        
        System.out.println(WordUtils.wrap("Welcome to Cows and Bulls! In this game, "
                + "a 4 digit code is randomly generated (digits are between 1 and 8) , "
                + "and you have 10 attempts to crack the code."
                + "You determine what the code is using Cows and Bulls. If you get a Cow, "
                + "that means you have the right number in the right place. If you "
                + "get a bull, you have the right number in the wrong place. \n", 100));
    }
    
    public void runGame()
    {
       while (reset == true)
       {
           reset = false;
           while(finisher == 0)
           {
               if(getUserInput())
               {
                   validate(gameAttempts.get(attempts));
                   if(gameAttempts.get(attempts).getCows() == 4)
                   {
                        finisher = 1;
                   }
               }
               else
               {
                   finisher = -2;
               }
               attempts++;
               if(attempts > 9)
                   finisher = -1;
           }
           endGame();
       }
       
       
    }
    private boolean getUserInput()
    {
       
        int[] userGuess = new int[4];
        String inputLine;
        boolean isValidInput = false;

        while(isValidInput == false)
        {
            isValidInput = true;
            
            System.out.println("Please input your guess (4 numbers from 1 to 8), separated by commas, on one line. \n"
                + "If you would like to end the game, please input 9. \n"
                + "If you would like to see all previous attempts, please input 10\n");
            
            try
            {
                inputLine = userInput.nextLine();
                if(!inputLine.contains(","))
                {
                    switch(Integer.parseInt(inputLine))
                    {
                        case 9:
                            attempts = -19;
                            return false;
                        case 10:
                            if(gameAttempts.get(0) != null)
                                printAttempts();
                            isValidInput = false;
                            break;
                        case 420:
                            System.out.println("420 blaze it");
                            isValidInput = false;
                            break;
                        default:
                           isValidInput = false; 
                    }   
                }
                else
                {
                    String[] inputAsString = inputLine.split(",");
                    int i =0;
                    for(String element: inputAsString)
                    {
                        System.out.println(element);
                        userGuess[i] = Integer.parseInt(element);
                        i++;
                    }

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
                System.out.println("Invalid data was inserted. Come on now!\n");
                isValidInput = false;    
            }    
        }
        gameAttempts.add(new Attempt(userGuess));
        System.out.println("Guess added");
        return true;
    }
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
                        userInput = new Scanner(System.in);
                        attempts = 0;
                        theRandom = new Random();
                        code = new int[4];
                        gameAttempts = new ArrayList();
                        reset = true;
                        finisher = 0;
                        for(int i = 0; i < 4; i++)
                        {
                            code[i] = theRandom.nextInt(8) + 1;
                        }

                        for(int number: code)
                            System.out.println(number);
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
                System.out.println("\n-------------------------------------------------------------\nYou lost! Loser! Here are your guesses!");
                printAttempts();
                reset();
                break;
            case -2:
                System.out.println("Quitting game...");
                break;
            default:
                System.out.println("\n-------------------------------------------------------------\nNot entirely sure how it ended up here, but game will finish executing.");
                printAttempts();
                break;
        }

    }
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
    private void printAttempts()
    {
       for(Attempt userGuess: gameAttempts)
           System.out.println(userGuess.toString());
       System.out.print("\n");
    }
}
