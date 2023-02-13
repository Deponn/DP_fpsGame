package dp_fpsgame.dp_fpsgame.Command;

public enum CmdName {


    EnableFPS("DpEnableFPS"),
    DisableFPS("DpDisableFPS"),
    ResetGame("DpResetGame"),
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

