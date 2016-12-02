package edu.mit.scansite.server.dispatch;

import com.google.inject.servlet.ServletModule;

import edu.mit.scansite.server.MotifFileUpload;

import net.customware.gwt.dispatch.server.guice.GuiceStandardDispatchServlet;

/**
 * @author Konstantin Krismer
 */
public final class DispatchServletModule extends ServletModule {
  @Override
  protected void configureServlets() {
    serve("*.gupld").with(MotifFileUpload.class);
    serve("/scansite/dispatch").with(GuiceStandardDispatchServlet.class);
  }
}
