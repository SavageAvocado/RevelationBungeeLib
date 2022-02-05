package net.revelationmc.lib.storage;

import net.revelationmc.lib.storage.implementation.StorageImplementation;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncStorageContainer {
    private static final ExecutorService storageExecutor = Executors.newCachedThreadPool();

    private final StorageImplementation implementation;

    public AsyncStorageContainer(StorageImplementation implementation) {
        this.implementation = implementation;
    }

    public void shutdown() {
        this.implementation.shutdown();
    }

    public CompletableFuture<Void> updateLocalUuidCache(UUID uuid, String username) {
        return CompletableFuture.runAsync(() -> this.implementation.updateLocalUuidCache(uuid, username));
    }

    public CompletableFuture<String> getUsername(UUID uuid) {
        return this.makeAsyncFuture(() -> this.implementation.getUsername(uuid));
    }

    public CompletableFuture<UUID> getUniqueId(String username) {
        return this.makeAsyncFuture(() -> this.implementation.getUniqueId(username));
    }

    private <T> CompletableFuture<T> makeAsyncFuture(Callable<T> callable) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        storageExecutor.submit(new LocalFutureRunner<>(future, callable));
        return future;
    }

    private record LocalFutureRunner<T>(CompletableFuture<T> future,
                                        Callable<T> callable) implements Runnable {
        @Override
        public void run() {
            try {
                this.future.complete(this.callable.call());
            } catch (Exception e) {
                this.future.completeExceptionally(e);
            }
        }
    }
}
