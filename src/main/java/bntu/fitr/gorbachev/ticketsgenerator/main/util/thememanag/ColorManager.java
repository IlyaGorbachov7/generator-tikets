package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import bntu.fitr.gorbachev.ticketsgenerator.main.views.component.jlist.tblslist.MyListButtons;

import java.awt.*;

/**
 * This implementation follows one of the principles <b>SOLID</b>.
 * Instead of writing hard code inside method getColor() for various case for colors.
 * Created interface, that return Color.class. And anybody cases for representation Color
 * you necessary implements this interface. <b>See above class {@link  MyListButtons.ColorsListBtn} </b>
 */
public interface ColorManager {
    Color getColor();
}