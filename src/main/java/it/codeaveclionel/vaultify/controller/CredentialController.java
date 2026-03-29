package it.codeaveclionel.vaultify.controller;

import it.codeaveclionel.vaultify.dto.CredentialRequest;
import it.codeaveclionel.vaultify.dto.CredentialResponse;
import it.codeaveclionel.vaultify.entity.User;
import it.codeaveclionel.vaultify.service.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    @GetMapping
    public ResponseEntity<List<CredentialResponse>> getAllCredentials(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(credentialService.getAllCredentials(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredentialResponse> getCredential(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(credentialService.getCredential(id, user));
    }

    @PostMapping
    public ResponseEntity<CredentialResponse> createCredential(
            @Valid @RequestBody CredentialRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(credentialService.createCredential(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CredentialResponse> updateCredential(
            @PathVariable Long id,
            @Valid @RequestBody CredentialRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(credentialService.updateCredential(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredential(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        credentialService.deleteCredential(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/decrypt")
    public ResponseEntity<Map<String, String>> decryptPassword(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        String password = credentialService.decryptPassword(id, user);
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        return ResponseEntity.ok(response);
    }
}