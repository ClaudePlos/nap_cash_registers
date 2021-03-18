package kskowronski;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringComponent
public class ApplicationServiceInitListener implements VaadinServiceInitListener {
    static Logger logger = Logger.getLogger(ApplicationServiceInitListener.class.getName());

    @Override
    public void serviceInit(ServiceInitEvent event) {

        event.addIndexHtmlRequestListener(indexHtmlResponse ->
            indexHtmlResponse.getDocument().getElementsByAttribute("lang")
                    .addClass("notranslate")
                    .attr("translate","no")
        );

        event.getSource().addSessionInitListener( sessionInitEvent -> {

        });

        event.getSource().addServiceDestroyListener( destroyEvent -> logger.log(Level.INFO, "destroyEvent"));

    }
}
