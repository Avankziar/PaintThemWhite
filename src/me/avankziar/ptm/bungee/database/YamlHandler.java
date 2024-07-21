package me.avankziar.ptm.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import me.avankziar.ptw.bungee.PaintThemWhite;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlHandler
{
	private PaintThemWhite plugin;
	private Configuration cfg = null;
	private Configuration com = null;
	private Configuration whl = null;
	private Configuration mam = null;
	private Configuration ara = null;
	private Configuration dut = null;
	private Configuration eng = null;
	private Configuration fre = null;
	private Configuration ger = null;
	private Configuration hin = null;
	private Configuration ita = null;
	private Configuration jap = null;
	private Configuration mad = null;
	private Configuration rus = null;
	private Configuration spa = null;
	private Configuration lang = null;
	private String languages;
	
	public YamlHandler(PaintThemWhite plugin)
	{
		this.plugin = plugin;
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		if(!mkdirGeneralFiles())
		{
			return false;
		}
		if(!loadGeneralFiles())
		{
			return false;
		}
		if(!mkdir())
		{
			return false;
		}
		if(!loadYamls())
		{
			return false;
		}
		languages = cfg.getString("Language", "English");
		initGetL();
		return true;
	}
	 
	public Configuration get()
	{
		return cfg;
	}
	
	public Configuration getCom()
	{
		return com;
	}
	
	public Configuration getL()
	{
		return lang;
	}
	
	public Configuration getWH()
	{
		return whl;
	}
	
	public Configuration getMM()
	{
		return mam;
	}
	
	public void initGetL()
	{
		if(languages.equalsIgnoreCase("Arabic"))
		{
			lang = ara;
		} else if(languages.equalsIgnoreCase("Dutch"))
		{
			lang = dut;
		} else if(languages.equalsIgnoreCase("French"))
		{
			lang = fre;
		} else if(languages.equalsIgnoreCase("German"))
		{
			lang = ger;
		} else if(languages.equalsIgnoreCase("Hindi"))
		{
			lang = hin;
		} else if(languages.equalsIgnoreCase("Italian"))
		{
			lang = ita;
		} else if(languages.equalsIgnoreCase("Japanese"))
		{
			lang = jap;
		} else if(languages.equalsIgnoreCase("Mandarin"))
		{
			lang = mad;
		} else if(languages.equalsIgnoreCase("Russian"))
		{
			lang = rus;
		} else if(languages.equalsIgnoreCase("Spanish"))
		{
			lang = spa;
		} else
		{
			lang = eng;
		}
	}
	
	public boolean mkdirGeneralFiles()
	{
		if (!plugin.getDataFolder().exists())
		{
			 plugin.getDataFolder().mkdir();
		}
		File c = new File(plugin.getDataFolder(), "config.yml");  
	    if (!c.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("config.yml")) 
	    	{   
	    		PaintThemWhite.log.info("Create config.yml...");    
	    		Files.copy(in, c.toPath());
	        } catch (IOException e) 
	    	{
	        	e.printStackTrace();
	        }
	    }
	    File commands = new File(plugin.getDataFolder(), "commands.yml");  
	    if (!commands.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("commands.yml")) 
	    	{       
	    		Files.copy(in, commands.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File whitelist = new File(plugin.getDataFolder(), "whitelist.yml");  
	    if (!whitelist.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("whitelist.yml")) 
	    	{       
	    		Files.copy(in, whitelist.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File maintenancemode = new File(plugin.getDataFolder(), "maintenancemode.yml");  
	    if (!maintenancemode.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("maintenancemode.yml")) 
	    	{       
	    		Files.copy(in, maintenancemode.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
		return true;
	}
	
	private boolean loadGeneralFiles()
	{
		try 
		{
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			com = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "commands.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			whl = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "whitelist.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			mam = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder(), "maintenancemode.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private boolean mkdir()
	{
		File directory = new File(plugin.getDataFolder()+"/Languages/");
		if(!directory.exists())
		{
			directory.mkdir();
		}
		File arabic = new File(directory.toPath().toString(), "arabic.yml");  
	    if (!arabic.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("arabic.yml")) 
	    	{       
	    		Files.copy(in, arabic.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File dutch = new File(directory.toPath().toString(), "dutch.yml");  
	    if (!dutch.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("dutch.yml")) 
	    	{       
	    		Files.copy(in, dutch.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File english = new File(directory.toPath().toString(), "english.yml");  
	    if (!english.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("english.yml")) 
	    	{       
	    		Files.copy(in, english.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File french = new File(directory.toPath().toString(), "french.yml");  
	    if (!french.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("french.yml")) 
	    	{       
	    		Files.copy(in, french.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File german = new File(directory.toPath().toString(), "german.yml");  
	    if (!german.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("german.yml")) 
	    	{       
	    		Files.copy(in, german.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File hindi = new File(directory.toPath().toString(), "hindi.yml");  
	    if (!hindi.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("hindi.yml")) 
	    	{       
	    		Files.copy(in, hindi.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File italian = new File(directory.toPath().toString(), "italian.yml");  
	    if (!italian.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("italian.yml")) 
	    	{       
	    		Files.copy(in, italian.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File japanese = new File(directory.toPath().toString(), "japanese.yml");  
	    if (!japanese.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("japanese.yml")) 
	    	{       
	    		Files.copy(in, japanese.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File mandarin = new File(directory.toPath().toString(), "mandarin.yml");  
	    if (!mandarin.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("mandarin.yml")) 
	    	{       
	    		Files.copy(in, mandarin.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File russian = new File(directory.toPath().toString(), "russian.yml");  
	    if (!russian.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("russian.yml")) 
	    	{       
	    		Files.copy(in, russian.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
	    File spanish = new File(directory.toPath().toString(), "spanish.yml");  
	    if (!spanish.exists()) 
	    {
	    	try (InputStream in = plugin.getResourceAsStream("spanish.yml")) 
	    	{       
	    		Files.copy(in, spanish.toPath());
	    	} catch (IOException e) 
	    	{
	    		e.printStackTrace();
	       	 	return false;
	    	}
	    }
		return true;
	}
	
	public boolean loadYamls()
	{
		try 
		{
			ara = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "arabic.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			dut = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "dutch.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			eng = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "english.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			fre = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "french.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			ger = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "german.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			hin = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "hindi.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			ita = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "italian.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			jap = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "japanese.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			mad = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "mandarin.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			rus = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "russian.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		try 
		{
			spa = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(plugin.getDataFolder()+"/Languages/", "spanish.yml"));
		} catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveConfig() 
	{
		try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class)
			 .save(cfg, new File(plugin.getDataFolder(), "config.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	}
	
	public boolean saveWhitelist() 
	{
		try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class)
			 .save(whl, new File(plugin.getDataFolder(), "whitelist.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	}
	
	public boolean saveMaintenanceMode() 
	{
		try 
		 {
			 ConfigurationProvider.getProvider(YamlConfiguration.class)
			 .save(mam, new File(plugin.getDataFolder(), "maintenancemode.yml"));
		 } catch (IOException e) 
		 {
			 e.printStackTrace();
			 return false;
		 }
		 return true;
	}
}