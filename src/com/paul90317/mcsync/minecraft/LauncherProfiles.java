package com.paul90317.mcsync.minecraft;

import com.paul90317.mcsync.util.JsonMap;

public class LauncherProfiles extends JsonMap {
    public final String filename;

    public LauncherProfiles(String _filename) {
        super();
        filename = _filename;
    }
    public LauncherProfiles() throws Exception{
        super();
        filename = String.format("%s\\.minecraft\\launcher_profiles.json",System.getenv("APPDATA"));
        super.load(filename);
    }
    public void saveAndClose() throws Exception{
        super.dump(filename);
    }
}
