package com.worldline.appprofiles.wizard.maven.archetype.goals;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.worldline.appprofiles.wizard.maven.Activator;

/**
 * Class that helps to define an Eclipse HTTP proxy
 * 
 * @author
 * 
 */
public class ProxyHelper {

	// Eclipse proxy parameters
	private static final String HTTP_PROXY_SET_KEY = "http.proxySet";
	private static final String HTTP_PROXY_HOST_KEY = "http.proxyHost";
	private static final String HTTP_PROXY_PORT_KEY = "http.proxyPort";

	private static boolean isProxyDefined = false;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void applyEclipseProxySettingsForURL(String url) {
		if (!isProxyDefined) {
			try {
				ServiceTracker proxyTracker = new ServiceTracker(FrameworkUtil.getBundle(ProxyHelper.class)
						.getBundleContext(), IProxyService.class.getName(), null);
				proxyTracker.open();

				URI uri = new URI(url);
				IProxyService proxyService = (IProxyService) proxyTracker.getService();
				IProxyData[] proxyDataForHost = proxyService.select(uri);

				for (IProxyData data : proxyDataForHost) {
					if (data.getHost() != null) {
						System.setProperty(HTTP_PROXY_SET_KEY, "true");
						System.setProperty(HTTP_PROXY_HOST_KEY, data.getHost());
					}
					if (data.getHost() != null) {
						System.setProperty(HTTP_PROXY_PORT_KEY, String.valueOf(data.getPort()));
					}
				}
				// Close the service and close the service tracker
				proxyService = null;
				proxyTracker.close();
				isProxyDefined = true;
			} catch (URISyntaxException e) {
				Activator.getDefault().logError("Error setting up proxy", e);
			}
		}
	}
}
