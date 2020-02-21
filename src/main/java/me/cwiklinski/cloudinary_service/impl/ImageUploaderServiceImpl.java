package me.cwiklinski.cloudinary_service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import me.cwiklinski.cloudinary_service.ImageUploaderService;
import me.cwiklinski.model.Image;
import me.cwiklinski.repo.ImageRepository;
import me.cwiklinski.service.ImageRepositoryService;
import me.cwiklinski.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ImageUploaderServiceImpl implements ImageUploaderService {

    private Cloudinary cloudinary;
    private ImageRepository imageRepository;
    private ImageRepositoryService imageRepositoryService;
    private UserRepositoryService userRepositoryService;
    protected final Logger log = Logger.getLogger(getClass().getName());
    private Environment environment;

    @Autowired
    public ImageUploaderServiceImpl(ImageRepository imageRepository, ImageRepositoryService imageRepositoryService, UserRepositoryService userRepositoryService, Environment environment) {
        this.environment = environment;
        this.userRepositoryService = userRepositoryService;
        this.imageRepositoryService = imageRepositoryService;
        this.imageRepository = imageRepository;
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", environment.getProperty("cloudinary.cloud_name"),
                "api_key", environment.getProperty("cloudinary.api_key"),
                "api_secret", environment.getProperty("cloudinary.api_secret")));
    }

    public String uploadFile(File file, long userId) {
        Map uploadResult = null;
        String thumbnailUrl = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            thumbnailUrl = cloudinary.url().transformation(new Transformation().width("200").height("200").crop("pad")).imageTag(uploadResult.get("public_id").toString() + "." + uploadResult.get("format")).substring(10,99);
            imageRepositoryService.saveImage(new Image(uploadResult.get("url").toString(), userId, thumbnailUrl));
            // deleteTmpFile(uploadResult.get(10).toString() + "." + uploadResult.get(1).toString());
        } catch (IOException e) {
            //todo
        }
        return uploadResult.get("url").toString();

    }

    public void deleteTmpFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            log.warning(fileName + " not found");
        }
    }
}
