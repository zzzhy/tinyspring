package org.triiskelion.tinyspring.dao.test;

import org.jboss.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.triiskelion.tinyspring.dao.OrderType;
import org.triiskelion.tinyspring.dao.TinyQuery;
import org.triiskelion.tinyspring.viewmodel.Page;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.triiskelion.tinyspring.dao.TinyPredicate.in;
import static org.triiskelion.tinyspring.dao.TinyPredicate.listOf;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 21, 2014
 * Time: 16:22
 */
public class TestTinyQuery {

	Logger log = Logger.getLogger(TestTinyQuery.class);

	EntityManager entityManager;

	String[] names = new String[]{ "alice", "beatrice", "carol", "daisy", "ellen" };

	@Before
	public void before() {

		EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("jpa-hsqldb");
		entityManager = emFactory.createEntityManager();


		entityManager.getTransaction().begin();
		for(String name : names) {
			DummyEntity entity = new DummyEntity();
			entity.setName(name);
			entityManager.persist(entity);
		}
		entityManager.getTransaction().commit();
	}

	@Test
	public void testCount() {

		TinyQuery<DummyEntity> query;
		query = new TinyQuery<>(entityManager, DummyEntity.class, true);

		long result = query.select().count();
		Assert.assertEquals(names.length, result);
	}

	@Test
	public void testGetPagedResult() {

		TinyQuery<DummyEntity> query = new TinyQuery<>(entityManager, DummyEntity.class, true);

		Page<DummyEntity> result = query.select().page(2, 2).getPagedResult();
		Assert.assertEquals(5, result.getTotal());
		Assert.assertEquals(3, result.getTotalPage());
		Assert.assertEquals(2, result.getMax());
		Assert.assertEquals(2, result.getPage());
		Assert.assertEquals(result.getDataSize(), result.getData().size());
	}

	@Test
	public void testIn() {

		TinyQuery<DummyEntity> query = new TinyQuery<>(entityManager, DummyEntity.class, true);

		List<DummyEntity> result
				= query.select()
				       .where(in("name", listOf("alice", "beatrice", "ellen")))
				       .orderBy("name", OrderType.ASC)
				       .getResultList();

		Assert.assertEquals(3, result.size());
		Assert.assertEquals("alice", result.get(0).getName());
		Assert.assertEquals("beatrice", result.get(1).getName());
		Assert.assertEquals("ellen", result.get(2).getName());
	}


}
