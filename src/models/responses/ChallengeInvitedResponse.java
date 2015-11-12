package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class ChallengeInvitedResponse<K, V> extends Response
{
    public ChallengeInvitedResponse(Map<K, V> payload)
    {
        super(Status.CHALLENGE_INVITED, payload);
    }
}
