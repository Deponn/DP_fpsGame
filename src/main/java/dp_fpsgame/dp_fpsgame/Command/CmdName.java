package dp_fpsgame.dp_fpsgame.Command;

public enum CmdName {


    EnableFPS("DpEnableFPS"),
    DisableFPS("DpDisableFPS"),
    InitializeGame("DpInitializeGame"),
    SetPlayMode("DpSetPlayMode"),
    SetProperties("DpSetProperties");

    private final String Command;

    // コンストラクタを定義
    CmdName(String Command) {
        this.Command = Command;
    }

    // メソッド
    public String getCmd() {
        return this.Command;
    }
}

