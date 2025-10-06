package com.etc.raw_materials_app.models;

public class UserContext {
    private static User currentUser;

    // Getter for the current user
    public static User getCurrentUser() {
        return currentUser;
    }

    // Setter for the current user
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
