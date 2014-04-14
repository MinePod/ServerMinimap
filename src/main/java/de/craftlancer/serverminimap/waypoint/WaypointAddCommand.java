package de.craftlancer.serverminimap.waypoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.serverminimap.ServerMinimap;

public class WaypointAddCommand extends WaypointSubCommand
{
    
    public WaypointAddCommand(String permission, ServerMinimap plugin)
    {
        super(permission, plugin, false);
    }
    
    @Override
    protected void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!checkSender(sender))
            sender.sendMessage("You don't have permission for this command!");
        else if (args.length < 1)
            help(sender);
        else
        {
            int x;
            int z;
            String world;
            
            if (args.length >= 3)
                try
                {
                    x = Integer.parseInt(args[1]);
                    z = Integer.parseInt(args[2]);
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(args[1] + " or " + args[2] + " are not a number!");
                    return;
                }
            else
            {
                x = ((Player) sender).getLocation().getBlockX();
                z = ((Player) sender).getLocation().getBlockZ();
            }
            
            if (args.length >= 4)
                world = args[3];
            else
                world = ((Player) sender).getWorld().getName();
            
            plugin.getWaypointHandler().addWaypoint(((Player) sender).getUniqueId(), x, z, world);
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage("/waypoint add for waypoint on your location, /waypoint add <x> <z> for remote waypoint.");
    }
}
