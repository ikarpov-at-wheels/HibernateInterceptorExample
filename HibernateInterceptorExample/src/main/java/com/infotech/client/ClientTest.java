package com.infotech.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.infotech.entities.Person;
import com.infotech.interceptor.LoggerInterceptor;
import com.infotech.util.HibernateUtil;

public class ClientTest {

	private static final Logger logger = LogManager.getLogger(ClientTest.class);

	public static void main(String[] args) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory()
				.withOptions()
				.interceptor(new LoggerInterceptor())
				.openSession()) {
			tx = session.beginTransaction();

			Person person1 = new Person();
			person1.setPersonName("Sean Murphy");
			person1.setUsername("seanm");
			person1.setPassword("pass#123");
			person1.setAccessLevel(1);

			session.persist(person1);

			logger.info("Person Record is saved successfully");

			tx.commit();
			
		} catch (Exception e) {
			logger.error("Failed to save Person Records:" + e);
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}
		
		try (Session session = HibernateUtil.getSessionFactory()
				.withOptions()
				.interceptor(new LoggerInterceptor())
				.openSession()) {
			Person person = session.get(Person.class, 1L);
			System.out.println(person);
		} catch (Exception e) {
			logger.error("Failed to fetch Record:" + e);
			throw e;
		}
	}
}