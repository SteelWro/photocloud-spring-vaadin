package me.cwiklinski.service;

public interface UserRepositoryService {

    Long getUserIdByUsername(String name);

    boolean isUserIsUsed(String username);

    void saveUser(String username, String password);
}
