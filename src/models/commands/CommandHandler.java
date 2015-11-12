package models.commands;

import models.Client;
import models.responses.ClientCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akatchi on 8-8-15.
 */
public class CommandHandler
{
    private List<ICommand> commandList;

    public CommandHandler()
    {
        commandList = new ArrayList<ICommand>();
    }

    public void addCommand(ICommand command)
    {
        synchronized(commandList)
        {
            commandList.add(command);
        }
    }

    public void removeCommand(ICommand command)
    {
        synchronized(commandList)
        {
            commandList.remove(command);
        }
    }

    public List<ICommand> getCommandList()
    {
        return commandList;
    }

    public ICommand getCommand(Client client, ClientCommand clientCommand)
    {
        synchronized(commandList)
        {
            for( ICommand command : commandList )
            {
                if( command.isSupported(client, clientCommand) )
                {
                    return command;
                }
            }
        }

        return new UnsupportedCommand();
    }
}
