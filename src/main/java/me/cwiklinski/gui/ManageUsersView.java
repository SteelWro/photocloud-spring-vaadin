package me.cwiklinski.gui;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import me.cwiklinski.model.UserList;
import me.cwiklinski.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = ManageUsersView.ROUTE)
@PageTitle("ManageUsers")
@CssImport("./RegistrationGuiStyle.css")
@Secured("admin")
public class ManageUsersView {
    public static final String ROUTE = "manageUsers";

    UserRepositoryService userRepositoryService;

    Grid<UserList> grid;

    public ManageUsersView(@Autowired UserRepositoryService userRepositoryService) {
        this.userRepositoryService = userRepositoryService;
        grid = new Grid<>(UserList.class);
    }
}
