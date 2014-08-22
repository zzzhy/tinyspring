import org.trii.tinyspring.dao.TinyQuery;

import static org.trii.tinyspring.dao.TinyPredicate.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 21, 2014
 * Time: 16:22
 */
public class TestTinyQuery2 {

	public static void main(String... args) {

		TinyQuery<Object> query = new TinyQuery<>(null, Object.class, true);

		String q = query.ignoreNullParameter(true)
				.select()
				.where(equal("col0", 0).and(not(equal("col1", 1))))
				.and(equal("col2", 2))
				.or(in("col3", 3, 3).and(between("col4", null, 4)))
				.toString();

		System.out.println(q);
	}
}
