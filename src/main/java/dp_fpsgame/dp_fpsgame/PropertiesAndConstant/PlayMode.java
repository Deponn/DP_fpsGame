package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

public enum PlayMode {

    None("None","何も役職がありません","none","none"),
    Duelist("Duelist","戦う役職","回復する","虚空みたいなやつ"),
    Supporter("Supporter","サポートする","バフポーション","スキャン"),
    Controller("Controller","技を駆使して戦う","浮き上がるポーション","毒ポーション"),
    Skirmisher("Skirmisher","走り回る","速く走る","ジャンプが高くなる");

    private final String Name;
    private final String Exp;
    private final String Ab1Exp;
    private final String Ab2Exp;

    // コンストラクタを定義
    PlayMode(String name,String explanation,String ab1Exp,String ab2Exp) {
        this.Name = name;
        this.Exp = explanation;
        this.Ab1Exp = ab1Exp;
        this.Ab2Exp = ab2Exp;
    }

    // メソッド
    public String getString() {
        return this.Name;
    }
    public String getExp() {
        return this.Exp;
    }
    public String getAb1Exp() {
        return this.Ab1Exp;
    }
    public String getAb2Exp() {
        return this.Ab2Exp;
    }
    public static PlayMode getEnum(String name){
        for(PlayMode playMode : PlayMode.values()){
            if(playMode.getString().equals(name)){
                return playMode;
            }
        }
        return null;
    }
    public static boolean isGameMode(PlayMode playMode){
        return playMode != PlayMode.None;
    }

}
