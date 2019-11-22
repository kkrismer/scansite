package edu.mit.scansite.server.dispatch;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import net.customware.gwt.dispatch.server.guice.ServerDispatchModule;

/**
 * @author Konstantin Krismer
 */
@Singleton
public class BootstrapListener extends GuiceServletContextListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static DbConnector getDbConnector(ServletContext context) {
		return (DbConnector) context.getAttribute("db");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		ServletContext context = servletContextEvent.getServletContext();
		try {
			context.setAttribute("db", DbConnector.getInstance());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServerDispatchModule(), new ActionsModule(), new DispatchServletModule());
	}
}
