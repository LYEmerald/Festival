package net.endlight.festival.utils;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.level.Level;
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

public class Utils {

    public static int MENU = 0xd3f3ba9;
    public static int SETTING = 0xfed0ee8;

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
     * 放烟花
     * 参考：https://github.com/PetteriM1/FireworkShow
     */
    public static void spawnFirework(Vector3 pos,Level level) {
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        CompoundTag ex = new CompoundTag()
                .putByteArray("FireworkColor", new byte[]{(byte) DyeColor.values()[Festival.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].getDyeData()})
                .putByteArray("FireworkFade", new byte[]{})
                .putBoolean("FireworkFlicker", Festival.RANDOM.nextBoolean())
                .putBoolean("FireworkTrail", Festival.RANDOM.nextBoolean())
                .putByte("FireworkType", ItemFirework.FireworkExplosion.ExplosionType.values()[Festival.RANDOM.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].ordinal());
        tag.putCompound("Fireworks", new CompoundTag("Fireworks")
                .putList(new ListTag<CompoundTag>("Explosions").add(ex))
                .putByte("Flight", 1));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", pos.x + 0.5))
                        .add(new DoubleTag("", pos.y + 0.5))
                        .add(new DoubleTag("", pos.z + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putCompound("FireworkItem", NBTIO.putItemHelper(item));

        FullChunk chunk = level.getChunkIfLoaded(pos.getChunkX(), pos.getChunkZ());
        if (chunk != null) {
            new EntityFirework(chunk, nbt).spawnToAll();
        }
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

    /**
     * 启动线程
     */
    public static void startThread(){
        PluginThread pluginThread = new PluginThread(Festival.getInstance().getConfig());
        pluginThread.start();
    }

    /**
     * 设置参数
     */
    public static void setConfig(int year, int month, int day, int hour, int minute, int second){
        Festival.getInstance().getConfig().set("Calendar.Year", year);
        Festival.getInstance().getConfig().set("Calendar.Month", month);
        Festival.getInstance().getConfig().set("Calendar.Day", day);
        Festival.getInstance().getConfig().set("Calendar.Hour", hour);
        Festival.getInstance().getConfig().set("Calendar.Minute", minute);
        Festival.getInstance().getConfig().set("Calendar.Second", second);
        Festival.getInstance().getConfig().save();
    }

    private final static List<String> RANDOM_MESSAGE = Arrays.asList(
            "愿你所念之人，此刻正陪伴左右",
            "时光流转，愿你与珍爱之人，能够再度重逢",
            "感谢您下载并使用本插件～(∠・ω< )⌒★",
            "音无结弦之时，悦动天使之心。立于浮华之世，奏响天籁之音。"
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
