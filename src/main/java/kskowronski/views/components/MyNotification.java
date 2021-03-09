package kskowronski.views.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Component
@UIScope
public class MyNotification extends Notification {


    public static void openAlert(String text, Integer duration, Notification.Position position){
        Div content = new Div();
        content.addClassName("my-style");
        content.setText(text);

        Notification notification = new Notification(content);
        notification.setDuration(duration);
        notification.setPosition(position);

        // @formatter:off
        String styles = ".my-style { "
                + "  color: red; font-size: 24px"
                + " }";
        // @formatter:on

        StreamRegistration resource = UI.getCurrent().getSession()
                .getResourceRegistry()
                .registerResource(new StreamResource("styles.css", () -> {
                    byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
                    return new ByteArrayInputStream(bytes);
                }));
        UI.getCurrent().getPage().addStyleSheet(
                "base://" + resource.getResourceUri().toString());
        notification.open();
    }


}
