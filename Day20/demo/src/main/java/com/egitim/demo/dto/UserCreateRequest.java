package com.egitim.demo.dto;

// İstemcinin kayıt olurken GÖNDEREBİLECEĞİ alanlar.
// id ve role yok: bunları istemci değil sunucu belirliyor.
public class UserCreateRequest {

    private String username;
    private String email;
    private String password;

    public UserCreateRequest() {}

    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}
