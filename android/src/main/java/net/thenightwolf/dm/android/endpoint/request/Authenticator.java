package net.thenightwolf.dm.android.endpoint.request;

import com.google.gson.Gson;
import net.thenightwolf.dm.android.TokenGenerator;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Authenticator {

    private static final Logger logger = LoggerFactory.getLogger(Authenticator.class);


    public boolean authenticate(IHTTPSession session){
        String token = session.getHeaders().get("authorization");
        for(String header: session.getHeaders().keySet()){
            System.out.println(header);
        }
        return TokenGenerator.validateToken(token);
    }

    public String buildErrorResponse(){
        return new Gson().toJson("Authentication failure");
    }
}
