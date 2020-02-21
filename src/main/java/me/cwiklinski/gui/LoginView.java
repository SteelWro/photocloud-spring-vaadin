package me.cwiklinski.gui;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import me.cwiklinski.config.CustomRequestCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout {
    public static final String ROUTE = "login";

    private LoginOverlay login = new LoginOverlay();

    @Autowired
    public LoginView(AuthenticationManager authenticationManager, CustomRequestCache requestCache) {
        login.setOpened(true);
        login.setForgotPasswordButtonVisible(false);
        login.setTitle("Photocloud");
        login.setDescription("simply cloud for your photos");

        add(login);

        login.addLoginListener(e -> {
            try {
                //throws AuthenticateException
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

                if (authentication != null) {
                    login.close();
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    UI.getCurrent().navigate(requestCache.resolveRedirectUrl());
                }

            } catch (AuthenticationException ex) {
                login.setError(true);
            }
        });
    }
}
