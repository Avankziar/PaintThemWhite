package me.avankziar.ptw.bungee;

import java.util.logging.Logger;

import me.avankziar.ptm.bungee.assistance.Utility;
import me.avankziar.ptm.bungee.commands.Maintenancemode;
import me.avankziar.ptm.bungee.commands.PTW;
import me.avankziar.ptm.bungee.commands.WhiteList;
import me.avankziar.ptm.bungee.database.YamlHandler;
import me.avankziar.ptm.bungee.listener.JoinListener;
import me.avankziar.ptm.bungee.metrics.Metrics;
import net.md_5.bungee.api.plugin.Plugin;

public class PaintThemWhite extends Plugin
{
	public static Logger log;
	private static PaintThemWhite plugin;
	private YamlHandler yamlHandler;
	private Utility utility;
	
	public void onEnable() 
	{
		plugin = this;
		log = getLogger();
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PTW
		log.info(" ██████╗ ████████╗██╗    ██╗ | API-Version: "+plugin.getDescription().getVersion());
		log.info(" ██╔══██╗╚══██╔══╝██║    ██║ | Author: "+plugin.getDescription().getAuthor());
		log.info(" ██████╔╝   ██║   ██║ █╗ ██║ | Plugin Website: https://www.spigotmc.org/resources/paint-them-white.60596/");
		log.info(" ██╔═══╝    ██║   ██║███╗██║ | Depend Plugins: "+plugin.getDescription().getDepends().toString());
		log.info(" ██║        ██║   ╚███╔███╔╝ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepends().toString());
		log.info(" ╚═╝        ╚═╝    ╚══╝╚══╝  | Have Fun^^");
		yamlHandler = new YamlHandler(this);
		utility = new Utility();
		if(yamlHandler.get().get("MaintenanceMode") != null)
		{
			if(yamlHandler.get().getBoolean("MaintenanceMode"))
			{
				log.info("Server is in maintenance mode");
			}
		}
		
		if(yamlHandler.get().get("Whitelist") != null)
		{
			if(yamlHandler.get().getBoolean("Whitelist"))
			{
				log.info("Whitelist is active");
			}
		}
		
		getProxy().getPluginManager().registerCommand(this, new PTW(this));
		getProxy().getPluginManager().registerCommand(this, new WhiteList(this));
		getProxy().getPluginManager().registerCommand(this, new Maintenancemode(this));
		
		this.getProxy().getPluginManager().registerListener(this, new JoinListener(this));
		
		setupBstats();
	}
	
	public static PaintThemWhite getPlugin() 
	{
		return plugin;
	}
	
	public YamlHandler getYamlHandler()
	{
		return yamlHandler;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(!utility.loadUtility())
		{
			return false;
		}
		return true;
	}
	
	public void setupBstats()
	{
		int pluginId = 7961;
        new Metrics(this, pluginId);
	}
}
