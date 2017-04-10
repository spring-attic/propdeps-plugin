package sample;

import org.junit.Test;

public class HasOptionalTest {

	@Test(expected = NoClassDefFoundError.class)
	public void hasOptional() {
		HasOptional.doStuffWithOptionalDependency();
	}
}
