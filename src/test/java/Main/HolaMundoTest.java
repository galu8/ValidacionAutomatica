package Main;

import static org.junit.Assert.*;

import org.junit.Test;

public class HolaMundoTest {

	@Test
	public void test() {
		HolaMundo hm = new HolaMundo();
		
		assertEquals(hm.getMensaje(), "Hola Mundo");
	}

}
