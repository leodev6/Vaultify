package it.codeaveclionel.vaultify.service;

import it.codeaveclionel.vaultify.dto.CredentialRequest;
import it.codeaveclionel.vaultify.dto.CredentialResponse;
import it.codeaveclionel.vaultify.entity.Credential;
import it.codeaveclionel.vaultify.entity.User;
import it.codeaveclionel.vaultify.exception.ResourceNotFoundException;
import it.codeaveclionel.vaultify.repository.CredentialRepository;
import it.codeaveclionel.vaultify.util.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final EncryptionUtil encryptionUtil;

    public CredentialService(CredentialRepository credentialRepository, EncryptionUtil encryptionUtil) {
        this.credentialRepository = credentialRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Transactional(readOnly = true)
    public List<CredentialResponse> getAllCredentials(User user) {
        return credentialRepository.findByUser(user).stream()
                .map(this::toResponseWithoutPassword)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CredentialResponse getCredential(Long id, User user) {
        Credential credential = credentialRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        return toResponse(credential);
    }

    @Transactional
    public CredentialResponse createCredential(CredentialRequest request, User user) {
        EncryptionUtil.EncryptedData encrypted = encryptionUtil.encrypt(request.getPassword());

        Credential credential = new Credential();
        credential.setSiteName(request.getSiteName());
        credential.setUrl(request.getUrl());
        credential.setUsername(request.getUsername());
        credential.setEncryptedPassword(encrypted.encryptedText());
        credential.setIv(encrypted.iv());
        credential.setCategory(request.getCategory());
        credential.setNotes(request.getNotes());
        credential.setUser(user);

        credential = credentialRepository.save(credential);
        return toResponseWithoutPassword(credential);
    }

    @Transactional
    public CredentialResponse updateCredential(Long id, CredentialRequest request, User user) {
        Credential credential = credentialRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        EncryptionUtil.EncryptedData encrypted = encryptionUtil.encrypt(request.getPassword());

        credential.setSiteName(request.getSiteName());
        credential.setUrl(request.getUrl());
        credential.setUsername(request.getUsername());
        credential.setEncryptedPassword(encrypted.encryptedText());
        credential.setIv(encrypted.iv());
        credential.setCategory(request.getCategory());
        credential.setNotes(request.getNotes());

        credential = credentialRepository.save(credential);
        return toResponseWithoutPassword(credential);
    }

    @Transactional
    public void deleteCredential(Long id, User user) {
        Credential credential = credentialRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        credentialRepository.delete(credential);
    }

    @Transactional(readOnly = true)
    public String decryptPassword(Long id, User user) {
        Credential credential = credentialRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        return encryptionUtil.decrypt(credential.getEncryptedPassword(), credential.getIv());
    }

    private CredentialResponse toResponse(Credential credential) {
        CredentialResponse response = new CredentialResponse();
        response.setId(credential.getId());
        response.setSiteName(credential.getSiteName());
        response.setUrl(credential.getUrl());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(credential.getEncryptedPassword());
        response.setIv(credential.getIv());
        response.setCategory(credential.getCategory());
        response.setNotes(credential.getNotes());
        response.setCreatedAt(credential.getCreatedAt());
        return response;
    }

    private CredentialResponse toResponseWithoutPassword(Credential credential) {
        CredentialResponse response = new CredentialResponse();
        response.setId(credential.getId());
        response.setSiteName(credential.getSiteName());
        response.setUrl(credential.getUrl());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(credential.getEncryptedPassword());
        response.setIv(credential.getIv());
        response.setCategory(credential.getCategory());
        response.setNotes(credential.getNotes());
        response.setCreatedAt(credential.getCreatedAt());
        return response;
    }
}