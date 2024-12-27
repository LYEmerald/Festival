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
                    String tipMessageA = config.getString("Bottom.A")
                            .replace("@hour", Utils.addZero(h))
                            .replace("@minute", Utils.addZero(m))
                            .replace("@second", Utils.addZero(s));
                    String tipMessageB = config.getString("Bottom.B")
                            .replace("@hour", Utils.addZero(h))
                            .replace("@minute", Utils.addZero(m))
                            .replace("@second", Utils.addZero(s));
                    if (h > 0) {
                        Utils.sendTipToAll(tipMessageA);
                    } else if (s > 30) {
                        Utils.sendTipToAll(tipMessageB);
                    } else if (m >= 1) {
                        Utils.sendTipToAll(tipMessageB);
                    }
                    if (h == 0 && m == 0 && s <= 30) {
                        if (config.getBoolean("SimpleMode")){
                            if (s > 20) {
                                Utils.sendTitleToAll(config.getString("Simple_Title.A").replaceAll("@second", String.valueOf(s)),
                                        config.getString("Simple_SubTitle.A"), 0, 40, 0);
                            } else if (s > 10) {
                                Utils.sendTitleToAll(config.getString("Simple_Title.B").replaceAll("@second", String.valueOf(s)),
                                        config.getString("Simple_SubTitle.B"), 0, 40, 0);
                            } else {
                                Utils.sendTitleToAll(config.getString("Simple_Title.C").replaceAll("@second", String.valueOf(s)),
                                        config.getString("Simple_SubTitle.C"), 0, 40, 0);
                            }
                        } else {
                            switch ((int) s){
                                case 30:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.30s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.30s"), 0, 40, 0);
                                    break;
                                case 29:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.29s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.29s"), 0, 40, 0);
                                    break;
                                case 28:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.28s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.28s"), 0, 40, 0);
                                    break;
                                case 27:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.27s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.27s"), 0, 40, 0);
                                    break;
                                case 26:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.26s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.26s"), 0, 40, 0);
                                    break;
                                case 25:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.25s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.25s"), 0, 40, 0);
                                    break;
                                case 24:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.24s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.24s"), 0, 40, 0);
                                    break;
                                case 23:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.23s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.23s"), 0, 40, 0);
                                    break;
                                case 22:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.22s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.22s"), 0, 40, 0);
                                    break;
                                case 21:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.21s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.21s"), 0, 40, 0);
                                    break;
                                case 20:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.20s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.20s"), 0, 40, 0);
                                    break;
                                case 19:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.19s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.19s"), 0, 40, 0);
                                    break;
                                case 18:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.18s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.18s"), 0, 40, 0);
                                    break;
                                case 17:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.17s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.17s"), 0, 40, 0);
                                    break;
                                case 16:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.16s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.16s"), 0, 40, 0);
                                    break;
                                case 15:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.15s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.15s"), 0, 40, 0);
                                    break;
                                case 14:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.14s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.14s"), 0, 40, 0);
                                    break;
                                case 13:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.13s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.13s"), 0, 40, 0);
                                    break;
                                case 12:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.12s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.12s"), 0, 40, 0);
                                    break;
                                case 11:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.11s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.11s"), 0, 40, 0);
                                    break;
                                case 10:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.10s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.10s"), 0, 40, 0);
                                    break;
                                case 9:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.9s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.9s"), 0, 40, 0);
                                    break;
                                case 8:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.8s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.8s"), 0, 40, 0);
                                    break;
                                case 7:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.7s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.7s"), 0, 40, 0);
                                    break;
                                case 6:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.6s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.6s"), 0, 40, 0);
                                    break;
                                case 5:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.5s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.5s"), 0, 40, 0);
                                    break;
                                case 4:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.4s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.4s"), 0, 40, 0);
                                    break;
                                case 3:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.3s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.3s"), 0, 40, 0);
                                    break;
                                case 2:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.2s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.2s"), 0, 40, 0);
                                    break;
                                case 1:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.1s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.1s"), 0, 40, 0);
                                    break;
                                case 0:
                                    Utils.sendTitleToAll(config.getString("Custom_Title.0s").replaceAll("@second", String.valueOf(s)),
                                            config.getString("Custom_SubTitle.0s"), 0, 40, 0);
                                    break;
                            }
                        }
                        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
                             Utils.playSound(player, Sound.NOTE_PLING, 0.840896F);
                             Utils.playSound(player, Sound.NOTE_CHIME, 0.840896F);
                    }
                }
                if (h == 0 && m == 0 && s == 0 ) {
                    Festival.getInstance().getServer().getScheduler().scheduleDelayedTask(Festival.getInstance(), () -> {
                        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
                            player.sendTitle(config.getString("Final_Title.Title"),
                                    config.getString("Final_Title.SubTitle"), 10, 60, 10);
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
                        Utils.sendTipToAll(this.config.getString("TipMessage"));
                        // 循环发送底部消息
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
