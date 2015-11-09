package es.udc.vvs.va.test.model;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.ServidorPlano;
import es.udc.vvs.va.util.ModelConstants;

public class ServidorPlanoTest {
	
	ServidorPlano servidorPlano;
	String token;
	String tokenVacio="";

	@Before
	public void setUp() throws Exception {
		// Como solo indicamos el nombre, genera un token magico aleatorio
		servidorPlano = new ServidorPlano("Servidor plano test");
		token = servidorPlano.alta();
	}


	@Test
	public void testObtenerNombre() {		
		String nombreServidorEsperado = "Servidor plano test";
		String nombreServidor = servidorPlano.obtenerNombre();
		
		assertEquals(nombreServidorEsperado, nombreServidor);
	}

	/*
	 * Prueba el metodo agregar y buscar
	 */
	@Test
	public void testAgregarBuscar() throws ContentManagerException {
		String tokenMagico = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("Read my mind", 5);
		Contenido contenido2 = new Anuncio();
		Contenido contenido3 = new Cancion("Sunday", 4);
		
		
		// Agregar contenido al servidor
		servidorPlano.agregar(contenido, tokenMagico);
		servidorPlano.agregar(contenido2, tokenMagico);
		servidorPlano.agregar(contenido3, tokenMagico);

		Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("Read my mind", token);
		assertEquals(contenidosEncontrados.size(), 1);		
		
		// Añadimos otro contenido con un nombre parecido y que debe encontrar
		Contenido contenido4 = new Cancion("Read my mind the killers", 4);
		servidorPlano.agregar(contenido4, tokenMagico);
		
		contenidosEncontrados = servidorPlano.buscar("Read my mind", token);
		assertEquals(contenidosEncontrados.size(), 2);	
	}
	
	/*
	 * Prueba el metodo buscar siendo el token vacio: 
	 * La lista de contenidos empieza con y tiene un anuncio cada 3 contenidos
	 */
	@Test
	public void testAgregarBuscarConTokenVacio() throws ContentManagerException {
		String tokenMagico = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("Read my mind", 5);
		
		// Agregamos 12 contenidos iguales
		for (int i=0; i < 12; i++){
			servidorPlano.agregar(contenido, tokenMagico);
		}
		Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("Read my mind", tokenVacio);
		// Comprobamos que se han creado 12 canciones + 4 anuncios (1 + 1 cada 3)
		assertEquals(contenidosEncontrados.size(), 16);

		long posicion = 0;
		for(Contenido c: contenidosEncontrados) {
			// Comprueba de que tipo es cada contenido
			if (posicion == 0) {
				assertEquals(c.obtenerTitulo(), "PUBLICIDAD");
			} else if (posicion % (ModelConstants.SV_POSANUNCIOCONTENIDOS +1) == 0) {
				assertEquals(c.obtenerTitulo(), "PUBLICIDAD");
			} else {
				assertEquals(c.obtenerTitulo(), "Read my mind");
			}
			posicion++;
		}
	}
	
	/*
	 * Prueba el metodo buscar siendo el token caducado: 
	 * El token caduca tras devolver 10 contenidos
	 */
	@Test
	public void testAgregarBuscarConTokenCaducado() throws ContentManagerException {
		String tokenMagico = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("Read my mind", 5);
		
		// Agregamos 16 contenidos
		for (int i=0; i < 16; i++){
			servidorPlano.agregar(contenido, tokenMagico);
		}
		Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("Read my mind", token);
		// Deberia haber más de 10 elementos si el token no hubiese caducado
		assertEquals(contenidosEncontrados.size(), 10);

		for(Contenido c: contenidosEncontrados) {
			assertEquals(c.obtenerTitulo(), "Read my mind");
		}
	}
	
	@Test(expected = ContentManagerException.class)
	public void testAgregarConTokenComun() throws ContentManagerException {
		
		Contenido contenido = new Cancion("Read my mind", 5);
		
		servidorPlano.agregar(contenido, token);		
	}

	@Test
	public void testEliminar() throws ContentManagerException {
		String tokenMagico = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("c1", 5);
		Contenido contenido2 = new Anuncio();
		Contenido contenido3 = new Cancion("c2", 4);
		
		// Agregar contenido al servidor
		servidorPlano.agregar(contenido, tokenMagico);
		servidorPlano.agregar(contenido2, tokenMagico);
		servidorPlano.agregar(contenido3, tokenMagico);

		Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("c1", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Se elimina otro contenido
		servidorPlano.eliminar(contenido2, tokenMagico);
		contenidosEncontrados = servidorPlano.buscar("c2", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Se elimina el contenido que se busca
		servidorPlano.eliminar(contenido, tokenMagico);
		contenidosEncontrados = servidorPlano.buscar("c1", token);
		assertEquals(contenidosEncontrados.size(), 0);
	}
	
	@Test(expected = ContentManagerException.class)
	public void testEliminarConTokenComun() throws ContentManagerException {
		String tokenMagico = servidorPlano.getTokenMagico();
		
		// Crear contenido
		Contenido contenido = new Cancion("c1", 5);
		Contenido contenido2 = new Anuncio();
		Contenido contenido3 = new Cancion("c2", 4);
		
		// Agregar contenido al servidor
		servidorPlano.agregar(contenido, tokenMagico);
		servidorPlano.agregar(contenido2, tokenMagico);
		servidorPlano.agregar(contenido3, tokenMagico);

		Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("c1", token);
		assertEquals(contenidosEncontrados.size(), 1);
		
		// Se intenta eliminar con un token que no es "especial"
		servidorPlano.eliminar(contenido2, token);
	}
}
