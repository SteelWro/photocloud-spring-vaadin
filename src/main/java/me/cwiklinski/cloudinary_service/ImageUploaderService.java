package me.cwiklinski.cloudinary_service;

import me.cwiklinski.model.Image;

import java.io.File;

public interface ImageUploaderService {
    String uploadFile(File file, long userId);

    void deleteTmpFile(String fileName);

}
