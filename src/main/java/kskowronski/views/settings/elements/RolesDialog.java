package kskowronski.views.settings.elements;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import kskowronski.data.entity.inap.Role;
import kskowronski.data.service.inap.RoleService;
import kskowronski.data.service.inap.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@UIScope
public class RolesDialog extends Dialog {

    private transient RoleService roleService;
    private transient UserRolesService userRolesService;

    private String txtWorkerRoleHas = "gridWorkerRoleHas";
    private String txtWorkerRoleNoHas = "gridWorkerRoleNoHas";

    private Grid<Role> gridWorkerRoleHas = new Grid<>(Role.class);
    private Grid<Role> gridWorkerRoleNoHas = new Grid<>(Role.class);
    private transient List<Role> draggedItems = new ArrayList<>();
    Grid<Role> dragSource = null;

    private transient BigDecimal userId = null;

    @Autowired
    public RolesDialog(RoleService roleService, UserRolesService userRolesService) {
        this.roleService = roleService;
        this.userRolesService = userRolesService;
        this.setWidth("700px");

        ComponentEventListener<GridDragStartEvent<Role>> dragStartListener = event -> {
            draggedItems = event.getDraggedItems();
            dragSource = event.getSource();
            gridWorkerRoleHas.setDropMode(GridDropMode.BETWEEN);
            gridWorkerRoleNoHas.setDropMode(GridDropMode.BETWEEN);
        };

        /**
         * System.out.println(draggedItems.get(0).getName() + " " + dragSource.getId());
         */
        ComponentEventListener<GridDragEndEvent<Role>> dragEndListener = event -> {
            gridWorkerRoleHas.setDropMode(null);
            gridWorkerRoleNoHas.setDropMode(null);

            if ( dragSource.getId().get().equals(txtWorkerRoleHas)){
                userRolesService.delete(userId, BigDecimal.valueOf(draggedItems.get(0).getId()));
            } else {
                userRolesService.save(userId, BigDecimal.valueOf(draggedItems.get(0).getId()));
            }
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
        gridWorkerRoleHas.setId(txtWorkerRoleHas);
        gridWorkerRoleHas.addDropListener(dropListener);
        gridWorkerRoleHas.addDragStartListener(dragStartListener);
        gridWorkerRoleHas.addDragEndListener(dragEndListener);
        gridWorkerRoleHas.setRowsDraggable(true);
        gridWorkerRoleHas.setColumns("id", "name");

        gridWorkerRoleNoHas.setSelectionMode(Grid.SelectionMode.MULTI);
        gridWorkerRoleNoHas.setId(txtWorkerRoleNoHas);
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

    public void setDataForGrid(BigDecimal userId){
        this.userId = userId;
        List<Role> roles = roleService.findAll();

        List<Role> userRoles = roleService.findAllUserRoles(userId);
        gridWorkerRoleHas.setItems(userRoles);

        userRoles.stream().forEach( item ->  roles.removeIf( x -> x.getName().equals(item.getName())) );

        gridWorkerRoleNoHas.setItems(roles);
    }

}
