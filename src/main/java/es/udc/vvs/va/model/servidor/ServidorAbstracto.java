package es.udc.vvs.va.model.servidor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import es.udc.vvs.va.model.contenido.Anuncio;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.exceptions.ContentManagerException;
import es.udc.vvs.va.util.ModelConstants;


/**
 * Representa la abstraccion de un servidor, cuyo comportamiento real es
 * comun a muchos tipos de servidor,
 */
public abstract class ServidorAbstracto implements Servidor {

	/**
	 * Nombre del servidor.
	 */
	private final String nombreServidor;
	
	/**
	 * La estructura tokens para gestionar las operaciones basicas como
	 * add(), remove() o contains()... eficienemente (log(n)). Son los tokens
	 * validos del servidor.
	 * @see java.util.TreeMap<K,V>
	 * @see http://docs.oracle.com/javase/7/docs/api/java/util/TreeMap.html
	 */
	protected TreeMap<String, Long> tokens;
	
	/**
	 * El unico token que puede agregar o eliminar contenidos del servidor.
	 */
	protected final String tokenMagico;
	
	/**
	 * La estructura que guarda la coleccion de contenidos del servidor.
	 */
	protected Collection<Contenido> almacen;	
	
	
	/**
	 * Construye un servidor abstracto.
	 * @param nombreServidor el nombre del servidor
	 */
	public ServidorAbstracto(String nombreServidor) {
		this(nombreServidor, null);
	}
	
	/**
	 * Construye un servidor abstracto.
	 * @param nombreServidor el nombre del servidor
	 * @param tokenMagico el token magico a establecer
	 */
	public ServidorAbstracto(String nombreServidor, String tokenMagico) {
		this.nombreServidor = nombreServidor;
		this.tokens = new TreeMap<>();
		this.tokenMagico =
				tokenMagico == null ? this.generarToken() : tokenMagico;
		this.almacen = new LinkedList<>();
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#obtenerNombre()
	 */
	@Override
	public String obtenerNombre() {
		return nombreServidor;
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#alta()
	 */
	@Override
	public String alta() {
		String tokenNuevo;
		
		// para asegurarnos de que el nuevo token generado es distinto
		// de tokenMagico y no se encuentra en nuestra estructura de tokens
		while ((tokenNuevo = this.generarToken()).equals(tokenMagico)
				|| tokens.containsKey(tokenNuevo));
		
		tokens.put(tokenNuevo, ModelConstants.SV_MAXCONTENIDOSTOKEN);
		return tokenNuevo;
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#baja(java.lang.String)
	 */
	@Override
	public void baja(String token) throws ContentManagerException {
		if (tokens.remove(token) == null) {
			throw new ContentManagerException("El token no se ha encontrado.");
		}
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#agregar(
	 * 	es.udc.vvs.vas.Contenido,java.lang.String)
	 */
	@Override
	public void agregar(Contenido contenido, String token)
			throws ContentManagerException {
		
		if (!tokenMagico.equals(token)) {
			throw new ContentManagerException("Token invalido.");
		}
		almacen.add(contenido);
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#eliminar(
	 * 	es.udc.vvs.vas.Contenido, java.lang.String)
	 */
	@Override
	public void eliminar(Contenido contenido, String token)
			 throws ContentManagerException {
		
		if (!tokenMagico.equals(token)) {
			throw new ContentManagerException("Token invalido.");
		}
		almacen.remove(contenido);		
	}
	
	/**
	 * Metodo privado que agrega anuncios a una busqueda con anuncios
	 * @param contenido contenidos a los que agregar anuncios
	 * @return una java.util.List<es.udc.vvs.va.Contenido> con anuncios
	 */
	private List<Contenido> agregarAnuncios(List<Contenido> contenido) {
		
		List<Contenido> contenidoAuxiliar = new LinkedList<>();
		
		long posicion = 0;
		for(Contenido c: contenido) {
			if (posicion++ % ModelConstants.SV_POSANUNCIOCONTENIDOS == 0) {
				contenidoAuxiliar.add(new Anuncio());
			}
			contenidoAuxiliar.add(c);
		}
		
		return contenidoAuxiliar;
	}

	/* Para mas informacion
	 * @see es.udc.vvs.vas.Servidor#buscar(java.lang.String, java.lang.String)
	 * 
	 * Quiza patron estrategia si la complejidad aumentase y hubiese que mante-
	 * ner este codigo.
	 */
	@Override
	public Collection<Contenido> buscar(String subcadena, String token)
			throws ContentManagerException {
		
		boolean anuncios = false;
		List<Contenido> busqueda = new LinkedList<>();
		
		if (tokens.containsKey(token)) { // busqueda de token valido
			long nbusquedas;
			if ((nbusquedas = tokens.get(token)) <= 0) {
				throw new ContentManagerException("Limite "
						+ "de busquedas sobrepasado.");
			} else {
				tokens.put(token, nbusquedas - 1);
			}
		} else if (tokenMagico.equals(token)) { // busqueda divina
			// nada que hacer antes de buscar
		} else if (token.equals("")) { // busqueda con anuncios
			anuncios = true;
		} else { // token invalido suministrado
			throw new ContentManagerException("Token invalido.");
		}
		
		for(Contenido c: almacen) {
			if (c.obtenerTitulo().contains(subcadena)) {
				busqueda.add(c);
			}
		}
		
		if (anuncios) {
			this.agregarAnuncios(busqueda);
		}
		
		return busqueda;
	}
	
	/**
	 * Este metodo genera nuevos tokens arbitrarios cada vez que se llama.
	 * Puede que quieras extraer este comportamiento a algun otro lugar y/o
	 * hacerlo mas seguro.
	 * @return el nuevo token generado.
	 */
	private String generarToken() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Devuelve el tokenMagico. Usa este metodo cuidadosamente. Definido para
	 * la validacion automatica por si se genera un token arbitrario y se
	 * quiere conocer.
	 * @return el tokenMagico
	 */
	public String getTokenMagico() {
		return tokenMagico;
	}
}
