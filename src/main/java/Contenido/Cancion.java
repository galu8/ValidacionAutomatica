package Contenido;

import java.util.List;

/*
 * LEAF
 * Representa los objetos “hoja” (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Cancion extends Contenido{
	
	private String titulo;
	private int duracion;

	/*
	 * Constructor
	 */
	public Cancion(String titulo, int duracion) {
		super();
		this.titulo = titulo;
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

	@Override
	public Contenido obtenerListaReproduccion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Contenido> buscar(String subCadena) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * No tiene efecto
	 */
	@Override
	public void agregar(Contenido contenido, Contenido predecesor) {
	}

	/*
	 * No tiene efecto
	 */
	@Override
	public void eliminar(Contenido contenido) {
	}

}
