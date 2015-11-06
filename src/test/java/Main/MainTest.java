package Main;

import static org.junit.Assert.*;

import org.junit.Test;

import Contenido.Anuncio;
import Contenido.Contenido;

public class MainTest {

	@Test
	public void test() {
		Contenido anuncio = new Anuncio();
		String tituloEsperado = "PUBLICIDAD";
		
		assertEquals(anuncio.obtenerTitulo(), tituloEsperado);
	}

}
