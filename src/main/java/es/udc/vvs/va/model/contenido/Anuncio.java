package es.udc.vvs.va.model.contenido;

import es.udc.vvs.va.util.ModelConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class Anuncio.
 */
/*
 * LEAF
 * Representa los objetos �hoja� (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Anuncio extends ContenidoAbstracto {


  /**
   * Instantiates a new anuncio.
   */
  public Anuncio() {
    super.setTitulo("PUBLICIDAD");
  }

  /* (non-Javadoc)
   * @see es.udc.vvs.va.model.contenido.ContenidoAbstracto#obtenerDuracion()
   */
  @Override
  public int obtenerDuracion() {
    return ModelConstants.CT_DURACIONANUNCIO;
  }

}
