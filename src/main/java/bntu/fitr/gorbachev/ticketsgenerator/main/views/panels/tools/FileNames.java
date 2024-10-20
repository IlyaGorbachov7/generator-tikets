package bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.tools;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Contains constants file-paths
 */
public class FileNames {
    private static final String targetPath = "/pictures/";

    // record "resources/html/" - important in such look
    private static final String targetPathHtml = "html/";
    private FileNames() {
    }

    /**
     * Accessing the source
     *
     * @return URL data
     */
    public static URL getResource(String filePath) {
        return FileNames.class.getResource(filePath);
    }

    public static String readResourceToString(String aboutProgramHtml) throws IOException {
        try (InputStream in = FileNames.class.getClassLoader().getResourceAsStream(aboutProgramHtml)) {
            return readFromInputStream(in);
        }
    }

    @SneakyThrows
    public static String readFromInputStream(InputStream inputStream) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl.SplashScreenPanel}
     */
    public final static String iconCoursework = targetPath + "iconCoursework.png";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl.AboutProgramPanel}
     */
    public final static String picturePrim1 = targetPath + "picturePrim.jpg";

    public final static String picturePrim2 = targetPath + "picturePrim.png";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl.AboutAuthorPanel}
     */
    public final static String pictureMy = targetPath + "pictureMy.jpg";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.views.panels.impl.MainWindowPanel}
     */
    public final static String openItemIcon = targetPath + "openItemIcon1.png";

    public static final String saveItemIcon = targetPath + "saveItemIcon1.png";

    public static final String removeItemIcon = targetPath + "removeItemIcon.png";

    public static final String exitItemIcon = targetPath + "exitIcon1.png";

    public static final String aboutAuthorItemIcon = targetPath + "aboutAuthorIcon1.png";

    public static final String aboutProgramItemIcon = targetPath + "aboutProgramIcon1.png";

    public static final String generateIcon = targetPath + "generateIcon.png";

    public static final String previewIcon = targetPath + "previewIcon.png";

    public static final String successIcon = targetPath + "successIcon.png";

    public static final String recordSettingIcon = targetPath + "recordSettingIcon.png";

    public static final String databaseSettingIcon = targetPath + "databaseSettingIcon.png";

    public static String languageIcon = targetPath + "lang.png";

    public static final String spinnerLoaderIcon = targetPath + "spinner-loader.gif";

    public static final String nightModeApp = targetPath + "night-mode.png";

    public static final String lightModeApp = targetPath + "light-mode.png";

    public static final String aboutProgramHtml = targetPathHtml + "AboutProgram_%s.html";

    public static final String aboutProgramSnippet1Html = targetPathHtml + "AboutProgramSnippet1_%s.html";
}
