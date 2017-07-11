

package mastermind;

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
