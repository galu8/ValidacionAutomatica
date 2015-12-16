package es.udc.vvs.va.test.model.servidor;

import static net.java.quickcheck.generator.CombinedGenerators.lists;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.iterable.Iterables;

import org.junit.Before;
import org.junit.Test;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.Servidor;
import es.udc.vvs.va.model.servidor.ServidorPlano;
import es.udc.vvs.va.model.servidor.ServidorTransitivo;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class ServidorTransitivoTest {
	
	private final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();
	
	private String token;
	private ServidorTransitivo servidorTransitivo;
	private ServidorTransitivo servidorLocal;
	private ServidorPlano servidorPlano;
	private String tokenMagicoPlano;
	
	private void agregarContenidoSP() throws ContentManagerException {
		tokenMagicoPlano = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("Read my mind", 5);
		Contenido contenido2 = new Anuncio();
		Contenido contenido3 = new Cancion("Sunday", 4);
		
		// Agregar contenido al servidor
		servidorPlano.agregar(contenido, tokenMagicoPlano);
		servidorPlano.agregar(contenido2, tokenMagicoPlano);
		servidorPlano.agregar(contenido3, tokenMagicoPlano);
	}

	/*
	 * Se hace una cadena con 1 servidor de respaldo
	 */
	@Before
	public void setUp() throws Exception {
		servidorPlano = new ServidorPlano("Servidor plano test");
		token = servidorPlano.alta();
		agregarContenidoSP();
		
		servidorTransitivo = new ServidorTransitivo("Servidor transitivo test",
				token, servidorPlano);
	}
	
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
	
	public void buscarEnServidorConCanciones() throws ContentManagerException {
		List<ServidorTransitivo> servidores = 
				new ArrayList<ServidorTransitivo>();
		Boolean rand;
		String token;

		for (int i = 0; i < 10; i++) {
			for (List<Cancion> lC : Iterables
					.toIterable(new CancionListGenerator())) {

				ServidorPlano sp = new ServidorPlano(lC.toString() + "sp");
				ServidorTransitivo st = 
						new ServidorTransitivo(lC.toString(), sp);

				for (Cancion cancion : lC) {
					rand = new Random().nextBoolean();
					if (rand) {
						sp.agregar(cancion, sp.getTokenMagico());
					} else {
						st.agregar(cancion, st.getTokenMagico());
					}
				}
				servidores.add(st);
			}
		}

		for (ServidorTransitivo st : servidores) {
			token = st.alta();
			EtmPoint point = etmMonitor
					.createPoint("ServidorTransitivo:buscarCanciones");
			st.buscar("123", token);
			point.collect();
		}
	}
	
	public void buscarEnServidorConContenidos() 
			throws ContentManagerException {
		List<ServidorTransitivo> servidores = 
				new ArrayList<ServidorTransitivo>();
		Boolean rand;
		String token;

		for (int i = 0; i < 10; i++) {
			for (List<Contenido> lC : Iterables
					.toIterable(new ContenidoListGenerator())) {

				ServidorPlano sp = new ServidorPlano(lC.toString() + "sp");
				ServidorTransitivo st = 
						new ServidorTransitivo(lC.toString(), sp);

				for (Contenido contenido : lC) {
					rand = new Random().nextBoolean();
					if (rand) {
						sp.agregar(contenido, sp.getTokenMagico());
					} else {
						st.agregar(contenido, st.getTokenMagico());
					}
				}
				servidores.add(st);
			}
		}

		for (ServidorTransitivo st : servidores) {
			token = st.alta();
			EtmPoint point = etmMonitor
					.createPoint("ServidorTransitivo:buscarContenidos");
			st.buscar("123", token);
			point.collect();
		}
	}

	/*
	 * Prueba la busqueda en uno o varios servidores de respaldo
	 */
	@Test
	public void testBuscar() throws ContentManagerException {
		String tokenMagicoTransitivo = servidorTransitivo.getTokenMagico();

		// Busca en el Servidor Plano
		Collection<Contenido> contenidosEncontrados = 
				servidorPlano.buscar("Read my mind", token);
		assertEquals(1, contenidosEncontrados.size());
		
		// Busca en el servidor Transitivo, no lo encuentra y busca en el plano
		contenidosEncontrados = servidorTransitivo.buscar("Read my mind", 
				token);
		assertEquals(1, contenidosEncontrados.size());
		
		Contenido contenido = new Cancion("Sunday", 5);
		servidorTransitivo.agregar(contenido, tokenMagicoTransitivo);
		
		// Busca en el servidor transitivo y lo encuentra
		contenidosEncontrados = servidorTransitivo.buscar("Sunday", token);
		assertEquals(1, contenidosEncontrados.size());
		
		// Añadimos un nuevo servidor de respaldo
		servidorLocal = new ServidorTransitivo("Servidor local test", token, 
				servidorTransitivo);
		
		// Busca en el servidor Local, no lo encuentra
		// busca en el transitivo, no lo encuentra
		// busca en el plano y lo encuentra
		contenidosEncontrados = servidorLocal.buscar("Read my mind", token);
		assertEquals(1, contenidosEncontrados.size());
		
		// Busca en el servidor local y no lo encuentra,
		// busca en el transitivo y lo encuentra
		contenidosEncontrados = servidorLocal.buscar("Sunday", token);
		assertEquals(1, contenidosEncontrados.size());
	}
	
	/*
	 * Prueba la busqueda transitiva en una unica relacion
	 */
	@Test
	public void testBusquedaTransitiva() throws ContentManagerException {
		ServidorPlano servidorPlano = new ServidorPlano("Servidor plano test");
		String tokenMagicoPlano = servidorPlano.getTokenMagico();
		servidorPlano.agregar(new Cancion("Read my mind", 5), 
				tokenMagicoPlano);
		
		ServidorTransitivo servidorTransitivo =
				new ServidorTransitivo(
						"Servidor transitivo test", servidorPlano);
		String tokenTransitivo = servidorTransitivo.alta();
		
		// Busca en el Servidor Plano a traves del Servidor Transitivo
		// es.udc.vvs.va.model.exceptions.ContentManagerException:
		// Token invalido.
		// Los tokens "no se comparten".
		List<Contenido> contenidosEncontrados
			= (List<Contenido>)
			servidorTransitivo.buscar("Read", tokenTransitivo);
		Contenido expected = contenidosEncontrados.get(0);
		Contenido actual = new Cancion("Read my mind", 5);
		assertEquals(expected, actual);
	}
	
	@Test(expected = ContentManagerException.class)
	public void testDarDeBajaTokenInexistente() 
			throws ContentManagerException {
		servidorTransitivo.baja("asdf");		
	}
	
	@Test(expected = ContentManagerException.class)
	public void testAgregarSinServidorRelacionado() 
			throws ContentManagerException {
		
		new ServidorTransitivo("Servidor transitivo test", token, null);
	
	}
	
	@Test(expected = ContentManagerException.class)
	public void tesBajaServidorRelacionado() 
			throws ContentManagerException {
		
		Servidor sp = new ServidorPlano("Servidor plano test");
		Servidor sr =
				new ServidorTransitivo(
						"Servidor transitivo test", token, sp);
		
		// sr y sp tienen el siguiente token compartido
		String tokenCompartido = sr.alta();
		sr.baja(tokenCompartido);
		
		// tokenCompartido también se borra en sp al borrarlo en sr
		sp.buscar("String aleatorio", tokenCompartido);
		
	}

}
