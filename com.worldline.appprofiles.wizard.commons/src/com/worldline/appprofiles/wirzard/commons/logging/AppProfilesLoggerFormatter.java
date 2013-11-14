package com.worldline.appprofiles.wirzard.commons.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Default message formatter for AppProfiles Console.
 * 
 * Pattern is as follows : <loggerId>@<time> [<severity>] <message>
 * 
 * @author mvanbesien
 * 
 */
public class AppProfilesLoggerFormatter extends Formatter {

	/**
	 * Date Formatter
	 */
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * Pattern used for standard messages
	 */
	private static final String MSGPATTERN = "%1$-10s@%2$s [%3$-7s] %5$s\n";

	/**
	 * Pattern used for messages containing an Exception
	 */
	private static final String MSGPATTERN_WITH_EXCEPTION = MSGPATTERN + "%6$s\n";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		String dateValue = this.dateFormatter.format(new Date(record.getMillis()));
		String message = record.getParameters() == null || record.getParameters().length == 0 ? record.getMessage()
				: MessageFormat.format(record.getMessage(), record.getParameters());
		if (record.getThrown() == null)
			return String.format(MSGPATTERN, record.getLoggerName(), dateValue, record.getLevel().getName(),
					record.getSourceClassName(), message);
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, true);
		record.getThrown().printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		return String.format(MSGPATTERN_WITH_EXCEPTION, record.getLoggerName(), dateValue, record.getLevel().getName(),
				record.getSourceClassName(), message, stringWriter.toString());
	}
}
