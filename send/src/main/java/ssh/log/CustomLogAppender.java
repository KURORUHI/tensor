package ssh.log;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(name = "CustomLogAppender", category = "Core", elementType = "appender", printObject = true)
public final class CustomLogAppender extends AbstractAppender {

	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();

	protected CustomLogAppender(String name, Filter filter, Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);
	}

	@Override
	public void append(LogEvent event) {
		readLock.lock();
		try {
			final byte[] bytes = getLayout().toByteArray(event);
//			System.err.write(bytes);
			PanelLog.logMessages(new String(bytes));
		} catch (Exception ex) {
			if (!ignoreExceptions()) {
				throw new AppenderLoggingException(ex);
			}
		} finally {
			readLock.unlock();
		}
	}

	@PluginFactory
	public static CustomLogAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") final Filter filter, @PluginAttribute("otherAttribute") String otherAttribute) {
		if (name == null) {
			LOGGER.error("No name provided for CustomLogAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new CustomLogAppender(name, filter, layout, true);
	}
}