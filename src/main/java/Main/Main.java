package Main;

import Contenido.Anuncio;
import Contenido.Cancion;
import Contenido.Contenido;
import Contenido.Emisora;

public class Main {
	public static void main(String[] args) {
		Contenido anuncio = new Anuncio();
		Contenido cancion = new Cancion("Read my mind", 3);
		
		Contenido emisora = new Emisora();
		// emisora.agregar(cancion, anuncio);
	}
}
