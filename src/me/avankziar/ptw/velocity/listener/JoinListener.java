package me.avankziar.ptw.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent.PreLoginComponentResult;

import me.avankziar.ptw.velocity.PTW;
import me.avankziar.ptw.velocity.assistance.ChatApi;

public class JoinListener
{
	private PTW plugin;
	
	public JoinListener(PTW plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
	public void onLogin(PreLoginEvent event)
	{
		boolean maintenanceMode = false;
		if(plugin.getYamlHandler().getConfig().get("MaintenanceMode") != null
				&& plugin.getYamlHandler().getConfig().getBoolean("MaintenanceMode"))
		{
			maintenanceMode = true;
		}
		if(maintenanceMode)
		{
			String uuid = event.getUniqueId().toString();
			if(plugin.getYamlHandler().getMM().contains(uuid))
			{
				return;
			}
			plugin.getLogger().info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getLang().getString("JoinListener.NotOnMaintenanceMode");
			String custompath = plugin.getYamlHandler().getConfig().getString("MaintenanceModeCustomPath");
			if(plugin.getYamlHandler().getLang().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getLang().getString(custompath);
			}
			event.setResult(PreLoginComponentResult.denied(ChatApi.tl(message)));
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
			String uuid = event.getUniqueId().toString();
			if(plugin.getYamlHandler().getWH().contains(uuid))
			{
				return;
			}
			plugin.getLogger().info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getLang().getString("JoinListener.NotWhitelisted");
			String custompath = plugin.getYamlHandler().getConfig().getString("WhitelistCustomPath");
			if(plugin.getYamlHandler().getLang().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getLang().getString(custompath);
			}
			event.setResult(PreLoginComponentResult.denied(ChatApi.tl(message)));
			return;
		}
	}
}