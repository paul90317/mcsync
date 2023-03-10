package com.paul90317.mcsync.command;

import com.paul90317.mcsync.minecraft.LauncherProfiles;
import com.paul90317.mcsync.util.Console;

import java.io.*;
import java.net.URL;
import java.util.AbstractMap;

public class CreateServer {
    public static void create(String profileId, String serverDir) throws Exception{
        if (new File(serverDir).isDirectory()){
            throw new Exception("The server directory existed.");
        }
        LauncherProfiles profiles = new LauncherProfiles();
        String versionId = profiles.getValue(profileId,"lastVersionId");
        String[] vSplit = versionId.split("-");
        if(vSplit.length==3&&vSplit[1].equals("forge")){
            String version = vSplit[0]+"-"+vSplit[2];
            String url = String.format("https://maven.minecraftforge.net/net/minecraftforge/forge/%s/forge-%s-installer.jar",version,version);
            File installer = new File(serverDir, String.format("forge-%s-installer.jar",version));
            installer.getParentFile().mkdirs();
            Console.WriteLine("Downloading mod: "+installer.getName());
            new URL(url).openStream().transferTo(new FileOutputStream(installer));
            Console.WriteLine("Mod downloaded: "+installer.getName());
            Process p = Runtime.getRuntime().exec(String.format("java -jar %s --installServer %s",installer.getPath(),serverDir));
            p.getInputStream().transferTo(System.out);
            p.getErrorStream().transferTo(System.err);
            p.waitFor();
            File modsDir = new File(serverDir,"mods");
            modsDir.mkdirs();
            File gameDir = new File(profiles.getValue(profileId,"gameDir"),"mods");
            modsDir.mkdirs();
            Console.WriteLine("Removing mods...");
            for (File mod: modsDir.listFiles()) {
                mod.delete();
            }
            Console.WriteLine("Copying mods...");
            for(File mod : gameDir.listFiles()){
                if(mod.isFile()){
                    new FileInputStream(mod).transferTo(new FileOutputStream(new File(modsDir.getPath(),mod.getName())));
                }
            }
        }else{
            throw new Exception("Unsupported version id");
        }

        Console.WriteLine("Copying settings...");
        new FileOutputStream(new File(serverDir,"eula.txt")).write("eula=true".getBytes("utf8"));
        CreateServer.class.getClassLoader().getResourceAsStream("local-survival.properties").transferTo(new FileOutputStream(new File(serverDir,"server.properties")));
        Console.WriteLine("Finish");
    }
}
