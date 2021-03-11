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
import kskowronski.data.entities.egeria.kg.CashRegister;
import kskowronski.data.services.egeria.kg.CashRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@UIScope
public class CashSetDialog extends Dialog {

    private transient CashRegisterService cashRegisterService;

    private String txtUserHas = "gridUserHas";
    private String txtUserHasNo = "gridUserHasNo";

    private Grid<CashRegister> gridUserHasCash = new Grid<>(CashRegister.class);
    private Grid<CashRegister> gridUserHasNoCash = new Grid<>(CashRegister.class);

    private transient List<CashRegister> draggedItems = new ArrayList<>();
    Grid<CashRegister> dragSource = null;

    private transient BigDecimal userId = null;

    @Autowired
    public CashSetDialog(CashRegisterService cashRegisterService) {
        this.cashRegisterService= cashRegisterService;
        this.setWidth("700px");

        ComponentEventListener<GridDragStartEvent<CashRegister>> dragStartListener = event -> {
            draggedItems = event.getDraggedItems();
            dragSource = event.getSource();
            gridUserHasCash.setDropMode(GridDropMode.BETWEEN);
            gridUserHasNoCash.setDropMode(GridDropMode.BETWEEN);
        };

        /**
         * System.out.println(draggedItems.get(0).getName() + " " + dragSource.getId());
         */
        ComponentEventListener<GridDragEndEvent<CashRegister>> dragEndListener = event -> {
            gridUserHasCash.setDropMode(null);
            gridUserHasNoCash.setDropMode(null);

            if ( dragSource.getId().get().equals(txtUserHas)){
                cashRegisterService.deleteSetting(userId, draggedItems.get(0).getCasId());
            } else {
                cashRegisterService.saveSetting(userId, draggedItems.get(0).getCasId());
            }
        };

        ComponentEventListener<GridDropEvent<CashRegister>> dropListener = event -> {
            Optional<CashRegister> target = event.getDropTargetItem();
            if (target.isPresent() && draggedItems.contains(target.get())) {
                return;
            }

            // Remove the items from the source grid
            @SuppressWarnings("unchecked")
            ListDataProvider<CashRegister> sourceDataProvider = (ListDataProvider<CashRegister>) dragSource
                    .getDataProvider();
            List<CashRegister> sourceItems = new ArrayList<>(
                    sourceDataProvider.getItems());
            sourceItems.removeAll(draggedItems);
            dragSource.setItems(sourceItems);

            // Add dragged items to the target Grid
            Grid<CashRegister> targetGrid = event.getSource();
            @SuppressWarnings("unchecked")
            ListDataProvider<CashRegister> targetDataProvider = (ListDataProvider<CashRegister>) targetGrid
                    .getDataProvider();
            List<CashRegister> targetItems = new ArrayList<>(
                    targetDataProvider.getItems());

            int index = target.map(person -> targetItems.indexOf(person)
                    + (event.getDropLocation() == GridDropLocation.BELOW ? 1
                    : 0))
                    .orElse(0);
            targetItems.addAll(index, draggedItems);
            targetGrid.setItems(targetItems);
        };

        gridUserHasCash.setSelectionMode(Grid.SelectionMode.MULTI);
        gridUserHasCash.setId(txtUserHas);
        gridUserHasCash.addDropListener(dropListener);
        gridUserHasCash.addDragStartListener(dragStartListener);
        gridUserHasCash.addDragEndListener(dragEndListener);
        gridUserHasCash.setRowsDraggable(true);
        gridUserHasCash.setColumns("casName", "casFrmId");

        gridUserHasNoCash.setSelectionMode(Grid.SelectionMode.MULTI);
        gridUserHasNoCash.setId(txtUserHasNo);
        gridUserHasNoCash.addDropListener(dropListener);
        gridUserHasNoCash.addDragStartListener(dragStartListener);
        gridUserHasNoCash.addDragEndListener(dragEndListener);
        gridUserHasNoCash.setRowsDraggable(true);
        gridUserHasNoCash.setColumns("casName", "casFrmId");

        VerticalLayout divHasCashRegister = new VerticalLayout(new Label("Ma dodane"), gridUserHasCash);
        VerticalLayout divHasNotCashRegister = new VerticalLayout(new Label("Kasy do dodania"), gridUserHasNoCash);
        HorizontalLayout divGrids = new HorizontalLayout(divHasCashRegister, divHasNotCashRegister);
        add(divGrids);
    }

    public void setDataForGrid(BigDecimal userId){
        this.userId = userId;
        List<CashRegister> cash = cashRegisterService.findAll();

        List<CashRegister> userCash = cashRegisterService.findAllUserCash(userId);
        gridUserHasCash.setItems(userCash);

        userCash.stream().forEach( item ->  cash.removeIf( x -> x.getCasId().equals(item.getCasId())) );

        gridUserHasNoCash.setItems(cash);
    }
}
