import org.trii.tinyspring.utils.StopWatch;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 18, 2014
 * Time: 14:33
 */
public class Test {


	public static void main(String... args) throws InterruptedException {

		StopWatch stopWatch = new StopWatch("test").start();
		Thread.sleep(500);
		stopWatch.mark();
		Thread.sleep(300);
		stopWatch.mark();
		Thread.sleep(200);
		stopWatch.mark();
		System.out.println(stopWatch.prettyPrint());
	}
}
