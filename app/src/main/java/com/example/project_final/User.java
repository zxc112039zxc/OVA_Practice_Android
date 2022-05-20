package com.example.project_final;

public class User {

    private String username = "";
    private boolean loggedIn = false;

    public User()
    {
        this("");
    }

    public User(String inputUsername)
    {
        username = inputUsername;
        loggedIn = false;
    }

    public Boolean logout()
    {
        if (isLoggedIn())
        {
            loggedIn = false;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }
}
