package net.thenightwolf.dm.android.generator.update;

import net.thenightwolf.dm.common.model.message.Message;

import java.util.List;

public interface IUpdateGenerator {
    List<Message> getLatestMessages();
}
