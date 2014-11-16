package scantest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.triiskelion.tinyspring.apidoc.annotation.ApiParam;
import org.triiskelion.tinyspring.apidoc.annotation.ApiRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: August 31, 2014
 * Time: 18:08
 */
@ApiRepository(name = "", description = "")
@Controller
@RequestMapping("test")
public class TestController {

	@RequestMapping("test")
	@ResponseBody
	public List<String> test(@ApiParam(name = "a") @RequestParam("aaa") List<Integer> a) {

		return null;
	}
}
