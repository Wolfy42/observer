package at.wolfy.observer.services;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.wolfy.observer.entities.AudioRecord;

public interface AudioRecordDAO {

	@CommitAfter
	public void add(AudioRecord audioRecord);
	
	@CommitAfter
	public AudioRecord findById(Date start);
	
	@CommitAfter
	public List<Date> findAllStartDates();
	
	@CommitAfter
	public List<AudioRecord> findAll();
}
