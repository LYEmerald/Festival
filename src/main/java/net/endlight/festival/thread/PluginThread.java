package net.endlight.festival.thread;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import net.endlight.festival.Festival;
import net.endlight.festival.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class PluginThread extends Thread {

    private Config config;

    public PluginThread(Config config) {
        this.config = config;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.set(this.config.getInt("Calendar.Year"),
                (this.config.getInt("Calendar.Month") - 1),
                this.config.getInt("Calendar.Day"),
                this.config.getInt("Calendar.Hour"),
                this.config.getInt("Calendar.Minute"),
                this.config.getInt("Calendar.Second"));

        long endTime = calendar.getTimeInMillis();
        long startTime = date.getTime();
        long midTime = (endTime - startTime) / 1000;

        while (midTime > 0) {
            midTime--;

            long h = midTime / 60 / 60 % 60;
            long m = midTime / 60 % 60;
            long s = midTime % 60;

            if (h <= 23) {
                if (this.config.getBoolean("DebugMode")) {
                    Festival.getInstance().getLogger().info("[Debug] " + h + "H" + m + "M" + s + "S");
                }
                for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
                    String tipMessageA = config.getString("Bottom.A")
                            .replace("@hour", Utils.addZero(h))
                            .replace("@minute", Utils.addZero(m))
                            .replace("@second", Utils.addZero(s));
                    String tipMessageB = config.getString("Bottom.B")
                            .replace("@hour", Utils.addZero(h))
                            .replace("@minute", Utils.addZero(m))
                            .replace("@second", Utils.addZero(s));
                    if (h > 0) {
                        player.sendTip(tipMessageA);
                    } else if (s >= 30) {
                        player.sendTip(tipMessageB);
                    } else if (m >= 1) {
                        player.sendTip(tipMessageB);
                    }
                    if (h == 0) {
                        if (m == 0) {
                            if (s < 30) {
                                if (s > 20) {
                                    player.sendTitle(config.getString("Title.A").replaceAll("@second", String.valueOf(s)),
                                            config.getString("SubTitle.A"), 0, 40, 0);
                                } else if (s > 10) {
                                    player.sendTitle(config.getString("Title.B").replaceAll("@second", String.valueOf(s)),
                                            config.getString("SubTitle.B"), 0, 40, 0);
                                } else {
                                    player.sendTitle(config.getString("Title.C").replaceAll("@second", String.valueOf(s)),
                                            config.getString("SubTitle.C"), 0, 40, 0);
                                }
                                Utils.playSound(player, Sound.NOTE_PLING, 0.797901F);
                                Utils.playSound(player, Sound.NOTE_CHIME, 0.797901F);
                            }
                        }
                    }
                }
                if (h == 0 && m == 0 && s == 0 ) {
                    Festival.getInstance().getServer().getScheduler().scheduleDelayedTask(Festival.getInstance(), () -> {
                        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
                            player.sendTitle(config.getString("Title.Title"),
                                    config.getString("SubTitle.SubTitle"), 10, 60, 10);
                            // 发送标题

                            Utils.spawnFirework(player.getPosition(),player.getLevel());
                            // 放烟花

                            player.sendMessage(this.config.getString("RewardMessage"));
                            // 奖励消息

                            for (String commands : this.config.getStringList("Rewards")) {
                                String[] cmd = commands.split("&");
                                if ((cmd.length > 1) && (cmd[1].equals("con"))){
                                    Server.getInstance().dispatchCommand(new ConsoleCommandSender(), cmd[0].replace("@player", player.getName()));
                                } else {
                                    Server.getInstance().dispatchCommand(player, cmd[0].replace("@player", player.getName()));
                                }
                            }
                            // 执行奖励命令
                        }
                    },this.config.getInt("DelayTime"));

                    Festival.getInstance().getServer().getScheduler().scheduleDelayedTask(Festival.getInstance(), () -> {
                        Server.getInstance().getCommandMap().dispatch(new ConsoleCommandSender(), this.config.getString("PlayMusicCmd"));
                        // 执行音乐播放命令
                    },this.config.getInt("PlayMusicDelayTime"));

                    Festival.getInstance().getServer().getScheduler().scheduleDelayedRepeatingTask(Festival.getInstance(), () -> {
                        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
                            player.sendTip(this.config.getString("TipMessage"));
                            // 循环发送底部消息
                        }
                    },this.config.getInt("TipMessageDelayTime"),this.config.getInt("TipMessagePeriod"));

                    Festival.getInstance().getServer().getScheduler().scheduleDelayedTask(Festival.getInstance(), () -> {
                        Festival.getInstance().getServer().getScheduler().cancelAllTasks();
                        // 一定时长后取消Task
                    },config.getInt("TipShowTime"));
                }
            }
                try {
                    Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }
    }
}
