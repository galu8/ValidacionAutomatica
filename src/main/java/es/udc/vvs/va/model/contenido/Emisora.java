package es.udc.vvs.va.model.contenido;

import java.util.ArrayList;
import java.util.List;

import es.udc.vvs.va.model.exceptions.ContentManagerException;

/*
 * COMPOSITE
 * Define el comportamiento de los componentes compuestos, 
 * almacena a los hijos e implementa las operaciones de manejo de los
 * componentes.
 */
public class Emisora extends Contenido {
	
	/**
	 * La lista de reproduccion de la emisora
	 */
	private List<Contenido> listaRepr = new ArrayList<Contenido>();

	/**
	 * Constructor Emisora
	 * @param titulo El titulo de la emisora
	 */
	public Emisora(String titulo) {
		super.setTitulo(titulo);
	}

	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#obtenerListaReproduccion()
	 */
	@Override
	public List<Contenido> obtenerListaReproduccion() {
		return listaRepr;
	}

	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#buscar(java.lang.String)
	 */
	@Override
	public List<Contenido> buscar(String subCadena) {
		List<Contenido> busqueda = new ArrayList<Contenido>();
		for (Contenido c:listaRepr) {
			if (c.obtenerTitulo().contains(subCadena)) { 
				busqueda.add(c);
			}
		}
		return busqueda;
	}
	
	/**
	 * Metodo privado para comprobar si un contenido es agregable (una emisora
	 * no puede estar contenida en si misma).
	 * @param contenido Contenido a agregar
	 * @return true si es agregable, false si no lo es
	 */
	private boolean esAgregable(Contenido contenido) {
		if (contenido instanceof Emisora) {
			if (((Emisora) contenido).listaRepr.contains(this)) {
				return false;
			} else {
				for (Contenido c: listaRepr) {
					if (c instanceof Emisora) {
						return ((Emisora)c).esAgregable(contenido);
					}
				}
				for (Contenido c: contenido.obtenerListaReproduccion()) {
					if (c instanceof Emisora) {
						return this.esAgregable(c);
					}
				}
				
			}
		}		
		return true;
	}

	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#agregar
	 *  (es.udc.vvs.va.model.contenido.Contenido, 
	 *  es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public void agregar(Contenido contenido, Contenido predecesor) 
			throws ContentManagerException {
		if ((this!=contenido) && (esAgregable(contenido))) {
			int i = listaRepr.indexOf(predecesor);
			if (i!=-1) { 
				// Si encuentra el predecesor, agrega el contenido despues
				listaRepr.add(listaRepr.indexOf(predecesor)+1, contenido);
			} else { // Si no lo encuentra, lo agrega al final.
				listaRepr.add(contenido);
			}
		} else {
			throw new ContentManagerException("Una emisora no se puede "
					+ "contener a si misma.");
		}
	}
	
	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#eliminar
	 *  (es.udc.vvs.va.model.contenido.Contenido)
	 */
	@Override
	public void eliminar(Contenido contenido) {
		listaRepr.remove(contenido);
	}

	/* (non-Javadoc)
	 * @see es.udc.vvs.va.model.contenido.Contenido#obtenerDuracion()
	 */
	@Override
	public int obtenerDuracion() {
		int duracion = 0;
		for (Contenido c: listaRepr) {
			duracion += c.obtenerDuracion();
		}
		return duracion;
	}

}
