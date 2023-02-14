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
    public static final int snowBallNum = 21;
    public static final int foodNum = 64;
    public static final int maxNum = 61;
    public static final Material Ability1_Item = Material.IRON_INGOT;
    public static final Material Ability2_Item = Material.GOLD_INGOT;
    public static final Material FOOD = Material.COOKED_BEEF;
    public static final Material Ability1_Progress_Item = Material.IRON_NUGGET;
    public static final Material Ability2_Progress_Item = Material.GOLD_NUGGET;
    public static final Material SNOWBALL = Material.SNOW;
    public static final String PropertiesFileName = "DpFPS.properties";
    public static final String VersionPropName =  "Dp_Version";
    public static final String thisVersion = "1.1";
    public static final String DamagePropName = "Damage";
    public static final float Default_Damage = 5.0f;

    public static List<String> getPropNames(){
        List<String> PropNames = new ArrayList<>();
        PropNames.add(DamagePropName);
        return PropNames;
    }
}
