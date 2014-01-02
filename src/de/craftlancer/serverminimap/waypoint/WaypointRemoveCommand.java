package de.craftlancer.serverminimap.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.serverminimap.ServerMinimap;

public class WaypointRemoveCommand extends WaypointSubCommand
{
    
    public WaypointRemoveCommand(String permission, ServerMinimap plugin)
    {
        super(permission, plugin, false);
    }
    
    @Override
    protected void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!checkSender(sender))
            sender.sendMessage("You don't have permission for this command!");
        else if (args.length > 2)
            sender.sendMessage("You need to specify a index!");
        else
        {
            int index;
            
            try
            {
                index = Integer.parseInt(args[1]) - 1;
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(args[1] + " is not a number!");
                return;
            }
            
            plugin.getWaypointHandler().removeWaypoint(sender.getName(), index);
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage("/waypoint remove <index> - remove the waypoint of this index, see /waypoint list for the indicies.");
    }
    
}
