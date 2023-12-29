package net.endlight.festival.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import net.endlight.festival.Festival;
import net.endlight.festival.utils.Utils;

public class PluginCommand extends Command {

    public PluginCommand() {
        super("festival", "Festival节日倒计时插件");
    }

    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (player.isOp()) {
                Utils.sendMainMenu(player);
            }
            return true;
        } else {
            Festival.getInstance().getLogger().info(TextFormat.RED + "此命令不能在控制台执行哦");
            return false;
        }
    }
}
