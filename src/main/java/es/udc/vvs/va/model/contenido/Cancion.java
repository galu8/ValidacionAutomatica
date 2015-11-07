package es.udc.vvs.va.model.contenido;

import es.udc.vvs.va.model.exceptions.ContentManagerException;


/*
 * LEAF
 * Representa los objetos �hoja� (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Cancion extends Contenido {

	/**
	 * La duracion de la cancion (en segundos)
	 */
	private int duracion;
	
	/**
	 * Constructor Cancion
	 * @param titulo	El titulo de la cancion
	 * @param duracion	La duracion de la cancion
	 * @throws ContentManagerException	Si la duracion es negativa
	 */
	public Cancion(String titulo, int duracion)
			throws ContentManagerException {
		if (duracion < 0) {
			throw new ContentManagerException("La duracion "
					+ "de la cancion no puede ser negativa.");
		}
		this.duracion = duracion;
		super.setTitulo(titulo);
	}

	/**
	 * @return La duracion de la cancion (en segundos)
	 */
	@Override
	public int obtenerDuracion() {
		return duracion;
	}
}
