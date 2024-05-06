package subtest1;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.JListDataBase;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.UniversityModelTbl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.mdldbtbl.mapper.factory.MapperViewFactoryImpl;
import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.table.reflectapi.ReflectionTableHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Test1 {

    @Test
    public void test1(){
        Assertions.assertTrue(Panel.class.hashCode() != JPanel.class.hashCode());
        System.out.println(Panel.class.hashCode() + " " + JPanel.class.hashCode());
        System.out.println(Panel.class.equals(JPanel.class));
    }

    @Test
    public void test2(){

    }

    @Test
    public void test3(){
        System.out.println(Arrays.toString(ReflectionTableHelper.extractColumnName(UniversityModelTbl.class)));

        Assertions.assertEquals(MapperViewFactoryImpl.getInstance(), MapperViewFactoryImpl.getInstance());
    }
}
