package es.udc.vvs.va.model.exceptions;

/**
 * @author manoel Esta excepcion representa cualquier excepcion verificada del
 *         modelo.
 */
@SuppressWarnings("serial")
public class ContentManagerException extends Exception {

  public ContentManagerException(String message) {
    super(message);
  }
}
