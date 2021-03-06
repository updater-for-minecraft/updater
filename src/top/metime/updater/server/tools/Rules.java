package top.metime.updater.server.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import top.metime.updater.share.description.Rule;
import top.metime.updater.share.description.Storage;

public class Rules 
{
	private static final String RULES = "./rules.txt";
	
	public final ArrayList<Rule> rules = new ArrayList<>();
	
	public static Rules getInstance()
	{
		return new Rules();
	}
	
	private Rules()
	{
		try
		{
			File file = new File(RULES);
			if(!file.exists())
			{
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				Properties pro = new Properties();
				pro.put("COMMENT_0", "规则填写格式为：    注释=服务端本地路径__客户端远程路径");
				pro.put("COMMENT_1", "例子： /home/user/dir/__./.minecraft/mods/");
				pro.store(writer, "");
				writer.close();
			}
			
			FileReader reader = new FileReader(file);
			Properties pro = new Properties();
			pro.load(reader);
			reader.close();

			Enumeration<?> em = pro.propertyNames();
			while(em.hasMoreElements())
			{
				String key = (String)em.nextElement();
				if(key.trim().length()>0 && !key.contains("COMMENT_"))
				{
					String value = pro.getProperty(key);
					if(value.trim().contains("__"))
					{
						System.out.println(value);
						File local = new File(value.split("__")[0]);//(_SPLIT_)
						String remote = value.split("__")[1];
						Storage.Builder builder = new Storage.Builder(local, remote);
						rules.add(builder.getRule());
					}
				}
			}
		}
		catch(IOException e){e.printStackTrace();}
	}

	
}
