package server;

import utils.ConfigFile;
import utils.GameModuleLoader;
import utils.Log;

import java.io.File;

/**
 * Created by akatchi on 2-8-15.
 */
public class Application
{
    private static Application instance = new Application();

    private ConfigFile configFile = new ConfigFile();

    private GameServer gameServer;
    private GameModuleLoader gameLoader;

    private Application()
    {
        try
        {
            File gameModulePath = new File(configFile.getValue(ConfigFile.GAMEMODULE_PATH));

            if( !gameModulePath.exists() )
            {
                gameModulePath.mkdirs();
            }

            gameLoader = new GameModuleLoader(gameModulePath);

            for( String gameType: gameLoader.getGameTypeList() )
            {
                Log.DEBUG("Loaded game module: " + gameType);
            }

            gameServer = new GameServer();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    public static Application getInstance()
    {
        return instance;
    }

    public GameServer getGameServer()
    {
        return gameServer;
    }

    public GameModuleLoader getGameLoader()
    {
        return gameLoader;
    }

    public static void main(String[] args)
    {
        getInstance();
    }
}
