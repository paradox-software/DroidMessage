package net.thenightwolf.dm.android.generator.authentication;

public interface IAuthGenerator {

    boolean isValid(String passwordAttempt);

    String getToken();
}
