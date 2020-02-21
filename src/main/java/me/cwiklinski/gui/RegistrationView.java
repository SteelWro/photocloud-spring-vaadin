package me.cwiklinski.gui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import me.cwiklinski.model.User;
import me.cwiklinski.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.access.annotation.Secured;

import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = RegistrationView.ROUTE)
@PageTitle("Registration")
@CssImport("./RegistrationGuiStyle.css")
@Secured("admin")
public class RegistrationView extends VerticalLayout {
    public static final String ROUTE = "registration";

    UserRepositoryService userRepositoryService;
    FormLayout layoutWithBinder;
    Binder<User> binder;
    User newUser;
    Label labelText;
    TextField loginField;
    PasswordField passwordField;
    Dialog good;
    Dialog bad;
    Div outer;
    Div inner;
    Button save;
    Button backButton;
    Button okSaveButton;
    Button okBadButton;
    Icon backIcon;



    public RegistrationView(@Autowired UserRepositoryService userRepositoryService) {
        layoutWithBinder = new FormLayout();
        binder = new Binder<>();
        newUser = new User();
        labelText = new Label("Create new user");
        loginField = new TextField("login");
        passwordField = new PasswordField("password");
        okSaveButton = new Button("OK");
        good = new Dialog(new VerticalLayout(new Label("User Created"), okSaveButton));
        okBadButton = new Button("OK");
        bad = new Dialog(new VerticalLayout(new Label("Bad passed data"), okBadButton));
        outer = new Div();
        inner = new Div();
        save = new Button("Save");
        backIcon = new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O);
        backButton = new Button(backIcon);
        layoutWithBinder.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));
        layoutWithBinder.addFormItem(loginField, loginField);
        layoutWithBinder.addFormItem(passwordField, passwordField);
        layoutWithBinder.setResponsiveSteps();

        loginField.setRequiredIndicatorVisible(true);
        passwordField.setRequiredIndicatorVisible(true);

        SerializablePredicate<String> phoneOrEmailPredicate = value -> !loginField
                .getValue().trim().isEmpty()
                || !passwordField.getValue().trim().isEmpty();

        Binder.Binding<User, String> loginBinding = binder.forField(loginField)
                .withValidator(new StringLengthValidator(
                        "login from 3 to 32 characters", 3, 32))
                .bind(User::getUsername, User::setUsername);

        binder.forField(loginField)
                .withValidator((Validator<String>) (value, valueContext) -> {
                    Boolean boo = !userRepositoryService.isUserIsUsed(value);
                    if (boo) {
                        return ValidationResult.ok();
                    }
                    return ValidationResult.error("Login in used");
                })
                .bind(User::getUsername, User::setUsername);

        Binder.Binding<User, String> passwordBinding = binder.forField(passwordField)
                .withValidator(new StringLengthValidator(
                        "Password minimum length is 6 characters", 6, 120))
                .bind(User::getPassword, User::setPassword);

        loginField.addValueChangeListener(e -> loginBinding.validate());
        passwordField.addValueChangeListener(e -> passwordBinding.validate());

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(newUser)) {
                good.open();
                userRepositoryService.saveUser(loginField.getValue(), passwordField.getValue());
            } else {
                BinderValidationStatus<User> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                bad.open();
            }
        });

        okBadButton.addClickListener(event -> {
            bad.close();
        });

        okSaveButton.addClickListener(event -> {
            good.close();
            UI.getCurrent().navigate(AdminView.ROUTE);
        });

        outer.addClassName("outer");
        inner.addClassName("inner");
        labelText.addClassName("label");
        save.addClassName("primary");
        inner.add(layoutWithBinder, save);
        outer.add(labelText);
        outer.add(inner);
        add(outer);
    }
}
