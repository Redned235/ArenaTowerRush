package mc.arena.towerRush;

import mc.alk.arena.BattleArena;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.ConcurrentHashMap;

public class TowerRushListener implements Listener {

    ConcurrentHashMap<String, ClickBlock> playersDoingCommand = new ConcurrentHashMap();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.isCancelled()) || (!this.playersDoingCommand.containsKey(event.getPlayer().getName()))) {
            return;
        }
        ClickBlock cb = (ClickBlock)this.playersDoingCommand.remove(event.getPlayer().getName());
        Arena arena = BattleArena.getArena(cb.arenaName);
        if (arena == null) {
            MessageUtil.sendMessage(event.getPlayer(), "&4Arena " + cb.arenaName + " not found");
            return;
        }
        if (!(arena instanceof TowerRushArena)) {
            MessageUtil.sendMessage(event.getPlayer(), "&4Arena " + cb.arenaName + " was not a BlockDefense arena");
            return;
        }
        TowerRushArena dra = (TowerRushArena)arena;
        dra.addBlock(Integer.valueOf(cb.teamIndex), event.getClickedBlock());
        BattleArena.saveArenas();
        event.setCancelled(true);
        MessageUtil.sendMessage(event.getPlayer(), "&2Added block &6" + event.getClickedBlock().getType() + "&2 to team &6" + (cb.teamIndex + 1));
    }

    public void addPlayer(String playerName, Integer team, String arenaName) {
        ClickBlock cb = new ClickBlock();
        cb.arenaName = arenaName;
        cb.teamIndex = team.intValue();
        this.playersDoingCommand.put(playerName, cb);
    }

    public class ClickBlock {
        String arenaName;
        int teamIndex;

        public ClickBlock() {}
    }
}
