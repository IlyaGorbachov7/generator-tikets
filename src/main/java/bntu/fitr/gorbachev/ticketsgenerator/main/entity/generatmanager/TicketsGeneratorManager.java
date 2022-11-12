package bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.impl.TicketsGeneratorWayImpl1;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.impl.TicketsGeneratorWayImpl2;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GeneratorManagerException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TicketsGeneratorManager {
    private static final Map<String, TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<?>>> generators;
    private static boolean isLoad = false;

    static {
        generators = new HashMap<>();
    }

    private TicketsGeneratorManager() {
    }

    private static <Q extends QuestionExt, T extends Ticket<? super Q>>
    void loadGenerators() {
        ServiceLoader<TicketsGeneratorWay> loader = ServiceLoader.load(TicketsGeneratorWay.class);
        Iterator<TicketsGeneratorWay> iterator = loader.iterator();

        while (iterator.hasNext()) {
            var generatorWay = iterator.next();
            generators.putIfAbsent(generatorWay.getClass().getName(), generatorWay);
        }

        isLoad = true;
    }

    public static <Q extends QuestionExt, T extends Ticket<? super Q>>
    void registerGenerator(Class<? extends TicketsGeneratorWay<Q, T>> clazz) {
        if (!generators.containsKey(clazz.getName())) {
            try {
                TicketsGeneratorWay<Q, T> generatorWay = clazz.getConstructor().newInstance();
                generators.put(generatorWay.getClass().getName(), generatorWay);
            } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException e) {
                throw new GeneratorManagerException("clazz: " + clazz.getName() + " Unable to get public no-arg constructor");
            }
        }
    }

    public static <Q extends QuestionExt, T extends Ticket<? super Q>>
    TicketsGeneratorWay<Q, T> getGenerator(Class<? extends TicketsGeneratorWay<Q, T>> clazz) {
        if (!isLoad) {
            loadGenerators();
        }
        return (TicketsGeneratorWay<Q, T>) generators.get(clazz.getName());
    }

    public static void main(String[] args) {
        TicketsGeneratorManager.getGenerator(TicketsGeneratorWayImpl1.class);
    }
}
