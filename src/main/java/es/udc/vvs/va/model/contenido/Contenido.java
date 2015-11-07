package es.udc.vvs.va.model.contenido;

import java.util.ArrayList;
import java.util.List;

import es.udc.vvs.va.model.exceptions.ContentManagerException;

/*
 * Component
 * Declara la interfaz para los objetos de la composici�n, 
 * es la interfaz de acceso y manipulaci�n de los componentes hijo
 * e implementa algunos comportamientos por defecto.
 */
public abstract class Contenido {
	
	/**
	 * Titulo del contenido.
	 */
	private String titulo;
	
	/**
	 * @param titulo Titulo del contenido
	 */
	protected void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Devuelve una lista de contenidos, en el caso de las emisoras devuelve la
	 * lista de reproduccion presente, en el caso de las canciones o anuncios
	 * se devuelve a si mismo como unico elemento de la lista.
	 * @return Lista de los contenidos
	 */
	public List<Contenido> obtenerListaReproduccion() {
		List<Contenido> listaRepro = new ArrayList<Contenido>();
		listaRepro.add(this);
		return listaRepro;
	}
	
	/**
	 * Devuelve una lista de los contenidos que tienen la subcadena en su
	 * titulo, en emisoras busca dentro de la lista de reproduccion, en
	 * canciones y anuncios busca en el propio titulo.
	 * @param subCadena La subcadena a buscar
	 * @return Lista de contenidos coincidentes
	 */
	public List<Contenido> buscar(String subCadena) {
		List<Contenido> busqueda = new ArrayList<Contenido>();
		if (this.titulo.contains(subCadena)) {
			busqueda.add(this);
		}
		return busqueda;
	}
	
	/**
	 * @return El titulo del contenido
	 */
	public String obtenerTitulo() { return titulo; }
	
	/**
	 * @return La duracion del contenido (en segundos)
	 */
	public abstract int obtenerDuracion();
	
	/**
	 * Agrega un contenido a la emisora, este se agrega en la posicion
	 * siguiente a la de la primera ocurrencia del predecesor. Si no existe el
	 * predecesor, se agrega al final. No hace nada en el caso de canciones y
	 * anuncios.
	 * @param contenido Contenido a agregar
	 * @param predecesor Contenido predecesor en la lista de reproduccion
	 */
	public void agregar (Contenido contenido, Contenido predecesor) 
		throws ContentManagerException {}
	
	/**
	 * Elimina la primera ocurrencia del contenido en la emisora si existe.
	 * No hace nada en el caso de canciones y anuncios.
	 * @param contenido Contenido a eliminar
	 */
	public void eliminar (Contenido contenido) {}
}
