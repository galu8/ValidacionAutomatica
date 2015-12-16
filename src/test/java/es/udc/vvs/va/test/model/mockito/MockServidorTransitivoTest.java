package es.udc.vvs.va.test.model.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.ServidorPlano;
import es.udc.vvs.va.model.servidor.ServidorTransitivo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MockServidorTransitivoTest {

  private Contenido c1, c2, c3, a, e;
  private ServidorTransitivo servidorTransitivo;
  private ServidorPlano servidorRespaldo;
  private String token;

  // private String tokenVacio="";

  @Before
  public void setUp() throws Exception {

    // Creamos los mocks de las clases que usa ServidorPlano
    c1 = mock(Cancion.class);
    c2 = mock(Cancion.class);
    c3 = mock(Cancion.class);
    a = mock(Anuncio.class);
    e = mock(Emisora.class);

    
    
    // Asignamos valores de retorno para los metodos de las clases mockeadas
    when(c1.obtenerTitulo()).thenReturn("Cancion 1");
    when(c2.obtenerTitulo()).thenReturn("Cancion 2");
    when(c3.obtenerTitulo()).thenReturn("Nueva");
    when(a.obtenerTitulo()).thenReturn("PUBLICIDAD");
    when(e.obtenerTitulo()).thenReturn("Emisora 1");
    when(e.obtenerListaReproduccion()).thenReturn(Arrays.asList(c1, c2, c3));
    when(e.buscar("c1")).thenReturn(Arrays.asList(c1));
    when(e.buscar("c")).thenReturn(Arrays.asList(c1, c2, c3));
    when(e.buscar("nada")).thenReturn(new ArrayList<Contenido>());

    // Inicializamos el servidor con un token
    // Como solo indicamos el nombre, genera un token magico aleatorio
    servidorRespaldo = new ServidorPlano("Servidor RESPALDO");
    
    servidorTransitivo = new ServidorTransitivo("Servidor plano test",servidorRespaldo);
    token = servidorTransitivo.alta();
    
    

  }

  @Test
  public void testBuscar() throws ContentManagerException {

    String tokenMagico = servidorTransitivo.getTokenMagico();
    String tokenMagico2 = servidorRespaldo.getTokenMagico();

    // Agregamos contenido al servidor
    servidorTransitivo.agregar(c1, tokenMagico);
    servidorTransitivo.agregar(a, tokenMagico);
    servidorTransitivo.agregar(c2, tokenMagico);
    
    servidorRespaldo.agregar(c3, tokenMagico2);
    
    Collection<Contenido>  contenidosEncontrados = servidorTransitivo.buscar(
        "nueva", token);
    
    contenidosEncontrados = servidorTransitivo.buscar(
        "cancion 1", token);
    
    assertEquals(1, contenidosEncontrados.size());
    assertTrue(contenidosEncontrados.contains(c1));

    assertFalse(contenidosEncontrados.contains(a));

    //Caso en el que busca en el servidor de respaldo
    contenidosEncontrados = servidorTransitivo.buscar(
        "nueva", token);
    
    assertEquals(1, contenidosEncontrados.size());
    assertTrue(contenidosEncontrados.contains(c3));
    
    //Caso en el que no encuentra nada
    contenidosEncontrados = servidorTransitivo.buscar(
        "inexistente", token);
    
    assertEquals(0, contenidosEncontrados.size());
    assertTrue(contenidosEncontrados.isEmpty());

  }



}
