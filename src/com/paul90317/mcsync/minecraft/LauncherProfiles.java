package com.paul90317.mcsync.minecraft;

import com.paul90317.mcsync.util.JsonMap;

import java.io.File;
import java.util.AbstractMap;

public class LauncherProfiles extends JsonMap {
    public final File file;
    public LauncherProfiles() throws Exception{
        super();
        file = new File(String.format("%s\\.minecraft\\launcher_profiles.json",System.getenv("APPDATA")));
        super.load(file.getPath());
    }
    public String getValue(String profileId, String key)throws Exception{
        if(get("profiles") instanceof AbstractMap profiles && profiles.get(profileId) instanceof AbstractMap profile){
            if(!profile.containsKey(key)){
                if(key.equals("gameDir"))
                    return file.getParent();
                if(key.equals("lastVersionId"))
                    return "latest-release";
                if(key.equals("name"))
                    return "Untitled Profile";
                return "";
            }else{
                return profile.get(key).toString();
            }
        }else{
            throw new Exception("Can't get the data of the profile: "+profileId+" -> "+key);
        }
    }
    public void saveAndClose() throws Exception{
        super.dump(file.getPath());
    }
}
