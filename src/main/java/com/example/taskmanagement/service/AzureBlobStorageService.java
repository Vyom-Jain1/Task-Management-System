package com.example.taskmanagement.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class AzureBlobStorageService {

    private final BlobServiceClient blobServiceClient;
    private final String containerName;

    public AzureBlobStorageService(
            @Value("${azure.storage.connection-string:}") String connectionString,
            @Value("${azure.storage.container-name:files}") String containerName) {
        this.containerName = containerName;

        if (connectionString != null && !connectionString.isEmpty()) {
            this.blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            // Create container if it doesn't exist
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!containerClient.exists()) {
                containerClient.create();
            }
        } else {
            this.blobServiceClient = null;
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (blobServiceClient == null) {
            throw new RuntimeException("Azure Blob Storage is not configured");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            blobClient.upload(inputStream, file.getSize(), true);
        }

        return blobClient.getBlobUrl();
    }

    public void deleteFile(String fileName) {
        if (blobServiceClient == null) {
            return;
        }

        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(fileName);

        if (blobClient.exists()) {
            blobClient.delete();
        }
    }

    public InputStream downloadFile(String fileName) {
        if (blobServiceClient == null) {
            throw new RuntimeException("Azure Blob Storage is not configured");
        }

        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(fileName);

        return blobClient.openInputStream();
    }

    public boolean isConfigured() {
        return blobServiceClient != null;
    }
}
