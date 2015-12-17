package es.udc.vvs.va.model.servidor;

import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

import java.util.Collection;

/**
 * Esta interfaz representa el comportamiento de cualquier servidor que pueda
 * existir en nuestro modelo.
 */
public interface Servidor {

  /**
   * Devuelve el nombre del servidor.
   * 
   * @return el nombre del servidor
   */
  String obtenerNombre();

  /**
   * Registra un token autogenerado.
   * 
   * @return el nuevo token registrado
   */
  String alta();

  /**
   * Este metodo elimina el token dado del servidor.
   * 
   * @param token
   *          a ser eliminado.
   */
  void baja(String token) throws ContentManagerException;

  /**
   * Agrega nuevo contenido al servidor. Solo el tokenMagico puede realizar esta
   * tarea.
   * 
   * @param contenido
   *          a ser agregado.
   * @param token
   *          valido/conocido.
   */
  void agregar(Contenido contenido, String token)
      throws ContentManagerException;

  /**
   * Deletes a content from the server. Solo el tokenMagico puede realizar esta
   * tarea.
   * 
   * @param contenido
   *          a ser eliminado.
   * @param token
   *          valido/conocido.
   */
  void eliminar(Contenido contenido, String token)
      throws ContentManagerException;

  /**
   * Intenta devolver contenido del servidor de acuerdo con la subcadena dada.
   * 
   * Hasta es.udc.vvs.va.util.ModelConstants.SV_MAXCONTENIDOSTOKEN busquedas se
   * pueden hacer por cada token comun.
   * 
   * tokenMagico tiene busquedas ilimitadas.
   * 
   * @param subcadena
   *          para buscar contenido.
   * @param token
   *          un token. Si el token es vacio, se agregan algunos anuncios al
   *          contenido de la respuesta
   * 
   *          Para mas informacion
   * @see es.udc.vvs.va.util.ModelConstants.SV_POSANUNCIOCONTENIDOS
   * 
   * @return java.util.Collection<es.udc.vvs.va.Contenido>
   */
  Collection<Contenido> buscar(String subcadena, String token)
      throws ContentManagerException;
}
