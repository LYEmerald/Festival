package net.endlight.festival;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.endlight.festival.thread.PluginThread;

import java.util.Random;

public class Festival extends PluginBase {

    public static final String VERSION = "1.0.0";

    public static final Random RANDOM = new Random();

    private static Festival plugin;

    public static Festival getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        PluginThread pluginThread = new PluginThread(this.getConfig());
        pluginThread.start();
        this.getLogger().info(TextFormat.GREEN + "线程启动成功!");
        this.getLogger().info(TextFormat.GREEN + "Festival v"+ VERSION +" 加载成功");
    }

}
