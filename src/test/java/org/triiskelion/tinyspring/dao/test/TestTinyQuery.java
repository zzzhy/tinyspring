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

import static org.triiskelion.tinyspring.dao.TinyPredicate.*;

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
		for(int i = 0; i < names.length; i++) {
			DummyEntity entity = new DummyEntity();
			entity.setName(names[i]);
			entity.setSort(i);

			entityManager.persist(entity);
		}
		entityManager.getTransaction().commit();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIgnoreNull() {

		TinyQuery<DummyEntity> query;
		query = new TinyQuery<>(entityManager, DummyEntity.class, true);

		// where clause will be ignored thus query is as same as count all.
		long result = query.ignoreNull(true).select()
		                   .where(equal("name", null))
		                   .count();
		Assert.assertEquals(names.length, result);

		// this should throw an IllegalArgumentException
		result = query.ignoreNull(false).select()
		              .where(equal("name", null))
		              .count();
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
	public void testPredicateIn() {

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
