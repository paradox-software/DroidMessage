package net.thenightwolf.dm.android.endpoint.request;

import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.router.RouterNanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class StandardRequestHandler extends RouterNanoHTTPD.DefaultHandler {

    private static final Logger logger = LoggerFactory.getLogger(StandardRequestHandler.class);


    private HashMap<String, String> map = new HashMap<>();

    public void init(IHTTPSession session){
        try {
            session.parseBody(map);
        } catch (IOException | NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
    }

    public abstract String postMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException;

    public abstract String getMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException;

    public abstract String putMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException;

    public abstract String updateMethod(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException;

    //public abstract String getText(Map<String, String> params, IHTTPSession session) throws IOException, NanoHTTPD.ResponseException;

    @Override
    public Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        try {
            System.out.println("Parsing parameters");
            init(session);
            String text = null;
            System.out.println("Checking method...");
            try {
                switch (session.getMethod()) {
                    case GET:
                        System.out.println("Method: GET");
                        text = getMethod(urlParams, session);
                        break;
                    case POST:
                        System.out.println("Method: POST");
                        text = postMethod(urlParams, session);
                        break;
                    case PUT:
                        System.out.println("Method: PUT");
                        text = putMethod(urlParams, session);
                        break;
                    default:
                        text = "Unknown method";
                        System.out.println("Method: Default");
                        logger.error("Unknown method! Please use PUT, GET, or POST");
                }
            } catch (IOException | NanoHTTPD.ResponseException e) {
                logger.error("Exception in StandardRequestHandler", e);
            }
            ByteArrayInputStream inp = new ByteArrayInputStream(text != null ? text.getBytes() : new byte[0]);
            int size = text != null ? text.getBytes().length : 0;
            return Response.newFixedLengthResponse(getStatus(), getMimeType(), inp, size);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
