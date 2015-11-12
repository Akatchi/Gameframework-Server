package models.responses;

import java.util.Map;

/**
 * Created by akatchi on 8-8-15.
 */
public class LoginOkResponse<K, V> extends Response
{
    public LoginOkResponse(Map<K, V> payload)
    {
        super(Status.LOGIN_OK, payload);
    }
}
