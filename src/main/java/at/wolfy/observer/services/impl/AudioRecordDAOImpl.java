package at.wolfy.observer.services.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecordDAOImpl implements AudioRecordDAO {

	@Inject
    private Session session;
	
	@Override
	public void add(AudioRecord audioRecord) {
		session.persist(audioRecord);
	}
	
	@Override
	public AudioRecord findById(Date start) {
		return (AudioRecord) session.createCriteria(AudioRecord.class)
		       .add(Restrictions.eq("start", start))
		       .list().get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AudioRecord> findAll() {
		return  session.createCriteria(AudioRecord.class).list();
	}
}
