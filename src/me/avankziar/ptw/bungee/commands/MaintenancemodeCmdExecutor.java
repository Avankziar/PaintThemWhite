package me.avankziar.ptw.bungee.commands;

import me.avankziar.ptw.bungee.PTW;
import me.avankziar.ptw.bungee.assistance.ChatApiOld;
import me.avankziar.ptw.general.assistance.Utility;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MaintenancemodeCmdExecutor extends Command
{
	private PTW plugin;

	public MaintenancemodeCmdExecutor(PTW plugin)
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
    		p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.list.msg1")+"none"));
        			return;
    			}
    			for(String key : plugin.getYamlHandler().getMM().getRoutesAsStrings(false))
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
    			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.list.msg1")+list));
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
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			plugin.getYamlHandler().getMM().set(u, args[1]);
    			saveMM();
    			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.add.msg2").replaceAll("%p%", args[1])));
    			return;
    		} else if(args[0].equalsIgnoreCase("remove"))
    		{
    			String u = Utility.getUUIDFromName(args[1]);
    			if(u.equals("error"))
    			{
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
    				return;
    			}
    			if(plugin.getYamlHandler().getMM().getString(u)==null)
    			{
    				p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.remove.msg2")));
    				return;
    			} else
    			{
    				plugin.getYamlHandler().getMM().set(u, null);
    				saveMM();
        			p.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.remove.msg3").replaceAll("%p%", args[1])));
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
        		plugin.getYamlHandler().getConfig().set("MaintenanceMode", true);
        		save();
        		sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("MaintenanceMode", false);
        		save();
        		sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.msg3")));
        	}
    	} else if(args.length == 2)
    	{
    		String selectpath = args[1];
    		plugin.getYamlHandler().getConfig().set("MaintenanceModeCustomPath", selectpath);
			save();
    		if(args[0].equalsIgnoreCase("on"))
        	{
        		plugin.getYamlHandler().getConfig().set("MaintenanceMode", true);
        		save();
        		sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.msg2")));
        	} else
        	{
        		plugin.getYamlHandler().getConfig().set("MaintenanceMode", false);
        		save();
        		sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("Maintenancemode.msg3")));
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
    
    private void saveMM()
    {
    	try
    	{
    		plugin.getYamlHandler().getMM().save();
    	} catch(Exception e)
    	{
    		
    	}
    }
}
