package es.udc.vvs.va.model.servidor;

/**
 * Representa a un servidor plano. Aquel que no tiene servidor relacionado.
 */
public class ServidorPlano extends ServidorAbstracto {

  /**
   * Construye un servidor plano.
   * 
   * @param nombreServidor
   *          el nombre del servidor
   */
  public ServidorPlano(String nombreServidor) {
    super(nombreServidor);
  }

  /**
   * Construye un nuevo servidor plano.
   * 
   * @param nombreServidor
   *          el nombre del servidor plano.
   */
  public ServidorPlano(String nombreServidor, String token) {
    super(nombreServidor, token);
  }

}
