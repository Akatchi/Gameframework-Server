package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class LoginErrorResponse<K, V> extends Response
{
    public LoginErrorResponse(Map<K, V> payload)
    {
        super(Status.LOGIN_ERROR, payload);
    }
}
