package kskowronski.views.settings.elements;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.inap.Role;
import kskowronski.data.entity.inap.User;
import kskowronski.data.service.inap.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@UIScope
public class RolesDialog extends Dialog {

    private transient RoleService roleService;

    private Grid<Role> gridWorkerRoleHas = new Grid<>(Role.class);
    private Grid<Role> gridWorkerRoleNoHas = new Grid<>(Role.class);
    List<Role> draggedItems = new ArrayList<>();
    Grid<Role>  dragSource = null;

    @Autowired
    public RolesDialog(RoleService roleService) {
        this.roleService = roleService;
        this.setWidth("700px");

        ComponentEventListener<GridDragStartEvent<Role>> dragStartListener = event -> {
            draggedItems = event.getDraggedItems();
            dragSource = event.getSource();
            gridWorkerRoleHas.setDropMode(GridDropMode.BETWEEN);
            gridWorkerRoleNoHas.setDropMode(GridDropMode.BETWEEN);
        };

        ComponentEventListener<GridDragEndEvent<Role>> dragEndListener = event -> {
            draggedItems = null;
            dragSource = null;
            gridWorkerRoleHas.setDropMode(null);
            gridWorkerRoleNoHas.setDropMode(null);
        };

        ComponentEventListener<GridDropEvent<Role>> dropListener = event -> {
            Optional<Role> target = event.getDropTargetItem();
            if (target.isPresent() && draggedItems.contains(target.get())) {
                return;
            }

            // Remove the items from the source grid
            @SuppressWarnings("unchecked")
            ListDataProvider<Role> sourceDataProvider = (ListDataProvider<Role>) dragSource
                    .getDataProvider();
            List<Role> sourceItems = new ArrayList<>(
                    sourceDataProvider.getItems());
            sourceItems.removeAll(draggedItems);
            dragSource.setItems(sourceItems);

            // Add dragged items to the target Grid
            Grid<Role> targetGrid = event.getSource();
            @SuppressWarnings("unchecked")
            ListDataProvider<Role> targetDataProvider = (ListDataProvider<Role>) targetGrid
                    .getDataProvider();
            List<Role> targetItems = new ArrayList<>(
                    targetDataProvider.getItems());

            int index = target.map(person -> targetItems.indexOf(person)
                    + (event.getDropLocation() == GridDropLocation.BELOW ? 1
                    : 0))
                    .orElse(0);
            targetItems.addAll(index, draggedItems);
            targetGrid.setItems(targetItems);
        };

        gridWorkerRoleHas.setSelectionMode(Grid.SelectionMode.MULTI);
        gridWorkerRoleHas.addDropListener(dropListener);
        gridWorkerRoleHas.addDragStartListener(dragStartListener);
        gridWorkerRoleHas.addDragEndListener(dragEndListener);
        gridWorkerRoleHas.setRowsDraggable(true);
        gridWorkerRoleHas.setColumns("id", "name");

        gridWorkerRoleNoHas.setSelectionMode(Grid.SelectionMode.MULTI);
        gridWorkerRoleNoHas.addDropListener(dropListener);
        gridWorkerRoleNoHas.addDragStartListener(dragStartListener);
        gridWorkerRoleNoHas.addDragEndListener(dragEndListener);
        gridWorkerRoleNoHas.setRowsDraggable(true);
        gridWorkerRoleNoHas.setColumns("id", "name");

        VerticalLayout divHasRole = new VerticalLayout(new Label("Ma dodane"), gridWorkerRoleHas);
        VerticalLayout divHasNotRole = new VerticalLayout(new Label("Role do dodania"), gridWorkerRoleNoHas);
        HorizontalLayout divGrids = new HorizontalLayout(divHasRole, divHasNotRole);
        add(divGrids);
    }

    public void setDataForGrid(User user){

        List<Role> roles = roleService.findAll();

        Set<Role> userRoles = user.getRoles();
        gridWorkerRoleHas.setItems(userRoles);

        userRoles.stream().forEach( item -> {
            roles.removeIf( x -> x.getName().equals(item.getName()));
        });

        gridWorkerRoleNoHas.setItems(roles);
    }

}
