package bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.basis.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.exceptions.GeneratorManagerException;
import bntu.fitr.gorbachev.ticketsgenerator.main.basis.impl.generatway.TicketsGeneratorWay;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Tickets Generator Manager to manage implementation interface {@link TicketsGeneratorWay}
 * <p>
 * Implemented classes are loaded from a file: <b>TicketsGeneratorWay</b>
 * <p>
 * or {@link #registerGenerator(Class)}
 *
 * @version 13.11.2022
 */
public class TicketGeneratorManager {
    private static final Map<String, TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<?>>> generators;
    private static boolean isLoad = false;

    static {
        generators = new HashMap<>();
    }

    private TicketGeneratorManager() {
    }

    /**
     * This method load classes, which implement interface TicketsGeneratorWay.
     * <p>
     * Definition classes should be describing in file <i>META-INF/services/
     * <b>bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl.generatway.TicketsGeneratorWay</b></i>
     *
     * @see ServiceLoader
     */
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

    /**
     * This method registers/load clazz, which should implement interface {@link TicketsGeneratorWay}
     *
     * @param clazz class realization TicketsGeneratorWay
     */
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

    /**
     * This method return object generator
     *
     * @param clazz the class whose object we want to get
     * @return object specified class
     */
    public static <Q extends QuestionExt, T extends Ticket<? super Q>>
    TicketsGeneratorWay<Q, T> getGenerator(Class<? extends TicketsGeneratorWay<Q, T>> clazz) {
        if (!isLoad) {
            loadGenerators();
        }
        return (TicketsGeneratorWay<Q, T>) generators.get(clazz.getName());
    }

    /**
     * @return stream data {@link TicketsGeneratorWay}
     */
    public static Stream<TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<? extends QuestionExt>>> getGenerators() {
        if (!isLoad) {
            loadGenerators();
        }
        return generators.values().stream();
    }
}
