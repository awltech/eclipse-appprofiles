package com.worldline.appprofiles.wizard.maven;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.worldline.appprofiles.wizard.maven.embedder.MavenManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.worldline.appprofiles.wizard.maven"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	// System property to enable debug
	public static final String DEBUG_PROPERTY = "appprofiles.wizard.maven.debug";

	/**
	 * The constructor
	 */
	public Activator() {
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

		try {
			String mavenSettingsLocalRepo = MavenManager.getMavenEmbedder().getLocalRepositoryPath();
			MavenLocalRepositoryLocator.setRepoLocation(mavenSettingsLocalRepo, false);
		} catch (Exception e) {
			Activator.getDefault().logError("An exception happened when locating repository", e);
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
	 * Returns ID in preferences for the management of Archetype versions
	 * 
	 * @param groupId
	 *            : Archetype Group ID
	 * @param artifactId
	 *            : Archetype Artifact ID
	 * @return id
	 */
	public static String getPreferenceIDForArchetypeVersion(String groupId, String artifactId) {
		return Activator.PLUGIN_ID + ".mavenArchetypes." + groupId + "." + artifactId;
	}

	/**
	 * Returns ID in preferences for the management of Archetype versions
	 * 
	 * @param archetypeId
	 *            : ID of the archetype (groupId.artifactId)
	 * @return id
	 */
	public static String getPreferenceIDForArchetypeVersion(String archetypeId) {
		return Activator.PLUGIN_ID + ".mavenArchetypes." + archetypeId;
	}

}
