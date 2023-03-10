package com.paul90317.mcsync.command;

import com.paul90317.mcsync.minecraft.LauncherProfiles;
import com.paul90317.mcsync.minecraft.Profile;
import com.paul90317.mcsync.util.Console;
import com.paul90317.mcsync.util.NetStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;

public class SyncRemote {
    public static void sync(String ip, int port)throws Exception{
        Console.WriteLine("Connecting to server...");
        var client = new NetStream(new Socket(ip,port));
        Console.WriteLine("Connected");
        Console.WriteLine("Asking modlist...");
        client.writeSized("modlist".getBytes("utf8"));
        var modList = new String(client.in.readAllBytes(),"utf8").split("&");
        client.close();
        Console.WriteLine("Modlist got:");
        for(var mod : modList){
            Console.WriteLine(mod);
        }
        Console.WriteLine("");
        Profile profile = Profile.mcSync(modList[0]);
        var versionSplit = profile.get("lastVersionId").split("-");
        if(versionSplit.length==3&&versionSplit[1].equals("forge")){
            var version = versionSplit[0]+"-"+versionSplit[2];
            String url = String.format("https://maven.minecraftforge.net/net/minecraftforge/forge/%s/forge-%s-installer.jar",version,version);
            File installer = new File(profile.get("gameDir"), String.format("forge-%s-installer.jar",version));
            installer.getParentFile().mkdirs();
            Console.WriteLine("Downloading mod: "+installer.getName());
            new URL(url).openStream().transferTo(new FileOutputStream(installer));
            Console.WriteLine("Mod downloaded: "+installer.getName());
            Process p = Runtime.getRuntime().exec("java -jar " +installer.getPath());
            p.getInputStream().transferTo(System.out);
            p.getErrorStream().transferTo(System.err);
            p.waitFor();
            var launcherProfiles = new LauncherProfiles();
            if(!(launcherProfiles.get("profiles") instanceof AbstractMap profiles)){
                throw new Exception("can't read or parse launcher_profiles.json.");
            }
            profiles.put("mcsync",profile);
            launcherProfiles.saveAndClose();
            new File(profile.get("gameDir"),"mods").mkdirs();
            for(var mod : new File(profile.get("gameDir"),"mods").listFiles()){
                if(mod.isFile()){
                    mod.delete();
                }
            }
            for(var mod : Arrays.copyOfRange(modList, 1,modList.length) ){
                Console.WriteLine("Downloading mod: "+mod);
                var modStream = new NetStream(new Socket(ip,port));
                modStream.writeSized(mod.getBytes("utf8"));
                modStream.in.transferTo(new FileOutputStream(new File(profile.get("gameDir"),"mods/"+mod)));
                modStream.close();
                Console.WriteLine("Mod downloaded: "+mod);
            }
            Console.WriteLine("Finish");
        }else{
            throw new Exception("Unsupported version id");
        }

    }
}
