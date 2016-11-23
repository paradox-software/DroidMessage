package net.thenightwolf.dm.desktop.controller.subscribers.connection;

import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.controller.components.dialog.DirectConnectDialog;

public interface ConnectionInterface {
    public void showSpinner();
    public String getIpAddress();
    public String getPassword();
    public void showErrorDialog(String title, String message);
    public void connectionComplete(ServerBundle bundle);
}
