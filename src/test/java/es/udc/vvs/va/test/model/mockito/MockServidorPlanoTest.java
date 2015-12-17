package es.udc.vvs.va.test.model.mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.model.servidor.ServidorPlano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MockServidorPlanoTest {

  private Contenido c1, c2, c3, a, e;
  private ServidorPlano servidorPlano;
  private String token;

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
    when(c3.obtenerTitulo()).thenReturn("Cancion 3");
    when(a.obtenerTitulo()).thenReturn("PUBLICIDAD");
    when(e.obtenerTitulo()).thenReturn("Emisora 1");
    when(e.obtenerListaReproduccion()).thenReturn(Arrays.asList(c1, c2, c3));
    when(e.buscar("c1")).thenReturn(Arrays.asList(c1));
    when(e.buscar("c")).thenReturn(Arrays.asList(c1, c2, c3));
    when(e.buscar("nada")).thenReturn(new ArrayList<Contenido>());

    // Inicializamos el servidor con un token
    // Como solo indicamos el nombre, genera un token magico aleatorio
    servidorPlano = new ServidorPlano("Servidor plano test");
    token = servidorPlano.alta();

  }

  @Test
  public void testBuscar() throws ContentManagerException {

    String tokenMagico = servidorPlano.getTokenMagico();

    // Agregamos contenido al servidor
    servidorPlano.agregar(c1, tokenMagico);
    servidorPlano.agregar(a, tokenMagico);
    servidorPlano.agregar(c2, tokenMagico);

    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar(
        "Cancion 1", token);
    assertEquals(1, contenidosEncontrados.size());
    assertTrue(contenidosEncontrados.contains(c1));

    // Probamos que no es case sensitive
    contenidosEncontrados = servidorPlano.buscar("cancion 1", token);
    assertEquals(1, contenidosEncontrados.size());

    assertTrue(contenidosEncontrados.contains(c1));

    assertFalse(contenidosEncontrados.contains(a));

    servidorPlano.agregar(c3, tokenMagico);

    contenidosEncontrados = servidorPlano.buscar("cancion", token);

    assertEquals(3, contenidosEncontrados.size());

    assertTrue(contenidosEncontrados.contains(c1));
    assertTrue(contenidosEncontrados.contains(c2));
    assertTrue(contenidosEncontrados.contains(c3));

    assertFalse(contenidosEncontrados.contains(a));

  }

}
