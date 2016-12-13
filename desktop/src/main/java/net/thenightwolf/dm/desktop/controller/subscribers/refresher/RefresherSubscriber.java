package net.thenightwolf.dm.desktop.controller.subscribers.refresher;

import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.desktop.controller.components.card.ThreadCard;
import net.thenightwolf.dm.desktop.controller.subscribers.MessageUpdater;
import net.thenightwolf.dm.desktop.services.MessengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import rx.Observable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RefresherSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(RefresherSubscriber.class);

    private Retrofit retrofit;
    private ServerBundle bundle;
    private RefresherInterface refresher;
    private String ownerNumber;

    private RefresherSubscriber(Retrofit retrofit, ServerBundle bundle, RefresherInterface refresher, String ownerNumber) {
        this.retrofit = retrofit;
        this.bundle = bundle;
        this.refresher = refresher;
        this.ownerNumber = ownerNumber;
    }

    public void subscribe(){
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .map(tick -> {
                    MessengerService service = retrofit.create(MessengerService.class);
                    Call<List<Message>> updateCall = service.getUpdates(bundle.token);
                    List<Message> messageList = null;
                    try {
                        messageList = updateCall.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return messageList;
                })
                .filter(list -> list != null)
                .observeOn(JavaFxScheduler.getInstance())
                .forEach(list -> {
                    for (Message message : list) {
                        logger.info("SMS update found [{}]: {}", message.getNumber(), message.getContent());
                        if (message.getNumber().equals(ownerNumber)) {
                            ((MessageUpdater) refresher).addMessage(message);
                        } else {
                            Iterator<ThreadCard> iterator = refresher.getThreadCardList().iterator();
                            while(iterator.hasNext()) {
                                ThreadCard card = iterator.next();
                                if (card.getNumber().equals(message.getNumber())) {
                                    card.setThreadSnippet(message.getContent());
                                    card.setThreadDate(message.getSentDate());
                                } else {
                                    refresher.getThreadCardList().add(new ThreadCard(message, refresher.getName(bundle, message)));
                                }
                            }
                        }
                    }
                });
    }

    public static class RefresherBuilder {
        private Retrofit retrofit;
        private ServerBundle bundle;
        private RefresherInterface refresher;
        private String ownerNumber;

        public RefresherBuilder(RefresherInterface refresher) {
            this.refresher = refresher;
        }

        public RefresherBuilder setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }

        public RefresherBuilder setBundle(ServerBundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public RefresherBuilder setOwnerNumber(String ownerNumber) {
            this.ownerNumber = ownerNumber;
            return this;
        }

        public RefresherSubscriber build(){
            return new RefresherSubscriber(retrofit, bundle, refresher, ownerNumber);
        }
    }
}
