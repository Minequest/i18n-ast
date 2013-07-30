import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import com.theminequest.i18n.Main;


public class UnderscoreTest {
	
	@Test
	public void test() throws IOException {
		Properties prop = new Properties();
		
		URL url = this.getClass().getResource("/Test.java");
		File test = new File(url.getFile());
		
		Main.recurse(test, prop);
		assertTrue(prop.containsKey("Hello World!"));
		assertTrue(prop.containsKey("Wheeee"));
		assertTrue(prop.containsKey("Hello World 2!"));
	}
	
}
