package it.codeaveclionel.vaultify.dto;

import jakarta.validation.constraints.NotBlank;

public class CredentialRequest {

    @NotBlank(message = "Site name is required")
    private String siteName;

    private String url;

    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    private String category;

    private String notes;

    public CredentialRequest() {}

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}