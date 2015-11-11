package es.udc.vvs.va.model.contenido;

import java.util.ArrayList;
import java.util.List;

import es.udc.vvs.va.model.exceptions.ContentManagerException;

/* @see es.udc.vvs.va.model.contenido.Contenido
 */
public abstract class ContenidoAbstracto implements Contenido {
	
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

	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#obtenerListaReproduccion
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public List<Contenido> obtenerListaReproduccion() {
		List<Contenido> listaRepro = new ArrayList<Contenido>();
		listaRepro.add(this);
		return listaRepro;
	}
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#buscar
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public List<Contenido> buscar(String subCadena) {
		List<Contenido> busqueda = new ArrayList<Contenido>();
		if (this.titulo.contains(subCadena)) {
			busqueda.add(this);
		}
		return busqueda;
	}
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#obtenerTitulo
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public String obtenerTitulo() { return titulo; }
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#obtenerDuracion
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	public abstract int obtenerDuracion();
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#agregar
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public void agregar (Contenido contenido, Contenido predecesor) 
		throws ContentManagerException {}
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#eliminar
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public void eliminar (Contenido contenido) {}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contenido other = (Contenido) obj;
		if (titulo == null) {
			if (other.obtenerTitulo() != null)
				return false;
		} else if (!titulo.equals(other.obtenerTitulo()))
			return false;
		return true;
	}
	
	
}
