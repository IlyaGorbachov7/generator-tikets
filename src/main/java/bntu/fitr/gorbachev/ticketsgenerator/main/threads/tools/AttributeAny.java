package bntu.fitr.gorbachev.ticketsgenerator.main.threads.tools;

public class AttributeAny {
    String name;
    Integer age;
    String avarage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAvarage() {
        return avarage;
    }

    public void setAvarage(String avarage) {
        this.avarage = avarage;
    }

    @Override
    public String toString() {
        return "AttributeAny{" +
               "name='" + name + '\'' +
               ", age=" + age +
               ", avarage=" + avarage +
               '}';
    }
}
