package it.codeaveclionel.vaultify.dto;

import java.time.LocalDateTime;

public class CredentialResponse {

    private Long id;
    private String siteName;
    private String url;
    private String username;
    private String encryptedPassword;
    private String iv;
    private String category;
    private String notes;
    private LocalDateTime createdAt;

    public CredentialResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEncryptedPassword() { return encryptedPassword; }
    public void setEncryptedPassword(String encryptedPassword) { this.encryptedPassword = encryptedPassword; }

    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}