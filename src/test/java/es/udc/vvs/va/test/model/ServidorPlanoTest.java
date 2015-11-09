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

	@Test
	public void testAlta() {
		assert true;
	}

	@Test
	public void testBaja() {
		assert true;
	}

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
	}
	
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
		assertEquals(contenidosEncontrados.size(), 16);

		long posicion = 0;
		for(Contenido c: contenidosEncontrados) {
			
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
