package com.paul90317.mcsync;

import com.paul90317.mcsync.command.CreateServer;
import com.paul90317.mcsync.command.ListProfiles;
import com.paul90317.mcsync.command.ShareProfile;
import com.paul90317.mcsync.command.SyncRemote;
import com.paul90317.mcsync.util.Console;

public class Main {
    private static void command(String[] args)throws Exception{
        switch(args[0]){
            case "create-server":
                CreateServer.create(args[1],args[2]);
                break;
            case "share-profile":
                ShareProfile.keepSharing(args[1],Integer.parseInt(args[2]));
                break;
            case "sync-remote":
                SyncRemote.sync(args[1],Integer.parseInt(args[2]));
                break;
            case "list-profiles":
                ListProfiles.list();
                break;
            default:
                throw new Exception("Unrecognized command.");
        }
    }
    public static void main(String[] args) {
        try{
            if(args.length>0){
                command(args);
            }else{
                Main.class.getClassLoader().getResourceAsStream("help.txt").transferTo(System.out);
                var args2 = Console.ReadLine().split(" ");
                command(args2);
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
        }

    }
}