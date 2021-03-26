package kskowronski.views.main;

import java.util.Collection;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import kskowronski.data.services.global.GlobalDataService;
import kskowronski.views.cashregister.CashRegisterView;
import kskowronski.views.about.AboutView;
import kskowronski.views.components.MyNotification;
import kskowronski.views.settings.SettingsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    private transient GlobalDataService globalDataService;

    @Autowired
    public MainView(GlobalDataService globalDataService) {
        this.globalDataService = globalDataService;
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        Anchor logout = new Anchor("/logout","Wyloguj");
        layout.add(new Image("images/user.svg", "Avatar"), logout);
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "nap_cash_registers logo"));
        logoLayout.add(new H1("nap_cash_registers"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    /*
    * Generate global data
     */
    private Component[] createMenuItems()  {
        Tab[] tabs = null;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        if (authorities.isEmpty()){
            MyNotification.openAlert("Brak uprawnień do strony!!!", 3000,  Notification.Position.MIDDLE);
            tabs = new Tab[]{};
            new MyThread().start();
            UI.getCurrent().getPage().executeJs("location.assign('logout')");
            return tabs;
        }

        globalDataService.getGlobalData(userDetails, authorities);

        if (authorities.contains(new SimpleGrantedAuthority("USER"))){
            tabs = new Tab[]{
                    createTab("Kasy", CashRegisterView.class),
            };
        }

        if (authorities.contains(new SimpleGrantedAuthority("ACCOUNTANT"))){
            tabs = new Tab[]{
                    createTab("Kasy", CashRegisterView.class),
            };
        }

        if (authorities.contains(new SimpleGrantedAuthority("ADMIN"))){
            tabs = new Tab[]{
                    createTab("Kasy", CashRegisterView.class),
                    createTab("Ustawienia", SettingsView.class),
                    createTab("About", AboutView.class)
            };
        }

        if (authorities.isEmpty()){
            tabs = new Tab[]{
                    createTab("Kasy", CashRegisterView.class),
            };
        }

        return tabs;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}

class MyThread extends Thread{
    @Override
    public void run(){
        try {
            Thread.sleep(3000);
            System.err.println("Brak uprawnień");
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
