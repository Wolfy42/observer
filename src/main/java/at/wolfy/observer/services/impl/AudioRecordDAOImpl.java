package at.wolfy.observer.services.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.entities.AudioRecord_;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecordDAOImpl implements AudioRecordDAO {

	private static Logger log = Logger.getLogger(AudioRecordDAOImpl.class);
	
	@Inject
    protected EntityManager em;
	
	@Override
	public void add(AudioRecord audioRecord) {
		em.persist(audioRecord);
	}
	
	@Override
	public AudioRecord findById(Date start) {
		return em.find(AudioRecord.class, start);
	}

	@Override
	public List<AudioRecord> findAll() {
		log.info("Going to search all records.");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<AudioRecord> cq = cb.createQuery(AudioRecord.class);
        
        Root<AudioRecord> rootEntry = cq.from(AudioRecord.class);
        CriteriaQuery<AudioRecord> all = cq.select(rootEntry);
        all.orderBy(cb.desc(rootEntry.get(AudioRecord_.start)));
        
        TypedQuery<AudioRecord> allQuery = em.createQuery(all);
        return allQuery.getResultList();
	}
	
	@Override
	public List<Date> findAllStartDates() {
		log.info("Going to search for all dates.");
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Date> cq = cb.createQuery(Date.class);
        
        Root<AudioRecord> rootEntry = cq.from(AudioRecord.class);
        CriteriaQuery<Date> all = cq.select(rootEntry.get(AudioRecord_.start));
        
        TypedQuery<Date> allQuery = em.createQuery(all);
        return allQuery.getResultList();
	}
}
