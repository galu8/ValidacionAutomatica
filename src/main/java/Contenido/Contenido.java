package Contenido;

import java.util.List;

/*
 * Component
 * Declara la interfaz para los objetos de la composici�n, 
 * es la interfaz de acceso y manipulaci�n de los componentes hijo
 * e implementa algunos comportamientos por defecto.
 */
public abstract class Contenido {

	public Contenido() {
	}
	
	public abstract String obtenerTitulo();
	public abstract int obtenerDuracion();
	public abstract Contenido obtenerListaReproduccion();
	public abstract List<Contenido> buscar(String subCadena);
	public abstract void agregar (Contenido contenido, Contenido predecesor);
	public abstract void eliminar (Contenido contenido);
	
}
