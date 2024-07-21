package me.avankziar.ptm.bungee.commands;

import me.avankziar.ptm.bungee.assistance.ChatApi;
import me.avankziar.ptm.bungee.assistance.Utility;
import me.avankziar.ptw.bungee.PaintThemWhite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhiteList extends Command
{
	private PaintThemWhite plugin;
	
	public WhiteList(PaintThemWhite plugin)
	{
        super("bungeewhitelist",null,"bwl");
        this.plugin = plugin;
    }
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		if(args.length == 1 || args.length == 2)
        	{
        		toggleWhitelist(sender, args);
        		return;
        	}
    		return;
    	}
    	ProxiedPlayer p = (ProxiedPlayer)sender;
    	if(!p.hasPermission("ptw.cmd.whitelist"))
    	{
    		p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg1")));
    		return;
    	}if(args.length == 1) 
    	{
    		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) 
    		{
    			toggleWhitelist(sender, args);
    			return;
    		} else if(args[0].equalsIgnoreCase("list"))
    		{
    			String list = "";
    			if(plugin.getYamlHandler().getWH().getKeys().isEmpty())
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.list.msg1")+"none"));
        			return;
    			}
    			for(String key : plugin.getYamlHandler().getWH().getKeys())
    			{
    				String target = plugin.getYamlHandler().getWH().getString(key);
    				if(list.length() < 1)
    				{
    					list += target;
    				} else 
    				{
    					list += ", "+target;
    				}
    			}
    			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.list.msg1")+list));
    			return;
    		}
    	} else if(args.length == 2)
    	{
    		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) 
    		{
    			toggleWhitelist(sender, args);
    			return;
    		} else if(args[0].equalsIgnoreCase("add"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg5")));
    				return;
    			}
    			plugin.getYamlHandler().getWH().set(u, args[1]);
    			plugin.getYamlHandler().saveWhitelist();
    			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.add.msg2").replaceAll("%p%", args[1])));
    			return;
    		} else if(args[0].equalsIgnoreCase("remove"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg5")));
    				return;
    			}
    			if(plugin.getYamlHandler().getWH().getString(u)==null)
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.remove.msg2")));
    				return;
    			} else
    			{
    				plugin.getYamlHandler().getWH().set(u, null);
    				plugin.getYamlHandler().saveWhitelist();
        			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.remove.msg3").replaceAll("%p%", args[1])));
        			return;
    			}
    			
    		}
    	}
    }
    
    private void toggleWhitelist(CommandSender sender, String args[])
    {
    	if(args.length == 1)
    	{
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().get().set("Whitelist", true);
    			plugin.getYamlHandler().saveConfig();
    			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().get().set("Whitelist", false);
    			plugin.getYamlHandler().saveConfig();
    			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg3")));
        	}
    	} else if(args.length == 2)
    	{
    		String selectpath = args[1];
    		plugin.getYamlHandler().get().set("WhitelistCustomPath", selectpath);
			plugin.getYamlHandler().saveConfig();
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().get().set("Whitelist", true);
    			plugin.getYamlHandler().saveConfig();
    			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().get().set("Whitelist", false);
    			plugin.getYamlHandler().saveConfig();
    			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdWhitelist.msg3")));
        	}
    	}
    }
}
