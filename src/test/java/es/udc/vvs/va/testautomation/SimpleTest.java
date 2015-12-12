package es.udc.vvs.va.testautomation;

import org.graphwalker.core.condition.EdgeCoverage;
import org.graphwalker.core.condition.ReachedVertex;
import org.graphwalker.core.condition.TimeDuration;
import org.graphwalker.core.generator.AStarPath;
import org.graphwalker.core.generator.RandomPath;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.test.TestBuilder;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class SimpleTest extends ExecutionContext implements Vending {

    public final static Path MODEL_PATH = Paths.get("es/udc/vvs/va/testautomation/Vending.graphml");

    @Override
    public void Ready() {
	System.out.println("La máquina está lista");
    }

    @Override
    public void purchase_coke() {
	System.out.println("Coca-cola comprada");
    }

    @Override
    public void insert_coin() {
	System.out.println("Moneda introducida");
    }

    @Override
    public void power_up_machine() {
	System.out.println("Se enciende la máquina");
    }

    @Override
    public void Coke_Available() {
	System.out.println("Puede comprarse una coca-cola");
    }

    @Override
    public void Coffee_Available() {
	System.out.println("Puede comprarse un café");
    }

    @Override
    public void purchase_coffee() {
	System.out.println("Café comprado");
    }



    @Test
    public void runSmokeTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new SimpleTest())
            .setPathGenerator(new AStarPath(new ReachedVertex("Coke_Available"))) // xeramos un test que chegue a este estado
            .setStart("power_up_machine") // primeira chamada
            .execute();
    }

    @Test
    public void runFunctionalTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new SimpleTest())
            .setPathGenerator(new RandomPath(new EdgeCoverage(100))) // xera tantos test como sexan necesarios para pasar alomenos unha vez por cada transición
            .setStart("power_up_machine") // primeira chamada
            .execute();
    }

    @Test
    public void runStabilityTest() {
        new TestBuilder()
            .setModel(MODEL_PATH)
            .setContext(new SimpleTest())
            .setPathGenerator(new RandomPath(new TimeDuration(3, TimeUnit.MINUTES))) // atravesamos aleatoriamente o grafo durante 3 minutos
            .setStart("power_up_machine") // primeira chamada
            .execute();
    }
}
