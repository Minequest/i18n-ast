import java.util.Properties;

public class Test {
	
	public static void _(String s, Object... args) {
		
	}
	
	public static void test() {
		// testing stuff that does nothing
		_("Hello World!", 0);
		_("Wheeee", 1);
		_("Hello World 2!", 10, new Properties());
	}
	
}
