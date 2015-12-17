package es.udc.vvs.va.test.model.mockito;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



@RunWith(MockitoJUnitRunner.class)
public class MockEmisoraTest {
  /** The e. */
  private Contenido c1, c2, c3, a, e;

  /**
   * Sets the up.
   *
   * @throws Exception
   *           the exception
   */
  @Before
  public void setUp() throws Exception {

    c1 = mock(Cancion.class);
    c2 = mock(Cancion.class);
    c3 = mock(Cancion.class);
    a = mock(Anuncio.class);

    when(c1.obtenerDuracion()).thenReturn(6);
    when(c2.obtenerDuracion()).thenReturn(4);
    when(c3.obtenerDuracion()).thenReturn(5);
    when(a.obtenerDuracion()).thenReturn(5);

    when(c1.obtenerTitulo()).thenReturn("Cancion 1");
    when(c2.obtenerTitulo()).thenReturn("Cancion 2");
    when(c3.obtenerTitulo()).thenReturn("Cancion 3");
    when(a.obtenerTitulo()).thenReturn("PUBLICIDAD");

    e = new Emisora("Emisora 1");
    e.agregar(c1, null);
    e.agregar(c2, c1);
    e.agregar(c3, c2);
    e.agregar(a, c3);

  }

  /**
   * Obtener titulo test.
   * 
   * @throws ContentManagerException
   */
  @Test
  public void buscarTest() throws ContentManagerException {

    // busqueda normal de un parametro
    // Comprobamos que la busqueda no es case sensitive
    assertEquals(3, e.buscar("cancion").size());

    e.eliminar(c2);

    // buscamos despues de eliminar 1 elemento
    assertEquals(2, e.buscar("cancion").size());

    // Buscamos una cancion inexistente

    assertTrue(e.buscar("inexsistente").isEmpty());

  }

  /**
   * Obtener duracion test.
   *
   * @throws ContentManagerException
   *           the content manager exception
   */
  @Test
  public void obtenerDuracionTest() throws ContentManagerException {

    assertEquals(20, e.obtenerDuracion());

    // Para una emisora vacia
    Emisora e2 = new Emisora("emisora Vacia");

    assertEquals(0, e2.obtenerDuracion());

  }

}
