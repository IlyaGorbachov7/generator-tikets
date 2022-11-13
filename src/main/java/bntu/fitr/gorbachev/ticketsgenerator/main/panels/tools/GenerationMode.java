package bntu.fitr.gorbachev.ticketsgenerator.main.panels.tools;


/**
 * Mode enum will define generation tickets algorithm.
 */
public enum GenerationMode {
    MODE_1("Группировать по темам"),
    MODE_2("Группировать по сложности"),
    MODE_3("Рандомно"),
    MODE_4("Группировать по темам и сложности");
    String name;

    GenerationMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}