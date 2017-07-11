

package mastermind;

//******************************************************************************
//  Attempt class
//  A separate class which allows us to track player guesses and how many cows and
//   bulls were found.
//  Date Created: July 10th 2017
//  Last Updated: July 11th 2017
//******************************************************************************

public class Attempt 
{
    private int cows, bulls;
    private int[] playerGuess;

    
   public Attempt(int[] playerGuess)
   {
       this.playerGuess = playerGuess;
       cows = 0; bulls = 0;

   }

    public int getCows() 
    {
        return cows;
    }

    public int getBulls() 
    {
        return bulls;
    }

    public void setCows(int cows) 
    {
        this.cows = cows;
    }

    public void setBulls(int bulls) 
    {
        this.bulls = bulls;
    }
    
    //Create a String which contains the guess code, number of cows, number of bulls
    @Override
    public String toString()
    {
       
       String output = "";
       
       for(int element: playerGuess)
           output = output + element + ",";
       
       output = output + ".................... Cows: " + cows + ". Bulls: " + bulls;
       
       return output;
    }

    public int[] getPlayerGuess() 
    {
        return playerGuess;
    }
    
    
   
}
