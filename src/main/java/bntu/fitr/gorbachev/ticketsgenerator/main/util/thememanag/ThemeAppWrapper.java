package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp.LIGHT;

@Data
@AllArgsConstructor
public class ThemeAppWrapper implements Serializable {
    @Serial
    private final static long serialVersionUID = 11111111111111111L;

    private ThemeApp currentTheme;

    {
        currentTheme = LIGHT;
    }
}
