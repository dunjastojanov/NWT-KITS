package com.uber.rocket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class ImageService {

    private final static String STATIC_PATH = "src/main/resources/static/";
    private final static String STATIC_PATH_TARGET = "target/classes/static/";
    private final static String BACKEND_URL = "http://localhost:8443";

    private final static String IMAGES_PATH = "/images/";

    public String saveProfilePicture(MultipartFile file, String userId) throws IOException {
        if (file == null) {
            throw new RuntimeException("File is empty");
        }
        Path path = Paths.get(STATIC_PATH + IMAGES_PATH + userId);
        Path path_target = Paths.get(STATIC_PATH_TARGET + IMAGES_PATH + userId);
        savePictureOnPath(file, path, userId);
        return savePictureOnPath(file, path_target, userId);
    }

    private String savePictureOnPath(MultipartFile file, Path path, String userId) throws IOException {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            assert fileName != null;
            Path filePath = path.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return BACKEND_URL + IMAGES_PATH + userId + "/" + fileName;
        } catch (DirectoryNotEmptyException exception) {
            throw new IOException("Directory Not Empty Exception: " + file.getOriginalFilename(), exception);
        } catch (IOException exception) {
            throw new IOException("Could not save image file: " + file.getOriginalFilename(), exception);
        }
    }

    public void deletePicture(String profilePicture) {
        String folderPathInStatic = STATIC_PATH.substring(0, STATIC_PATH.length() - 1) + profilePicture;
        File file = new File(folderPathInStatic);
        if (!file.delete())
            throw new RuntimeException("Picture isn't successfully deleted");
    }
}
