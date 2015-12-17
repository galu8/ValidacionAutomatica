package es.udc.vvs.va.test.model.servidor;

import static net.java.quickcheck.generator.CombinedGenerators.lists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import es.udc.vvs.va.model.servidor.ServidorPlano;
import es.udc.vvs.va.util.ModelConstants;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class ServidorPlanoTest {

  private final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

  private ServidorPlano servidorPlano;
  private String token;
  private String tokenVacio = "";

  @Before
  public void setUp() throws Exception {
    // Como solo indicamos el nombre, genera un token magico aleatorio
    servidorPlano = new ServidorPlano("Servidor plano test");
    token = servidorPlano.alta();
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
    List<ServidorPlano> servidores = new ArrayList<ServidorPlano>();

    for (int i = 0; i < 10; i++) {
      for (List<Cancion> lC : Iterables.toIterable(new CancionListGenerator())) {

        ServidorPlano sp = new ServidorPlano(lC.toString());

        for (Cancion cancion : lC) {
          sp.agregar(cancion, sp.getTokenMagico());
        }
        servidores.add(sp);
      }
    }

    for (ServidorPlano sp : servidores) {
      EtmPoint point = etmMonitor.createPoint("ServidorPlano:buscarCanciones");
      sp.buscar("123", sp.getTokenMagico());
      point.collect();
    }
  }

  public void buscarEnServidorConContenidos() throws ContentManagerException {

    List<ServidorPlano> servidores = new ArrayList<ServidorPlano>();

    for (int i = 0; i < 10; i++) {
      for (List<Contenido> lC : Iterables
          .toIterable(new ContenidoListGenerator())) {

        ServidorPlano sp = new ServidorPlano(lC.toString());

        for (Contenido contenido : lC) {
          sp.agregar(contenido, sp.getTokenMagico());
        }

        servidores.add(sp);
      }
    }

    for (ServidorPlano sp : servidores) {
      EtmPoint point = etmMonitor.createPoint("ServidorPlano:buscarContenidos");
      sp.buscar("123", sp.getTokenMagico());
      point.collect();
    }
  }

  /*
   * Prueba el constructor con token especificado, el metodo obtenerNombre y el
   * metodo getTokenMagico
   */
  @Test
  public void testCrearServidorConToken() {
    String nombreServidorEsperado = "Servidor plano test";
    String tokenExpected = "tokenPrueba";

    ServidorPlano servidorPlanoConToken = new ServidorPlano(
        "Servidor plano test", tokenExpected);

    String nombreServidor = servidorPlano.obtenerNombre();

    assertEquals(nombreServidorEsperado, nombreServidor);
    assertEquals(tokenExpected, servidorPlanoConToken.getTokenMagico());
  }

  /*
   * Prueba el metodo obtenerNombre
   */
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

    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar(
        "Read my mind", token);
    assertEquals(1, contenidosEncontrados.size());
    // Probar que no es case sensitive
    Collection<Contenido> contenidosEncontrados2 = servidorPlano.buscar(
        "read my mind", token);
    assertEquals(1, contenidosEncontrados2.size());

    // Añadimos otro contenido con un nombre parecido y que debe encontrar
    Contenido contenido4 = new Cancion("Read my mind the killers", 4);
    servidorPlano.agregar(contenido4, tokenMagico);

    contenidosEncontrados = servidorPlano.buscar("Read my mind", token);
    assertEquals(2, contenidosEncontrados.size());
  }

  /*
   * Prueba el metodo buscar una vez sobrepasado el limite de contenidos del
   * token. Salta la excepcion de límite sobrepasado.
   */
  @Test(expected = ContentManagerException.class)
  public void testLimiteSobrepasadoBusquedas() throws ContentManagerException {
    String tokenMagico = servidorPlano.getTokenMagico();

    // Crear contenido
    Contenido contenido = new Cancion("Read my mind", 5);

    // Agregar contenido al servidor
    servidorPlano.agregar(contenido, tokenMagico);

    for (int i = 0; i < ModelConstants.SV_MAXCONTENIDOSTOKEN; i++) {
      servidorPlano.buscar("read my mind", token);
    }

    // Excepcion
    servidorPlano.buscar("read my mind", token);
  }

  /*
   * Prueba el metodo buscar siendo el token vacio: La lista de contenidos
   * empieza con y tiene un anuncio cada 3 contenidos
   */
  @Test
  public void testAgregarBuscarConTokenVacio() throws ContentManagerException {
    String tokenMagico = servidorPlano.getTokenMagico();

    // Crear contenido
    Contenido contenido = new Cancion("Read my mind", 5);

    // Agregamos 12 contenidos iguales
    for (int i = 0; i < 12; i++) {
      servidorPlano.agregar(contenido, tokenMagico);
    }
    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar(
        "Read my mind", tokenVacio);
    // Comprobamos que se han creado
    // 12 canciones + 4 anuncios (1 + 1 cada 3)
    assertEquals(16, contenidosEncontrados.size());

    long posicion = 0;
    for (Contenido c : contenidosEncontrados) {
      // Comprueba de que tipo es cada contenido
      if (posicion == 0) {
        assertEquals("PUBLICIDAD", c.obtenerTitulo());
      } else if (posicion % (ModelConstants.SV_POSANUNCIOCONTENIDOS + 1) == 0) {
        assertEquals("PUBLICIDAD", c.obtenerTitulo());
      } else {
        assertEquals("Read my mind", c.obtenerTitulo());
      }
      posicion++;
    }
  }

  /*
   * Prueba el metodo buscar siendo el token caducado: El token caduca tras
   * devolver 10 contenidos
   */
  @Test
  public void testAgregarBuscarConTokenCaducado()
      throws ContentManagerException {
    String tokenMagico = servidorPlano.getTokenMagico();

    // Crear contenido
    Contenido contenido = new Cancion("Read my mind", 5);

    // Agregamos 16 contenidos
    for (int i = 0; i < 16; i++) {
      servidorPlano.agregar(contenido, tokenMagico);
    }
    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar(
        "Read my mind", token);
    // Deberia haber más de 10 elementos si el token no hubiese caducado
    assertEquals(10, contenidosEncontrados.size());

    for (Contenido c : contenidosEncontrados) {
      assertEquals("Read my mind", c.obtenerTitulo());
    }
  }

  /*
   * Prueba el metodo agregar con un token comun (invalido para esto)
   */
  @Test(expected = ContentManagerException.class)
  public void testAgregarConTokenComun() throws ContentManagerException {

    Contenido contenido = new Cancion("Read my mind", 5);

    servidorPlano.agregar(contenido, token);
  }

  /*
   * Prueba el metodo eliminar
   */
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

    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("c1",
        token);
    assertEquals(1, contenidosEncontrados.size());

    // Se elimina otro contenido
    servidorPlano.eliminar(contenido2, tokenMagico);
    contenidosEncontrados = servidorPlano.buscar("c2", token);
    assertEquals(1, contenidosEncontrados.size());

    // Se elimina el contenido que se busca
    servidorPlano.eliminar(contenido, tokenMagico);
    contenidosEncontrados = servidorPlano.buscar("c1", token);
    assertEquals(0, contenidosEncontrados.size());
  }

  /*
   * Prueba el metodo eliminar con un token comun (invalido para esto)
   */
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

    Collection<Contenido> contenidosEncontrados = servidorPlano.buscar("c1",
        token);
    assertEquals(1, contenidosEncontrados.size());

    // Se intenta eliminar con un token que no es "especial"
    servidorPlano.eliminar(contenido2, token);
  }

  /*
   * Prueba el metodo buscar con un token dado de baja
   */
  @Test(expected = ContentManagerException.class)
  public void testDarDeBajaToken() throws ContentManagerException {

    // Damos de baja el token existente, no debería dar excepcion
    try {
      // Primero buscamos con el token para comprobar que es un token
      // valido
      servidorPlano.buscar("c1", token);
      servidorPlano.baja(token);
    } catch (ContentManagerException e) {
      assertTrue(false);
    }

    // Se intenta buscar con el token dado de baja
    servidorPlano.buscar("c1", token);
  }
}
