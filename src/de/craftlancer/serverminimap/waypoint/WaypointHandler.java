package de.craftlancer.serverminimap.waypoint;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.map.MapCursor;

import de.craftlancer.serverminimap.ExtraCursor;
import de.craftlancer.serverminimap.ServerMinimap;
import de.craftlancer.serverminimap.event.MinimapExtraCursorEvent;

public class WaypointHandler implements Listener
{
    private ServerMinimap plugin;
    private Map<String, List<ExtraCursor>> waypoints = new HashMap<String, List<ExtraCursor>>();
    
    public WaypointHandler(ServerMinimap plugin)
    {
        this.plugin = plugin;
    }
    
    public boolean addWaypoint(String player, int x, int z, String world)
    {
        if (!waypoints.containsKey(player))
            waypoints.put(player, new ArrayList<ExtraCursor>());
        
        return waypoints.get(player).add(new ExtraCursor(x, z, true, MapCursor.Type.WHITE_CROSS, (byte) 0, world));
    }
    
    public List<ExtraCursor> getWaypoints(String player)
    {
        return waypoints.get(player);
    }
    
    public boolean addWaypoint(Player p)
    {
        return addWaypoint(p.getName(), p.getLocation());
    }
    
    public boolean addWaypoint(String player, Location loc)
    {
        return addWaypoint(player, loc.getBlockX(), loc.getBlockZ(), loc.getWorld().getName());
    }
    
    public boolean addWaypoint(String player, String loc)
    {
        String[] arr = loc.split(" ");
        
        if (arr.length != 3)
            return false;
        
        int x;
        int z;
        
        try
        {
            x = Integer.parseInt(arr[0]);
            z = Integer.parseInt(arr[1]);
        }
        catch (NumberFormatException e)
        {
            plugin.getLogger().severe(arr[0] + " and/or " + arr[1] + " is not a number!");
            return false;
        }
        
        return addWaypoint(player, x, z, arr[2]);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onMinimapExtraCursor(MinimapExtraCursorEvent e)
    {
        e.getCursors().addAll(getWaypoints(e.getPlayer().getName()));
    }
    
    public void load()
    {
        if (plugin.getConfig().getBoolean("useMySQL", false))
            loadMySQL();
        else
            loadFile();
    }
    
    private void loadFile()
    {
        File file = new File(plugin.getDataFolder(), "waypoints.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        for (String key : config.getKeys(false))
            for (String value : config.getStringList(key))
                if (!addWaypoint(key, value))
                    plugin.getLogger().warning("Invalid location string: " + value);
    }
    
    private void loadMySQL()
    {
        // TODO Auto-generated method stub
        
    }
    
    public boolean removeWaypoint(String name, int index)
    {
        return getWaypoints(name).remove(index) != null;
    }
    
    public ExtraCursor getWaypoint(String name, int index)
    {
        return getWaypoints(name).get(index);
    }
}
