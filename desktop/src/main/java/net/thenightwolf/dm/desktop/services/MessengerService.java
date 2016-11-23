package net.thenightwolf.dm.desktop.services;

import net.thenightwolf.dm.common.model.message.ConversationMessageBundle;
import net.thenightwolf.dm.common.model.message.Message;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MessengerService {

    @FormUrlEncoded
    @POST("/thread/{id}")
    Call<ConversationMessageBundle> getThreadBundle(@Header("Authorization") String token,
                                                    @Path("id") int threadID,
                                                    @Field("limit") int limit,
                                                    @Field("offset") int offset);
    @POST("/updates")
    Call<List<Message>> getUpdates(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/sms")
    Call<String> sendSMS(@Header("Authorization") String token,
                         @Field("number") String number,
                         @Field("message") String message);
}
