package kskowronski;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {

        event.addIndexHtmlRequestListener(indexHtmlResponse -> {
            System.out.println(indexHtmlResponse);
            indexHtmlResponse.getDocument().getElementsByAttribute("lang").addClass("notranslate\" translate=\"no");
        });

        event.getSource().addSessionInitListener( sessionInitEvent -> {
            //System.out.println("Init session");
        });

        event.getSource().addServiceDestroyListener( destroyEvent -> {
            System.out.println("destroyEvent");
        });

    }
}
