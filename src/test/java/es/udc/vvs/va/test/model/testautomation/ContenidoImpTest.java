package es.udc.vvs.va.test.model.testautomation;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.condition.ReachedVertex;
import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.generator.AStarPath;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;

import es.udc.vvs.va.model.contenido.Cancion;
import es.udc.vvs.va.model.contenido.Contenido;
import es.udc.vvs.va.model.contenido.Emisora;
import es.udc.vvs.va.model.exceptions.ContentManagerException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ContenidoImpTest extends ExecutionContext implements ContenidoTest{
	public final static Path MODEL_PATH = Paths.get("es/udc/vvs/va/test/model/testautomation/ContenidoTest.graphml");

	@Override
	public void Ready() {
		String titulo = "RockFm";
		Contenido rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		System.out.println("Emisora lista");
	}

	@Override
	public void e_eliminar_contenido() {
		String titulo = "RockFm";
		Emisora rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		
		Contenido cancion = null;
		Contenido cancion2 = null;
		try {
			cancion = new Cancion("c1", 3);
			cancion2 = new Cancion("c2", 5);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		try {
			rockFm.agregar(cancion, null);
			rockFm.agregar(cancion2, cancion);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		List<Contenido> listaRepr = rockFm.buscar("c1");
		assertEquals(listaRepr.get(0), cancion);
		
		System.out.println("Borrando contenido de listResult cuando tiene >1 contenido");
		rockFm.eliminar(cancion);
		listaRepr = rockFm.buscar("c");
		assertEquals(listaRepr.size(), 1);
		
		System.out.println("Borrando contenido de listResult cuando tiene =1 contenido");
		rockFm.eliminar(cancion2);
		listaRepr = rockFm.buscar("c");
		assertEquals(listaRepr.size(), 0);
	}

	@Override
	public void e_iniciar_emisora() {
		String titulo = "RockFm";
		Contenido rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		
		System.out.println("Preparando emisora");
	}

	@Override
	public void e_agregar_contenido() {
		String titulo = "RockFm";
		Emisora rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		
		Contenido cancion = null;
		Contenido cancion2 = null;
		try {
			cancion = new Cancion("c1", 3);
			cancion2 = new Cancion("c2", 5);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("Agregando contenido en emisora vacia");
			rockFm.agregar(cancion, null);
			
			System.out.println("Agregando contenido en emisora con >1 contenido");
			rockFm.agregar(cancion2, cancion);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		//List<Contenido> listaRepr = rockFm.buscar("c1");
		//assertEquals(listaRepr.get(1), cancion);
	}

	@Override
	public void v_Emisora_Con_Contenido() {
		String titulo = "RockFm";
		Emisora rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		
		Contenido cancion = null;
		try {
			cancion = new Cancion("c1", 3);
			rockFm.agregar(cancion, null);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}

		System.out.println("Emisora con contenido");
		
		List<Contenido> listaRepr = rockFm.buscar("c1");
		assertEquals(listaRepr.get(0), cancion);
		
	}

	@Override
	public void e_buscar_contenido() {
		String titulo = "RockFm";
		Emisora rockFm = new Emisora(titulo);
		assertEquals(rockFm.obtenerTitulo(), titulo);
		
		Contenido cancion = null;
		Contenido cancion2 = null;
		try {
			cancion = new Cancion("c1", 3);
			cancion2 = new Cancion("c2", 5);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		try {
			rockFm.agregar(cancion, null);
			rockFm.agregar(cancion2, cancion);
		} catch (ContentManagerException e) {
			e.printStackTrace();
		}
		
		System.out.println("Buscando contenido en emisora");
		List<Contenido> listaRepr = rockFm.buscar("c1");
		assertEquals(listaRepr.get(0), cancion);
	}
	
	@Test
    public void runSmokeTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new ContenidoImpTest())
            .setPathGenerator(new AStarPath(new ReachedVertex("v_Emisora_Con_Contenido"))) // xeramos un test que chegue a este estado
            .setStart("e_iniciar_emisora") // primeira chamada
            .execute();
    }

	@Test
    public void runFunctionalTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new ContenidoImpTest())
            .setPathGenerator(new RandomPath(new EdgeCoverage(100))) // xera tantos test como sexan necesarios para pasar alomenos unha vez por cada transición
            .setStart("e_iniciar_emisora") // primeira chamada
            .execute();
    }

	@Test
    public void runStabilityTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new ContenidoImpTest())
            .setPathGenerator(new RandomPath(new TimeDuration(5, TimeUnit.SECONDS))) // atravesamos aleatoriamente o grafo durante 3 minutos
            .setStart("e_iniciar_emisora") // primeira chamada
            .execute();
    }

}
