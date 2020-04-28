package aup.test.constants;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import aup.constants.EmailSenderConstants;
import aup.constants.LibConstants;
import aup.constants.RawDataSourceConstants;

public class ConstantsBuilderTest {

	@Test
	@DisplayName("EmailSenderConstants")
	public void EmailSenderTest() {
		Constructor<?>[] constructors = EmailSenderConstants.class.getDeclaredConstructors();
		Assertions.assertEquals(1, constructors.length);
		Assertions.assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
	}
	
	@Test
	@DisplayName("LibConstants")
	public void LibConstantsTest() {
		Constructor<?>[] constructors = LibConstants.class.getDeclaredConstructors();
		Assertions.assertEquals(1, constructors.length);
		Assertions.assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
	}
	 
	@Test
	@DisplayName("RawDataSourceConstants")
	public void RawDataSourceConstantsTest() {
		Constructor<?>[] constructors = RawDataSourceConstants.class.getDeclaredConstructors();
		Assertions.assertEquals(1, constructors.length);
		Assertions.assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
	}
	
}
