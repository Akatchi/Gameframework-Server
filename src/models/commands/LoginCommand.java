package models.commands;

import models.Client;
import models.responses.*;
import server.Application;
import utils.Log;
import utils.ResponseMapGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 8-8-15.
 */
public class LoginCommand extends AbstractCommandHandler
{
    private ResponseMapGenerator responseMapGenerator;

    public LoginCommand()
    {
        super("login");
        responseMapGenerator = new ResponseMapGenerator();
    }

    @Override
    public void handleCommand(Client client, ClientCommand command)
    {
        if( command.getArgument().equals("") )
        {
            client.writeResponse(new LoginErrorResponse(responseMapGenerator.getNoLoginNameEnteredMap()));
        }
        else if( client.isLoggedIn() )
        {
            client.writeResponse(new LoginErrorResponse(responseMapGenerator.getAlreadyLoggedInMap()));
        }
        else
        {
            if( Application.getInstance().getGameServer().getClientManager().login(client, command.getArgument()) )
            {
                client.writeResponse(new LoginOkResponse(responseMapGenerator.getLoginSuccessMap()));
            }
            else
            {
                client.writeResponse(new LoginErrorResponse(responseMapGenerator.getNameAlreadyInUseMap()));
            }
        }
    }

    @Override
    public String getDescription()
    {
        return "Login as player.";
    }

    @Override
    public List<String> getUsage()
    {
        List<String> usageList = new ArrayList<String>();

        usageList.add("usage: login <player name>");
        usageList.add("");
        usageList.add("Valid options and arguments:");
        usageList.add("  <player name>    : Name to login with");

        return usageList;
    }
}
