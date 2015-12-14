package es.udc.vvs.va.test.model.contenido;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

import org.junit.Test;

import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

public class CancionTest {
	
	public class DuracionGenerator implements Generator<Integer> {
		
		private final Generator<Integer> duracion = 
				PrimitiveGenerators.positiveIntegers();

		@Override
		public final Integer next() {
			return new Integer(duracion.next());
		}
	}
	
	@Test
	public final void testObtenerDuracionQC() throws ContentManagerException {
		final DuracionGenerator genDurac = new DuracionGenerator();
		
		Integer duracion = genDurac.next();
		String nombreCancion = duracion.toString();
		Contenido c = new Cancion(nombreCancion, duracion);

		assertTrue(Integer.compare(c.obtenerDuracion(),
				duracion) == 0);
	}
	
	@Test
	public void testObtenerDuracion() 
			throws ContentManagerException {
		
		Contenido c = new Cancion ("c1",5);
		
		assertEquals(c, c.obtenerListaReproduccion().get(0));
		
	}

	@Test
	public void testBuscarCancion() 
			throws ContentManagerException {
		
		Contenido c = new Cancion ("c1",5);
		
		 List<Contenido> result = c.buscar("c");
		 
		assertEquals(c,result.get(0));
		assertEquals(1,result.size());
	}
	
	@Test
	public void testBuscarCancionSinExito() 
			throws ContentManagerException {
		
		Contenido c = new Cancion ("c1",5);
		
		List<Contenido> result= c.buscar("b");

		assertEquals(0,result.size());
	}
	
	@Test
	public void testAgregar() 
			throws ContentManagerException {
		
		Contenido c1 = new Cancion ("c1",5);
		Contenido c2 = new Cancion ("c2",3);
		
		c1.agregar(c2, c1);
		
		List<Contenido> result = c1.obtenerListaReproduccion();
		assertEquals(c1,result.get(0));
		assertEquals(1,result.size());
		
	}
	
	
	@Test
	public void testEliminar() 
			throws ContentManagerException {
		
		Contenido c = new Cancion ("c1",5);
		
		c.eliminar(c);
		
		assertEquals(c, c.obtenerListaReproduccion().get(0));
	}
	

}
