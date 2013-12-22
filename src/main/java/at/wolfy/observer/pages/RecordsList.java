package at.wolfy.observer.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;
import at.wolfy.observer.streams.AttachmentStreamResponse;
import at.wolfy.observer.streams.AttachmentStreamResponse.ContentType;

public class RecordsList {

	@Inject
	private AudioRecordDAO audioRecordDAO;

	@Property
	private AudioRecord record;

	@Persist
	private List<AudioRecord> audioRecords;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@BeginRender
	public void init() {
		audioRecords = audioRecordDAO.findAll();
	}

	public List<AudioRecord> getRecords() {
		return audioRecords;
	}

	public String getRecordName() throws ParseException {
		return dateFormatter.format(record.getStart());
	}
	
	@OnEvent(component="recordLink")
	public StreamResponse getRecordStream(AudioRecord audioRecord) throws FileNotFoundException {
		String path = audioRecord.getDirectory() + File.separator + audioRecord.getFile();
		InputStream input = new FileInputStream(path);
		return new AttachmentStreamResponse(input, audioRecord.getFile(), ContentType.WAV);
	}
}
