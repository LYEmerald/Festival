package net.endlight.festival.utils;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.utils.DyeColor;
import net.endlight.festival.Festival;
import net.endlight.festival.thread.PluginThread;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    public static int MENU = 0xd3f3ba9;
    public static int SETTING = 0xfed0ee8;
    public static int SYSTEM = 0xe36c80e;


    /**
     * 倒计时格式
     */
    public static String addZero(long i) {
        if (i < 10) {
            return "0" + i;
        }
        return "" + i;
    }

    /**
     * 播放音效
     */
    public static void playSound(Player player, Sound sound,Float pitch) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.name = sound.getSound();
        packet.volume = 1.0F;
        packet.pitch = pitch;
        packet.x = player.getFloorX();
        packet.y = player.getFloorY();
        packet.z = player.getFloorZ();
        player.dataPacket(packet);
    }

    /**
     * 字符串转坐标
     */
    public static Position strToPos(String str) {
        double x = Double.valueOf(str.split(":")[0]);
        double y = Double.valueOf(str.split(":")[1]);
        double z = Double.valueOf(str.split(":")[2]);
        Level level = Festival.getInstance().getServer().getLevelByName(str.split(":")[3]);
        return new Position(x+0.5, y, z+0.5, level);
    }

    /**
     * 放烟花
     */
    public static void spawnFirework(Vector3 pos, Level level) {
        // 创建烟花实体
        EntityFirework firework = new EntityFirework(
                level.getChunk((int) pos.x >> 4, (int) pos.z >> 4),
                createFireworkNBT(pos)
        );

        // 生成到世界
        firework.spawnToAll();
    }

    private static CompoundTag createFireworkNBT(Vector3 pos) {
        Random random = new Random();

        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", pos.x + 0.5))
                        .add(new DoubleTag("", pos.y + 0.5))
                        .add(new DoubleTag("", pos.z + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0.5)) // 向上速度
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putCompound("FireworksItem", createRandomFireworkItem(random));

        return nbt;
    }

    private static CompoundTag createRandomFireworkItem(Random random) {
        CompoundTag explosion = new CompoundTag();

        int type = random.nextInt(5);
        explosion.putByte("Type", type);

        int colorCount = random.nextInt(3) + 1;
        ListTag<CompoundTag> colors = new ListTag<>("Colors");
        for (int i = 0; i < colorCount; i++) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            int rgb = (r << 16) | (g << 8) | b;
            colors.add(new CompoundTag().putInt("", rgb));
        }
        explosion.putList(colors);

        if (random.nextBoolean()) {
            ListTag<CompoundTag> fadeColors = new ListTag<>("FadeColors");
            int fadeR = random.nextInt(256);
            int fadeG = random.nextInt(256);
            int fadeB = random.nextInt(256);
            int fadeRGB = (fadeR << 16) | (fadeG << 8) | fadeB;
            fadeColors.add(new CompoundTag().putInt("", fadeRGB));
            explosion.putList(fadeColors);
        }

        explosion.putBoolean("Flicker", random.nextBoolean()); // 闪烁
        explosion.putBoolean("Trail", random.nextBoolean());   // 拖尾

        CompoundTag fireworks = new CompoundTag()
                .putList(new ListTag<CompoundTag>("Explosions")
                        .add(explosion))
                .putByte("Flight", random.nextInt(3) + 1); // 飞行时间(1-3)

        return new CompoundTag()
                .putShort("id", ItemFirework.FIREWORKS)
                .putByte("Count", 1)
                .putCompound("tag", fireworks);
    }


    /**
     * 插件表单
     */
    public static void sendMainMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple("§l§cFestival",getRandomMessage());
        simple.addButton(new ElementButton("启动线程", new ElementButtonImageData("path","textures/ui/realms_green_check.png")));
        simple.addButton(new ElementButton("编辑时间参数",  new ElementButtonImageData("path","textures/items/clock_item.png")));
        simple.addButton(new ElementButton("编辑系统参数",  new ElementButtonImageData("path","textures/ui/icon_setting.png")));
        player.showFormWindow(simple,MENU);
    }

    public static void sendSettingMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom("时间参数设置");
        custom.addElement(new ElementInput("年","",Festival.getInstance().getConfig().getString("Calendar.Year")));
        custom.addElement(new ElementInput("月","",Festival.getInstance().getConfig().getString("Calendar.Month")));
        custom.addElement(new ElementInput("日","",Festival.getInstance().getConfig().getString("Calendar.Day")));
        custom.addElement(new ElementInput("时","",Festival.getInstance().getConfig().getString("Calendar.Hour")));
        custom.addElement(new ElementInput("分","",Festival.getInstance().getConfig().getString("Calendar.Minute")));
        custom.addElement(new ElementInput("秒","",Festival.getInstance().getConfig().getString("Calendar.Second")));
        player.showFormWindow(custom,SETTING);
    }

    public static void sendSystemMenu(Player player){
        FormWindowCustom custom = new FormWindowCustom("系统参数设置");
        custom.addElement(new ElementToggle("自动启动线程",Festival.getInstance().getConfig().getBoolean("AutoStart")));
        custom.addElement(new ElementInput("奖励消息",Festival.getInstance().getConfig().getString("RewardMessage"),Festival.getInstance().getConfig().getString("RewardMessage")));
        custom.addElement(new ElementInput("发送标题、放烟花、发送奖励执行延迟",Festival.getInstance().getConfig().getString("DelayTime"),Festival.getInstance().getConfig().getString("DelayTime")));
        custom.addElement(new ElementInput("底部奖励消息内容",Festival.getInstance().getConfig().getString("TipMessage"),Festival.getInstance().getConfig().getString("TipMessage")));
        custom.addElement(new ElementInput("底部奖励消息循环间隔时长",Festival.getInstance().getConfig().getString("TipMessagePeriod"),Festival.getInstance().getConfig().getString("TipMessagePeriod")));
        custom.addElement(new ElementInput("底部奖励消息延迟时长",Festival.getInstance().getConfig().getString("TipMessageDelayTime"),Festival.getInstance().getConfig().getString("TipMessageDelayTime")));
        custom.addElement(new ElementInput("底部奖励消息显示时长",Festival.getInstance().getConfig().getString("TipShowTime"),Festival.getInstance().getConfig().getString("TipShowTime")));
        custom.addElement(new ElementInput("播放音乐命令(若使用外部音乐播放器）",Festival.getInstance().getConfig().getString("PlayMusicCmd"),Festival.getInstance().getConfig().getString("PlayMusicCmd")));
        custom.addElement(new ElementInput("播放音乐延迟时长",Festival.getInstance().getConfig().getString("PlayMusicDelayTime"),Festival.getInstance().getConfig().getString("PlayMusicDelayTime")));
        player.showFormWindow(custom,SYSTEM);
    }

    /**
     * 启动线程
     */
    public static void startThread(){
        PluginThread pluginThread = new PluginThread(Festival.getInstance().getConfig());
        pluginThread.start();
    }

    /**
     * 设置时间参数
     */
    public static void setTimeConfig(int year, int month, int day, int hour, int minute, int second){
        Festival.getInstance().getConfig().set("Calendar.Year", year);
        Festival.getInstance().getConfig().set("Calendar.Month", month);
        Festival.getInstance().getConfig().set("Calendar.Day", day);
        Festival.getInstance().getConfig().set("Calendar.Hour", hour);
        Festival.getInstance().getConfig().set("Calendar.Minute", minute);
        Festival.getInstance().getConfig().set("Calendar.Second", second);
        Festival.getInstance().getConfig().save();
    }

    public static void setSystemConfig(boolean AutoStart, String RewardMessage, int DelayTime,String TipMessage,int TipMessagePeriod,int TipMessageDelayTime, int TipShowTime, String PlayMusicCmd,int PlayMusicDelayTime){
        Festival.getInstance().getConfig().set("AutoStart",AutoStart);
        Festival.getInstance().getConfig().set("RewardMessage",RewardMessage);
        Festival.getInstance().getConfig().set("DelayTime",DelayTime);
        Festival.getInstance().getConfig().set("TipMessage",TipMessage);
        Festival.getInstance().getConfig().set("TipMessagePeriod",TipMessagePeriod);
        Festival.getInstance().getConfig().set("TipMessageDelayTime",TipMessageDelayTime);
        Festival.getInstance().getConfig().set("TipShowTime",TipShowTime);
        Festival.getInstance().getConfig().set("PlayMusicCmd",PlayMusicCmd);
        Festival.getInstance().getConfig().set("PlayMusicDelayTime",PlayMusicDelayTime);
    }

    private final static List<String> RANDOM_MESSAGE = Arrays.asList(
            "Thank you for downloading and using this plugin!",
            "For more information about this plugin, visit https://github.com/LYEmerald/Festival",
            "You are running Festival v"+Festival.VERSION + " on Nukkit (" + Nukkit.VERSION +")"
    );

    private static String getRandomMessage() {
        return RANDOM_MESSAGE.get(Festival.RANDOM.nextInt(RANDOM_MESSAGE.size()));
    }

    public static void sendMessageToAll(String string){
        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
            player.sendMessage(string);
        }
    }
    public static void sendTitleToAll(String title,String subtitle,int fadeIn,int stay,int fadeOut){
        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
            player.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
        }
    }

    public static void sendTipToAll(String string){
        for (Player player : Festival.getInstance().getServer().getOnlinePlayers().values()) {
            player.sendTip(string);
        }
    }

}
