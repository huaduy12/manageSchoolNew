package com.example.school.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageUploadService {
    String uploadFile(File file, String fileName) throws IOException;
    File convertToFile(MultipartFile multipartFile, String fileName) throws FileNotFoundException;
    String getExtension(String fileName);
    String upload(MultipartFile multipartFile);
}
