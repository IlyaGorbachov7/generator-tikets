package proxy;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class TestProxy {

	@Test
	void test() {
		Proxy proxy = (Proxy) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{InterfaceAny.class}, new InvocationHandler() {
			Map<String, Object> context = new HashMap<>();
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				return null;

			}
		});
	}

	@Test
	void test2Enum(){
		Ticket.SessionType type = Ticket.SessionType.AUTUMN;
		System.out.println(type.key());
		System.out.println(type.name());
		System.out.println(type.toString());
		System.out.println(type.ordinal());
	}
}
