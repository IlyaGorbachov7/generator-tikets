package bntu.fitr.gorbachev.ticketsgenerator.main.panels.tools;

import java.net.URL;

/**
 * Contains constants file-paths
 */
public class FileNames {
    private static final String targetPath = "/resources/pictures/";

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

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl.SplashScreenPanel}
     */
    public final static String iconCoursework = targetPath + "iconCoursework.png";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl.AboutProgramPanel}
     */
    public final static String picturePrim1 = targetPath + "picturePrim.jpg";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl.AboutAuthorPanel}
     */
    public final static String pictureMy = targetPath + "pictureMy.jpg";

    /**
     * {@link bntu.fitr.gorbachev.ticketsgenerator.main.panels.impl.MainWindowPanel}
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

}
