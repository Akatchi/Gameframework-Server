package models;

import models.responses.Response;
import server.Application;
import server.ClientManager;
import utils.ReadWriteBuffer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 2-8-15.
 */
public class Client
{
    private String playerName;
    private boolean loggedIn;

    private SocketChannel socket;
    private ReadWriteBuffer buffers;
    private ClientManager clientManager;

    private Match activeMatch;
    private Challenge challenge;
    private List<Challenge> challenges;

    public Client(SocketChannel socket, ReadWriteBuffer buffers, ClientManager clientManager)
    {
        this.socket = socket;
        this.buffers = buffers;
        this.clientManager = clientManager;

        clientManager.addClient(this);

        loggedIn = false;

        activeMatch = null;
        challenge = null;
        challenges = new ArrayList<Challenge>();
    }

    public void writeResponse(Response response)
    {
        writeLine(response.toString());
    }

    public void writeLine(String line)
    {
        if( line != null )
        {
            line = line + "\r\n";

            synchronized(buffers.getWriteBuffer())
            {
                buffers.getWriteBuffer().put(line.getBytes());
            }
        }

        Application.getInstance().getGameServer().setWritable(socket);
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }

    public ReadWriteBuffer getBuffers()
    {
        return buffers;
    }

    public ByteBuffer getWriteBuffer()
    {
        return buffers.getWriteBuffer();
    }

    public ByteBuffer getReadBuffer()
    {
        return buffers.getReadBuffer();
    }

    public Challenge getChallenge()
    {
        return challenge;
    }

    public Challenge getChallengeWithId(int challengeNumber)
    {
        for( Challenge challenge : challenges )
        {
            if( challenge.getChallengeNumber() == challengeNumber )
            {
                return challenge;
            }
        }

        return null;
    }

    public void addChallenge(Challenge challenge)
    {
        challenges.add(challenge);
    }

    // TODO send notify whenever a challenge gets removed
    public void removeChallenge(Challenge challenge)
    {
        challenges.remove(challenge);
    }

    public void setChallenge(Challenge challenge)
    {
        this.challenge = challenge;
    }

    public ClientManager getClientManager()
    {
        return clientManager;
    }

    public Match getActiveMatch()
    {
        return activeMatch;
    }

    public void setActiveMatch(Match activeMatch)
    {
        this.activeMatch = activeMatch;
    }

    @Override
    public boolean equals(Object obj)
    {
        Client other = (Client) obj;

        //TODO NULL CHECK!

        if( this.getPlayerName().equals(other.getPlayerName()) )
        {
            return true;
        }

        return false;
    }
}
