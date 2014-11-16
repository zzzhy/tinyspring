import org.triiskelion.tinyspring.utils.StopWatch;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 18, 2014
 * Time: 14:33
 */
public class TestStopWatch {


	public static void main(String... args) throws InterruptedException {

		StopWatch stopWatch = new StopWatch("test").start();

		Thread.sleep(200);
		stopWatch.mark();
		stopWatch.beginLoop("loop");
		for(int i = 0; i < 10; i++) {
			Thread.sleep((long) (Math.random()*500));
			stopWatch.markLoop();
		}
		stopWatch.endLoop();
		stopWatch.prettyPrint();
	}
}
