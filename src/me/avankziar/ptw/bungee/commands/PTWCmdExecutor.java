package me.avankziar.ptw.bungee.commands;

import me.avankziar.ptw.bungee.PTW;
import me.avankziar.ptw.bungee.assistance.ChatApiOld;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PTWCmdExecutor extends Command
{
	private PTW plugin;
	
	public PTWCmdExecutor(PTW plugin)
	{
		super("ptwreload", null, "paintthemwhitereload");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if(sender instanceof ProxiedPlayer)
		{
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if(!player.hasPermission("ptw.cmd.reload"))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
	    		return;
			}
			if(plugin.reload())
			{
				///Yaml Datein wurden neugeladen.
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PtwReload.Success")));
				return;
			} else
			{
				///Es wurde ein Fehler gefunden! Siehe Konsole!
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PtwReload.Error")));
				return;
			}
		}
	}
}
