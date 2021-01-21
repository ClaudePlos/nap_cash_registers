package kskowronski.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/login")
@PageTitle("Login | Rekeep")

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm login = new LoginForm();

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setI18n(createPolandI18n());

        add(new H1("Rekeep"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    private LoginI18n createPolandI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Nome do aplicativo");
        i18n.getHeader().setDescription("Opis aplikacji");
        i18n.getForm().setUsername("Login");
        i18n.getForm().setTitle("");
        i18n.getForm().setSubmit("Zaloguj");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setForgotPassword("");
        i18n.getErrorMessage().setTitle("Niepoprawna nazwa użytkownika lub hasło");
        i18n.getErrorMessage().setMessage("Sprawdź, czy podałeś poprawną nazwę użytkownika i hasło, i spróbuj ponownie.");
        i18n.setAdditionalInformation(
                "Info: v2020/10");
        return i18n;
    }
}
