package com.example.school.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class ImageUploadServiceImp implements ImageUploadService {

    private final String bucketName = "school-49c9a.appspot.com";

    @Override
    public String uploadFile(File file, String fileName) throws IOException {
        // cấu hình và xác định đối tượng trc khi uppload
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();

        InputStream inputStream = ImageUploadServiceImp.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/school-49c9a.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) throws FileNotFoundException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempFile;
    }

    @Override
    public String getExtension(String fileName) {
        // lấy định dạng của ảnh, ví dụ : .jpg, .png
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            // lấy tên ảnh
            String fileName = multipartFile.getOriginalFilename();

            // sinh ra 1 chuỗi id duy nhất rồi nối với .jpg hoặc .png
            // ví dụ 712ac656-5c3a-424a-bb1b-c2934b031b1b.jpg
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
            // to convert multipartFile to File
            File file = this.convertToFile(multipartFile, fileName);

            // to get uploaded file link
            String URL = this.uploadFile(file, fileName);

            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
