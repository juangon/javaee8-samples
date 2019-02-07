package org.javaee8.cdi.dynamic.bean;

import static javax.faces.application.ViewVisitOption.RETURN_AS_MINIMAL_IMPLICIT_OUTCOME;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * 
 * @author Arjan Tijms
 */
public class MappingInit implements SystemEventListener {
    
 
	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		Application application = (Application) event.getSource();
		FacesContext facesContext = event.getFacesContext();
        ServletContext sc = (ServletContext) facesContext.getExternalContext().getContext();
        Map<String, ? extends ServletRegistration> servletRegistrations = (Map<String, ? extends ServletRegistration>) sc.getAttribute("mappings");
        
        if (servletRegistrations == null) {
        	return;
        }

        servletRegistrations
           .values()
           .stream()
           .filter(e -> e.getClassName().equals(FacesServlet.class.getName()))
           .findAny()
           .ifPresent(
               reg -> application
                             .getViewHandler()
                             .getViews(facesContext, "/", RETURN_AS_MINIMAL_IMPLICIT_OUTCOME)
                             .forEach(e -> reg.addMapping(e)));
    }
		

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof Application;
	}

}
