/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.FXMLController;
import io.datafx.controller.context.ApplicationContext;
import io.datafx.controller.context.FXMLApplicationContext;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.thenightwolf.dm.common.model.comparator.TemporalThreadComparator;
import net.thenightwolf.dm.common.model.message.*;
import net.thenightwolf.dm.desktop.MainApp;
import net.thenightwolf.dm.desktop.controller.components.card.ContactCard;
import net.thenightwolf.dm.desktop.controller.components.card.ThreadCard;
import net.thenightwolf.dm.desktop.controller.components.icon.EmoticonIcon;
import net.thenightwolf.dm.desktop.controller.components.icon.InsertIcon;
import net.thenightwolf.dm.desktop.controller.components.icon.SearchIcon;
import net.thenightwolf.dm.desktop.controller.components.message.MessageGrid;
import net.thenightwolf.dm.desktop.services.MessengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@FXMLController(value = "/view/MessengerOverview.fxml", title = "DroidMessage")
public class MessengerOverviewController {

    private static final Logger logger = LoggerFactory.getLogger(MessengerOverviewController.class);


    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXListView<ContactCard> contactList;

    @FXML
    private JFXListView<ThreadCard> threadList;
    //private ListView<ThreadCard> threadList;

    @FXML
    private VBox messageContainer;

    @FXML
    private JFXTextField textField;

    @FXML
    private JFXButton textSendButton;

    @FXML
    private StackPane root;


    @FXML
    private JFXButton emoticonButton;

    @FXML
    private JFXButton imageButton;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXButton searchButton;

    @FXMLApplicationContext
    private ApplicationContext appContext;


    private String ownerNumber = "9189231326";
    private String selectedContactNumber;
    private ServerBundle bundle;


    private MessageGrid mGrid;
    private ScrollPane messageWrapper;

    private ObservableList<ContactCard> contactCardList;
    private ObservableList<ThreadCard> threadCardList;

    @PostConstruct
    public void init() {
        //root.getStylesheets().add(MainApp.class.getResource("/view/css/twilight.css").toExternalForm());

        mGrid = new MessageGrid();
        messageWrapper = new ScrollPane(mGrid);
        messageContainer.getChildren().add(messageWrapper);

        appContext = ApplicationContext.getInstance();

        ServerBundle bundle = (ServerBundle) context.getRegisteredObject("ServerBundle");
        this.bundle = bundle;
        contactCardList = FXCollections.observableArrayList();
        threadCardList = FXCollections.observableArrayList();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + bundle.url + ":5001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contactCardList.addAll(bundle.manifest.contacts.stream().map(ContactCard::new).collect(Collectors.toList()));

        threadCardList.addAll(bundle.manifest.threads.stream().map(thread -> new ThreadCard(thread, numberToName(bundle.manifest.contacts, thread.address))).collect(Collectors.toList()));

        emoticonButton.setGraphic(new EmoticonIcon(24));
        imageButton.setGraphic(new InsertIcon(24));
        searchButton.setGraphic(new SearchIcon(24));

        FilteredList<ContactCard> contactFilteredData = new FilteredList<ContactCard>(contactCardList, s -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            contactList.getSelectionModel().clearSelection();
           contactFilteredData.setPredicate(s -> s.getContactNameProperty().getValue().toLowerCase().contains(newValue.toLowerCase()));
        });

        contactList.setItems(contactFilteredData);
        threadList.setItems(threadCardList);

        contactList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            selectedContactNumber = newValue.getContactNumberProperty().getValue();

        }));
        threadList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Clicking in threadList!");

            int threadID = newValue.getConvoThread().thread_id;
            selectedContactNumber = newValue.getNumber();

            MessengerService messengerService = retrofit.create(MessengerService.class);
            Call<ThreadBundle> manifestCall = messengerService.getThreadBundle(bundle.token, threadID, 200, 0);

            try {
                ThreadBundle threadBundle = manifestCall.execute().body();
                mGrid.reset();
                mGrid.load(threadBundle);
                for(Sms sms : threadBundle.messages){
                    logger.debug("SMS: {} - {}", sms.getNumber(), sms.getMessage());
                }
                messageWrapper.setFitToWidth(true);
                scrollToBottom();
           } catch (IOException e) { e.printStackTrace(); }

        }));

        Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> {
                    MessengerService service = retrofit.create(MessengerService.class);
                    Call<List<Sms>> updateCall = service.getUpdates(bundle.token);

                    List<Sms> smsList = null;
                    try {
                        smsList = updateCall.execute().body();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return smsList;
                })
                .filter(list -> list != null)
                .observeOn(JavaFxScheduler.getInstance())
                .forEach(list -> {
                    for (Sms sms : list) {
                        logger.info("SMS update found [{}]: {}", sms.getNumber(), sms.getMessage());
                        if(sms.getNumber().equals(selectedContactNumber)){
                            mGrid.addSms(sms);
                            scrollToBottom();
                        } else {
                            for(ThreadCard card : threadCardList){
                                if(card.getNumber().equals(sms.getNumber())){
                                    card.setThreadSnippet(sms.getMessage());
                                    card.setThreadDate(sms.getSentDate());
                                } else {
                                    threadCardList.add(new ThreadCard(sms, numberToName(bundle.manifest.contacts, sms.getNumber())));
                                }
                            }
                        }
                    }
                });

        JavaFxObservable.fromNodeEvents(textField, KeyEvent.KEY_RELEASED)
                .observeOn(Schedulers.io())
                .filter(e -> e.getCode() == KeyCode.ENTER)
                .map(e -> textField.getText())
                .filter(message -> !message.equals(""))
                .map(message -> {
                    MessengerService service = retrofit.create(MessengerService.class);
                    Call<String> sendCall = service.sendSMS(bundle.token, selectedContactNumber, message);
                    try {
                        String s = sendCall.execute().body();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    return message;
                })
                .observeOn(JavaFxScheduler.getInstance())
                .subscribe(message -> {
                    mGrid.addSms(new Sms(50, message, ownerNumber, new Date()));
                    textField.setText("");
                    scrollToBottom();
                });
    }

    public String numberToName(List<Contact> contactList, String number){
        for(Contact contact : contactList){
            if(contact.cleanNumber.equals(PhoneFormatter.cleanPhoneNumber(number)))
                return contact.name;
        }
        return PhoneFormatter.cleanPhoneNumber(number);
    }

    private void scrollToBottom(){
        messageContainer.layout();
        messageWrapper.setVvalue(messageWrapper.getVmax());
    }
}
