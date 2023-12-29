package net.endlight.festival.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.TextFormat;
import net.endlight.festival.utils.Utils;

public class PluginListener implements Listener {

    @EventHandler
    public void onFormResponse(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        int id = event.getFormID();
        if(event.wasClosed()){
            return;
        }
        if(id == Utils.MENU){
            FormResponseSimple response = (FormResponseSimple) event.getResponse();
            int clickedButtonId = response.getClickedButtonId();
            switch (clickedButtonId) {
                case 0:
                    Utils.startThread();
                    player.sendMessage(TextFormat.GREEN + "线程启动成功!");
                    break;
                case 1:
                    if (player.isOp()) {
                        Utils.sendSettingMenu(player);
                    }
                    break;
                default:
                    break;
            }
        }
        if (id == Utils.SETTING){
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            int year = Integer.parseInt(String.valueOf(response.getResponse(0)));
            int month = Integer.parseInt(String.valueOf(response.getResponse(1)));
            int day = Integer.parseInt(String.valueOf(response.getResponse(2)));
            int hour = Integer.parseInt(String.valueOf(response.getResponse(3)));
            int minute = Integer.parseInt(String.valueOf(response.getResponse(4)));
            int second = Integer.parseInt(String.valueOf(response.getResponse(5)));
            Utils.setConfig(year,month,day,hour,minute,second);
            player.sendMessage(TextFormat.GREEN + "配置保存成功!");
        }
    }
}
