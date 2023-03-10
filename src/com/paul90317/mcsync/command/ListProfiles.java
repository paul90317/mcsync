package com.paul90317.mcsync.command;

import com.paul90317.mcsync.minecraft.LauncherProfiles;
import com.paul90317.mcsync.util.Console;

import java.util.AbstractMap;

public class ListProfiles {
    public static void list()throws Exception{
        if(!(new LauncherProfiles().get("profiles") instanceof AbstractMap profiles))
            throw new Exception("can't read or parse launcher_profiles.json");
        Console.WriteLine("");
        profiles.forEach((key,value)->{
            if (value instanceof AbstractMap profile) {
                System.out.printf("ID       %s\n",key);
                System.out.printf("Name     %s\n",profile.get("name"));
                System.out.printf("Version  %s\n",profile.get("lastVersionId"));
                System.out.printf("GameDir  %s\n",profile.get("gameDir"));
                Console.WriteLine("");
            }
        });

    }
}
