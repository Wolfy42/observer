package at.wolfy.observer.services.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecordDAOImpl implements AudioRecordDAO {

	private static Logger log = Logger.getLogger(AudioRecordDAOImpl.class);
	
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
		       .uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AudioRecord> findAll() {
		log.info("Going to search all records.");
		return session.createCriteria(AudioRecord.class)
					  .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					  .addOrder(Order.desc("start"))
					  .list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Date> findAllStartDates() {
		log.info("Going to search for all dates.");
		return session.createCriteria(AudioRecord.class)
				.setProjection(Projections.property("start"))
				.list();
	}
}
