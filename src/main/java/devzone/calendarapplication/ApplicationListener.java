package devzone.calendarapplication;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import devzone.calendarapplication.database.MongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application Lifecycle Listener implementation class ApplicationListener
 *
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

	static Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

	public ApplicationListener() {
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("**************** contextDestroyed ****************");
		MongoDB.stopDB();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("**************** contextInitialized ****************");
		MongoDB.startDB();
	}

}
