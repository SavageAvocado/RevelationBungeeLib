package net.revelationmc.lib;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.revelationmc.lib.api.RevelationBungeeLib;
import net.revelationmc.lib.listeners.ConnectionListener;
import net.revelationmc.lib.services.uuid.UuidService;
import net.revelationmc.lib.services.uuid.implementation.LocalUuidService;
import net.revelationmc.lib.services.uuid.implementation.MojangUuidService;
import net.revelationmc.lib.storage.AsyncStorageContainer;
import net.revelationmc.lib.storage.implementation.sql.MySqlStorage;

public class RevelationBungeeLibPlugin extends Plugin implements RevelationBungeeLib {
    private static RevelationBungeeLib instance;

    public static RevelationBungeeLib getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The plugin has not been loaded yet!");
        }
        return instance;
    }

    private final MojangUuidService mojangUuidService = new MojangUuidService();

    private AsyncStorageContainer asyncStorageContainer;
    private LocalUuidService localUuidService;

    @Override
    public void onEnable() {
        this.initStorage();
        this.initLocalUuidService();
        this.initListeners();
        instance = this;
    }

    @Override
    public void onDisable() {
        this.asyncStorageContainer.shutdown();
    }

    private void initStorage() {
        this.asyncStorageContainer = new AsyncStorageContainer(new MySqlStorage("", 3306, "", "", ""));
    }

    private void initLocalUuidService() {
        this.localUuidService = new LocalUuidService(this.asyncStorageContainer);
    }

    private void initListeners() {
        final PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerListener(this, new ConnectionListener(this.localUuidService));
    }

    @Override
    public UuidService getMojangUuidService() {
        return this.mojangUuidService;
    }

    @Override
    public UuidService getLocalUuidService() {
        return this.localUuidService;
    }
}
