package me.avankziar.ptm.bungee.assistance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utility
{
	//private PaintThemWhite plugin;
	
	/*final public static String 
	PERMBYPASSCOLOR = "scc.channels.bypass.color",
	PERMBYPASSCOMMAND = "scc.channels.bypass.command";*/
	
	public Utility(//PaintThemWhite plugin
			)
	{
		//this.plugin = plugin;
		loadUtility();
	}
	
	public boolean loadUtility()
	{
		return true;
	}
	
	public static String getUUIDFromName(String name)
	{
		String uuid = "error";
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
			//JsonElement element = new JsonParser().parse(in);
			JsonElement element = JsonParser.parseReader(in);
			if(element instanceof JsonNull)
			{
				in.close();
				return uuid;
			}
            JsonObject object = element.getAsJsonObject();
            String uuidraw = object.get("id").toString();
            uuid = uuidraw.substring(1,9)+"-"+uuidraw.substring(9,13)+"-"+uuidraw.substring(13,17)+"-"+uuidraw.substring(17,21)+"-"+uuidraw.substring(21,33);
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return uuid;
	}
    
    public static String getNameFromUUID(String uuid)
	{
		String name = null;
		try
		{
			if(uuid.contains("-")){
				uuid = uuid.replaceAll("-", "");
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
			//JsonElement element = new JsonParser().parse(in);
			JsonElement element = JsonParser.parseReader(in);
			if(element instanceof JsonNull)
			{
				in.close();
				return uuid;
			}
            JsonObject object = element.getAsJsonObject();
            name = object.get("name").toString();
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return name;
	}
	
	public static LocalDateTime deserialisedDateTime(String datetime)
	{
		LocalDateTime dt = LocalDateTime.parse((CharSequence) datetime,
				DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		return dt;
	}
	
	public static String serialisedDateTime(LocalDateTime dt)
	{
		String MM = "";
		int month = 0;
		if(dt.getMonthValue()<10)
		{
			MM+=month;
		}
		MM += dt.getMonthValue();
		String dd = "";
		int day = 0;
		if(dt.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=dt.getDayOfMonth();
		String hh = "";
		int hour = 0;
		if(dt.getHour()<10)
		{
			hh+=hour;
		}
		hh += dt.getHour();
		String mm = "";
		int min = 0;
		if(dt.getMinute()<10)
		{
			mm+=min;
		}
		mm += dt.getMinute();
		String ss = "";
		int sec = 0;
		if(dt.getSecond()<10)
		{
			ss+=sec;
		}
		ss += dt.getSecond();
		return dd+"."+MM+"."+dt.getYear()+" "+hh+":"+mm+":"+ss;
	}
	
	public static LocalDate deserialisedDate(String date)
	{
		LocalDate d = LocalDate.parse((CharSequence) date,
				DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		return d;
	}
	
	public static String serialisedDate(LocalDate d)
	{
		String mm = "";
		int month = 0;
		if(d.getMonthValue()<10)
		{
			mm+=month;
		}
		mm += d.getMonthValue();
		String dd = "";
		int day = 0;
		if(d.getDayOfMonth()<10)
		{
			dd+=day;
		}
		dd +=d.getDayOfMonth();
		return dd+"."+mm+"."+d.getYear();
	}
	
	public static LocalTime deserialisedTime(String date)
	{
		LocalTime d = LocalTime.parse((CharSequence) date,
				DateTimeFormatter.ofPattern("HH:mm:ss"));
		return d;
	}
	
	public static String serialiseTime(LocalTime d)
	{
		String hh = "";
		int hour = 0;
		if(d.getHour()<10)
		{
			hh+=hour;
		}
		hh += d.getHour();
		String mm = "";
		int min = 0;
		if(d.getMinute()<10)
		{
			mm+=min;
		}
		mm +=d.getMinute();
		String ss = "";
		int sec = 0;
		if(d.getSecond()<10)
		{
			ss+=sec;
		}
		ss +=d.getSecond();
		return hh+":"+mm+":"+ss;
	}
	
	public static long timeToLong(LocalTime t)
	{
		long x = 1000L;
		long r = t.getHour()*60*60*x+t.getMinute()*60*x+t.getSecond()*x;
		return r;
	}
}
