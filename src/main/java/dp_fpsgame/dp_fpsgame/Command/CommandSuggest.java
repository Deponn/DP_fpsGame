package dp_fpsgame.dp_fpsgame.Command;

import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.Const;
import dp_fpsgame.dp_fpsgame.PropertiesAndConstant.PlayMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSuggest implements TabCompleter{
    /**
     * コマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase(CmdName.SetPlayMode.getCmd())) {
            return suggest_SetPlayMode(sender, args);
        }else if (command.getName().equalsIgnoreCase(CmdName.SetProperties.getCmd())){
            return Suggest_Properties(sender, args);
        }else {
            return new ArrayList<>();
        }
    }
    /**
     * どのモードにするか
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    private static List<String> suggest_SetPlayMode(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        List<String> suggest = new ArrayList<>();
        if (argsList.size() == 1) {
            for(PlayMode playMode : PlayMode.values()) {
                suggest.add(playMode.getString());
            }
        }
        return suggest;
    }
    /**
     * プロパティを決めるコマンドのTAB補完候補を返す
     *
     * @param sender コマンド送信者
     * @param args   引数
     * @return コマンド補完候補
     */
    private static List<String> Suggest_Properties(CommandSender sender, String[] args) {
        List<String> argsList = Arrays.asList(args);
        List<String> suggestList = new ArrayList<>();

        String Bar = "-" + Const.BarPropName;

        if (argsList.size() > 1 && argsList.get(argsList.size() - 2).equals(Bar)) {
            return Arrays.asList("hoge","hogehoge","hogehogehoge");

        }else if(argsList.size() == 1) {
            return Stream.of(Bar)
                    .filter(s -> !argsList.contains(s))
                    .collect(Collectors.toList());
        }else {
            return suggestList;
        }
    }
}
