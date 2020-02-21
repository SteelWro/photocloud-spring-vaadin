package me.cwiklinski.service.impl;

import me.cwiklinski.model.Image;
import me.cwiklinski.repo.ImageRepository;
import me.cwiklinski.service.ImageRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageRepositoryServiceImpl implements ImageRepositoryService {
    ImageRepository imageRepository;

    @Autowired
    public ImageRepositoryServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private List<com.vaadin.flow.component.html.Image> convertToVaadinImages(List<Image> images) {
        List<com.vaadin.flow.component.html.Image> vaadinImages = new ArrayList<>();
        images.stream().forEach(e ->
        {
            vaadinImages.add(new com.vaadin.flow.component.html.Image(e.getThumbnailAddress(), e.getId().toString()));
        });
        return vaadinImages;
    }

    @Override
    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public List<Image> getImagesById(Long id) {
        return imageRepository.findAllByUserId(id);
    }

    @Override
    public String getThumbnailAddress(Long id) {
        return imageRepository.getOne(id).getImageAddress();
    }

    @Override
    public String getImageAddress(Long id) {
        return imageRepository.getOne(id).getImageAddress();
    }

    @Override
    public List<com.vaadin.flow.component.html.Image> getAllThumbnails() {
        List<Image> images = imageRepository.findAll();
        return convertToVaadinImages(images);
    }

    @Override
    public List<com.vaadin.flow.component.html.Image> getAllThumbnailsById(Long id) {
        List<Image> images = imageRepository.findAllByUserId(id);
        return convertToVaadinImages(images);
    }

    @Override
    public void deletePhoto(Long id) {
        imageRepository.deleteById(id);
    }
}
