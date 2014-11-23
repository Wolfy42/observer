package at.wolfy.observer.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.wolfy.observer.entities.AudioRecord;
import at.wolfy.observer.services.AudioRecordDAO;

public class AudioRecordDAOTest {
	
    private static EntityManagerFactory emFactory;

    private EntityManager em;
    
    private AudioRecordDAO dao;

    @BeforeClass
    public static void beforeClass() throws Exception {
    	emFactory = Persistence.createEntityManagerFactory("observer");
    }
    
    @AfterClass
    public static void afterClass() {
    	emFactory.close();
    }
    
    @Before
    public void before() {
    	em = emFactory.createEntityManager();
    	AudioRecordDAOImpl daoImpl = new AudioRecordDAOImpl();
    	daoImpl.em = em;
    	dao = daoImpl;
    	
    	em.getTransaction().begin();
    	for (AudioRecord ar : dao.findAll()) {
    		em.remove(ar);
    	}
    	em.getTransaction().commit();
    }
    
    @After
    public void after() {
    	em.close();
    }
    
    @Test
    public void testNoObject() {
    	em.getTransaction().begin();
    	
    	assertEquals(0, dao.findAll().size());
    	assertNull(dao.findById(new Date()));
    	
    	em.getTransaction().commit();
    }
    
    @Test
    public void testCreateAndFindObject() {
    	em.getTransaction().begin();
    	
    	Date start = new Date();
    	AudioRecord ar = new AudioRecord();
    	ar.setStart(start);
    	dao.add(ar);
    	
    	em.getTransaction().commit();
    	em.getTransaction().begin();
    	
    	assertEquals(1, dao.findAll().size());
    	assertEquals(start, dao.findAll().get(0).getStart());
    	assertEquals(start, dao.findById(start).getStart());
    	
    	em.getTransaction().commit();
    }
    
    @Test
    public void testFindAllStartDates() {
    	em.getTransaction().begin();
    	
    	Date start = new Date();
    	AudioRecord ar = new AudioRecord();
    	ar.setStart(start);
    	dao.add(ar);
    	
    	Date start2 = new Date();
    	AudioRecord ar2 = new AudioRecord();
    	ar2.setStart(start2);
    	dao.add(ar2);
    	
    	em.getTransaction().commit();
    	em.getTransaction().begin();
    	
    	List<Date> startDates = dao.findAllStartDates();
    	assertEquals(2, startDates.size());
    	assertTrue(startDates.contains(start));
    	assertTrue(startDates.contains(start2));
    	
    	em.getTransaction().commit();
    }
}
