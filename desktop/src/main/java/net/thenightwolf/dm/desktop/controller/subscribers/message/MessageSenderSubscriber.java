package net.thenightwolf.dm.desktop.controller.subscribers.message;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.thenightwolf.dm.common.model.message.Message;
import net.thenightwolf.dm.common.model.message.ServerBundle;
import net.thenightwolf.dm.common.model.message.builder.MessageBuilder;
import net.thenightwolf.dm.desktop.controller.subscribers.MessageUpdater;
import net.thenightwolf.dm.desktop.services.MessengerService;
import retrofit2.Call;
import retrofit2.Retrofit;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import java.util.Date;

public class MessageSenderSubscriber {

    private Retrofit retrofit;
    private ServerBundle bundle;
    private TextField textField;
    private String ownerPhoneNumber;
    private MessageUpdater messageSender;

    private MessageSenderSubscriber(Retrofit retrofit, ServerBundle bundle, TextField textField, MessageUpdater messageSender, String ownerPhoneNumber) {
        this.retrofit = retrofit;
        this.bundle = bundle;
        this.textField = textField;
        this.messageSender = messageSender;
        this.ownerPhoneNumber = ownerPhoneNumber;
    }

    public void subscribe(){
        JavaFxObservable.fromNodeEvents(textField, KeyEvent.KEY_RELEASED)
                .observeOn(Schedulers.io())
                .filter(e -> e.getCode() == KeyCode.ENTER)
                .map(e -> {
                    String message = textField.getText();
                    textField.setText("");
                    return message;
                })
                .filter(message -> !message.equals(""))
                .map(message -> {
                    MessengerService service = retrofit.create(MessengerService.class);
                    Call<String> sendCall = service.sendSMS(bundle.token, ownerPhoneNumber, message);
                    try {
                        String s = sendCall.execute().body();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return message;
                })
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(messageContent -> {
                    Message message = new MessageBuilder(-1)
                            .setSMSMessage()
                            .setContent(messageContent)
                            .setNumber(ownerPhoneNumber)
                            .setSentDate(new Date())
                            .build();
                    messageSender.addMessage(message);
                });
    }


    public static class MessageSenderBuilder {
        private Retrofit retrofit;
        private ServerBundle bundle;
        private TextField textField;
        private String ownerPhoneNumber;
        private MessageUpdater messageSender;

        public MessageSenderBuilder(MessageUpdater  subscriber) {
            this.messageSender = subscriber;
        }

        public MessageSenderBuilder setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }

        public MessageSenderBuilder setBundle(ServerBundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public MessageSenderBuilder setTextField(TextField textField) {
            this.textField = textField;
            return this;
        }

        public MessageSenderBuilder setOwnerPhoneNumber(String number){
            this.ownerPhoneNumber = number;
            return this;
        }

        public MessageSenderSubscriber build(){
            return new MessageSenderSubscriber(retrofit, bundle, textField, messageSender, ownerPhoneNumber);
        }
    }
}
