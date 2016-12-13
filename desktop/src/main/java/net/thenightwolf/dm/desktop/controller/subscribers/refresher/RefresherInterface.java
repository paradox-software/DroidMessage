package net.thenightwolf.dm.desktop.controller.subscribers.refresher;

import javafx.collections.ObservableList;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.controller.components.card.ThreadCard;
import rx.Observable;

public interface RefresherInterface {
    public String getName(ServerBundle bundle, Message message);
    public ObservableList<ThreadCard> getThreadCardList();
}
