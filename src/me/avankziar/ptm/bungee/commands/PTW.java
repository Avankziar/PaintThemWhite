package me.avankziar.ptm.bungee.commands;

import me.avankziar.ptm.bungee.assistance.ChatApi;
import me.avankziar.ptw.bungee.PaintThemWhite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PTW extends Command
{
	private PaintThemWhite plugin;
	
	public PTW(PaintThemWhite plugin)
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg1")));
	    		return;
			}
			if(plugin.reload())
			{
				///Yaml Datein wurden neugeladen.
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PtwReload.Success")));
				return;
			} else
			{
				///Es wurde ein Fehler gefunden! Siehe Konsole!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PtwReload.Error")));
				return;
			}
		}
	}
}
