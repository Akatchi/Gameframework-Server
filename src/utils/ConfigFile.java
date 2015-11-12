package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Vasco on 30-7-2015.
 */
public class ConfigFile
{
    public static final String HOST_IP = "host_ip";
    public static final String HOST_PORT = "host_port";
    public static final String GAMEMODULE_PATH = "gamemodule_path";

    private String configFileName = "config.properties";

    private File configFile;
    private Properties properties = new Properties();

    public ConfigFile()
    {
        configFile = new File(configFileName);

        if( !configFile.exists() )
        {
            try
            {
                configFile.createNewFile();

                loadDefaultConfigValues();
            }
            catch( IOException ioe )
            {
                throw new RuntimeException("Got an IOException while trying to make a new config file!", ioe);
            }
        }
    }

    public String getValue(String key)
    {
        return getValue(key, null);
    }

    public String getValue(String key, String defaultValue)
    {
        String value = null;
        FileInputStream input = openInputStream();

        if( input != null )
        {
            try
            {
                properties.load(input);

                value = properties.getProperty(key, defaultValue);

                input.close();
            }
            catch( IOException ioe )
            {
                Log.ERROR("IOException while trying to retrieve the string value!\n" + ioe.getMessage());
                value = null;
            }
        }

        return value;
    }

    private FileInputStream openInputStream()
    {
        FileInputStream input = null;

        try
        {
            input = new FileInputStream(configFile);
        }
        catch( IOException ioe )
        {
            Log.ERROR("Error while trying to get the inputstream!\n" + ioe.getMessage());
        }

        return input;
    }

    private FileOutputStream openOutputStream()
    {
        FileOutputStream output = null;

        try
        {
            output = new FileOutputStream(configFile, false); //don't append to the file we will just set properties
        }
        catch( IOException ioe )
        {
            Log.ERROR("Error while trying to get the outputstream!\n" + ioe.getMessage());
        }

        return output;
    }

    public void setValue(String key, String value)
    {
        FileInputStream input = openInputStream();
        FileOutputStream output = openOutputStream();

        if( output != null )
        {
            try
            {
                //Load the properties
                properties.load(input);
                input.close();

                //Set the new propertie
                properties.setProperty(key, value);
                properties.store(output, null);
                output.close();
            }
            catch( IOException ioe )
            {
                Log.ERROR("IOException while trying to set a property!\n" + ioe.getMessage());
            }
        }
    }

    public void setMultipleValues(Map<String, String> valueMap)
    {
        FileInputStream input = openInputStream();
        FileOutputStream output = openOutputStream();

        if( output != null )
        {
            try
            {
                //Load the properties
                properties.load(input);
                input.close();

                //Set multiple new properties
                for(Map.Entry<String, String> entry : valueMap.entrySet())
                {
                    properties.setProperty(entry.getKey(), entry.getValue());
                }

                properties.store(output, null);
                output.close();
            }
            catch( IOException ioe )
            {
                Log.ERROR("IOException while trying to set multiple properties!\n" + ioe.getMessage());
            }
        }
    }

    private void loadDefaultConfigValues()
    {
        //Put the default values in a map
        Map<String, String> defaultConfigValues = new HashMap<String, String>();
            defaultConfigValues.put(HOST_IP, "127.0.0.1");
            defaultConfigValues.put(HOST_PORT, "8282");
            defaultConfigValues.put(GAMEMODULE_PATH, "gamemodules");

        setMultipleValues(defaultConfigValues);
    }
}
