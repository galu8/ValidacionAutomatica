package es.udc.vvs.va.test.model.jetm;

import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.test.model.contenido.CancionTest;
import es.udc.vvs.va.test.model.contenido.EmisoraTest;
import es.udc.vvs.va.test.model.servidor.ServidorPlanoTest;
import es.udc.vvs.va.test.model.servidor.ServidorTransitivoTest;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;

public class Informe {

  private static EtmMonitor monitor;

  protected Informe() {

  }

  public static void main(final String[] args) {
    // configure measurement framework
    setup();

    CancionTest cancion = new CancionTest();
    EmisoraTest emisora = new EmisoraTest();
    ServidorPlanoTest servidorPlano = new ServidorPlanoTest();
    ServidorTransitivoTest servidorTransitivo = new ServidorTransitivoTest();

    try {
      cancion.testObtenerDuracionQC();
      cancion.buscar();
      emisora.duracionEmisoraConCanciones();
      emisora.duracionEmisoraConContenidos();
      emisora.buscarEnEmisoraConCanciones();
      emisora.buscarEnEmisoraConContenidos();
      servidorPlano.buscarEnServidorConCanciones();
      servidorPlano.buscarEnServidorConContenidos();
      servidorTransitivo.buscarEnServidorConCanciones();
      servidorTransitivo.buscarEnServidorConContenidos();
    } catch (ContentManagerException e) {
      e.printStackTrace();
    }

    // visualize results
    monitor.render(new SimpleTextRenderer());

    // shutdown measurement framework
    tearDown();
  }

  /**
   * Initializates JETM.
   */
  private static void setup() {
    BasicEtmConfigurator.configure();
    monitor = EtmManager.getEtmMonitor();
    monitor.start();
  }

  /**
   * Cleans JETM.
   */
  private static void tearDown() {
    monitor.stop();
  }
}