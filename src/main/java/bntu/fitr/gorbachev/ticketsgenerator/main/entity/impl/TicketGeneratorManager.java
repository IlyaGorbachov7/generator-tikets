package bntu.fitr.gorbachev.ticketsgenerator.main.entity.impl;

import bntu.fitr.gorbachev.ticketsgenerator.main.entity.QuestionExt;
import bntu.fitr.gorbachev.ticketsgenerator.main.entity.Ticket;
import bntu.fitr.gorbachev.ticketsgenerator.main.exceptions.GeneratorManagerException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Tickets Generator Manager to manage implementation interface {@link bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay}
 * <p>
 * Implemented classes are loaded from a file: <b>bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay</b>
 * <p>
 * or {@link #registerGenerator(Class)}
 *
 * @version 13.11.2022
 */
public class TicketGeneratorManager {
    private static final Map<String, bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<?>>> generators;
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
     * <b>bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay</b></i>
     *
     * @see ServiceLoader
     */
    private static <Q extends QuestionExt, T extends Ticket<? super Q>>
    void loadGenerators() {
        ServiceLoader<bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay> loader = ServiceLoader.load(bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay.class);
        Iterator<bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay> iterator = loader.iterator();

        while (iterator.hasNext()) {
            var generatorWay = iterator.next();
            generators.putIfAbsent(generatorWay.getClass().getName(), generatorWay);
        }

        isLoad = true;
    }

    /**
     * This method registers/load clazz, which should implement interface {@link bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay}
     *
     * @param clazz class realization TicketsGeneratorWay
     */
    public static <Q extends QuestionExt, T extends Ticket<? super Q>>
    void registerGenerator(Class<? extends bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<Q, T>> clazz) {
        if (!generators.containsKey(clazz.getName())) {
            try {
                bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<Q, T> generatorWay = clazz.getConstructor().newInstance();
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
    bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<Q, T> getGenerator(Class<? extends bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<Q, T>> clazz) {
        if (!isLoad) {
            loadGenerators();
        }
        return (bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<Q, T>) generators.get(clazz.getName());
    }

    /**
     * @return stream data {@link bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay}
     */
    public static Stream<bntu.fitr.gorbachev.ticketsgenerator.main.entity.generatmanager.TicketsGeneratorWay<? extends QuestionExt, ? extends Ticket<? extends QuestionExt>>> getGenerators() {
        if (!isLoad) {
            loadGenerators();
        }
        return generators.values().stream();
    }
}
