//package me.cwiklinski.gui;
//
//import com.vaadin.flow.component.Key;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.dependency.CssImport;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.html.H1;
//import com.vaadin.flow.component.html.Label;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.server.PWA;
//import me.cwiklinski.service.GreetService;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * A sample Vaadin view class.
// * <p>
// * To implement a Vaadin view just extend any Vaadin component and
// * use @Route annotation to announce it in a URL as a Spring managed
// * bean.
// * Use the @PWA annotation make the application installable on phones,
// * tablets and some desktop browsers.
// * <p>
// * A new instance of this class is created for every new user and every
// * browser tab/window.
// */
//@Route(value = MainView.ROUTE)
//@PWA(name = "Photocloud application",
//        shortName = "photocloud",
//        description = "simply cloud for your photos",
//        enableInstallPrompt = true)
//@PageTitle("Main")
//public class MainView extends VerticalLayout {
//    public static final String ROUTE = "";
//
//    private Button registryButton = new Button("Sing Up");
//    private Button loginButton = new Button("Sign In");
//    private Div outer = new Div();
//    private Div middle = new Div();
//    private Div topDiv = new Div();
//    private Div bottomDiv = new Div();
//    private Div buttonsDiv = new Div();
//    private H1 titleText = new H1("Photo Cloud");
//    private Label descriptionText = new Label("Free cloud service for photos");
//
//    public MainView() {
//
//        topDiv.addClassName("topDiv");
//        bottomDiv.addClassName("bottomDiv");
//        outer.addClassName("outer");
//        middle.addClassName("middle");
//        titleText.addClassName("title");
//        loginButton.addClassName("button");
//        registryButton.addClassName("button");
//        buttonsDiv.addClassName("buttonsDiv");
//        descriptionText.addClassName("descriptionText");
//        //descriptionText.getElement().getStyle().set("color","#ACCEFB");
//        buttonsDiv.add(new HorizontalLayout(loginButton, registryButton));
//        topDiv.add(new VerticalLayout(titleText, descriptionText));
//        bottomDiv.add(buttonsDiv);
//        outer.add(topDiv, bottomDiv);
//        add(outer);
//
//        loginButton.addClickListener(e -> {
//            UI.getCurrent().navigate(LoginView.ROUTE);
//        });
//
//        registryButton.addClickListener(e -> {
//            UI.getCurrent().navigate(RegistrationView.ROUTE);
//        });
//
//    }
//
//}
