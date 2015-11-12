package models.responses;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class Response<K, V>
{
    private String message;

    public enum Status
    {
        OK,
        LOGIN_OK,
        LOGIN_ERROR,
        ERROR,
        CHALLENGE_INVITED,
        CHALLENGE_ACCEPTED,
        PLAYERLIST_RECEIVED,
        GAME,
        GAME_OVER,
        MATCH_STARTED,
        TURN,
        MOVE;
    }

    public Response(Status status, Map<K, V> payload)
    {
        // Add teh status to the arraylist to indicate the type of message
        payload.put((K) "STATUS", (V) status);

        this.message = mapToJsonString(payload);
    }

    private String mapToJsonString(Map<K, V> payload)
    {
        //Create a json string from the hashmap with the response data
        Gson gson = new Gson();
        return gson.toJson(payload);
    }

    @Override
    public String toString()
    {
        return message;
    }

}
