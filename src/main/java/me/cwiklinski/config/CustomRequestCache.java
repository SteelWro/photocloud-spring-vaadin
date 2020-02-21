package me.cwiklinski.config;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import me.cwiklinski.gui.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class CustomRequestCache extends HttpSessionRequestCache {
    /**
     * {@inheritDoc}
     *
     * If the method is considered an internal request from the framework, we skip
     * saving it.
     *
     * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }


    /**
     * Unfortunately, it's not that easy to resolve the redirect URL from the saved request. But with some
     * casting (we always use {@link DefaultSavedRequest}) and mangling we are able to get the request URI.
     */
    public String resolveRedirectUrl() {
        SavedRequest savedRequest = getRequest(VaadinServletRequest.getCurrent().getHttpServletRequest(), VaadinServletResponse.getCurrent().getHttpServletResponse());
        if(savedRequest instanceof DefaultSavedRequest) {
            final String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI();
            // check for valid URI and prevent redirecting to the login view
            if (requestURI != null
                    && !requestURI.isEmpty()
                    && !requestURI.contains(LoginView.ROUTE)
                    && !requestURI.contains(MainView.ROUTE)
                    && !requestURI.contains(RegistrationView.ROUTE)) {
                return requestURI.startsWith("/") ? requestURI.substring(1) : requestURI;
            }
        }
        return successLoginHandler();
    }

    public String successLoginHandler(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("user")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("admin")) {
                isAdmin = true;
                break;
            }
        }

        if (isUser) {
            return UserView.ROUTE;
        } else if (isAdmin) {
            return AdminView.ROUTE;
        } else {
            return MainView.ROUTE;
        }
    }

}
