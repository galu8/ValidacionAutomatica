package es.udc.vvs.va.model.contenido;

import java.util.List;

/*
 * Component
 * Declara la interfaz para los objetos de la composici�n, 
 * es la interfaz de acceso y manipulaci�n de los componentes hijo
 * e implementa algunos comportamientos por defecto.
 */
public abstract class Contenido {
	
	/**
	 * Metodos de las hojas.
	 */
	public abstract String obtenerTitulo();
	public abstract int obtenerDuracion();
	
	/**
	 * Metodos de la composicion. Implementacion por defecto.
	 */
	public Contenido obtenerListaReproduccion() { return null; }
	public List<Contenido> buscar(String subCadena) { return null; }
	public void agregar (Contenido contenido, Contenido predecesor) {}
	public void eliminar (Contenido contenido) {}
	
}
