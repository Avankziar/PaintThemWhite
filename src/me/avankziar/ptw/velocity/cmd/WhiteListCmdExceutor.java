package me.avankziar.ptw.velocity.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import me.avankziar.ptw.general.assistance.Utility;
import me.avankziar.ptw.velocity.PTW;
import me.avankziar.ptw.velocity.assistance.ChatApi;

public class WhiteListCmdExceutor implements SimpleCommand
{
	private PTW plugin;
	
	public WhiteListCmdExceutor(PTW plugin)
	{
		this.plugin = plugin;
	}

    public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
		if (!(sender instanceof Player)) 
		{
			if(args.length == 1 || args.length == 2)
        	{
        		toggleWhitelist(sender, args);
        		return;
        	}
    		return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission("ptw.cmd.whitelist"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length == 1) 
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
    				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.list.msg1")+"none"));
        			return;
    			}
    			for(String key : plugin.getYamlHandler().getWH().getRoutesAsStrings(false))
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
    			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.list.msg1")+list));
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
    				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			plugin.getYamlHandler().getWH().set(u, args[1]);
    			saveWH();
    			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.add.msg2").replaceAll("%p%", args[1])));
    			return;
    		} else if(args[0].equalsIgnoreCase("remove"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			if(plugin.getYamlHandler().getWH().get(u) == null)
    			{
    				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.remove.msg2")));
    				return;
    			} else
    			{
    				plugin.getYamlHandler().getWH().remove(u);
    				saveWH();
        			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.remove.msg3").replaceAll("%p%", args[1])));
        			return;
    			}
    			
    		}
    	}
    }
    
    private void toggleWhitelist(CommandSource sender, String args[])
    {
    	if(args.length == 1)
    	{
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", true);
        		save();
    			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", false);
        		save();
    			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg3")));
        	}
    	} else if(args.length == 2)
    	{
    		String selectpath = args[1];
    		plugin.getYamlHandler().getConfig().set("WhitelistCustomPath", selectpath);
    		save();
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", true);
        		save();
    			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", false);
        		save();
    			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg3")));
        	}
    	}
    }
    
    private void save()
    {
    	try
    	{
    		plugin.getYamlHandler().getConfig().save();
    	} catch(Exception e)
    	{
    		
    	}
    }
    
    private void saveWH()
    {
    	try
    	{
    		plugin.getYamlHandler().getWH().save();
    	} catch(Exception e)
    	{
    		
    	}
    }
}