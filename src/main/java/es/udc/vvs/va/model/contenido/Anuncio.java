package es.udc.vvs.va.model.contenido;

import es.udc.vvs.va.util.ModelConstants;

/*
 * LEAF
 * Representa los objetos �hoja� (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Anuncio extends Contenido {
	@Override
	public String obtenerTitulo() {
		return "PUBLICIDAD";
	}

	@Override
	public int obtenerDuracion() {
		return ModelConstants.CT_DURACIONANUNCIO;
	}
}
