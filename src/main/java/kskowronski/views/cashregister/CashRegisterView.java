package kskowronski.views.cashregister;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import kskowronski.data.entity.egeria.CashRegister;
import kskowronski.views.main.MainView;

@Route(value = "cashregister", layout = MainView.class)
@PageTitle("Cash Register")
@CssImport("./styles/views/helloworld/hello-world-view.css")
@RouteAlias(value = "", layout = MainView.class)
public class CashRegisterView extends HorizontalLayout {


    private Grid<CashRegister> gridCashRegisters;

    public CashRegisterView() {

    }
}
