package me.avankziar.ptw.bungee.commands;

import me.avankziar.ptw.bungee.PTW;
import me.avankziar.ptw.bungee.assistance.ChatApiOld;
import me.avankziar.ptw.general.assistance.Utility;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhiteListCmdExecutor extends Command
{
	private PTW plugin;
	
	public WhiteListCmdExecutor(PTW plugin)
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
    		p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.list.msg1")+"none"));
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
    			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.list.msg1")+list));
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
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			plugin.getYamlHandler().getWH().set(u, args[1]);
    			saveWH();
    			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.add.msg2").replaceAll("%p%", args[1])));
    			return;
    		} else if(args[0].equalsIgnoreCase("remove"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			if(plugin.getYamlHandler().getWH().getString(u)==null)
    			{
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.remove.msg2")));
    				return;
    			} else
    			{
    				plugin.getYamlHandler().getWH().set(u, null);
    				saveWH();
        			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.remove.msg3").replaceAll("%p%", args[1])));
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
        		plugin.getYamlHandler().getConfig().set("Whitelist", true);
    			save();
    			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", false);
    			save();
    			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg3")));
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
    			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("Whitelist", false);
    			save();
    			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdWhitelist.msg3")));
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
