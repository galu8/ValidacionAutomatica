package es.udc.vvs.va.test.model.contenido;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

public class EmisoraTest {

	@Before
	public void setUp() throws Exception {
	}

	private Emisora crearEmisoraConContenido(String titulo) {
		try {
			Cancion c1 = new Cancion("c1",210);
			Cancion c2 = new Cancion("c2",300);
			Cancion c3 = new Cancion("c3",240);
			Anuncio a = new Anuncio();
			
			Emisora e = new Emisora(titulo);
			e.agregar(c1, e);
			e.agregar(c2, e);
			e.agregar(c3, e);
			e.agregar(a, e);
			return e;
			
		} catch (ContentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}
	
	
	@Test(expected = ContentManagerException.class)
	public void testCrearCancionInvalida() 
			throws ContentManagerException  {
		
			new Cancion("c1",-1);
	}
	
	@Test
	public void testObtenerDuracion() {
		
		Emisora e1 =  crearEmisoraConContenido("e1");
		//La emisora con contenido tiene en total 755 segundos de duracion
		assertEquals(e1.obtenerDuracion(),755);		

	}

	@Test
	public void testObtenerListaReproduccion() 
			throws ContentManagerException {

		Emisora e1 = new Emisora("e1");
		Cancion c1 = new Cancion ("c1",300);
		Cancion c2 = new Cancion ("c2",250);
		e1.agregar(c1, null);
		e1.agregar(c2,c1);
		
		List<Contenido> c = e1.obtenerListaReproduccion();
		assertEquals(c.size(),2);
		assertEquals(c.get(0),c1);
		assertEquals(c.get(1),c2);
		
		e1.eliminar(c1);
		c = e1.obtenerListaReproduccion();
		assertFalse(c.contains(c1));
		
	}

	@Test
	public void testBuscar() throws ContentManagerException {
		Emisora e1 = new Emisora("e1");
		Cancion c1 = new Cancion("c1",250);
		e1.agregar(c1,null);
		
		List<Contenido> c = e1.buscar("c1");
		assertTrue(c.size()>0); //ha encontrado el elemento al menos
		assertEquals(e1.obtenerListaReproduccion().get(0), c1);
		
		c= e1.buscar("Wally");
		assertTrue(c.isEmpty());
	}

	@Test(expected = ContentManagerException.class)
	public void testAgregarEmisorasEntreSi() 
			throws ContentManagerException {
		
		Emisora e1 = crearEmisoraConContenido("e1");
		Emisora e2 = crearEmisoraConContenido("e2");

		e1.agregar(e2, null);
		e2.agregar(e1, e2);
		
	}

	@Test
	public void testEliminar() {
		Emisora e1 = crearEmisoraConContenido("e1");
		//devuelve una cancion
		List<Contenido> busqueda = e1.buscar("c1");
		//La eliminamos
		e1.eliminar(busqueda.get(0));
		List<Contenido> busqueda2 = e1.buscar("c1");
		
		assertEquals (busqueda2.size(),busqueda.size()-1);
		assertFalse(busqueda2.contains(busqueda.get(0)));
	}

}
