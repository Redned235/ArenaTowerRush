package mc.arena.towerRush;


import mc.alk.arena.BattleArena;
import mc.alk.arena.executors.CustomCommandExecutor;
import mc.alk.arena.executors.MCCommand;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import org.bukkit.command.CommandSender;

public class TowerRushExecutor extends CustomCommandExecutor {

    TowerRushListener listener;

    public TowerRushExecutor(TowerRushListener l)
    {
        this.listener = l;
    }

    @MCCommand(cmds={"addBlock"}, /*inGame=true,*/ admin=true)
    public boolean addBlock(ArenaPlayer sender, Arena arena, Integer teamIndex) {

        if (!(arena instanceof TowerRushArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a tower rush arena! It's a " + arena.getClass().getSimpleName());
        }
        this.listener.addPlayer(sender.getName(), Integer.valueOf(teamIndex.intValue() - 1), arena.getName());
        BattleArena.saveArenas(TowerRush.getSelf());
        return sendMessage(sender, "&eThe next block you hit will be a capture block. &6/dr <arena> clear &e to clear all capture blocks");
    }

    @MCCommand(cmds={"clearBlocks"}, admin=true)
    public boolean clearBlocks(CommandSender sender, Arena arena) {
        if (!(arena instanceof TowerRushArena)) {
            return sendMessage(sender, "&eArena " + arena.getName() + " is not a tower rush arena! It's a " + arena.getClass().getSimpleName());
        }
        ((TowerRushArena)arena).clearCaptureBlocks();
        BattleArena.getBAController().updateArena(arena);
        BattleArena.saveArenas(TowerRush.getSelf());
        return sendMessage(sender, "&2capture blocks cleared!");
    }
}
