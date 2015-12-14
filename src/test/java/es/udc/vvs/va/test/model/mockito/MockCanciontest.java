package es.udc.vvs.va.test.model.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

@RunWith(MockitoJUnitRunner.class)
public class MockCanciontest {

	private Contenido c;
	
	
	@Before
	public void setUp() throws Exception {
		
		c = mock(Cancion.class);
		
		when(c.obtenerListaReproduccion()).thenReturn(Arrays.asList(c));
		when(c.buscar("c")).thenReturn(Arrays.asList(c));
		when(c.obtenerTitulo()).thenReturn("cancion 1");
		when(c.obtenerDuracion()).thenReturn(3);
		
		
	}
	
	
	@Test
	public void obtenerTituloTest() {
		
		assertEquals("cancion 1", c.obtenerTitulo());
	}

	@Test
	public void buscarTest(){
		
		List<Contenido> content = c.buscar("c1");
		
		assertEquals(content.get(0),c);
			
	}
	
	@Test
	public void obtenerDuracionTest() 
			throws ContentManagerException {
		
		assertEquals(3,c.obtenerDuracion());
		
	}
	
	@Test
	public void obtenerListaDereproduccionTest() 
			throws ContentManagerException { 
		
		List<Contenido> content = c.obtenerListaReproduccion();
		
		assertEquals(c, content.get(0));
		
	}
	
}
