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
     * 参考：https://github.com/PetteriM1/FireworkShow
     */
    public static void spawnFirework(Position position) {
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        CompoundTag ex = new CompoundTag();
        ex.putByteArray("FireworkColor",new byte[]{
                (byte) DyeColor.values()[Festival.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].getDyeData()
        });
        ex.putByteArray("FireworkFade",new byte[0]);
        ex.putBoolean("FireworkFlicker",Festival.RANDOM.nextBoolean());
        ex.putBoolean("FireworkTrail",Festival.RANDOM.nextBoolean());
        ex.putByte("FireworkType",ItemFirework.FireworkExplosion.ExplosionType.values()
                [Festival.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].ordinal());
        tag.putCompound("Fireworks",(new CompoundTag("Fireworks"))
                .putList(new ListTag<CompoundTag>("Explosions").add(ex)).putByte("Flight",3));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag();
        nbt.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("",position.x+0.5D))
                .add(new DoubleTag("",position.y+0.5D))
                .add(new DoubleTag("",position.z+0.5D))
        );
        nbt.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
        );
        nbt.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("",0.0F))
                .add(new FloatTag("",0.0F))

        );
        nbt.putCompound("FireworkItem", NBTIO.putItemHelper(item));
        EntityFirework entity = new EntityFirework(position.getLevel().getChunk((int)position.x >> 4, (int)position.z >> 4), nbt);
        entity.spawnToAll();
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
