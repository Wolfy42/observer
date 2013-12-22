package at.wolfy.observer.components;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
    private List<AudioRecord> audioRecords;
    
    @Property
    @Parameter
    private boolean startWithZero;
    
    public String getGraphName() {
    	return "Graph_" + audioRecords.hashCode();
    }
    
    public String getTickFormat() {
    	if (startWithZero) {
    		return "%M:%S";
    	} else {
    		return "%H:%M";
    	}
    }
    
    public String getGraphData() throws ParseException {
		Calendar cal = GregorianCalendar.getInstance();
		StringBuilder sb = new StringBuilder();
		
    	for (AudioRecord ar : audioRecords) {
    		if (startWithZero) {
    			cal.set(Calendar.MINUTE, 0);
    			cal.set(Calendar.SECOND, 0);
    		} else {
    			cal.setTime(ar.getStart());
    		}
    		
        	for (Integer result : ar.getVolumes()) {
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
    	}
    	
    	return sb.toString();
    }
}
