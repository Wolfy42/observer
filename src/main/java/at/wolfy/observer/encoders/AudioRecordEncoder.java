package at.wolfy.observer.encoders;

import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecordEncoder implements ValueEncoder<AudioRecord>, ValueEncoderFactory<AudioRecord> {

	@Inject
	private AudioRecordDAO audioRecordDAO;

	@Override
	public String toClient(AudioRecord value) {
		return String.valueOf(value.getStart().getTime());
	}

	@Override
	public AudioRecord toValue(String id) {
		return audioRecordDAO.findById(new Date(Long.valueOf(id)));
	}

	@Override
	public ValueEncoder<AudioRecord> create(Class<AudioRecord> type) {
		return this;
	}
}
