package me.avankziar.ptw.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import me.avankziar.ptw.general.database.Language.ISO639_2B;

public class YamlManager
{
	public enum Type
	{
		BUNGEE, SPIGOT, VELO;
	}
	
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private Type type;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	
	/**
	 * The parameter <b>Type</b> declares on which server is this class called.<br>
	 * This ensures to first, create only needed files and if a chatconversion to the old format is needed.
	 * @param type
	 */
	public YamlManager(Type type)
	{
		this.type = type;
		initConfig(type);
		initCommands();
		initLanguage();
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */	
	public void setFileInput(dev.dejvokep.boostedyaml.YamlDocument yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType) throws org.spongepowered.configurate.serialize.SerializationException
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(key.startsWith("#"))
		{
			if(type == Type.BUNGEE)
			{
				//On Bungee dont work comments
				return;
			}
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no actual key are present
				return;
			}
			if(yml.getBlock(k) == null)
			{
				return;
			}
			if(yml.getBlock(k).getComments() != null && !yml.getBlock(k).getComments().isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.getBlock(k).setComments(Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.getBlock(k).setComments((List<String>) stringList);
			}
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, convertMiniMessageToBungee(((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "")));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(convertMiniMessageToBungee(((String) o).replace("\r\n", "")));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private String convertMiniMessageToBungee(String s)
	{
		if(type != Type.BUNGEE)
		{
			//If Server is not Bungee, there is no need to convert.
			return s;
		}
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '<' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+8 < s.length())
				{
					//Hexcolors
					//     i12345678
					//f.e. <#00FF00>
					String rc = s.substring(i, i+8);
					b.append(rc.replace("<#", "&#").replace(">", ""));
					i += 8;
				} else
				{
					//Normal Colors
					String r = null;
					StringBuilder sub = new StringBuilder();
					sub.append(c).append(cc);
					i++;
					for(int j = i+1; j < s.length(); j++)
					{
						i++;
						char jc = s.charAt(j);
						if(jc == '>')
						{
							sub.append(jc);
							switch(sub.toString())
							{
							case "</color>":
							case "</black>":
							case "</dark_blue>":
							case "</dark_green>":
							case "</dark_aqua>":
							case "</dark_red>":
							case "</dark_purple>":
							case "</gold>":
							case "</gray>":
							case "</dark_gray>":
							case "</blue>":
							case "</green>":
							case "</aqua>":
							case "</red>":
							case "</light_purple>":
							case "</yellow>":
							case "</white>":
							case "</obf>":
							case "</obfuscated>":
							case "</b>":
							case "</bold>":
							case "</st>":
							case "</strikethrough>":
							case "</u>":
							case "</underlined>":
							case "</i>":
							case "</em>":
							case "</italic>":
								r = "";
								break;
							case "<black>":
								r = "&0";
								break;
							case "<dark_blue>":
								r = "&1";
								break;
							case "<dark_green>":
								r = "&2";
								break;
							case "<dark_aqua>":
								r = "&3";
								break;
							case "<dark_red>":
								r = "&4";
								break;
							case "<dark_purple>":
								r = "&5";
								break;
							case "<gold>":
								r = "&6";
								break;
							case "<gray>":
								r = "&7";
								break;
							case "<dark_gray>":
								r = "&8";
								break;
							case "<blue>":
								r = "&9";
								break;
							case "<green>":
								r = "&a";
								break;
							case "<aqua>":
								r = "&b";
								break;
							case "<red>":
								r = "&c";
								break;
							case "<light_purple>":
								r = "&d";
								break;
							case "<yellow>":
								r = "&e";
								break;
							case "<white>":
								r = "&f";
								break;
							case "<obf>":
							case "<obfuscated>":
								r = "&k";
								break;
							case "<b>":
							case "<bold>":
								r = "&l";
								break;
							case "<st>":
							case "<strikethrough>":
								r = "&m";
								break;
							case "<u>":
							case "<underlined>":
								r = "&n";
								break;
							case "<i>":
							case "<em>":
							case "<italic>":
								r = "&o";
								break;
							case "<reset>":
								r = "&r";
								break;
							case "<newline>":
								r = "~!~";
								break;
							}
							b.append(r);
							break;
						} else
						{
							//Search for the color.
							sub.append(jc);
						}
					}
				}
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configKeys, "#"+path, o);
	}
	
