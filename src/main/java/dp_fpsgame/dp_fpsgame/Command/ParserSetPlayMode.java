package dp_fpsgame.dp_fpsgame.Command;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ParserSetPlayMode implements CmdParser{

    private final boolean isSuccess;//パース成功したかどうか
    public final PlayMode playMode;//相手のチーム名

    private ParserSetPlayMode(boolean isSuccess,PlayMode playMode){
        this.isSuccess = isSuccess;
        this.playMode = playMode;
    }

    @Override
    public boolean isSuccess() {
        return isSuccess;
    }

    public static ParserSetPlayMode Parse(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);

        for(PlayMode mode : PlayMode.values()){
            if(argsList.contains(mode.getString())){
                return new ParserSetPlayMode(true,mode);
            }
        }
        return new ParserSetPlayMode(false,null);
    }
}
