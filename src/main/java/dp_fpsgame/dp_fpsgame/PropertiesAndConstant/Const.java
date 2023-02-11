package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

import java.util.ArrayList;
import java.util.List;

public class Const {

    public static final String foo = "foo";
    public static final String PropertiesFileName = "./plugins/DP_fpsGame/DpFPS.properties";
    public static final String VersionPropName =  "Dp_Version";
    public static final String thisVersion = "1.0";
    public static final String BarPropName = "Bar";
    public static final String Default_Bar = "Bar";


    public static List<String> getPropNames(){
        List<String> PropNames = new ArrayList<>();
        PropNames.add(BarPropName);
        return PropNames;
    }

}
