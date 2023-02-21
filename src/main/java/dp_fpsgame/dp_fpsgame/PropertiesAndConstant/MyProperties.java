package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class MyProperties {

    public final float Damage;
    public final int SnowBallNum;
    public final int LaunchDelay;
    public final int RunCoolTime;
    public final int JumpCoolTime;
    public final int LeviCoolTime;
    public final int PoisonCoolTime;
    public final int HealCoolTime;
    public final int RunAwayCoolTime;
    public final int BuffCoolTime;
    public final int ScanCoolTime;

    public MyProperties() {
        File file = new File(Const.PropertiesFileName);
        Properties settings = new Properties();
        try {
            float damage = Const.Default_Damage;
            int snowBallNum = Const.Default_snowBallNum;
            int launchDelay = Const.Default_LaunchDelay;
            int runCoolTime = Const.Default_RunCoolTime;
            int jumpCoolTime = Const.Default_JumpCoolTime;
            int leviCoolTime = Const.Default_LeviCoolTime;
            int poisonCoolTime = Const.Default_PoisonCoolTime;
            int healCoolTime = Const.Default_HealCoolTime;
            int runAwayCoolTime = Const.Default_RunAwayCoolTime;
            int buffCoolTime = Const.Default_BuffCoolTime;
            int scanCoolTime = Const.Default_ScanCoolTime;
            boolean flag = false;
            if (file.exists()) {
                settings = load(file);
                if (Objects.equals(settings.getProperty(Const.VersionPropName), Const.thisVersion)) {
                    damage = Float.parseFloat(settings.getProperty(Const.DamagePropName));
                    snowBallNum = Integer.parseInt(settings.getProperty(Const.snowBallNumPropName));
                    launchDelay = Integer.parseInt(settings.getProperty(Const.LaunchDelayPropName));
                    runCoolTime = Integer.parseInt(settings.getProperty(Const.RunCoolTimePropName));
                    jumpCoolTime = Integer.parseInt(settings.getProperty(Const.JumpCoolTimePropName));
                    leviCoolTime = Integer.parseInt(settings.getProperty(Const.LeviCoolTimePropName));
                    poisonCoolTime = Integer.parseInt(settings.getProperty(Const.PoisonCoolTimePropName));
                    healCoolTime = Integer.parseInt(settings.getProperty(Const.HealCoolTimePropName));
                    runAwayCoolTime = Integer.parseInt(settings.getProperty(Const.RunAwayCoolTimePropName));
                    buffCoolTime = Integer.parseInt(settings.getProperty(Const.BuffCoolTimePropName));
                    scanCoolTime = Integer.parseInt(settings.getProperty(Const.ScanCoolTimePropName));
                    flag = true;
                }
            }
            if (!flag) {
                settings.setProperty(Const.VersionPropName, Const.thisVersion);
                settings.setProperty(Const.DamagePropName, String.valueOf(damage));
                settings.setProperty(Const.snowBallNumPropName, String.valueOf(snowBallNum));
                settings.setProperty(Const.LaunchDelayPropName, String.valueOf(launchDelay));
                settings.setProperty(Const.RunCoolTimePropName, String.valueOf(runCoolTime));
                settings.setProperty(Const.JumpCoolTimePropName, String.valueOf(jumpCoolTime));
                settings.setProperty(Const.LeviCoolTimePropName, String.valueOf(leviCoolTime));
                settings.setProperty(Const.PoisonCoolTimePropName, String.valueOf(poisonCoolTime));
                settings.setProperty(Const.HealCoolTimePropName, String.valueOf(healCoolTime));
                settings.setProperty(Const.RunAwayCoolTimePropName, String.valueOf(runAwayCoolTime));
                settings.setProperty(Const.BuffCoolTimePropName, String.valueOf(buffCoolTime));
                settings.setProperty(Const.ScanCoolTimePropName, String.valueOf(scanCoolTime));
                save(file, settings);
            }
            this.Damage = damage;
            this.SnowBallNum = snowBallNum;
            this.LaunchDelay = launchDelay;
            this.RunCoolTime = runCoolTime;
            this.JumpCoolTime = jumpCoolTime;
            this.LeviCoolTime = leviCoolTime;
            this.PoisonCoolTime = poisonCoolTime;
            this.HealCoolTime = healCoolTime;
            this.RunAwayCoolTime = runAwayCoolTime;
            this.BuffCoolTime = buffCoolTime;
            this.ScanCoolTime = scanCoolTime;
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
