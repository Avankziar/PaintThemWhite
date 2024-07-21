package me.avankziar.ptm.bungee.commands;

import me.avankziar.ptm.bungee.assistance.ChatApi;
import me.avankziar.ptm.bungee.assistance.Utility;
import me.avankziar.ptw.bungee.PaintThemWhite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Maintenancemode extends Command
{
	private PaintThemWhite plugin;

	public Maintenancemode(PaintThemWhite plugin)
	{
        super("bungeewartungsmodus",null,"bungeemaintenancemode", "bmm");
        this.plugin = plugin;
    }
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		if(args.length == 1 || args.length == 2)
        	{
        		toggleMaintenancemode(sender, args);
        		return;
        	}
    		return;
    	}
    	ProxiedPlayer p = (ProxiedPlayer)sender;
    	if(!p.hasPermission("ptw.cmd.maintenancemode"))
    	{
    		p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg1")));
    		return;
    	}
    	if(args.length == 1) 
    	{
    		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) 
    		{
    			toggleMaintenancemode(sender, args);
    			return;
    		} else if(args[0].equalsIgnoreCase("list"))
    		{
    			String list = "";
    			if(plugin.getYamlHandler().getMM().getKeys().isEmpty())
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.list.msg1")+"none"));
        			return;
    			}
    			for(String key : plugin.getYamlHandler().getMM().getKeys())
    			{
    				String target = plugin.getYamlHandler().getMM().getString(key);
    				if(list.length() < 1)
    				{
    					list += target;
    				} else 
    				{
    					list += ", "+target;
    				}
    			}
    			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.list.msg1")+list));
    			return;
    		}
    	} else if(args.length == 2)
    	{
    		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) 
    		{
    			toggleMaintenancemode(sender, args);
    			return;
    		} else if(args[0].equalsIgnoreCase("add"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg4")));
    				return;
    			}
    			plugin.getYamlHandler().getMM().set(u, args[1]);
    			plugin.getYamlHandler().saveMaintenanceMode();
    			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.add.msg2").replaceAll("%p%", args[1])));
    			return;
    		} else if(args[0].equalsIgnoreCase("remove"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg4")));
    				return;
    			}
    			if(plugin.getYamlHandler().getMM().getString(u)==null)
    			{
    				p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.remove.msg2")));
    				return;
    			} else
    			{
    				plugin.getYamlHandler().getMM().set(u, null);
    				plugin.getYamlHandler().saveMaintenanceMode();
        			p.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.remove.msg3").replaceAll("%p%", args[1])));
        			return;
    			}
    			
    		}
    	}
    }
    
    private void toggleMaintenancemode(CommandSender sender, String[] args)
    {
    	if(args.length == 1)
    	{
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().get().set("MaintenanceMode", true);
        		plugin.getYamlHandler().saveConfig();
        		sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().get().set("MaintenanceMode", false);
        		plugin.getYamlHandler().saveConfig();
        		sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg3")));
        	}
    	} else if(args.length == 2)
    	{
    		String selectpath = args[1];
    		plugin.getYamlHandler().get().set("MaintenanceModeCustomPath", selectpath);
			plugin.getYamlHandler().saveConfig();
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().get().set("MaintenanceMode", true);
        		plugin.getYamlHandler().saveConfig();
        		sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().get().set("MaintenanceMode", false);
        		plugin.getYamlHandler().saveConfig();
        		sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Maintenancemode.msg3")));
        	}
    	}
    	
    }
}
