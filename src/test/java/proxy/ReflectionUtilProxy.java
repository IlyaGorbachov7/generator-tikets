package proxy;

public class ReflectionUtilProxy {

	private static void traceMethodCall() {
		String methodName = getCallingMethodName();
		System.out.println("Method called: " + methodName);
	}

	private static String traceMethodCell(int value, long value2) {
		return getCallingMethodName(2);
	}

	public static String getCallingMethodName() {
		return getCallingMethodName(3);
	}

	public static String getCallingMethodName(int deep) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		// Пропускаем первые два элемента (main() и getCallingMethodName())
		return stackTrace[deep].getMethodName();
	}
}
