package es.udc.vvs.va.model.contenido;

import java.util.List;

/*
 * COMPOSITE
 * Define el comportamiento de los componentes compuestos, 
 * almacena a los hijos e implementa las operaciones de manejo de los componentes.
 */
public class Emisora extends Contenido {

	@Override
	public String obtenerTitulo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtenerDuracion() {
		// TODO Auto-generated method stub
		return 0;
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
	 * A�ade Canciones o Anuncios de la lista
	 */
	@Override
	public void agregar(Contenido contenido, Contenido predecesor) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Elimina Canciones o Anuncios de la lista
	 */
	@Override
	public void eliminar(Contenido contenido) {
		// TODO Auto-generated method stub
		
	}

}
