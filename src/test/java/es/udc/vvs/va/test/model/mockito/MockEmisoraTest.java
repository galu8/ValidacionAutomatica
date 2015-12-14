package es.udc.vvs.va.test.model.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;



@RunWith(MockitoJUnitRunner.class)
public class MockEmisoraTest {
	
	private Contenido c1,c2,c3,a,e;

	
	@Before
	public void setUp() throws Exception {
		c1 = new Cancion ("c1",3);
		c2 = new Cancion ("c2",5);
		c3 = new Cancion ("c3",4);
		a = new Anuncio();
		e = mock(Emisora.class);
		
		when(e.obtenerListaReproduccion())
			.thenReturn(Arrays.asList(c1,c2,c3));
		when(e.buscar("c1"))
			.thenReturn(Arrays.asList(c1));
		when(e.buscar("c"))
			.thenReturn(Arrays.asList(c1,c2,c3));
		when(e.buscar("nada"))
			.thenReturn(new ArrayList<Contenido>());
		when(e.obtenerTitulo())
			.thenReturn("Emisora 1");
		when(e.obtenerDuracion()).thenReturn(12);
		
	}
	
	@Test
	public void obtenerTituloTest(){
		
		assertEquals("Emisora 1", e.obtenerTitulo());
		
	}
	
	@Test
	public void buscarTest(){
		
		List<Contenido> c = e.buscar("c1");
		
		assertEquals(c.get(0),c1);
		
		c = e.buscar("c");
		
		assertEquals(c.get(0),c1);
		assertEquals(c.get(1),c2);
		assertEquals(c.get(2),c3);
		
		c = e.buscar("nada");
		
		assertTrue(c.isEmpty());
		
	}
	
	
	@Test
	public void obtenerDuracionTest() 
			throws ContentManagerException {
		
		
		assertEquals(12,e.obtenerDuracion());
		
		e.agregar(a, c3);
		verify(e).agregar(a, c3);
		verify(e,times(1)).agregar(a, c3);
		
		when(e.obtenerDuracion()).thenReturn(17);
		
		assertEquals(17,e.obtenerDuracion());
		
		e.eliminar(c2);
		verify(e).eliminar(c2);
		
		when(e.obtenerDuracion()).thenReturn(12);
		
		assertEquals(12, e.obtenerDuracion());
		
	}
	
	@Test
	public void obtenerListaDereproduccionTest() 
			throws ContentManagerException {
		
		//obtenemos la lista actual
		List<Contenido> c = e.obtenerListaReproduccion();
		
		assertEquals(c1,c.get(0));
		assertEquals(c2,c.get(1));
		assertEquals(c3,c.get(2));
		
		e.agregar(a, c3);
		verify(e).agregar(a, c3);
		
		when(e.obtenerListaReproduccion())
			.thenReturn(Arrays.asList(c1,c2,c3,a));
		
		c = e.obtenerListaReproduccion();
		
		assertEquals(a,c.get(3));
		
		//Probamos a eliminar 1 elemento
		e.eliminar(c1);
		verify(e).eliminar(c1);
		
		//actualizamos la condicion del objeto mock 
		when(e.obtenerListaReproduccion())
			.thenReturn(Arrays.asList(c2,c3,a));
		
		c = e.obtenerListaReproduccion();
		
		assertEquals(c2,c.get(0));
		assertEquals(c3,c.get(1));
		assertEquals(a,c.get(2));
		
	}
	
	
	
}
