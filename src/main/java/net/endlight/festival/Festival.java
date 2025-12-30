package net.endlight.festival;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.endlight.festival.command.PluginCommand;
import net.endlight.festival.listener.PluginListener;
import net.endlight.festival.thread.FireworkTask;
import net.endlight.festival.thread.PluginThread;

import java.util.Random;

public class Festival extends PluginBase {

    public static final String VERSION = "1.0.2";

    public static final Random RANDOM = new Random();

    private static Festival plugin;

    public static Festival getInstance() {
        return plugin;
    }


    @Override
    public void onLoad() {
        this.getLogger().info(TextFormat.BLUE + "Festival启动中,感谢您的下载与使用!");
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        PluginThread pluginThread = new PluginThread(this.getConfig());
        if (this.getConfig().getBoolean("AutoStart")) {
            pluginThread.start();
            this.getLogger().info(TextFormat.GREEN + "主线程启动成功!");

            if (this.getConfig().getBoolean("FireworkShow")){
                this.getServer().getScheduler().scheduleDelayedRepeatingTask(this, new FireworkTask(), this.getConfig().getInt("SpawnTick"), this.getConfig().getInt("SpawnTick"));
                this.getLogger().info(TextFormat.GREEN + "烟花秀任务启动成功!");
            } else {
                this.getLogger().info(TextFormat.GREEN + "烟花秀任务未启用!");
            }
        }
        this.getServer().getCommandMap().register("festival",new PluginCommand());
        this.getServer().getPluginManager().registerEvents(new PluginListener(), this);
        this.getLogger().info(TextFormat.GREEN + "Festival v"+ VERSION +" 加载成功");
    }

}
