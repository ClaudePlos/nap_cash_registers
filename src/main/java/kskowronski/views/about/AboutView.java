package kskowronski.views.about;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kskowronski.views.main.MainView;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
public class AboutView extends Div {

    public AboutView() {
        setId("about-view");
        add(new Text("Content: "), new Html("<div><br></div>"));

        Anchor anchor = new Anchor("https://github.com/ClaudePlos/nap_cash_registers/issues/2", "Analiza");
        anchor.setTarget("_blank");
        add(anchor);
    }

}
