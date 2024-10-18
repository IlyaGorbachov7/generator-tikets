package proxy;

public class InterfaceAnyImpl implements InterfaceAny{
	@Override
	public void methodWidthOutParam() {
		System.out.println("Hi I method without param");
	}

	@Override
	public void methodWithWithParam(String v, String v1) {
		System.out.println("Ox-ox-ox I method with 2 param");
	}

	@Override
	public void methodWithMorParam(int intV, String strV) {
		System.out.println("EEEE boy");
	}
}
