package me.avankziar.ptw.bungee;

import java.util.logging.Logger;

import me.avankziar.ptw.bungee.commands.MaintenancemodeCmdExecutor;
import me.avankziar.ptw.bungee.commands.PTWCmdExecutor;
import me.avankziar.ptw.bungee.commands.WhiteListCmdExecutor;
import me.avankziar.ptw.bungee.listener.JoinListener;
import me.avankziar.ptw.bungee.metrics.Metrics;
import me.avankziar.ptw.general.assistance.Utility;
import me.avankziar.ptw.general.database.YamlHandler;
import me.avankziar.ptw.general.database.YamlManager;
import net.md_5.bungee.api.plugin.Plugin;

public class PTW extends Plugin
{
	public static Logger logger;
	private static PTW plugin;
	private YamlHandler yamlHandler;
	public String pluginname = "PaintThemWhite";
	private Utility utility;
	
	public void onEnable() 
	{
		plugin = this;
		logger = getLogger();
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PTW
		logger.info(" ██████╗ ████████╗██╗    ██╗ | API-Version: "+plugin.getDescription().getVersion());
		logger.info(" ██╔══██╗╚══██╔══╝██║    ██║ | Author: "+plugin.getDescription().getAuthor());
		logger.info(" ██████╔╝   ██║   ██║ █╗ ██║ | Plugin Website: https://www.spigotmc.org/resources/paint-them-white.60596/");
		logger.info(" ██╔═══╝    ██║   ██║███╗██║ | Depend Plugins: "+plugin.getDescription().getDepends().toString());
		logger.info(" ██║        ██║   ╚███╔███╔╝ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepends().toString());
		logger.info(" ╚═╝        ╚═╝    ╚══╝╚══╝  | Have Fun^^");
		yamlHandler = new YamlHandler(YamlManager.Type.BUNGEE, pluginname, logger, plugin.getDataFolder().toPath(),
        		null);
		utility = new Utility();
		if(yamlHandler.getConfig().get("MaintenanceMode") != null)
		{
			if(yamlHandler.getConfig().getBoolean("MaintenanceMode"))
			{
				logger.info("Server is in maintenance mode");
			}
		}
		
		if(yamlHandler.getConfig().get("Whitelist") != null)
		{
			if(yamlHandler.getConfig().getBoolean("Whitelist"))
			{
				logger.info("Whitelist is active");
			}
		}
		
		getProxy().getPluginManager().registerCommand(this, new PTWCmdExecutor(this));
		getProxy().getPluginManager().registerCommand(this, new WhiteListCmdExecutor(this));
		getProxy().getPluginManager().registerCommand(this, new MaintenancemodeCmdExecutor(this));
		
		this.getProxy().getPluginManager().registerListener(this, new JoinListener(this));
		
		setupBstats();
	}
	
	public static PTW getPlugin() 
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
		if(!yamlHandler.loadYamlHandler(YamlManager.Type.BUNGEE))
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
