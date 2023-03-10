# Minecraft Sync
This app is useful if you run a forge mods server, and you want to share the mods with your friend.  

You use `mcsync share-profile` to create a server. this is different from the game server.  

Your friend use `mcsync sync-remote` connect to the server above, and your friend will get the profile named ***Remote Profile*** automatically. Your friend can enter this profile to play in your game server.  

`mcsync create-server` can install a game server with one of your profile, but this don't run the game server.  

`list-profiles` can display all your profile available.

```
Diplay all of your profiles
    mcsync list-profiles

Create a server with your profile
    mcsync create-server <profile id> <server dir>

Share a profile on network
    mcsync share-profile <profile id> <listen ip> <port>

Sync with remote profile
    mcsync sync-remote <remote ip> <port>
```