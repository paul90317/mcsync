package com.paul90317.mcsync.command;

import com.paul90317.mcsync.minecraft.LauncherProfiles;
import com.paul90317.mcsync.util.Console;
import com.paul90317.mcsync.util.NetStream;

import javax.security.auth.callback.LanguageCallback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ShareProfile {
    public static void keepSharing(String profileId, int port)throws Exception{
        NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(e->{
            e.getInetAddresses().asIterator().forEachRemaining(ee ->{
                if(ee instanceof Inet4Address){
                    System.out.printf("Server listening to %s:%d%n", ee.getHostAddress(), port);
                }
            });
        });
        LauncherProfiles launcherProfiles = new LauncherProfiles();
        if(!(launcherProfiles.get("profiles") instanceof AbstractMap<?,?> profiles && profiles.get(profileId) instanceof AbstractMap profile))
            throw new Exception("can't read or parse launcher_profiles.json");
        String versionId = profile.get("lastVersionId").toString();
        ServerSocket server = new ServerSocket(port);
        HashMap<String,File> modsMap = new HashMap<>();
        for(var mod : new File(profile.get("gameDir").toString(),"mods").listFiles()){
            if(mod.isFile()){
                modsMap.put(mod.getName(),mod);
            }
        }
        while(true){
            try{
                System.out.println("Waiting for a client...");
                NetStream client = new NetStream(server.accept());
                System.out.println("Client connected: "+client.socket.getRemoteSocketAddress().toString());
                String data = new String(client.readSized(),"utf8");
                System.out.println("Request get: "+data);
                if(data.equals("modlist")){
                    StringBuffer dataSend = new StringBuffer(versionId);
                    for (Map.Entry<String, File> entry : modsMap.entrySet()) {
                        dataSend.append("&" + entry.getKey());
                    }
                    client.out.write(dataSend.toString().getBytes("utf8"));
                    System.out.println("Modlist sent: "+ dataSend.toString());
                }else{
                    new FileInputStream(modsMap.get(data)).transferTo(client.out);
                    System.out.println("File sent: "+data);
                }
                client.close();
            }catch (Exception ex){
                Console.WriteLine(ex);
                Console.WriteLine("Nothing happen, server continue.");
            }
        }
    }
}
