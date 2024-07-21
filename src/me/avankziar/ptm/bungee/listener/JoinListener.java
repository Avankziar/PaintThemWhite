package me.avankziar.ptm.bungee.listener;


import me.avankziar.ptm.bungee.assistance.ChatApi;
import me.avankziar.ptw.bungee.PaintThemWhite;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener
{
	private PaintThemWhite plugin;
	
	public JoinListener(PaintThemWhite plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(LoginEvent event)
	{
		boolean maintenanceMode = false;
		if(plugin.getYamlHandler().get().get("MaintenanceMode") != null
				&& plugin.getYamlHandler().get().getBoolean("MaintenanceMode"))
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
			PaintThemWhite.log.info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getL().getString("JoinListener.NotOnMaintenanceMode");
			String custompath = plugin.getYamlHandler().get().getString("MaintenanceModeCustomPath");
			if(plugin.getYamlHandler().getL().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getL().getString(custompath);
			}
			event.setCancelReason(ChatApi.tctl(message));
			event.setCancelled(true);
			return;
		}
		boolean whitelist = false;
		if(plugin.getYamlHandler().get().get("Whitelist") != null
				&& plugin.getYamlHandler().get().getBoolean("Whitelist"))
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
			PaintThemWhite.log.info(uuid+" try to login!");
			String message = plugin.getYamlHandler().getL().getString("JoinListener.NotWhitelisted");
			String custompath = plugin.getYamlHandler().get().getString("WhitelistCustomPath");
			if(plugin.getYamlHandler().getL().get(custompath, null) != null)
			{
				message = plugin.getYamlHandler().getL().getString(custompath);
			}
			event.setCancelReason(ChatApi.tctl(message));
			event.setCancelled(true);
			return;
		}
	}
}
