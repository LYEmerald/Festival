package net.endlight.festival.thread;

import cn.nukkit.level.Position;
import net.endlight.festival.Festival;
import net.endlight.festival.utils.Utils;

public class FireworkTask implements Runnable{

    @Override
    public void run() {
        for (String pos_str : Festival.getInstance().getConfig().getStringList("FireworkPosition")){
            if (PluginThread.in23h){
                Position firework_pos = Utils.strToPos(pos_str);
                Utils.spawnFirework(firework_pos);
            }
        }
    }
}
