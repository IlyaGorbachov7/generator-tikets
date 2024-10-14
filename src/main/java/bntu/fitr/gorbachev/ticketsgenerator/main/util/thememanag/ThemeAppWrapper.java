package bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static bntu.fitr.gorbachev.ticketsgenerator.main.util.thememanag.ThemeApp.LIGHT;

@Data
@AllArgsConstructor
public class ThemeAppWrapper implements Serializable {
    private ThemeApp currentTheme;

    {
        currentTheme = LIGHT;
    }
}
