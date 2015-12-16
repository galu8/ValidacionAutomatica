package es.udc.vvs.va.test.model.contenido;

import static net.java.quickcheck.generator.CombinedGenerators.lists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.iterable.Iterables;

import org.junit.Test;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class EmisoraTest {

	private final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	class CancionListGenerator implements Generator<List<Cancion>> {
		Generator<List<Integer>> lGen = lists(PrimitiveGenerators
				.positiveIntegers());

		@Override
		public List<Cancion> next() {
			List<Integer> l = lGen.next();

			List<Cancion> lC = new ArrayList<Cancion>();

			for (Integer dur : l) {
				try {
					lC.add(new Cancion(dur.toString(), dur));
				} catch (ContentManagerException e) {
					e.printStackTrace();
				}
			}

			return lC;
		}
	}

	class ContenidoListGenerator implements Generator<List<Contenido>> {
		Generator<List<Integer>> lGen = lists(PrimitiveGenerators
				.positiveIntegers());

		@Override
		public List<Contenido> next() {
			List<Integer> l = lGen.next();
			List<Contenido> lC = new ArrayList<Contenido>();
			boolean rand;

			for (Integer dur : l) {
				try {
					rand = new Random().nextBoolean();
					if (rand) {
						lC.add(new Cancion(dur.toString(), dur));
					} else {
						Emisora e = new Emisora(dur.toString());
						e.agregar(new Cancion(dur.toString(), dur), null);
					}
				} catch (ContentManagerException e) {
					e.printStackTrace();
				}
			}

			return lC;
		}
	}

	@Test
	public void testDuracionEmisoraCancionesGeneradas()
			throws ContentManagerException {

		int sumaDuraciones;

		for (List<Cancion> lC : Iterables
				.toIterable(new CancionListGenerator())) {

			Emisora e = new Emisora(lC.toString());

			sumaDuraciones = 0;

			for (Cancion cancion : lC) {
				e.agregar(cancion, null);
				sumaDuraciones += cancion.obtenerDuracion();
			}

			assertEquals(sumaDuraciones, e.obtenerDuracion());
		}
	}

	public void duracionEmisoraConCanciones() throws ContentManagerException {

		List<Emisora> emisoras = new ArrayList<Emisora>();

		for (int i = 0; i < 10; i++) {
			for (List<Cancion> lC : Iterables
					.toIterable(new CancionListGenerator())) {

				Emisora e = new Emisora(lC.toString());

				for (Cancion cancion : lC) {
					e.agregar(cancion, null);
				}
				emisoras.add(e);
			}
		}

		for (Emisora e : emisoras) {
			EtmPoint point = etmMonitor
					.createPoint("Emisora:obtenerDuracionCanciones");
			e.obtenerDuracion();
			point.collect();
		}
	}
	
	public void buscarEnEmisoraConCanciones() throws ContentManagerException {

		List<Emisora> emisoras = new ArrayList<Emisora>();

		for (int i = 0; i < 10; i++) {
			for (List<Cancion> lC : Iterables
					.toIterable(new CancionListGenerator())) {

				Emisora e = new Emisora(lC.toString());

				for (Cancion cancion : lC) {
					e.agregar(cancion, null);
				}
				emisoras.add(e);
			}
		}

		for (Emisora e : emisoras) {
			EtmPoint point = etmMonitor
					.createPoint("Emisora:buscarCanciones");
			e.buscar("1");
			point.collect();
		}
	}

	public void duracionEmisoraConContenidos() throws ContentManagerException {

		List<Emisora> emisoras = new ArrayList<Emisora>();
		
		for (int i = 0; i < 10; i++) {
			for (List<Contenido> lC : Iterables
					.toIterable(new ContenidoListGenerator())) {

				Emisora e = new Emisora(lC.toString());

				for (Contenido contenido : lC) {
					e.agregar(contenido, null);
				}

				emisoras.add(e);
			}
		}
		
		for (Emisora e : emisoras) {
			EtmPoint point = etmMonitor
					.createPoint("Emisora:obtenerDuracionContenidos");
			e.obtenerDuracion();
			point.collect();
		}
	}
	
	public void buscarEnEmisoraConContenidos() throws ContentManagerException {

		List<Emisora> emisoras = new ArrayList<Emisora>();
		
		for (int i = 0; i < 10; i++) {
			for (List<Contenido> lC : Iterables
					.toIterable(new ContenidoListGenerator())) {

				Emisora e = new Emisora(lC.toString());

				for (Contenido contenido : lC) {
					e.agregar(contenido, null);
				}

				emisoras.add(e);
			}
		}
		
		for (Emisora e : emisoras) {
			EtmPoint point = etmMonitor
					.createPoint("Emisora:buscarContenidos");
			e.buscar("1");
			point.collect();
		}
	}

	@Test
	public void testDuracionEmisoraContenidosGenerados()
			throws ContentManagerException {

		int sumaDuraciones;

		for (List<Contenido> lC : Iterables
				.toIterable(new ContenidoListGenerator())) {

			Emisora e = new Emisora(lC.toString());
			sumaDuraciones = 0;

			for (Contenido contenido : lC) {
				e.agregar(contenido, null);
				sumaDuraciones += contenido.obtenerDuracion();
			}

			assertEquals(sumaDuraciones, e.obtenerDuracion());
		}
	}

	private Emisora crearEmisoraConContenido(String titulo) {
		try {
			Contenido c1 = new Cancion("c1", 210);
			Contenido c2 = new Cancion("c2", 300);
			Contenido c3 = new Cancion("c3", 240);
			Contenido a = new Anuncio();

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
	public void testCrearCancionInvalida() throws ContentManagerException {

		new Cancion("c1", -1);
	}

	@Test
	public void testObtenerDuracion() {

		Contenido e1 = crearEmisoraConContenido("e1");
		// La emisora con contenido tiene en total 755 segundos de duracion
		assertEquals(755, e1.obtenerDuracion());

	}

	@Test
	public void testObtenerDuracionEmisoraConEmisora()
			throws ContentManagerException {

		// Obtener duracion de una emisora con otra emisora dentro
		Contenido e1 = crearEmisoraConContenido("e1");
		Contenido e2 = crearEmisoraConContenido("e2");
		e1.agregar(e2, null); // emisora2 en emisora1
		assertEquals(1510, e1.obtenerDuracion());

		// Obtener duracion de e1 una vez modificado el contenido de e2
		Contenido anuncio = new Anuncio(); // nuevo anuncio (duracion = 5)
		e2.agregar(anuncio, null); // anuncio en emisora2 (y en emisora1)
		assertEquals(1515, e1.obtenerDuracion()); // se esperaba 5 pero fue 0

	}

	@Test
	public void testObtenerListaReproduccion() throws ContentManagerException {

		Contenido e1 = new Emisora("e1");
		Contenido c1 = new Cancion("c1", 300);
		Contenido c2 = new Cancion("c2", 250);
		e1.agregar(c1, null);
		e1.agregar(c2, c1);

		List<Contenido> c = e1.obtenerListaReproduccion();
		assertEquals(2, c.size());
		assertEquals(c1, c.get(0));
		assertEquals(c2, c.get(1));

		e1.eliminar(c1);
		c = e1.obtenerListaReproduccion();
		assertFalse(c.contains(c1));

	}

	@Test
	public void testBuscar() throws ContentManagerException {
		Contenido e1 = new Emisora("e1");
		Contenido c1 = new Cancion("c1", 250);
		e1.agregar(c1, null);

		List<Contenido> c = e1.buscar("c1");
		assertTrue(c.size() > 0); // ha encontrado el elemento al menos
		assertEquals(c1, e1.obtenerListaReproduccion().get(0));

		c = e1.buscar("Wally");
		assertTrue(c.isEmpty());
	}

	/*
	 * Test que prueba el funcionamiento de la busqueda recursiva en emisoras
	 * dentro de emisoras.
	 */
	@Test
	public void testBuscarRecursivo() throws ContentManagerException {

		Contenido e1 = new Emisora("e1");
		Contenido e2 = new Emisora("e2");
		Contenido c1 = new Cancion("c1", 250);
		e1.agregar(e2, null);
		e2.agregar(c1, null);

		List<Contenido> cont = e1.buscar("c1");
		assertTrue(cont.size() > 0); // ha encontrado el elemento al menos
		assertEquals(c1, cont.get(0));

		// Emisora e4 con subcadena de busqueda en su titulo y en su contenido
		// buscar debe devolver la emisora y la cancion
		Contenido e3 = new Emisora("e3");
		Contenido e4 = new Emisora("e4c1");
		e3.agregar(e4, null);
		e4.agregar(c1, null);
		List<Contenido> cont2 = e3.buscar("c1");
		assertTrue(cont2.size() == 2);
		assertEquals(e4, cont2.get(0));
		assertEquals(c1, cont2.get(1));
	}

	@Test(expected = ContentManagerException.class)
	public void testAgregarEmisorasEntreSi() throws ContentManagerException {

		Contenido e1 = crearEmisoraConContenido("e1");
		Contenido e2 = crearEmisoraConContenido("e2");

		e1.agregar(e2, null);
		e2.agregar(e1, e2);

	}

	@Test(expected = ContentManagerException.class)
	public void testAgregarEmisoraEnSiMisma() throws ContentManagerException {

		Contenido e1 = crearEmisoraConContenido("e1");

		e1.agregar(e1, null);

	}

	@Test
	public void testAgregarEmisorasEnOtra() throws ContentManagerException {

		Contenido e1 = crearEmisoraConContenido("e1");
		Contenido e2 = crearEmisoraConContenido("e2");
		Contenido e3 = crearEmisoraConContenido("e3");
		Contenido e4 = new Emisora("e4");

		assertEquals(4, e2.obtenerListaReproduccion().size());

		e1.agregar(e4, null);
		assertEquals(5, e1.obtenerListaReproduccion().size());

		e2.agregar(e3, null);
		e2.agregar(e1, null);
		int ultPos = e2.obtenerListaReproduccion().size() - 1;

		assertEquals(e1, e2.obtenerListaReproduccion().get(ultPos));
		assertEquals(6, e2.obtenerListaReproduccion().size());

	}

	@Test
	public void testEliminar() {
		Contenido e1 = crearEmisoraConContenido("e1");
		// devuelve una cancion
		List<Contenido> busqueda = e1.buscar("c1");
		// La eliminamos
		e1.eliminar(busqueda.get(0));
		List<Contenido> busqueda2 = e1.buscar("c1");

		assertEquals(busqueda.size() - 1, busqueda2.size());
		assertFalse(busqueda2.contains(busqueda.get(0)));
	}

	@Test
	public void testEqualsContenido() {
		Emisora e1 = new Emisora("e1");
		Emisora e2 = new Emisora("e1");
		assertEquals(e1, e2);
		int e1Code = e1.hashCode();
		assertEquals(e1Code, e2.hashCode());
		assertEquals(e1Code, e1.hashCode());

		// titulo null
		e1 = new Emisora(null);
		// emisora con titulo null y other con titulo distinto a null
		assertFalse(e1.equals(e2));
		// ambas emisoras con título null
		e2 = new Emisora(null);
		assertEquals(e1, e2);
		e1Code = e1.hashCode();
		assertEquals(e1Code, e2.hashCode());
		assertEquals(e1Code, e1.hashCode());

		// comparar con objeto nulo
		assertFalse(e2.equals(null));
	}

}
