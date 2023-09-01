package my.edu.utar.groupassignment;


public class HelperClass {
    String userID, name, email, username;

    public HelperClass(String userID, String name, String username, String email) {
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public HelperClass() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
