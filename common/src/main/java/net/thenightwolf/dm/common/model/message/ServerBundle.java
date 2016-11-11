package net.thenightwolf.dm.common.model.message;


import net.thenightwolf.dm.common.model.Manifest;

public class ServerBundle {
    public String url;
    public String token;
    public Manifest manifest;

    public ServerBundle(String url, String token, Manifest manifest) {
        this.url = url;
        this.token = token;
        this.manifest = manifest;
    }
}
