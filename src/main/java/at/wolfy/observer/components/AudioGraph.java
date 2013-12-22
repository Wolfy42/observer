package at.wolfy.observer.components;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import at.wolfy.observer.entities.AudioRecord;

@Import(stylesheet =  "context:jqplot/jquery.jqplot.min.css",
        library    = {"context:jqplot/jquery.jqplot.min.js",
		              "context:jqplot/jqplot.dateAxisRenderer.min.js"})
public class AudioGraph {

    @Property
    @Parameter(required = true)
    private AudioRecord audioRecord;
    
    public String getGraphName() {
    	return "Graph_" + audioRecord.getStart().getTime();
    }
    
    public String getGraphData() throws ParseException {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
    	
    	StringBuilder sb = new StringBuilder();
    	for (Integer result : audioRecord.getVolumes()) {
    		if  (sb.length() != 0) {
    			sb.append(",");
    		}
    		sb.append("[")
    		  .append(cal.getTimeInMillis())
    		  .append(",")
    		  .append(result)
    		  .append("]");
    		cal.add(Calendar.SECOND, 1);
    	}
    	return sb.toString();
    }
}
