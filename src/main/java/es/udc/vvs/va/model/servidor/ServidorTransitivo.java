package es.udc.vvs.va.model.servidor;

import java.util.Collection;

import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

/**
 * Representa a un servidor transitivo, aquel que tiene otro servidor aliado.
 */
public class ServidorTransitivo extends ServidorAbstracto {

	/**
	 * Representa al servidor relacionado.
	 */
	private final Servidor servidorRelacionado;
	
	/**
	 * Construye un servidor transitivo.
	 * @param nombreServidor el nombre del servidor
	 */
	public ServidorTransitivo(String nombreServidor,
			Servidor servidorRelacionado) throws ContentManagerException {
		
		this(nombreServidor, null, servidorRelacionado);
	}
	
	/**
	 * Construye un servidor transitivo. Requiere de un servidor relacionado.
	 * @param nombreServidor el nombre del servidor.
	 */
	public ServidorTransitivo(String nombreServidor, String token,
				Servidor servidorRelacionado)
						throws ContentManagerException {
		super(nombreServidor, token);
		
		if (servidorRelacionado == null) {
			throw new ContentManagerException("Los servidores transitivos "
					+ "necesitan un servidor de apoyo.");
		}
		
		this.servidorRelacionado = servidorRelacionado;
	}
	
	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#buscar(java.lang.String, java.lang.String)
	 * 
	 * Realiza la búsqueda de servidor transitivo.
	 */
	@Override
	public Collection<Contenido> buscar(String subcadena, String token)
			throws ContentManagerException {
		
		Collection<Contenido> busqueda = super.buscar(subcadena, token);
		
		if (busqueda.isEmpty()) {
			busqueda = servidorRelacionado.buscar(subcadena, token);
		}
		
		return busqueda;
	}

}
