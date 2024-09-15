package com.example.vimca.Azure;


import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

@Service
public class AzureBlobClient {

    private BlobContainerClient blobContainerClient;

    @Value("${azure.blob.endpointUrl}")
    private String endpointUrl;

    @Value("${azure.blob.containerName}")
    private String containerName;

    @Value("${azure.storage.connectionString}")
    private String connectionString;

    @PostConstruct
    private void initializeAzureBlob() {
        this.blobContainerClient = new BlobContainerClientBuilder()
            .connectionString(connectionString)
            .containerName(containerName)
            .buildClient();
    }

    public String uploadFile(MultipartFile multipartFile) throws Exception {
        String fileName = generateFileName(multipartFile);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.upload(multipartFile.getInputStream(), multipartFile.getSize(), true);
        return fileName;
    }

    public String deleteFileFromBlob(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.delete();
        return "Successfully deleted";
    }

    public ByteArrayOutputStream downloadFile(String keyName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BlobClient blobClient = blobContainerClient.getBlobClient(keyName);
            blobClient.download(outputStream);
            return outputStream;
        } catch (Exception e) {
            // Handle exceptions accordingly
        }
        return null;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
