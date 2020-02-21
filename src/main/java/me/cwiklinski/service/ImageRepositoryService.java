package me.cwiklinski.service;

import me.cwiklinski.model.Image;

import java.util.List;

public interface ImageRepositoryService {
    void saveImage(Image image);
    List<Image> getAllImages();
    List<Image> getImagesById(Long id);
    String getThumbnailAddress(Long id);
    String getImageAddress(Long id);
    List<com.vaadin.flow.component.html.Image> getAllThumbnails();
    List<com.vaadin.flow.component.html.Image> getAllThumbnailsById(Long id);
    void deletePhoto(Long id);
}
