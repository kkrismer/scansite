package edu.mit.scansite.server.dispatch;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import net.customware.gwt.dispatch.server.guice.ServerDispatchModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.databaseconnector.DbConnector;
import edu.mit.scansite.shared.DatabaseException;

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
		try {
//			getDbConnector(servletContextEvent.getServletContext())
//					.closeDataSource();
//			No need to close the data source
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		try {
			DbConnector dbConnector = new DbConnector(ServiceLocator
					.getInstance().getDbAccessFile());
			dbConnector.initConnectionPooling();
			ServletContext context = servletContextEvent.getServletContext();
			context.setAttribute("db", dbConnector);
		} catch (DatabaseException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServerDispatchModule(),
				new ActionsModule(), new DispatchServletModule());
	}
}
