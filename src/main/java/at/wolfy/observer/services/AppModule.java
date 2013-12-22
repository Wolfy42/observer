package at.wolfy.observer.services;

import java.io.IOException;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.cron.IntervalSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.slf4j.Logger;

import at.wolfy.observer.encoders.AudioRecordEncoder;
import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.impl.AnalyzingServiceImpl;
import at.wolfy.observer.services.impl.AudioRecordDAOImpl;
import at.wolfy.observer.services.impl.RecordsServiceImpl;

/**
 * AppModule for the Observer.
 */
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
    	binder.bind(RecordsService.class, RecordsServiceImpl.class);
    	binder.bind(AnalyzingService.class, AnalyzingServiceImpl.class);
    	binder.bind(AudioRecordDAO.class, AudioRecordDAOImpl.class);
    }
    
	@Startup
    public static void scheduleJobs(PeriodicExecutor executor, final AnalyzingService analyzingService) {
		executor.addJob(
			new IntervalSchedule(60000L),
			"AnalyzingService",
			new Runnable() {
				public void run() {
					analyzingService.analyzeRecords();
				}
			}
		);
    }
	
	@Match("*DAO")
	public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
	{
	    advisor.addTransactionCommitAdvice(receiver);
	}
	
	public static void contributeValueEncoderSource(MappedConfiguration<Class<?>, ValueEncoderFactory<?>> configuration) {
		configuration.overrideInstance(AudioRecord.class, AudioRecordEncoder.class);
	}

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        configuration.override(SymbolConstants.APPLICATION_VERSION, "0.0.1-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        configuration.add(SymbolConstants.MINIFICATION_ENABLED, "true");
    }


    public RequestFilter buildTimingFilter(final Logger log)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    return handler.service(request, response);
                } finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter)
    {
        configuration.add("Timing", filter);
    }
}
