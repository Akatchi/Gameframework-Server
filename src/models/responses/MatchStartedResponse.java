package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 10-8-15.
 */
public class MatchStartedResponse<K, V> extends Response
{
    public MatchStartedResponse(Map<K, V> payload)
    {
        super(Status.MATCH_STARTED, payload);
    };
}
