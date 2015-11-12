package models.responses;


import utils.Log;

/**
 * Created by akatchi on 8-8-15.
 */
public class ClientCommand
{
    private String action;
    private String argument;

    public ClientCommand(String command)
    {
        String[] trimmedCommand = command.split(" ", 2);

        action = trimmedCommand[0].trim();

        if( trimmedCommand.length > 1 )
        {
            argument = trimmedCommand[1].trim();
        }
        else
        {
            argument = "";
        }
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getArgument()
    {
        return argument;
    }

    public void setArgument(String argument)
    {
        this.argument = argument;
    }
}
