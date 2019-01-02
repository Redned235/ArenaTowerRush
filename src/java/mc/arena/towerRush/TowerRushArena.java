package mc.arena.towerRush;

import mc.alk.arena.BattleArena;
import mc.alk.arena.alib.battlescoreboardapi.scoreboard.SAPIDisplaySlot;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.scoreboard.ArenaDisplaySlot;
import mc.alk.arena.objects.scoreboard.ArenaObjective;
import mc.alk.arena.objects.scoreboard.ArenaScoreboard;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.serializers.Persist;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TowerRushArena extends Arena {

    static int damage = 10;
    @Persist
    List<CaptureBlock> towerBlocks = new ArrayList<CaptureBlock>();
    ArenaObjective scores;
    ArenaObjective playerScores;
    int nTeams = 0;
    int blocksToWin = 1;
    HashMap<ArenaTeam, Long> dfTimers = new HashMap();
    HashMap<ArenaTeam, Long> atTimers = new HashMap();

    public void onStart()  {
        List<ArenaTeam> teams = getTeams();
        ArenaScoreboard scoreboard = this.match.getScoreboard();
        this.scores = scoreboard.createObjective("Tower", "Break the tower", ChatColor.GOLD + "Tower Health",
                SAPIDisplaySlot.SIDEBAR);
        this.scores.setDisplayPlayers(false);

        this.playerScores = scoreboard.createObjective("Hits", "Tower hits", ChatColor.GOLD + "Tower Hits",
                SAPIDisplaySlot.PLAYER_LIST);

        this.nTeams = teams.size();
        resetTowerBlocks();
        this.scores.setAllPoints(CaptureBlock.MAX_HEALTH);
        for (ArenaTeam t : teams) {
            this.dfTimers.put(t, Long.valueOf(0L));
            this.atTimers.put(t, Long.valueOf(0L));
            this.scores.setPoints(t, CaptureBlock.MAX_HEALTH);
        }

        this.scores.setDisplaySlot(ArenaDisplaySlot.SIDEBAR);
        this.scores.setAllPoints(this.match, CaptureBlock.MAX_HEALTH);
    }

    private void resetTowerBlocks() {
        for (CaptureBlock cb : this.towerBlocks) {
            cb.reset();
        }
    }

    public void onComplete() {}

    public void onCancel() {}

    @ArenaEventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        for (int i = 0; i < this.towerBlocks.size(); i++) {
            if (sameLocation(loc, ((CaptureBlock)this.towerBlocks.get(i)).getLocation())) {
                event.getBlock().setType(Material.STONE);
                event.setCancelled(true);

                getMatch().killTeam(i);
                break;
            }
        }
    }

    @ArenaEventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Location loc = event.getClickedBlock().getLocation();
        for (int i = 0; i < this.towerBlocks.size(); i++) {
            if (sameLocation(((CaptureBlock)this.towerBlocks.get(i)).getLocation(), loc))  {
                event.setCancelled(true);
                Player p = event.getPlayer();
                ArenaTeam team = getTeam(event.getPlayer());
                ArenaTeam defendingTeam = getTeam(i);
                if ((team == null) || (defendingTeam == null) || (team == defendingTeam)) {
                    return;
                }
                CaptureBlock cb = (CaptureBlock)this.towerBlocks.get(i);
                Long t = (Long)this.dfTimers.get(defendingTeam);
                ArenaPlayer ap = BattleArena.toArenaPlayer(p);
                Long now = Long.valueOf(System.currentTimeMillis());
                if (cb.getHealth() < 0) {
                    return;
                }
                cb.reduceHealth(1);
                this.scores.subtractPoints(defendingTeam, 1);
                this.playerScores.addPoints(ap, 1);
                if (now.longValue() - t.longValue() > 5000L) {
                    this.dfTimers.put(defendingTeam, now);
                    String msg = "&4[" + getMatch().getParams().getPrefix() + "&4] &6" +
                            p.getDisplayName() + "&c is attacking your Tower ! Nexus Health left! : " +
                            cb.getHealth();
                    defendingTeam.sendMessage(msg);
                }
                t = (Long)this.atTimers.get(team);
                if (now.longValue() - t.longValue() > 5000L) {
                    this.atTimers.put(team, now);
                    String msg = "&4[" + getMatch().getParams().getPrefix() + "&4]&2 &6" +
                            p.getDisplayName() + "&2 is capturing &6" +
                            defendingTeam.getDisplayName() + "&2 tower! Health : &4" + cb.getHealth();
                    team.sendMessage(msg);
                }
                if (cb.getHealth() > 0) {
                    break;
                }
                event.getClickedBlock().setType(Material.STONE);

                getMatch().killTeam(i);
                if (getAliveTeams().size() != 1) {
                    break;
                }
                setWinner(team);

                break;
            }
        }
    }

    public boolean valid() {
        return (super.valid()) && (this.towerBlocks.size() >= 2);
    }

    public List<String> getInvalidReasons()  {
        List<String> reasons = new ArrayList<String>();
        if ((this.towerBlocks == null) || (this.towerBlocks.size() < 2)) {
            reasons.add("You need to select at least 2 capture blocks!");
        }
        reasons.addAll(super.getInvalidReasons());
        return reasons;
    }

    public void addBlock(Integer team, Block block) {
        if (this.towerBlocks == null) {
            this.towerBlocks = new ArrayList<CaptureBlock>();
        }
        if (this.towerBlocks.size() == team.intValue()) {
            this.towerBlocks.add(new CaptureBlock(block));
        } else {
            if (this.towerBlocks.size() < team.intValue()) {
                throw new ArrayIndexOutOfBoundsException("&cYou need to add blocks for team &6" + (this.towerBlocks.size() + 1) + " &cfirst");
            }
            this.towerBlocks.set(team.intValue(), new CaptureBlock(block));
        }
    }

    public void clearCaptureBlocks() {
        this.towerBlocks.clear();
    }

    public boolean sameLocation(Location l1, Location l2) {
        return (l1.getWorld().equals(l2.getWorld())) && (l1.getBlockX() == l2.getBlockX()) && (l1.getBlockY() == l2.getBlockY()) && (l1.getBlockZ() == l2.getBlockZ());
    }
}
