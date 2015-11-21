package es.udc.vvs.va.test.model.servidor;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.Servidor;
import es.udc.vvs.va.model.servidor.ServidorPlano;
import es.udc.vvs.va.model.servidor.ServidorTransitivo;

public class ServidorTransitivoTest {
	String token;
	ServidorTransitivo servidorTransitivo;
	ServidorTransitivo servidorLocal;
	ServidorPlano servidorPlano;
	String tokenMagicoPlano;
	
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

	/*
	 * Prueba la busqueda en uno o varios servidores de respaldo
	 */
	@Test
	public void testBuscar() throws ContentManagerException {
		String tokenMagicoTransitivo = servidorTransitivo.getTokenMagico();

		// Busca en el Servidor Plano
		Collection<Contenido> contenidosEncontrados = 
				servidorPlano.buscar("Read my mind", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Busca en el servidor Transitivo, no lo encuentra y busca en el plano
		contenidosEncontrados = servidorTransitivo.buscar("Read my mind", 
				token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		Contenido contenido = new Cancion("Sunday", 5);
		servidorTransitivo.agregar(contenido, tokenMagicoTransitivo);
		
		// Busca en el servidor transitivo y lo encuentra
		contenidosEncontrados = servidorTransitivo.buscar("Sunday", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Añadimos un nuevo servidor de respaldo
		servidorLocal = new ServidorTransitivo("Servidor local test", token, 
				servidorTransitivo);
		
		// Busca en el servidor Local, no lo encuentra
		// busca en el transitivo, no lo encuentra
		// busca en el plano y lo encuentra
		contenidosEncontrados = servidorLocal.buscar("Read my mind", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Busca en el servidor local y no lo encuentra,
		// busca en el transitivo y lo encuentra
		contenidosEncontrados = servidorLocal.buscar("Sunday", token);
		assertEquals(contenidosEncontrados.size(), 1);
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
		
		Servidor s = new ServidorTransitivo("Servidor transitivo test", token, 
				null);
	
	}

}
