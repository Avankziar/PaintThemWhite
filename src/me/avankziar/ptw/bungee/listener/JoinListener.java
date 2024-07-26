package me.avankziar.ptw.bungee.listener;


import me.avankziar.ptw.bungee.PTW;
import me.avankziar.ptw.bungee.assistance.ChatApiOld;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener
{
	private PTW plugin;
	
	public JoinListener(PTW plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(LoginEvent event)
	{
		boolean maintenanceMode = false;
		if(plugin.getYamlHandler().getConfig().get("MaintenanceMode") != null
				&& plugin.getYamlHandler().getConfig().getBoolean("MaintenanceMode"))
		{
			maintenanceMode = true;
		}
		if(maintenanceMode)
		{
			String uuid = event.getConnection()
					.getUniqueId().toString();
			if(plugin.getYamlHandler().getMM().contains(uuid))
			{
				return;
			}
			PTW.logger.info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getLang().getString("JoinListener.NotOnMaintenanceMode");
			String custompath = plugin.getYamlHandler().getConfig().getString("MaintenanceModeCustomPath");
			if(plugin.getYamlHandler().getLang().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getLang().getString(custompath);
			}
			event.setReason(ChatApiOld.tctl(message));
			event.setCancelled(true);
			return;
		}
		boolean whitelist = false;
		if(plugin.getYamlHandler().getConfig().get("Whitelist") != null
				&& plugin.getYamlHandler().getConfig().getBoolean("Whitelist"))
		{
			whitelist = true;
		}
		if(whitelist)
		{
			String uuid = event.getConnection()
					.getUniqueId().toString();
			if(plugin.getYamlHandler().getWH().contains(uuid))
			{
				return;
			}
			PTW.logger.info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getLang().getString("JoinListener.NotWhitelisted");
			String custompath = plugin.getYamlHandler().getConfig().getString("WhitelistCustomPath");
			if(plugin.getYamlHandler().getLang().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getLang().getString(custompath);
			}
			event.setReason(ChatApiOld.tctl(message));
			event.setCancelled(true);
			return;
		}
	}
}
