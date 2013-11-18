package com.worldline.appprofiles.wizard.commons.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;

/**
 * Central Console Manager for AppProfiles Applications
 * 
 * @author mvanbesien
 * 
 */
public class AppProfilesConsoleManager {

	/**
	 * Name of the console
	 */
	private static final String CONSOLE_NAME = "AppProfiles Console";

	/**
	 * Internal instance of Eclipse console
	 */
	private MessageConsole console;

	/**
	 * Singleton holder class
	 * 
	 * @author mvanbesien
	 * 
	 */
	private static class SingletonHolder {
		private static AppProfilesConsoleManager instance = new AppProfilesConsoleManager();
	}

	/**
	 * Private constructor. Internally creates and initializes console instance.
	 */
	private AppProfilesConsoleManager() {
		IConsole[] consoles = ConsolePlugin.getDefault().getConsoleManager().getConsoles();
		for (int i = 0; i < consoles.length && this.console == null; i++) {
			if (CONSOLE_NAME.equals(consoles[i].getName()) && consoles[i] instanceof MessageConsole) {
				this.console = (MessageConsole) consoles[i];
			}
		}
		if (this.console == null) {
			this.console = new MessageConsole(CONSOLE_NAME, null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { this.console });
			this.console.initialize();
		}
	}

	/**
	 * @return instance of Console Manager
	 */
	public static AppProfilesConsoleManager getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * This method is used to link the provided logger with this console
	 * instance. It uses the provided formatter for formatting messages.
	 * 
	 * @param logger
	 * @param formatter
	 */
	public void register(Logger logger, Formatter formatter) {
		if (formatter == null)
			throw new IllegalStateException("Cannot register logger without formatter");
		StreamHandler streamHandler = new StreamHandler(this.console.newMessageStream(), formatter) {

			@Override
			public synchronized void publish(LogRecord record) {
				super.publish(record);
				this.flush();
			}
		};
		logger.addHandler(streamHandler);
	}

	/**
	 * This method is used to link the provided logger with this console
	 * instance. It uses a default message formatter.
	 * 
	 * @see AppProfilesLoggerFormatter
	 * @param logger
	 */
	public void register(Logger logger) {
		this.register(logger, new AppProfilesLoggerFormatter());
	}

	/**
	 * Programmatically shows console in editor
	 */
	public void show() {
		this.console.activate();
	}

	/**
	 * Clears the contents of the console
	 */
	public void clear() {
		Display display = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		display.asyncExec(new Runnable() {

			public void run() {
				AppProfilesConsoleManager.this.console.getDocument().set("");
				
			}
		});
	}

}
