package me.avankziar.ptw.velocity.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import me.avankziar.ptw.velocity.PTW;
import me.avankziar.ptw.velocity.assistance.ChatApi;

public class PTWCmdExecutor implements SimpleCommand
{
	private PTW plugin;
	
	public PTWCmdExecutor(PTW plugin)
	{
		this.plugin = plugin;
	}

    public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        //String[] args = invocation.arguments();
        if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(!player.hasPermission("ptw.cmd.reload"))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
	    		return;
			}
			if(plugin.reload())
			{
				///Yaml Datein wurden neugeladen.
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PtwReload.Success")));
				return;
			} else
			{
				///Es wurde ein Fehler gefunden! Siehe Konsole!
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PtwReload.Error")));
				return;
			}
		}
	}
}
