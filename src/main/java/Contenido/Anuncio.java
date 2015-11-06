package Contenido;

import java.util.List;

/*
 * LEAF
 * Representa los objetos “hoja” (no poseen hijos). 
 * Define comportamientos para objetos primitivos.
 */
public class Anuncio extends Contenido{
	@Override
	public String obtenerTitulo() {
		return "PUBLICIDAD";
	}

	@Override
	public int obtenerDuracion() {
		return 5;
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
