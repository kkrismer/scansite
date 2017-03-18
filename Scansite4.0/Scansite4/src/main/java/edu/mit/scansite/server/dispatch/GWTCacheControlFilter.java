package edu.mit.scansite.server.dispatch;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link Filter} to add cache control headers for GWT generated files to ensure
 * that the correct files get cached.
 * @author See Wah Cheng
 * @author Konstantin Krismer
 */
public class GWTCacheControlFilter implements Filter {
	private static final long ONE_DAY_MS = 86400000L;
	private static final long ONE_YEAR_MS = ONE_DAY_MS * 365;

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestURI = httpRequest.getRequestURI();

		if (requestURI.contains(".nocache.")) {
			final Date now = new Date();
			// set create date to current timestamp
			httpResponse.setDateHeader("Date", now.getTime());
			// set modify date to current timestamp
			httpResponse.setDateHeader("Last-Modified", now.getTime());
			// set expiry to back in the past (makes us a bad candidate for
			// caching)
			httpResponse.setDateHeader("Expires", 0);
			// HTTP 1.0 (disable caching)
			httpResponse.setHeader("Pragma", "no-cache");
			// HTTP 1.1 (disable caching of any kind)
			// HTTP 1.1 'pre-check=0, post-check=0' => (Internet Explorer should
			// always check)
			// Note: no-store is not included here as it will disable offline
			// application storage on Firefox
			httpResponse.setHeader("Cache-control",
					"no-cache, must-revalidate, pre-check=0, post-check=0");

			// httpResponse.setHeader("Cache-control",
			// "public, max-age=0, must-revalidate");
		} else if (requestURI.contains(".cache.")) {
			long now = System.currentTimeMillis();
			httpResponse.setDateHeader("Date", now);
			httpResponse.setDateHeader("Expires", now + ONE_YEAR_MS);
		}

		filterChain.doFilter(request, response);
	}
}