	public void initConfig(Type type) //INFO:Config
	{
		addConfig("useIFHAdministration",
				new Object[] {
				true},
				new Object[] {
				"Boolean um auf das IFH Interface Administration zugreifen soll.",
				"Wenn 'true' eingegeben ist, aber IFH Administration ist nicht vorhanden, so werden automatisch die eigenen Configwerte genommen.",
				"Boolean to access the IFH Interface Administration.",
				"If 'true' is entered, but IFH Administration is not available, the own config values are automatically used."});
		addConfig("IFHAdministrationPath", 
				new Object[] {
				"scc"},
				new Object[] {
				"",
				"Diese Funktion sorgt dafür, dass das Plugin auf das IFH Interface Administration zugreifen kann.",
				"Das IFH Interface Administration ist eine Zentrale für die Daten von Sprache, Servername und Mysqldaten.",
				"Diese Zentralisierung erlaubt für einfache Änderung/Anpassungen genau dieser Daten.",
				"Sollte das Plugin darauf zugreifen, werden die Werte in der eigenen Config dafür ignoriert.",
				"",
				"This function ensures that the plugin can access the IFH Interface Administration.",
				"The IFH Interface Administration is a central point for the language, server name and mysql data.",
				"This centralization allows for simple changes/adjustments to precisely this data.",
				"If the plugin accesses it, the values in its own config are ignored."});
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"",
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
		if(type == Type.SPIGOT)
		{
			addConfig("Server",
					new Object[] {
					"hub"},
					new Object[] {
					"",
					"Der Server steht für den Namen des Spigotservers, wie er in BungeeCord/Waterfall config.yml unter dem Pfad 'servers' angegeben ist.",
					"Sollte kein BungeeCord/Waterfall oder andere Proxys vorhanden sein oder du nutzt IFH Administration, so kannst du diesen Bereich ignorieren.",
					"",
					"The server stands for the name of the spigot server as specified in BungeeCord/Waterfall config.yml under the path 'servers'.",
					"If no BungeeCord/Waterfall or other proxies are available or you are using IFH Administration, you can ignore this area."});
			configKeys.put("IsBungeeActive"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
		}
		addConfig("Mysql.Status",
				new Object[] {
				false},
				new Object[] {
				"",
				"'Status' ist ein simple Sicherheitsfunktion, damit nicht unnötige Fehler in der Konsole geworfen werden.",
				"Stelle diesen Wert auf 'true', wenn alle Daten korrekt eingetragen wurden.",
				"",
				"'Status' is a simple security function so that unnecessary errors are not thrown in the console.",
				"Set this value to 'true' if all data has been entered correctly."});
		addComments(configKeys, "#Mysql", 
				new Object[] {
				"",
				"Mysql ist ein relationales Open-Source-SQL-Databaseverwaltungssystem, das von Oracle entwickelt und unterstützt wird.",
				"'My' ist ein Namenkürzel und 'SQL' steht für Structured Query Language. Eine Programmsprache mit der man Daten auf einer relationalen Datenbank zugreifen und diese verwalten kann.",
				"Link https://www.mysql.com/de/",
				"Wenn du IFH Administration nutzt, kann du diesen Bereich ignorieren.",
				"",
				"Mysql is an open source relational SQL database management system developed and supported by Oracle.",
				"'My' is a name abbreviation and 'SQL' stands for Structured Query Language. A program language that can be used to access and manage data in a relational database.",
				"Link https://www.mysql.com",
				"If you use IFH Administration, you can ignore this section."});
		addConfig("Mysql.Host",
				new Object[] {
				"127.0.0.1"},
				new Object[] {
				"",
				"Der Host, oder auch die IP. Sie kann aus einer Zahlenkombination oder aus einer Adresse bestehen.",
				"Für den Lokalhost, ist es möglich entweder 127.0.0.1 oder 'localhost' einzugeben. Bedenke, manchmal kann es vorkommen,",
				"das bei gehosteten Server die ServerIp oder Lokalhost möglich ist.",
				"",
				"The host, or IP. It can consist of a number combination or an address.",
				"For the local host, it is possible to enter either 127.0.0.1 or >localhost<.",
				"Please note that sometimes the serverIp or localhost is possible for hosted servers."});
		addConfig("Mysql.Port",
				new Object[] {
				3306},
				new Object[] {
				"",
				"Ein Port oder eine Portnummer ist in Rechnernetzen eine Netzwerkadresse,",
				"mit der das Betriebssystem die Datenpakete eines Transportprotokolls zu einem Prozess zuordnet.",
				"Ein Port für Mysql ist standart gemäß 3306.",
				"",
				"In computer networks, a port or port number ",
				"is a network address with which the operating system assigns the data packets of a transport protocol to a process.",
				"A port for Mysql is standard according to 3306."});
		addConfig("Mysql.DatabaseName",
				new Object[] {
				"mydatabase"},
				new Object[] {
				"",
				"Name der Datenbank in Mysql.",
				"",
				"Name of the database in Mysql."});
		addConfig("Mysql.SSLEnabled",
				new Object[] {
				false},
				new Object[] {
				"",
				"SSL ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"SSL is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.AutoReconnect",
				new Object[] {
				true},
				new Object[] {
				"",
				"AutoReconnect ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"AutoReconnect is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.VerifyServerCertificate",
				new Object[] {
				false},
				new Object[] {
				"",
				"VerifyServerCertificate ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"VerifyServerCertificate is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.User",
				new Object[] {
				"admin"},
				new Object[] {
				"",
				"Der User, welcher auf die Mysql zugreifen soll.",
				"",
				"The user who should access the Mysql."});
		addConfig("Mysql.Password",
				new Object[] {
				"not_0123456789"},
				new Object[] {
				"",
				"Das Passwort des Users, womit er Zugang zu Mysql bekommt.",
				"",
				"The user's password, with which he gets access to Mysql."});
		configKeys.put("Enable.InterfaceHub.Providing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		addConfig("Logging",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, dann wird in der Console alle Nachrichte angezeigt, welche im Chat gesendet werden.",
				"",
				"If so, all messages sent in the chat are displayed in the console."});
		if(type == Type.SPIGOT || type == Type.BUNGEE)
		{
			configKeys.put("Use.Mail"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
			configKeys.put("Mail.UseChannelForMessageParser"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"Global"}));
			configKeys.put("Mail.ConsoleReplacerInSendedMails"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"Console"}));
			configKeys.put("Mail.CCSeperator"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"@"}));
			configKeys.put("Mail.SubjectMessageSeperator"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					"<>"}));
		}
		addConfig("PrivateChannel.UseDynamicColor",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, dann wird wechseln die Farben des Privatchats sich ab. Die neue Farbe bleibt mit dem jeweiligen Spieler während der Session erhalten.",
				"",
				"If true, the colors of the private chat will alternate. The new color remains with the respective player during the session."});
		configKeys.put("PrivateChannel.DynamicColorPerPlayerChat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#F5A9F2",
				"&#F7819F",
				"&#FA58F4",
				"&#FE2E64",
				"&#FF00FF",
				"&#DF013A",
				"&#B404AE",
				"&#8A0829",
				"&#D0A9F5",
				"&#D0A9F5",
				"&#9A2EFE"}));
		addConfig("PermanentChannel.AmountPerPlayer",
				new Object[] {
				1},
				new Object[] {
				"",
				"Anzahl an PermanentChannel der ein Spieler haben darf. Permission müssen trotzdem vorhanden sein.",
				"",
				"Number of permanent channels a player may have. Permission must still be available."});
		addConfig("PermanentChannel.InviteCooldown",
				new Object[] {
				60},
				new Object[] {
				"",
				"Anzahl an Sekunden, welche man warten muss, bevor man eine weitere Person oder die gleiche mehrmals in den permanenten Channel einladen kann.",
				"",
				"Number of seconds you have to wait before you can invite another person or the same person several times to the permanent channel."});
		addConfig("TemporaryChannel.InviteCooldown",
				new Object[] {
				60},
				new Object[] {
				"",
				"Anzahl an Sekunden, welche man warten muss, bevor man eine weitere Person oder die gleiche mehrmals in den temporären Channel einladen kann.",
				"",
				"Number of seconds you have to wait before you can invite another person or the same person several times to the temporary channel."});
		addConfig("BroadCast.UsingChannel",
				new Object[] {
				"Global"},
				new Object[] {
				"",
				"Name des Channels, welche als Parser für den Broadcast genutzt werden soll.",
				"",
				"Name of the channel to be used as a parser for the broadcast."});
		addConfig("Mute.SendGlobal",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird die Nachricht, dass ein Spieler gemutet wurde, an alle anwesenden Spieler gesendet.",
				"",
				"If true, the message that a player has been muted is sent to all players present."});
		addConfig("MsgSoundUsage",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird beim erhalt einer Nachricht ein Sound abgespielt.",
				"",
				"If true, a sound is played when a message is received."});
		addConfig("JoinMessageDefaultValue",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, werden alle neuen Spieler automatisch angezeigt, wenn ein Spieler den Server betritt oder verlässt. Durch den Spieler selbst änderbar.",
				"",
				"If true, all new players are automatically displayed when a player enters or leaves the server. Can be changed by the player himself."});
		addConfig("CleanUp.RunAutomaticByRestart",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird der Aufräumtask bei (Neu)Starten ausgeführt.",
				"",
				"If true, the cleanup task is executed on (re)startup."});
		addConfig("CleanUp.DeletePlayerWhichJoinIsOlderThanDays",
				new Object[] {
				120},
				new Object[] {
				"",
				"Löscht einen Spieler nach x Tagen ohne Aktivität.",
				"",
				"Deletes a player after x days without activity."});
		if(type == Type.SPIGOT || type == Type.BUNGEE)
		{
			configKeys.put("CleanUp.DeleteReadedMailWhichIsOlderThanDays"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					365}));
		}		
		configKeys.put("ChatReplacer.Command.RunCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd|/"}));
		addComments(configKeys, "ChatReplacer",
				new Object[] {
				"",
				"ChatReplacer sind eine Form von SCC um in den Chat Formate hinzuzufügen, die vorher nicht so gewollt oder möglich waren.",
				"Um ChatReplacer nutzen zu können, muss im Channel dieser auch aktiv sein oder man muss eine BypassPerm besitzten.",
				"Alle ChatReplacer werden durch Start-Symbole angefangen. Manche brauchen auch End-Symbole. Der mittlere Teil ist die benutzerdefinierte Angabe.",
				"Bei korrekter Eingabe, wandelt SCC dies in die entsprechende um.",
				"",
				"ChatReplacers are a form of SCC to add formats to the chat that were not wanted or possible before.",
				"To be able to use ChatReplacer, it must also be active in the channel or you must have a BypassPerm.",
				"All ChatReplacers are started by start symbols. Some also need end symbols. The middle part is the user-defined specification.",
				"If the input is correct, SCC converts this into the corresponding."});
		configKeys.put("ChatReplacer.Command.SuggestCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd/"}));
		configKeys.put("ChatReplacer.Command.RunCommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<yellow>ClickCmd: "}));
		configKeys.put("ChatReplacer.Command.RunCommandEndReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>]"}));
		configKeys.put("ChatReplacer.Command.SuggestCommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<white>ClickCmd: "}));
		configKeys.put("ChatReplacer.Command.SuggestCommandEndReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>]"}));
		configKeys.put("ChatReplacer.Command.SpaceReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"+"}));
		configKeys.put("ChatReplacer.Item.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<item"}));
		configKeys.put("ChatReplacer.Item.Seperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Item.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				">"}));
		configKeys.put("ChatReplacer.Book.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<book"}));
		configKeys.put("ChatReplacer.Book.Seperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Book.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				">"}));
		configKeys.put("ChatReplacer.Emoji.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Emoji.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Mention.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@@"}));
		configKeys.put("ChatReplacer.Mention.Color"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_red>"}));
		configKeys.put("ChatReplacer.Mention.SoundEnum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		configKeys.put("ChatReplacer.Position.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<pos>"}));
		configKeys.put("ChatReplacer.Position.Replace"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<blue>%server% <light_purple>%world% <yellow>%x% %y% %z%<gray>]"}));
		configKeys.put("ChatReplacer.NewLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~!~"}));
		configKeys.put("Gui.ActiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<green>✔"}));
		configKeys.put("Gui.DeactiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<red>✖"}));
		configKeys.put("Gui.Channels.RowAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				6}));
		configKeys.put("GuiList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"CHANNELS", "DUMMY"}));
		
	}
	
	//INFO:Commands
	public void initCommands()
	{
		comBypass();
		commandsInput("scc", "scc", "scc.cmd.scc.scc", 
				"/scc [pagenumber]", "/scc ",
				"<red>/scc [Seitenzahl] <white>| Infoseite für alle Befehle.",
				"<red>/scc [pagenumber] <white>| Info page for all commands.");
		commandsInput("scceditor", "scceditor", "scc.cmd.scceditor", 
				"/scceditor", "/scceditor ",
				"<red>/scceditor [true|false] [Spielername] <white>| ChatEditor Toggle.",
				"<red>/scceditor [true|false] [playername] <white>| ChatEditor toggle.");
		commandsInput("clch", "clch", "scc.cmd.clch", 
				"/clch [pagenumber]", "/clch ",
				"<red>/clch <Spielername> <Zahl> <Nachricht...> <white>| Sendet einen klickbaren Chat für den Spieler. Geeignet für Citizen / Denizen Plugin.",
				"<red>/clch <player name> <number> <message...> <white>| Sends a clickable chat for the player. Suitable for Citizen / Denizen plugin.");
		commandsInput("msg", "msg", "scc.cmd.msg", 
				"/msg <player> <message...>", "/msg ",
				"<red>/msg <Spielername> <Nachricht> <white>| Schreibt dem Spieler privat. Alle Spieler, welche online sind, werden als Vorschlag angezeigt.",
				"<red>/msg <player name> <message> <white>| Write to the player privately. All players who are online will be displayed as a suggestion.");
		commandsInput("re", "re", "scc.cmd.re", 
				"/re <player> <message...>", "/re ",
				"<red>/re <Spielername> <Nachricht> <white>| Schreibt dem Spieler privat. Alle Spieler mit denen man schon geschrieben hat, werden als Vorschlag angezeigt.",
				"<red>/re <player name> <message> <white>| Write to the player privately. All players with whom you have already written are displayed as suggestions.");
		commandsInput("r", "r", "scc.cmd.r", 
				"/r <message...>", "/r ",
				"<red>/r <message...> <white>| Schreibt dem Spieler, welcher einem selbst zuletzt privat geschrieben hat.",
				"<red>/r <message...> <white>| Write to the player who last wrote to you privately.");
		commandsInput("w", "w", "scc.cmd.w", 
				"/w <player>", "/w ",
				"<red>/w [Spielername] <white>| Consolenbefehl für Privatnachrichten an Spieler.",
				"<red>/w [playername] <white>| Consolecommand for private message to player.");
		String path = "scc_";
		String basePermission = "scc.cmd.scc";
		//INFO:Argument Start
		argumentInput(path+"book", "book", basePermission,
				"/scc book <itemname> [playername]", "/scc book ",
				"<red>/scc book <Itemname> [Spielername] <white>| Öffnet das Buch vom ItemReplacer.",
				"<red>/scc book <Itemname> [playername] <white>| Open the book from ItemReplacer.");
		argumentInput(path+"broadcast", "broadcast", basePermission,
				"/scc broadcast <message...>", "/scc broadcast ",
				"<red>/scc broadcast <Nachricht> <white>| Zum Senden einer Broadcast Nachricht. Falls Bungeecord aktiviert ist, kann man auch von Spigot als Console, bungeecordübergreifend dies an alle Spieler senden.",
				"<red>/scc broadcast <message> <white>| To send a broadcast message. If bungeecord is enabled, you can also send this to all players from Spigot as a console, across bungeecords.");
		argumentInput(path+"broadcastserver", "broadcastserver", basePermission,
				"/scc broadcastserver <message...>", "/scc broadcastserver ",
				"<red>/scc broadcastserver <Nachricht> <white>| Zum Senden einer Broadcast Nachricht an alle Spieler, welche auf dem gleichen Server sind.",
				"<red>/scc broadcastserver <message> <white>| To send a broadcast message to all players who are on the same server.");
		argumentInput(path+"broadcastworld", "broadcastworld", basePermission,
				"/scc broadcastworld <message...>", "/scc broadcastserver ",
				"<red>/scc broadcastworld <Nachricht> <white>| Zum Senden einer Broadcast Nachricht an alle Spieler, welche auf der gleichen Welt sind.",
				"<red>/scc broadcastworld <message> <white>| To send a broadcast message to all players who are on the same world.");
		argumentInput(path+"channel", "channel", basePermission,
				"/scc channel <channel>", "/scc channel ",
				"<red>/scc channel <Channelname> <white>| Zum An- & Ausstellen des angegebenen Channels.",
				"<red>/scc channel <channelname> <white>| To turn the specified channel on & off.");
		argumentInput(path+"channelgui", "channelgui", basePermission,
				"/scc channelgui ", "/scc channelgui ",
				"<red>/scc channelgui <white>| Öffnet ein Menü, wo die Channels aus und eingestellt werden können.",
				"<red>/scc channelgui <white>| Opens a menu where the channels can be selected and set.");
		argumentInput(path+"debug", "debug", basePermission,
				"/scc debug ", "/scc debug ",
				"<red>/scc debug <white>| Debugbefehl",
				"<red>/scc debug <white>| Debugcommand");
		argumentInput(path+"ignore", "ignore", basePermission,
				"/scc ignore <playername>", "/scc ignore ",
				"<red>/scc ignore <Spielername> <white>| Zum Einsetzen oder Aufheben des Ignores für den Spieler.",
				"<red>/scc ignore <playername> <white>| To set or remove the ignore for the player.");
		argumentInput(path+"ignorelist", "ignorelist", basePermission,
				"/scc ignorelist [playername]", "/scc ignorelist ",
				"<red>/scc ignorelist [Spielername] <white>| Zum Anzeigen aller Spieler auf der Ignoreliste.",
				"<red>/scc ignorelist [playername] <white>| To show all players on the ignore list.");
		argumentInput(path+"mute", "mute", basePermission,
				"/scc mute <playername> [values...]", "/scc mute ",
				"<red>/scc mute <Spielername> [Werte...] <white>| Stellt den Spieler für die angegebene Zeit stumm. Bei keinem Wert ist es permanent. Mögliche hinzufügbare und kombinierbare Werte sind: (Format x:<Zahl>) y=Jahre, M=Monate, d=Tage, H=Stunden, m=Minuten, s=Sekunden. Z.B. H:2 m:10 bedeutet für 2 Stunden und 10 Minuten gemutet.",
				"<red>/scc mute <playername> [values...] <white>| Mutes the player for the specified time. With no value, it is permanent. Possible addable and combinable values are: (format x:<number>) y=years, M=months, d=days, H=hours, m=minutes, s=seconds. E.g. H:2 m:10 means muted for 2 hours and 10 minutes.");
		argumentInput(path+"performance", "performance", basePermission,
				"/scc performance ", "/scc performance ",
				"<red>/scc performance <white>| Zeigt die MysqlPerformances des Plugins an.",
				"<red>/scc performance <white>| Displays the MysqlPerformances of the plugin.");
		argumentInput(path+"unmute", "unmute", basePermission,
				"/scc unmute <playername> ", "/scc unmute ",
				"<red>/scc unmute <Spielername> <white>| Zum sofortigen entmuten des Spielers.",
				"<red>/scc unmute <playername> <white>| To immediately unmute the player.");
		argumentInput(path+"updateplayer", "updateplayer", basePermission,
				"/scc updateplayer <playername>", "/scc updateplayer ",
				"<red>/scc updateplayer <Spielername> <white>| Updatet die Zugangsrechte des Spielers für alle Channels.",
				"<red>/scc updateplayer <playername> <white>| Updates the player's access rights for all channels.");
		
		argumentInput(path+"option", "option", basePermission,
				"/scc option ", "/scc option ",
				"<red>/scc option <white>| Zwischenbefehl",
				"<red>/scc option <white>| Intermediate command");
		basePermission = "scc.cmd.scc.option";
		argumentInput(path+"option_channel", "channel", basePermission,
				"/scc option channel ", "/scc option channel ",
				"<red>/scc option channel <white>| Aktiviert oder deaktiviert, ob man beim Joinen seine aktiven Channels sieht.",
				"<red>/scc option channel <white>| Enables or disables, whether you can see your active channels when joining.");
		argumentInput(path+"option_join", "join", basePermission,
				"/scc option join", "/scc option join",
				"<red>/scc option join <white>| Aktiviert oder deaktiviert, ob man die Joinnachricht anderer Spieler sieht.",
				"<red>/scc option join <white>| Enables or disables, whether you can see the join message of other players.");
		argumentInput(path+"option_spy", "spy", basePermission,
				"/scc option spy ", "/scc option spy ",
				"<red>/scc option spy <white>| Aktiviert oder deaktiviert, ob man Nachrichten sehen kann, welche einem sonst verborgen wären.",
				"<red>/scc option spy <white>| Enables or disables, whether you can see messages that would otherwise be hidden from you.");
		basePermission = "scc.cmd.scc.item.";
		argumentInput(path+"item", "item", basePermission,
				"/scc item ", "/scc item ",
				"<red>/scc item <white>| Öffnet das Menü, wo man die Items für den Replacer einstellen kann.",
				"<red>/scc item <white>| Opens the menu where you can set the items for the replacer.");
		argumentInput(path+"item_rename", "rename", basePermission,
				"/scc item rename <oldname> <newname> ", "/scc item rename ",
				"<red>/scc item rename <Alter Name> <Neuer Name> <white>| Benennt das Item, welches auf den alten Namen registriert ist, um.",
				"<red>/scc item rename <oldname> <newname> <white>| Renames the item that goes by the old name.");
		argumentInput(path+"item_replacers", "replacers", basePermission,
				"/scc item replacers ", "/scc item replacers ",
				"<red>/scc item replacers <white>| Zeigt alle möglichen Replacer im Chat an, sowie dessen Item als Hovernachricht.",
				"<red>/scc item replacers <white>| Displays all possible replacers in the chat, as well as their item as a hovermessage.");
		//INFO:PermanentChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"pc", "pc", basePermission,
				"/scc pc ", "/scc pc ",
				"<red>/scc pc <white>| Zwischenbefehl",
				"<red>/scc pc <white>| Intermediate command");
		basePermission = "scc.cmd.scc.pc";
		argumentInput(path+"pc_ban", "ban", basePermission,
				"/scc pc ban <channelname> <playername> ", "/scc pc ban ",
				"<red>/scc pc ban <Channelname> <Spielername> <white>| Bannt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc ban <channelname> <playername> <white>| Bans a player from a permanent channel.");
		argumentInput(path+"pc_unban", "unban", basePermission,
				"/scc pc unban <channelname> <playername>", "/scc pc unban ",
				"<red>/scc pc <Channelname> <Spielername> <white>| Entbannt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc <channelname> <playername> <white>| Unbans a player from a permanent channel.");
		argumentInput(path+"pc_changepassword", "changepassword", basePermission,
				"/scc pc changepassword <channelname> <password>", "/scc pc changepassword ",
				"<red>/scc pc changepassword <Channelname> <Passwort> <white>| Ändert das Passwort von einem permanenten Channel.",
				"<red>/scc pc changepassword <channelname> <password> <white>| Changes the password of a permanent channel.");
		argumentInput(path+"pc_channels", "channels", basePermission,
				"/scc pc channels <channel> ", "/scc pc channels ",
				"<red>/scc pc channels <Channel> <white>| Zeigt alle Channels an mit Infobefehl.",
				"<red>/scc pc channels <channel> <white>| Shows all channels with info command.");
		argumentInput(path+"pc_chatcolor", "chatcolor", basePermission,
				"/scc pc chatcolor <channelname> <color> ", "/scc pc chatcolor ",
				"<red>/scc pc chatcolor <Channelname> <Farbe> <white>| Ändert die Farbe des permanenten Channel für den Chat.",
				"<red>/scc pc chatcolor <channelname> <color> <white>| Changes the color of the permanent channel for the chat.");
		argumentInput(path+"pc_create", "create", basePermission,
				"/scc pc create <channelname> [password] ", "/scc pc create ",
				"<red>/scc pc create <Channelname> [Passwort] <white>| Erstellt einen permanenten Channel. Optional mit Passwort.",
				"<red>/scc pc create <channelname> [password] <white>| Creates a permanent channel. Optionally with password.");
		argumentInput(path+"pc_delete", "delete", basePermission,
				"/scc pc delete <channelname> ", "/scc pc delete ",
				"<red>/scc pc delete <Channelname> <white>| Löscht den Channel.",
				"<red>/scc pc delete <channelname> <white>| Delete the channel.");
		argumentInput(path+"pc_info", "info", basePermission,
				"/scc pc info [channelname] ", "/scc pc info ",
				"<red>/scc pc info [Channelname] <white>| Zeigt alle Infos zum permanenten Channel an.",
				"<red>/scc pc info [channelname] <white>| Displays all info about the permanent channel.");
		argumentInput(path+"pc_inherit", "inherit", basePermission,
				"/scc pc inherit <channelname> <playername> ", "/scc pc inherit ",
				"<red>/scc pc inherit <Channelname> <Spielername> <white>| Lässt den Spieler den Channel als Ersteller beerben.",
				"<red>/scc pc inherit <channelname> <playername> <white>| Lets the player inherit the channel as creator.");
		argumentInput(path+"pc_invite", "invite", basePermission,
				"/scc pc invite <channelname> <playername>", "/scc pc invite ",
				"<red>/scc pc invite <Channelname> <Spielername> <white>| Lädt einen Spieler in den permanenten Channel ein.",
				"<red>/scc pc invite <channelname> <playername> <white>| Invites a player to the permanent Channel.");
		argumentInput(path+"pc_join", "join", basePermission,
				"/scc pc join <channelname> [password] ", "/scc pc join ",
				"<red>/scc pc join <Channelname> [Passwort] <white>| Betritt einen permanenten Channel.",
				"<red>/scc pc join <channelname> [password] <white>| Enter a permanent channel.");
		argumentInput(path+"pc_kick", "kick", basePermission,
				"/scc pc kick <channelname> <playername> ", "/scc pc kick ",
				"<red>/scc pc kick <Channelname> <Spielername> <white>| Kickt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc kick <channelname> <playername> <white>| Kicks a player from a permanent channel.");
		argumentInput(path+"pc_leave", "leave", basePermission,
				"/scc pc leave <channelname> ", "/scc pc leave ",
				"<red>/scc pc leave <Channelname> <white>| Verlässt einen permanenten Channel.",
				"<red>/scc pc leave <channelname> <white>| Leaves a permanent channel.");
		argumentInput(path+"pc_namecolor", "namecolor", basePermission,
				"/scc pc namecolor <channelname> <color> ", "/scc pc namecolor ",
				"<red>/scc pc namecolor <Channelname> <Farbe> <white>| Ändert die Farbe des permanenten Channelpräfix.",
				"<red>/scc pc namecolor <channelname> <color> <white>| Changes the color of the permanent Channelprefix.");
		argumentInput(path+"pc_player", "player", basePermission,
				"/scc pc player [playername] ", "/scc pc player ",
				"<red>/scc pc player [Spielername] <white>| Zeigt alle permanenten Channels an, wo der Spieler beigetreten ist.",
				"<red>/scc pc player [playername] <white>| Displays all permanent channels where the player has joined.");
		argumentInput(path+"pc_rename", "rename", basePermission,
				"/scc pc rename <channelname> <newname>", "/scc pc rename ",
				"<red>/scc pc rename <Channelname> <Neuer Name> <white>| Ändert den Namen des permanenten Channel.",
				"<red>/scc pc rename <channelname> <newname> <white>| Changes the name of the permanent Channel.");
		argumentInput(path+"pc_symbol", "symbol", basePermission,
				"/scc pc symbol <channelname> <symbols>", "/scc pc symbol ",
				"<red>/scc pc symbol <Channelname> <Symbole> <white>| Ändert das Zugangssymbol des Channels.",
				"<red>/scc pc symbol <channelname> <symbols> <white>| Changes the access icon of the channel.");
		argumentInput(path+"pc_vice", "vice", basePermission,
				"/scc pc vice <channelname> <playername> ", "/scc pc vice ",
				"<red>/scc pc vice <Channelname> <Spielername> <white>| Befördert oder degradiert einen Spieler innerhalb des permanenten Channels.",
				"<red>/scc pc vice <channelname> <playername> <white>| Promotes or demotes a player within the permanent Channel.");
		//INFO:TemporaryChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"tc", "temporarychannel", basePermission,
				"/scc tc ", "/scc tc ",
				"<red>/scc tc <white>| Zwischenbefehl",
				"<red>/scc tc <white>| Intermediate command");
		basePermission = "scc.cmd.scc.temporarychannel.";
		argumentInput(path+"tc_ban", "ban", basePermission,
				"/scc tc ban <playername> ", "/scc tc ban ",
				"<red>/scc tc ban <Spielername> <white>| Bannt einen Spieler von einem temporären Channel.",
				"<red>/scc tc ban <playername> <white>| Bans a player from a temporary channel.");
		argumentInput(path+"tc_unban", "unban", basePermission,
				"/scc tc unban <playername> ", "/scc tc unban ",
				"<red>/scc tc unban <Spielername> <white>| Entbannt einen Spieler von einem temporären Channel.",
				"<red>/scc tc unban <playername> <white>| Unbans a player from a temporary channel.");
		argumentInput(path+"tc_changepassword", "changepassword", basePermission,
				"/scc tc changepassword <password> ", "/scc tc changepassword ",
				"<red>/scc tc changepassword <Passwort> <white>| Ändert das Passwort von einem temporären Channel.",
				"<red>/scc tc changepassword <password> <white>| Changes the password of a temporary channel.");
		argumentInput(path+"tc_create", "create", basePermission,
				"/scc tc create <channelname> [password] ", "/scc tc create ",
				"<red>/scc tc create <Channelname> [Passwort] <white>| Erstellt einen temporären Channel. Optional mit Passwort.",
				"<red>/scc tc create <channelname> [password] <white>| Creates a temporary channel. Optionally with password.");
		argumentInput(path+"tc_info", "info", basePermission,
				"/scc tc info ", "/scc tc info ",
				"<red>/scc tc info <white>| Zeigt alle Informationen bezüglich des temporären Channels an.",
				"<red>/scc tc info <white>| Displays all information related to the temporary channel.");
		argumentInput(path+"tc_invite", "invite", basePermission,
				"/scc tc invite <playername> ", "/scc tc invite ",
				"<red>/scc tc invite <Spielername> <white>| Lädt einen Spieler in den eigenen temporären Channel ein.",
				"<red>/scc tc invite <playername> <white>| Invites a player to the own temporary channel.");
		argumentInput(path+"tc_join", "join", basePermission,
				"/scc tc join <channelname> [password] ", "/scc tc join ",
				"<red>/scc tc join <Channelname> [Passwort] <white>| Betritt einen temporären Channel.",
				"<red>/scc tc join <channelname> [password] <white>| Enter a temporary channel.");
		argumentInput(path+"tc_kick", "kick", basePermission,
				"/scc tc kick <playername> ", "/scc tc kick ",
				"<red>/scc tc kick <Spielername> <white>| Kickt einen Spieler von einem temporären Channel.",
				"<red>/scc tc kick <playername> <white>| Kicks a player from a temporary channel.");
		argumentInput(path+"tc_leave", "leave", basePermission,
				"/scc tc leave ", "/scc tc leave ",
				"<red>/scc tc leave <white>| Verlässt einen temporären Channel.",
				"<red>/scc tc leave <white>| Leaves a temporary channel.");
		commandsInput("mail", "mail", "scc.cmd.mail.mail", 
				"/mail [page]", "/mail ",
				"<red>/mail [Seitenzahl] <white>| Zeigt alle ungelesenen Mails mit Klick- und Hovernachrichten.",
				"<red>/mail [pagen] <white>| Shows all unread mails with click and hover events.");
		path = "mail_";
		basePermission = "scc.cmd.mail";
		argumentInput(path+"lastreceivedmails", "lastreceivedmails", basePermission,
				"/mail lastreceivedmails [page] [playername] ", "/mail lastreceivedmails",
				"<red>/mail lastreceivedmails [Seitenzahl] [Spielername] <white>| Zeigt die letzten empfangen Mails an.",
				"<red>/mail lastreceivedmails [page] [playername] <white>| Show the last received mails.");
		argumentInput(path+"lastsendedmails", "lastsendedmails", basePermission,
				"/mail lastsendedmails [page] [playername] ", "/mail lastsendedmails",
				"<red>/mail lastsendedmails [Seitenzahl] [Spielername] <white>| Zeigt die letzten gesendeten Mails.",
				"<red>/mail lastsendedmails [page] [playername] <white>| Show the last sended mails.");
		argumentInput(path+"forward", "forward", basePermission,
				"/mail forward <id> ", "/mail forward ",
				"<red>/mail forward <id> <Spielername> <white>| Leitet die Mail an den Spieler weiter.",
				"<red>/mail forward <id> <playername> <white>| Forwards the mail to the player.");
		argumentInput(path+"read", "read", basePermission,
				"/mail read <id> ", "/mail read ",
				"<red>/mail read <id> <white>| Liest die Mail.",
				"<red>/mail read <id> <white>| Read the mail.");
		argumentInput(path+"send", "send", basePermission,
				"/mail send <receiver, multiple seperate with @> <subject...> <seperator> <message...> ", "/mail send ",
				"<red>/mail send <Empfänger, mehrere getrennt mit @> <Betreff...> <Trennzeichen> <Nachricht...> <white>| Schreibt eine Mail.",
				"<red>/mail send <receiver, multiple seperate with @> <subject...> <seperator> <message...> <white>| Write a mail.");
		/*argumentInput(path+"", "", basePermission,
				"/scc ", "/scc ",
				"<red>/scc <white>| ",
				"<red>/scc <white>| ");*/
	}
	
	private void comBypass() //INFO:ComBypass
	{
		String path = "Bypass.";
		String normal = "scc.channel.";
		String by = "scc.bypass.";
		String cus = "scc.custom.";
		commandsKeys.put(path+"Color.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"color"}));
		commandsKeys.put(path+"Color.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"color"}));
		commandsKeys.put(path+"Item.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"item"}));
		commandsKeys.put(path+"Item.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"item"}));
		commandsKeys.put(path+"Book.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"book"}));
		commandsKeys.put(path+"Book.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"book"}));
		commandsKeys.put(path+"RunCommand.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"runcommand"}));
		commandsKeys.put(path+"RunCommand.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"runcommand"}));
		commandsKeys.put(path+"SuggestCommand.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"suggestcommand"}));
		commandsKeys.put(path+"SuggestCommand.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"suggestcommand"}));
		commandsKeys.put(path+"Website.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"website"}));
		commandsKeys.put(path+"Website.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"website"}));
		commandsKeys.put(path+"Emoji.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"emoji"}));
		commandsKeys.put(path+"Emoji.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"emoji"}));
		commandsKeys.put(path+"Mention.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"mention"}));
		commandsKeys.put(path+"Mention.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"mention"}));
		commandsKeys.put(path+"Position.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"position"}));
		commandsKeys.put(path+"Position.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"position"}));
		commandsKeys.put(path+"Sound.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"sound"}));
		commandsKeys.put(path+"Ignore"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"ignore"}));
		commandsKeys.put(path+"OfflineChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"offlinechannel"}));
		commandsKeys.put(path+"PermanentChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"permanentchannel"}));
		commandsKeys.put(path+"BookOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"bookother"}));
		commandsKeys.put(path+"Mail.ReadOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"mail.readother"}));
		commandsKeys.put(path+"WordFilter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"wordfilter"}));
		path = "Custom.";
		commandsKeys.put(path+"ItemReplacerStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				cus+"itemreplacerstorage."}));
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("GeneralError",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Genereller Fehler!",
						"<red>General Error!"}));
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Deine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"<red>Your input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast dafür keine Rechte!",
						"<red>You have no rights for this!"}));
		languageKeys.put("PlayerNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler existiert nicht!",
						"<red>The player does not exist!"}));
		languageKeys.put("PlayerNotOnline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist nicht online!",
						"<red>The player is not online!"}));
		languageKeys.put("NotNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Einer oder einige der Argumente muss eine Zahl sein!",
						"<red>One or some of the arguments must be a number!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick mich!",
						"<yellow>Click me!"}));
		
		languageKeys.put("JoinListener.NotWhitelisted",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu stehst nicht auf der Whiteliste!",
						"&cYou are not on the white list!"}));
		languageKeys.put("JoinListener.NotOnMaintenanceMode",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Server befindet sich zur Zeit im Wartungsmodus!",
						"&cThe server is currently in maintenance mode!"}));
		languageKeys.put("CmdWhitelist.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&6PaintThemWhite&7] &fDie Whiteliste wurde &2angeschaltet&f!",
						"&7[&6PaintThemWhite&7] &fThe whitelist was &2switched on&f!"}));
		languageKeys.put("CmdWhitelist.msg3",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&6PaintThemWhite&7] &fDie Whiteliste wurde &causgeschaltet&f!",
						"&7[&6PaintThemWhite&7] &fThe whitelist has been &cswitched off&f!"}));
		languageKeys.put("CmdWhitelist.msg4",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&6PaintThemWhite&7] &4Unerwarteter Wert! &fDie Whiteliste wurde &causgeschaltet&f!",
						"&7[&6PaintThemWhite&7] &4Unexpected value! &fThe whitelist was &cswitched off&f!"}));
		languageKeys.put("CmdWhitelist.add.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fSpieler &7%p% wurde der Whitelist &2hinzugefügt&f!",
						"&fPlayer &7%p% was added to the whitelist &2&f!"}));
		languageKeys.put("CmdWhitelist.remove.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDieser Spieler ist nicht auf der Whiteliste!",
						"&cThis player is not on the whitelist!"}));
		languageKeys.put("CmdWhitelist.remove.msg3",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fSpieler &7%p% wurde von der Whitelist &cgelöscht&f!",
						"&fPlayer &7%p% has been &deleted from the whitelist&f!"}));
		languageKeys.put("CmdWhitelist.list.msg1",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Auf der Whiteliste sind: &e",
						"&7On the white list are: &e"}));
		languageKeys.put("Maintenancemode.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&6PaintThemWhite&7] &fWartungsmodus wurde &caktiviert!",
						"&7[&6PaintThemWhite&7] &fMaintenance mode was &aactivated!"}));
		languageKeys.put("Maintenancemode.msg3",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&6PaintThemWhite&7] &fWartungsmodus wurde &adeaktiviert!",
						"&7[&6PaintThemWhite&7] &fMaintenance mode has been &cdeactivated!"}));
		languageKeys.put("Maintenancemode.add.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fSpieler &7%p% wurde der Wartungsmodusliste &2hinzugefügt&f!",
						"&fPlayer &7%p% was added to the maintenance mode list&f!"}));
		languageKeys.put("Maintenancemode.remove.msg2",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDieser Spieler ist nicht auf der Wartungsmodusliste!",
						"&cThis player is not on the maintenance mode list!"}));
		languageKeys.put("Maintenancemode.remove.msg3",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fSpieler &7%p% wurde von der Wartungsmodusliste &cgelöscht&f!",
						"&fPlayer &7%p% was deleted from the maintenance mode list&f!"}));
		languageKeys.put("Maintenancemode.list.msg1",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Auf der Wartungsmodusliste sind: &e",
						"&7The maintenance mode list includes: &e"}));
		languageKeys.put("PtwReload.Success",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&aYaml-Dateien & Mysql wurden neu geladen.",
						"&aYaml files & Mysql were reloaded."}));
		languageKeys.put("PtwReload.Error",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs wurde ein Fehler gefunden! Siehe Konsole!",
						"&cAn error was found! See console!"}));
		languageKeys.put("wnull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWhitelist sagt Nein...",
						"&cWhitelist says no..."}));
		languageKeys.put("mnull",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cWartungsmodus sagt Nein...",
						"&cMaintenance mode says no..."}));
	}
}
