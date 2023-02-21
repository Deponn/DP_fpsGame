package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final float Velocity = 2.0f;
    public static final int foodSlot = 0;
    public static final int snowBallSlot = 1;
    public static final int abilitySlot1 = 2;
    public static final int abilitySlot2 = 3;
    public static final int progressSlot1 = 4;
    public static final int progressSlot2 = 5;

    public static final int foodNum = 64;
    public static final int maxNum = 64;
    public static final String Exp = "雪を持って右クリックまたは左クリックで雪玉を発射する。鉄インゴットまたは金インゴットをクリックすると" +
            "アビリティみたいなことができる。クールタイムがある。鉄塊や金塊の数が溜まっていき64になったら再度使える。ポーションは敵味方関係ないので" +
            "注意しなければならない。";
    public static final Material Ability1_Item = Material.IRON_INGOT;
    public static final Material Ability2_Item = Material.GOLD_INGOT;
    public static final Material FOOD = Material.COOKED_BEEF;
    public static final Material Ability1_Progress_Item = Material.IRON_NUGGET;
    public static final Material Ability2_Progress_Item = Material.GOLD_NUGGET;
    public static final Material SNOWBALL = Material.SNOW;
    public static final String PropertiesFileName = "DpFPS.properties";
    public static final String VersionPropName =  "Dp_Version";
    public static final String thisVersion = "1.2";
    public static final String DamagePropName = "Damage";
    public static final float Default_Damage = 5.0f;
    public static final String snowBallNumPropName = "snowBallNum";
    public static final int Default_snowBallNum = 21;
    public static final String LaunchDelayPropName = "LaunchDelay";
    public static final int Default_LaunchDelay = 5;
    public static final String RunCoolTimePropName = "RunCoolTime";
    public static final int Default_RunCoolTime = 10;
    public static final String JumpCoolTimePropName = "JumpCoolTime";
    public static final int Default_JumpCoolTime = 10;
    public static final String LeviCoolTimePropName = "LeviCoolTime";
    public static final int Default_LeviCoolTime = 10;
    public static final String PoisonCoolTimePropName = "PoisonCoolTime";
    public static final int Default_PoisonCoolTime = 20;
    public static final String HealCoolTimePropName = "HealCoolTime";
    public static final int Default_HealCoolTime = 30;
    public static final String RunAwayCoolTimePropName = "RunAwayCoolTime";
    public static final int Default_RunAwayCoolTime = 30;
    public static final String BuffCoolTimePropName = "BuffCoolTime";
    public static final int Default_BuffCoolTime = 15;
    public static final String ScanCoolTimePropName = "ScanCoolTime";
    public static final int Default_ScanCoolTime = 15;
    public static List<String> getPropNames(){
        List<String> PropNames = new ArrayList<>();
        PropNames.add(DamagePropName);
        PropNames.add(snowBallNumPropName);
        PropNames.add(LaunchDelayPropName);
        PropNames.add(RunCoolTimePropName);
        PropNames.add(JumpCoolTimePropName);
        PropNames.add(LeviCoolTimePropName);
        PropNames.add(PoisonCoolTimePropName);
        PropNames.add(HealCoolTimePropName);
        PropNames.add(RunAwayCoolTimePropName);
        PropNames.add(BuffCoolTimePropName);
        PropNames.add(ScanCoolTimePropName);
        return PropNames;
    }
}
