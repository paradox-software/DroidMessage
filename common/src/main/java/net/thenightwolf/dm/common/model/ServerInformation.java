package net.thenightwolf.dm.common.model;

public class ServerInformation {
    public String hostname;
    public String IPAddress;
    public String version;

    @Override
    public String toString() {
        return "ServerInformation{" +
                "hostname='" + hostname + '\'' +
                ", IPAddress='" + IPAddress + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
