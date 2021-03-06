package es.udc.vvs.va.model.servidor;

import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

import java.util.Collection;

/**
 * Representa a un servidor transitivo, aquel que tiene otro servidor aliado.
 */
public class ServidorTransitivo extends ServidorAbstracto {

  /**
   * Representa al servidor relacionado.
   */
  private final Servidor servidorRelacionado;

  /**
   * Construye un servidor transitivo.
   * 
   * @param nombreServidor
   *          el nombre del servidor
   */
  public ServidorTransitivo(String nombreServidor, Servidor servidorRelacionado)
      throws ContentManagerException {

    this(nombreServidor, null, servidorRelacionado);
  }

  /**
   * Construye un servidor transitivo. Requiere de un servidor relacionado.
   * 
   * @param nombreServidor
   *          el nombre del servidor.
   */
  public ServidorTransitivo(String nombreServidor, String token,
      Servidor servidorRelacionado) throws ContentManagerException {
    super(nombreServidor, token);

    if (servidorRelacionado == null) {
      throw new ContentManagerException("Los servidores transitivos "
          + "necesitan un servidor de apoyo.");
    }

    this.servidorRelacionado = servidorRelacionado;
  }

  @Override
  public String alta() {
    String token = super.alta();
    if (servidorRelacionado instanceof ServidorAbstracto) {
      ((ServidorAbstracto) servidorRelacionado).agregarToken(token);
    }
    return token;
  }

  @Override
  public void baja(String token) throws ContentManagerException {
    super.baja(token);
    servidorRelacionado.baja(token);
  }

  /*
   * Para mas informacion
   * 
   * @see es.udc.vvs.vas.Servidor#buscar(java.lang.String, java.lang.String)
   * 
   * Realiza la búsqueda de servidor transitivo.
   */
  @Override
  public Collection<Contenido> buscar(String subcadena, String token)
      throws ContentManagerException {

    Collection<Contenido> busqueda = super.buscar(subcadena, token);

    if (busqueda.isEmpty()) {
      busqueda = servidorRelacionado.buscar(subcadena, token);
    }

    return busqueda;
  }

}
