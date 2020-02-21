package me.cwiklinski.gui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import me.cwiklinski.cloudinary_service.ByteConverter;
import me.cwiklinski.cloudinary_service.ImageUploaderService;
import me.cwiklinski.model.Image;
import me.cwiklinski.service.ImageRepositoryService;
import me.cwiklinski.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = AdminView.ROUTE)
@Secured("admin")
public class AdminView extends VerticalLayout {
    public static final String ROUTE = "admin";

    private ImageUploaderService imageUploaderService;
    private ByteConverter byteConverter;
    private UserRepositoryService userRepositoryService;
    private ImageRepositoryService imageRepositoryService;
    private Div gallery;
    private Tab tab1;
    private Div page1;
    private Tab tab2;
    private Div page2;
    private Tabs tabs;
    private Div pages;
    private Set<Component> pagesShown;
    private Map<Tab, Component> tabsToPages;
    private Button logoutButton;
    private HttpServletRequest req;
    private Boolean isUploadRequested;
    private Button registrationButton;
    private Button manageUsersButton;

    @Autowired
    public AdminView(ImageUploaderService imageUploaderService, ByteConverter byteConverter, UserRepositoryService userRepositoryService, ImageRepositoryService imageRepositoryService, HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.isAuthenticated());
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        this.req = req;
        this.imageRepositoryService = imageRepositoryService;
        this.byteConverter = byteConverter;
        this.imageUploaderService = imageUploaderService;
        this.userRepositoryService = userRepositoryService;
        Upload upload = new Upload(buffer);
        gallery = new Div();
        tab1 = new Tab("Upload");
        page1 = new Div();
        tab2 = new Tab("Gallery");
        page2 = new Div();
        tabs = new Tabs(tab1, tab2);
        pages = new Div(page1, page2);
        pagesShown = Stream.of(page1).collect(Collectors.toSet());
        tabsToPages = new HashMap<>();
        logoutButton = new Button("Logout");
        isUploadRequested = true;
        registrationButton = new Button("Registry user");
        manageUsersButton = new Button("Manage users");


        page1.add(upload);
        page2.add(gallery);
        page2.setVisible(false);
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        tabs.addSelectedChangeListener(e -> tabChanger());

        upload.addSucceededListener(e -> {
            byteConverter.byteArrayToFile(buffer.getOutputBuffer(e.getFileName()), e);
            imageUploaderService.uploadFile(new File(e.getFileName()), userRepositoryService.getUserIdByUsername(authentication.getName()));
            isUploadRequested = true;
        });

        logoutButton.addClickListener(e -> {
            SecurityContextHolder.clearContext();
            req.getSession(false).invalidate();
            UI.getCurrent().getSession().close();
            UI.getCurrent().getPage().reload();
        });

        registrationButton.addClickListener(e ->{
            UI.getCurrent().navigate(RegistrationView.ROUTE);
        });
        manageUsersButton.addClickListener(e ->{
            UI.getCurrent().navigate(ManageUsersView.ROUTE);
        });

        add(new HorizontalLayout(tabs, logoutButton, registrationButton), pages);
    }

    private void updateGallery() {
        if (isUploadRequested) {
            gallery.removeAll();
            List<Image> images = imageRepositoryService.getAllImages();
            images.forEach(this::accept);
            page2.add(gallery);
            isUploadRequested = false;
        }
    }

    private void deletePhoto(Optional<String> alt) {
        isUploadRequested = true;
        imageRepositoryService.deletePhoto(Long.valueOf(alt.get()));
        updateGallery();
    }

    private void tabChanger() {
        if (tab1.isVisible()) {
            updateGallery();
        }
        pagesShown.forEach(page -> page.setVisible(false));
        pagesShown.clear();
        Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
        selectedPage.setVisible(true);
        pagesShown.add(selectedPage);
    }

    private void accept(Image image) {
        Button button = new Button("delete");
        Dialog dialog = new Dialog();
        com.vaadin.flow.component.html.Image vaadinImage = new com.vaadin.flow.component.html.Image(image.getThumbnailAddress(), image.getId().toString());

        dialog.add(new com.vaadin.flow.component.html.Image(image.getImageAddress(), image.getId().toString()));
        dialog.add(new Button("exit", event -> {
            dialog.close();
        }));

        vaadinImage.addClickListener(e -> {
            dialog.open();
        });

        button.addClickListener(event -> {
            deletePhoto(vaadinImage.getAlt());
        });

        gallery.add(new HorizontalLayout(vaadinImage, button));
    }

}
