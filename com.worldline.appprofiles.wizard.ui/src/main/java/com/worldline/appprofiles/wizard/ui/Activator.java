package com.worldline.appprofiles.wizard.ui;

import java.util.logging.Level;
import java.util.logging.Logger;





import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.worldline.appprofiles.wirzard.commons.logging.AppProfilesConsoleManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// Property file in .metadata/.plugin/PLUGIN_ID
	public static final String PROPERTIES_FILE = "config.properties";

	// The plug-in ID
	public static final String PLUGIN_ID = "com.worldline.appprofiles.wizard.ui"; //$NON-NLS-1$

	// Logger property
	private static final String LOGGER_LVL_KEY = PLUGIN_ID + ".logger.level"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	public static final Logger logger = Logger.getLogger("AppProfilesWizardUI");

	private boolean imageRegistryUsed = false;

	/**
	 * The constructor
	 */
	public Activator() {
		logger.setLevel(Level.WARNING);
		AppProfilesConsoleManager.getInstance().register(logger);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// Handle logger level on startup
		try {
			String property = System.getProperty(LOGGER_LVL_KEY);
			if (property != null && property.length() > 0) {
				Level parsed = Level.parse(property);
				logger.setLevel(parsed);
			}
		} catch (IllegalArgumentException e) {
			getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		if (imageRegistryUsed)
			Activator.plugin.getImageRegistry().dispose();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Logs message with INFO severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
	public void logInfo(String message) {
		this.getLog().log(new Status(IStatus.INFO, Activator.PLUGIN_ID, message));
	}

	/**
	 * Logs message with WARNING severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
	public void logWarning(String message) {
		this.getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, message));
	}

	/**
	 * Logs message with ERROR severity in Error Log
	 * 
	 * @param message
	 *            : String
	 */
	public void logError(String message) {
		this.getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message));
	}

	/**
	 * Logs message and Throwable with Error severity in Error Log
	 * 
	 * @param message
	 *            : String
	 * @param t
	 *            : Throwable
	 */
	public void logError(String message, Throwable t) {
		this.getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, t));
	}

	/**
	 * Returns image in plugin
	 * 
	 * @param pluginId
	 *            : Id of the plugin containing thie image
	 * @param imageFilePath
	 *            : image File Path in plugin
	 * @return Image if exists
	 */
	public Image getImage(String pluginId, String imageFilePath) {
		Image image = Activator.getDefault().getImageRegistry().get(pluginId + ":" + imageFilePath);
		if (image == null) {
			image = loadImage(pluginId, imageFilePath);
		}
		return image;
	}

	/**
	 * Loads image in Image Registry is not available in it
	 * 
	 * @param pluginId
	 *            : Id of the plugin containing thie image
	 * @param imageFilePath
	 *            : image File Path in plugin
	 * @return Image if loaded
	 */
	private synchronized Image loadImage(String pluginId, String imageFilePath) {
		imageRegistryUsed = true;
		String id = pluginId + ":" + imageFilePath;
		Image image = Activator.getDefault().getImageRegistry().get(id);
		if (image != null)
			return image;
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, imageFilePath);
		if (imageDescriptor != null) {
			image = imageDescriptor.createImage();
			Activator.getDefault().getImageRegistry().put(pluginId + ":" + imageFilePath, image);
		}
		return image;
	}

	/**
	 * Returns image in this plugin
	 * 
	 * @param imageFilePath
	 *            : image File Path in this plugin
	 * @return Image if exists
	 */
	public Image getImage(String imageFilePath) {
		Image image = Activator.getDefault().getImageRegistry().get(Activator.PLUGIN_ID + ":" + imageFilePath);
		if (image == null)
			image = loadImage(Activator.PLUGIN_ID, imageFilePath);
		return image;
	}

}
