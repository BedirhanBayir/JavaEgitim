package com.egitim.demo.model;

// Sistemin içinde tutulan tam kullanıcı. API'den asla doğrudan dönmüyor.
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    public User(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId()          { return id; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getPassword()  { return password; }
    public String getRole()      { return role; }
}
