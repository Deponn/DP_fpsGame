package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MyProperties {

    public final String Bar;

    public MyProperties() {
        File file = new File(Const.PropertiesFileName);
        Properties settings = new Properties();

        try {

            String bar = Const.Default_Bar;

            boolean flag = false;
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty(Const.VersionPropName), Const.thisVersion)) {

                    bar = settings.getProperty(Const.BarPropName);

                    flag = true;
                }
            }
            if(!flag){
                settings.setProperty(Const.VersionPropName, Const.thisVersion);

                settings.setProperty(Const.BarPropName,bar);

                save(file,settings);
            }
            this.Bar = bar;
        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void save(File file,Properties settings) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            settings.store(out, "Properties");
        }
    }
    private static Properties load(File file) throws IOException {
        // Properties の読み込み
        Properties settings = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            settings.load(in);
        }
        return settings;
    }

    public static void PropertiesChange(String PropertiesName , String Value){
        File file = new File(Const.PropertiesFileName);
        Properties settings;
        try {
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty(Const.VersionPropName), Const.thisVersion)) {
                    settings.setProperty(PropertiesName,Value);
                    save(file,settings);
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
