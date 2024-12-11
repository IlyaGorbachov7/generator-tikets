package preference;

import model.Person;
import org.junit.jupiter.api.Test;

import java.util.IdentityHashMap;

public class JavahashCode {

	@Test
	public void test() {
		Person s = new Person("Ilya", 22);
		int code = System.identityHashCode(s);
		System.out.println(code);

		code = System.identityHashCode(s);
		System.out.println(code);

		s.setName("IlyaChange ");
		code = System.identityHashCode(s);
		System.out.println(code);

		s = new Person("Object", 1);
		code = System.identityHashCode(s);
		System.out.println(code);
	}

	/**
	 * String Pool
	 */
	@Test
	public void testString() {
		String s = "String"; 						// from String Pool
		String s1 = "String"; 						// from String Pool
		String s2 = new String(s); 					// from Heap
		String s3 = new String("AAAklj"); 	// from Heap

		System.out.println(s == s1); 			// true
		System.out.println(s == s2); 			// false
		System.out.println(s.intern() == s2); 	// false
		System.out.println(s == s2.intern()); 	// true,

		System.out.println(s3 == s3.intern()); 	// false
	}
}
