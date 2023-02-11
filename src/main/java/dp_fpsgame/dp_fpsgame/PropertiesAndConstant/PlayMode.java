package dp_fpsgame.dp_fpsgame.PropertiesAndConstant;

public enum PlayMode {

    None("None"),
    Duelist("Duelist"),
    Supporter("Supporter"),
    Controller("Controller"),
    Skirmisher("Skirmisher");

    private final String Name;

    // コンストラクタを定義
    PlayMode(String name) {
        this.Name = name;
    }

    // メソッド
    public String getString() {
        return this.Name;
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
