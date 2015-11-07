package es.udc.vvs.va.model.contenido;

import es.udc.vvs.va.model.exceptions.ContentManagerException;


/*
 * LEAF
 * Representa los objetos �hoja� (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Cancion extends Contenido {
	
	private String titulo;
	private int duracion;

	/*
	 * Constructor
	 */
	public Cancion(String titulo, int duracion)
			throws ContentManagerException {
		super();
		this.titulo = titulo;
		if (duracion < 0) {
			throw new ContentManagerException("La duracion "
					+ "de la cancion no puede ser negativa.");
		}
		this.duracion = duracion;
	}

	@Override
	public String obtenerTitulo() {
		return titulo;
	}

	@Override
	public int obtenerDuracion() {
		return duracion;
	}
}
