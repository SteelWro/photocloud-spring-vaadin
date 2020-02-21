package me.cwiklinski.model;

import com.vaadin.flow.component.button.Button;

public class UserList {
    String username;
    Button deleteButton;

    public UserList(String username, Button deleteButton) {
        this.username = username;
        this.deleteButton = deleteButton;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
