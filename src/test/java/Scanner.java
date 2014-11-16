import org.triiskelion.tinyspring.apidoc.ApiScanner;
import org.triiskelion.tinyspring.apidoc.model.ApiDocumentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 18:05
 */
public class Scanner {

	public static void main(String... args) {

		List<String> list = new ArrayList<>();
		list.add("scantest");
		ApiDocumentation a = ApiScanner.scan("1.2.3", "/a", list);
		int ab = 1;
	}
}
