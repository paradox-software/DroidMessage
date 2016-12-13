/*
 * Copyright (c) 2016 Jordan Knott
 * License file can be found in the root directory (LICENSE.txt)
 *
 * For the project DroidMessage, which can be found at: www.github.com/paradox-software/droidmessage
 *
 */

package net.thenightwolf.dm.desktop.controller.inner;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.thenightwolf.dm.common.model.message.*;
import net.thenightwolf.dm.desktop.controller.components.card.ContactCard;
import net.thenightwolf.dm.desktop.controller.components.card.ThreadCard;
import net.thenightwolf.dm.desktop.controller.components.icon.EmoticonIcon;
import net.thenightwolf.dm.desktop.controller.components.icon.InsertIcon;
import net.thenightwolf.dm.desktop.controller.components.icon.SearchIcon;
import net.thenightwolf.dm.desktop.controller.components.message.MessageGrid;
import net.thenightwolf.dm.desktop.controller.subscribers.MessageUpdater;
import net.thenightwolf.dm.desktop.controller.subscribers.message.MessageSenderSubscriber;
import net.thenightwolf.dm.desktop.controller.subscribers.refresher.RefresherInterface;
import net.thenightwolf.dm.desktop.controller.subscribers.refresher.RefresherSubscriber;
import net.thenightwolf.dm.desktop.data.LocalMessageBundle;
import net.thenightwolf.dm.desktop.services.MessengerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@FXMLController(value = "/view/MessengerOverview.fxml", title = "DroidMessage")
public class MessengerOverviewController implements MessageUpdater, RefresherInterface {

    private static final Logger logger = LoggerFactory.getLogger(MessengerOverviewController.class);

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXListView<ContactCard> contactList;

    @FXML
    private JFXListView<ThreadCard> threadList;

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

    private String selectedContactNumber;
    private ServerBundle bundle;

    private MessageGrid mGrid;
    private ScrollPane messageWrapper;

    private ObservableList<ContactCard> contactCardList;
    private ObservableList<ThreadCard> threadCardList;

    @PostConstruct
    public void init() {
        try {
            createNodesAndLists();

            setBundleFromContext();

            Retrofit retrofit = buildRetrofit();

            loadDataIntoLists();

            setupIcons();

            setupContactListAndBindSearchField();

            setupThreadList(retrofit);

            String ownerNumber = "9189231326";
            new RefresherSubscriber.RefresherBuilder(this)
                    .setBundle(bundle)
                    .setRetrofit(retrofit)
                    .setOwnerNumber(ownerNumber)
                    .build()
                    .subscribe();

            new MessageSenderSubscriber.MessageSenderBuilder(this)
                    .setBundle(bundle)
                    .setRetrofit(retrofit)
                    .setOwnerPhoneNumber(ownerNumber)
                    .setTextField(textField)
                    .build()
                    .subscribe();

        } catch (Exception e) {
            logger.error("Exception in MessengerOverview", e);
        }
    }

    private String numberToName(List<Contact> contactList, String number) {
        for (Contact contact : contactList) {
            if (contact.cleanNumber.equals(PhoneFormatter.cleanPhoneNumber(number)))
                return contact.name;
        }
        return PhoneFormatter.cleanPhoneNumber(number);
    }

    private void scrollToBottom() {
        messageContainer.layout();
        messageWrapper.setVvalue(messageWrapper.getVmax());
    }

    @Override
    public String getName(ServerBundle bundle, Message message) {
        return numberToName(bundle.manifest.contacts, message.getNumber());
    }

    @Override
    public void addMessage(Message message) {
        mGrid.addSms(message);
        scrollToBottom();
    }

    @Override
    public ObservableList<ThreadCard> getThreadCardList() {
        return threadCardList;
    }

    private void setupThreadList(Retrofit retrofit) {
        threadList.setItems(threadCardList);

        threadList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("Clicking in threadList!");

            int threadID = newValue.getConversation().getConversationId();
            selectedContactNumber = newValue.getNumber();

            MessengerService messengerService = retrofit.create(MessengerService.class);
            Call<ConversationMessageBundle> manifestCall = messengerService.getThreadBundle(bundle.token, threadID, 200, 0);

            try {
                ConversationMessageBundle conversationMessageBundle = manifestCall.execute().body();
                LocalMessageBundle localBundle = new LocalMessageBundle(conversationMessageBundle, newValue.getContactImage());
                mGrid.reset();
                mGrid.load(localBundle);
                for (Message message : conversationMessageBundle.messages) {
                    logger.info("Message [{}]: {} - {}", message.getContent(), message.getNumber(), message.getContent());
                }
                messageWrapper.setFitToWidth(true);
                scrollToBottom();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }));
    }

    private void setupContactListAndBindSearchField() {
        FilteredList<ContactCard> contactFilteredData = new FilteredList<>(contactCardList, s -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            contactList.getSelectionModel().clearSelection();
            contactFilteredData.setPredicate(s -> s.getContactNameProperty().getValue().toLowerCase().contains(newValue.toLowerCase()));
        });

        contactList.setItems(contactFilteredData);

        contactList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
            selectedContactNumber = newValue.getContactNumberProperty().getValue()
        ));
    }

    private void setupIcons() {
        emoticonButton.setGraphic(new EmoticonIcon(24));
        imageButton.setGraphic(new InsertIcon(24));
        searchButton.setGraphic(new SearchIcon(24));
    }

    private void createNodesAndLists() {
        mGrid = new MessageGrid();
        messageWrapper = new ScrollPane(mGrid);
        messageContainer.getChildren().add(messageWrapper);

        contactCardList = FXCollections.observableArrayList();
        threadCardList = FXCollections.observableArrayList();
    }

    private void setBundleFromContext() {
        this.bundle = (ServerBundle) context.getRegisteredObject("ServerBundle");
    }

    private void loadDataIntoLists(){
        contactCardList.addAll(bundle.manifest.contacts.stream().map(ContactCard::new).collect(Collectors.toList()));
        threadCardList.addAll(bundle.manifest.threads.stream().map(thread -> new ThreadCard(thread, numberToName(bundle.manifest.contacts, thread.getAddress()))).collect(Collectors.toList()));
    }

    private Retrofit buildRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://" + bundle.url + ":5001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}