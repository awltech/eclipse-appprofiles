package com.worldline.appprofiles.wizard.ui.model.loaders;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.ui.Activator;

public class ExtensionPointHelper {

	public static String optionalArgumentValue(IConfigurationElement element, String value) {
		return element.getAttribute(value);
	}

	public static String mandatoryArgumentValue(IConfigurationElement element, String value)
			throws NullArgumentException {
		String result = optionalArgumentValue(element, value);
		if (result == null || "".equals(result))
			throw new NullArgumentException(element.getName(), value);
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T optionalExecutable(Class<T> clazz, IConfigurationElement element, String value) {
		if (value == null || value.length() == 0) {
			// In this case, nothing was specified which is not a big deal since
			// it is optional. We return nothing then.
			// https://github.com/awltech/eclipse-appprofiles/issues/2
			return null;
		}
		try {
			return (T) element.createExecutableExtension(value);
		} catch (ClassCastException e) {
			Activator.getDefault().getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Type of executable is false", e));
		} catch (Exception e) {
			Activator
					.getDefault()
					.getLog()
					.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID,
							"Exception caught when loading executable !!!", e));
		}
		return null;
	}

	public static <T> T mandatoryExecutable(Class<T> clazz, IConfigurationElement element, String value)
			throws NullExecutableException {
		T optionalExecutable = optionalExecutable(clazz, element, value);
		if (optionalExecutable == null)
			throw new NullExecutableException(element.getName(), value);
		return optionalExecutable;
	}

}
