import model.Person;
import org.junit.jupiter.api.Test;

import java.util.prefs.Preferences;

public class PreferenceTest {

	@Test
	public void test() {
		// Preferences - очень охаянная вещь. Грубо это файл в опр. Деректории
		// Можно получить Pref текущего пользователя и сохранять разные данные
		Preferences prefRoot = Preferences.userRoot();
		System.out.println(prefRoot.absolutePath());

		// Получить для всей системы
		Preferences prefSys = Preferences.systemRoot();
		System.out.println(prefSys.absolutePath());

		// Можно создать node-у от другой ноды - и это все pathName
		Preferences prefNode = prefSys.node("myApp");
		System.out.println(prefNode);
	}

	@Test
	public void test1() {
		Person s = new Person("Ilya",22);
		int code = System.identityHashCode(s);
		System.out.println(code);

		code = System.identityHashCode(s);
		System.out.println(code);

		s.setName("IlyaChange "); // здесь мы поменял
		code = System.identityHashCode(s);
		System.out.println(code);

		s =  new Person("Object", 1);
		code = System.identityHashCode(s);
		System.out.println(code);

		s = new Person("Ilya", 22);
		code = System.identityHashCode(s);
		System.out.println(code);
	}
}
