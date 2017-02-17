package cs2340.teamnasamovierecommender.pojo;

/**
 * @author Anas Tahir Khan
 * @author Anuragsharma Venukadasula
 * @author Sai Srivatsav Muppiri
 * @author Sayed Nafiz Imtiaz Ali
 */
public class User {
    private String name;

    private String username;

    private String password;
    private String email;
    private String major;
    private boolean banned;
    public User(String username, String password, String email, String major) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.major = major;
        this.banned = false;
    }

    public User(String username, String password, String email, String major, boolean banned) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.major = major;
        this.banned = banned;
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
