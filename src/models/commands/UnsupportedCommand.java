package models.commands;

import models.Client;
import models.responses.ClientCommand;
import models.responses.ErrorResponse;
import utils.ResponseMapGenerator;

import java.util.ArrayList;

public class UnsupportedCommand implements ICommand
{
	private ResponseMapGenerator responseMapGenerator;

    public UnsupportedCommand()
    {
        responseMapGenerator = new ResponseMapGenerator();
    }

	@Override
	public boolean isSupported(Client client, ClientCommand command)
	{
		return true;
	}
	
	@Override
	public void handleCommand(Client client, ClientCommand command)
	{
		client.writeResponse(new ErrorResponse(responseMapGenerator.getUnsupportedCommandMap(command.getAction())));
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public ArrayList<String> getUsage()
	{
		return null;
	}

	@Override
	public String getCommandName()
	{
		return null;
	}

}
