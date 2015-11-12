package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class ChallengeAcceptedResponse<K, V> extends Response
{
    public ChallengeAcceptedResponse(Map<K, V> payload)
    {
        super(Status.CHALLENGE_ACCEPTED, payload);
    }
}
