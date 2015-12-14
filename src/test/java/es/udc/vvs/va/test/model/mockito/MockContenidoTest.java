package es.udc.vvs.va.test.model.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.ServidorPlano;


@RunWith(MockitoJUnitRunner.class)
public class MockContenidoTest {
	
	private Contenido c1,c2,c3,a;

	
	@Before
	public void setUp() throws Exception {
		c1 = new Cancion ("c1",300);
		c2 = new Cancion ("c2",250);
		c3 = new Cancion ("c3",320);
		a = new Anuncio();
		
	}
	

	
	@Test
	public void obtenerDuracionTest() throws ContentManagerException {
		
		Contenido e = mock(Emisora.class, withSettings().name("Emisora 1"));
		
		e.agregar(c1, null);
		e.agregar(c2, c1);
		e.agregar(c3,c2);
		
		verify(e).agregar(c2, c1);
		verify(e).agregar(c1, null);
		verify(e).agregar(c3,c2);
		
		when(e.obtenerDuracion()).thenReturn(870);
		assertEquals(870,e.obtenerDuracion());
		
		e.eliminar(c1);
		
		verify(e).eliminar(Matchers.eq(c1));
		
		when(e.obtenerDuracion()).thenReturn(570);
		assertEquals(570,e.obtenerDuracion());
		
	}
	
	@Test
	public void obtenerListaDereproduccionTest() throws ContentManagerException {
		
		Contenido e = mock(Emisora.class, withSettings().name("Emisora 1"));
		
		e.agregar(c1, null);
		e.agregar(c2, c1);
		e.agregar(c3,c2);
		
		verify(e).agregar(c1, null);
		verify(e).agregar(c2, c1);
		verify(e).agregar(c3,c2);

		//List<Contenido> listRep = spy(e.obtenerListaReproduccion());
		
		when(e.obtenerListaReproduccion()).thenReturn(Arrays.asList(c1,c2,c3));
		
		List<Contenido> c = e.obtenerListaReproduccion();

		assertEquals(c1,c.get(0));
		assertEquals(c2,c.get(1));
		assertEquals(c3,c.get(2));
		
		/*doReturn(c1).when(listRep).get(0);
		doReturn(c2).when(listRep).get(1);
		doReturn(c3).when(listRep).get(2);*/
		
		
		e.eliminar(c1);
		
		verify(e).eliminar(c1);
		
		when(e.obtenerListaReproduccion()).thenReturn(Arrays.asList(c2,c3));
		
		c = e.obtenerListaReproduccion();
		
		assertEquals(c2,c.get(0));
		assertEquals(c3,c.get(1));
		
		//listRep = spy(e.obtenerListaReproduccion());
		
		/*doReturn(c2).when(listRep).get(0);
		doReturn(c3).when(listRep).get(1);*/
		
		
	}
}
