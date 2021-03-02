package kskowronski.views.settings;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kskowronski.views.main.MainView;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
public class SettingsView extends Div {


    public SettingsView() {
        setId("settings-view");
        add(new Text("Ustawienia"));
    }

}
