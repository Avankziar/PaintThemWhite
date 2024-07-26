package me.avankziar.ptw.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import me.avankziar.ptw.general.assistance.Utility;
import me.avankziar.ptw.general.database.YamlHandler;
import me.avankziar.ptw.general.database.YamlManager;
import me.avankziar.ptw.velocity.cmd.MaintenacemodeCmdExecutor;
import me.avankziar.ptw.velocity.cmd.PTWCmdExecutor;
import me.avankziar.ptw.velocity.cmd.WhiteListCmdExceutor;
import me.avankziar.ptw.velocity.listener.JoinListener;

@Plugin(
	id = "paintthemwhite",
	name = "PaintThemWhite",
	version = "8-4-0",
	url = "https://www.spigotmc.org/resources/paint-them-white.60596/",
	dependencies = {},
	description = "Whitelist and MaintenaceMode",
	authors = {"Avankziar"}
)
public class PTW
{
	private static PTW plugin;
    private final ProxyServer server;
    private Logger logger = null;
    private Path dataDirectory;
    public String pluginname = "PaintThemWhite";
    private YamlHandler yamlHandler;
    private YamlManager yamlManager;
    private Utility utility;
    
    @Inject
    public PTW(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	PTW.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	this.logger = Logger.getLogger("PTW");
    	PluginDescription pd = server.getPluginManager().getPlugin(pluginname.toLowerCase()).get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=BM
		logger.info(" ██████╗ ████████╗██╗    ██╗ | Id: "+pd.getId());
		logger.info(" ██╔══██╗╚══██╔══╝██║    ██║ | Version: "+pd.getVersion().get());
		logger.info(" ██████╔╝   ██║   ██║ █╗ ██║ | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔═══╝    ██║   ██║███╗██║ | Description: "+(pd.getDescription().isPresent() ? pd.getDescription().get() : "/"));
		logger.info(" ██║        ██║   ╚███╔███╔╝ | Plugin Website:"+pd.getUrl().toString());
		logger.info(" ╚═╝        ╚═╝    ╚══╝╚══╝  | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
		
		yamlHandler = new YamlHandler(YamlManager.Type.VELO, pluginname, logger, dataDirectory,
        		null);
        setYamlManager(yamlHandler.getYamlManager());
        utility = new Utility();
        
        setListeners();
        CommandManager cm = getServer().getCommandManager();
		CommandMeta whmeta = cm.metaBuilder("velocitywhitelist").plugin(plugin).aliases("vwl").build();
		cm.register(whmeta, new WhiteListCmdExceutor(plugin));
		CommandMeta ptwmeta = cm.metaBuilder("ptwreload").plugin(plugin).build();
		cm.register(ptwmeta, new PTWCmdExecutor(plugin));
		CommandMeta mmmeta = cm.metaBuilder("velocitymaintenancemode").plugin(plugin).aliases("velocitywartungsmodus", "vmm").build();
		cm.register(mmmeta, new MaintenacemodeCmdExecutor(plugin));
    }
    
    public static PTW getPlugin()
    {
    	return PTW.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Logger getLogger()
    {
    	return logger;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
    }
    
    public YamlHandler getYamlHandler()
    {
    	return yamlHandler;
    }
    
    public YamlManager getYamlManager()
    {
    	return yamlManager;
    }
    
    public void setYamlManager(YamlManager yamlManager)
    {
    	this.yamlManager = yamlManager;
    }
    
    public Utility getUtility()
    {
    	return utility;
    }
    
    public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler(YamlManager.Type.VELO))
		{
			return false;
		}
		if(!utility.loadUtility())
		{
			return false;
		}
		return true;
	}
    
    private void setListeners()
    {
    	server.getEventManager().register(plugin, new JoinListener(plugin));
    }
}