package kskowronski.views.settings;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kskowronski.data.entity.inap.User;
import kskowronski.data.service.inap.UserService;
import kskowronski.views.main.MainView;
import kskowronski.views.settings.elements.RolesDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
@CssImport("./styles/views/settings/settings-view.css")
public class SettingsView extends Div {

    private transient UserService userService;
    private Grid<User> gridWorkers;
    private TextField filterText = new TextField();

    @Autowired
    private RolesDialog rolesDialog;

    @Autowired
    public SettingsView(UserService userService) {
        setId("settings-view");
        this.userService = userService;

        filterText.setPlaceholder("Search...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> getUsers());

        this.gridWorkers = new Grid<>(User.class);
        gridWorkers.setClassName("gridWorkers");
        gridWorkers.setColumns();
        gridWorkers.addColumn("uzId");
        gridWorkers.addColumn("prcId");
        gridWorkers.addColumn("username");
        gridWorkers.addColumn(new NativeButtonRenderer<>("Role",
                item -> {
                    rolesDialog.setDataForGrid(item);
                    rolesDialog.open();
                }
        )).setWidth("50px");

        HorizontalLayout divWorkers = new HorizontalLayout(gridWorkers);
        divWorkers.setClassName("divWorkers");

        Button butAll = new Button("All",  e -> getAllUsers());
        add(new Text("Znajdź w nap_uzytkownik "), filterText, butAll, divWorkers);
    }

    private void getUsers(){
        if (filterText.getValue().length() > 2) {
            Optional<List<User>> users = userService.findFastUsers(filterText.getValue());
            if (users.isPresent()){
                users.get().sort(Comparator.comparing(User::getUsername));
                gridWorkers.setItems(users.get());
            }
        } else {
            gridWorkers.setItems();
        }
    }

    private void getAllUsers(){
        List<User> users = userService.findAll();
        users.sort(Comparator.comparing(User::getUsername));
        gridWorkers.setItems(users);
    }

}
