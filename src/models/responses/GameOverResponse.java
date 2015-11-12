package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 10-8-15.
 */
public class GameOverResponse<K, V> extends Response
{
    public GameOverResponse(Map<K, V> payload)
    {
        super(Status.GAME_OVER, payload);
    };
}
