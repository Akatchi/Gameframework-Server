package models;

/**
 * Created by akatchi on 9-8-15.
 */
public class Challenge
{
    private static int challengeCount = 1;

    private int challengeNumber;
    private Client challenger;
    private Client opponent;
    private String gameType;

    public Challenge(Client challenger, Client opponent, String gameType)
    {
        challengeNumber = challengeCount++;

        this.challenger = challenger;
        this.opponent = opponent;
        this.gameType = gameType;
    }

    public int getChallengeNumber()
    {
        return challengeNumber;
    }

    public Client getChallenger()
    {
        return challenger;
    }

    public Client getOpponent()
    {
        return opponent;
    }

    public String getGameType()
    {
        return gameType;
    }
}
