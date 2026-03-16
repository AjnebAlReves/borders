package net.axay.levelborder.paper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.border.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public class PaperLevelBorderHandler extends LevelBorderHandler<ServerPlayer, WorldBorder, MinecraftServer> {
    private BorderMode mode = BorderMode.OWN;

    @Override
    protected BorderMode getMode() {
        return mode;
    }

    public void setMode(BorderMode mode) {
        this.mode = mode;
        super.setMode(mode);
    }

    @Override
    protected WorldBorder createWorldBorder(ServerPlayer player) {
        var border = new WorldBorder();
        border.world = player.getLevel();
        return border;
    }

    @Override
    protected void setCenter(WorldBorder border, double centerX, double centerZ) {
        border.setCenter(centerX, centerZ);
    }

    @Override
    protected void initBorder(ServerPlayer player, WorldBorder border, double size) {
        border.setSize(size);
        player.getLevel().getWorldBorder().setCenter(border.getCenterX(), border.getCenterZ());
        player.getLevel().getWorldBorder().setSize(size);
    }

    @Override
    protected void interpolateBorder(ServerPlayer player, WorldBorder border, double size, long time) {
        player.getLevel().getWorldBorder().lerpSizeBetween(player.getLevel().getWorldBorder().getSize(), size, time);
    }

    @Override
    protected MinecraftServer getServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    @Override
    protected Collection<ServerPlayer> getPlayers() {
        return getServer().getPlayerList().getPlayers();
    }

    @Override
    protected double getDistance(ServerPlayer player, WorldBorder border) {
        return border.getDistanceToBorder(player.getX(), player.getZ());
    }

    @Override
    protected Pos3i sharedOverworldSpawn() {
        ServerLevel overworld = getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (overworld != null) {
            var pos = overworld.getSharedSpawnPos();
            return new Pos3i(pos.getX(), pos.getY(), pos.getZ());
        }
        return new Pos3i(0, 64, 0);
    }

    @Override
    protected int getTotalExperience(ServerPlayer player) {
        return player.experienceTotal;
    }

    @Override
    protected int getExperienceLevel(ServerPlayer player) {
        return player.experienceLevel;
    }

    @Override
    protected void copyExperience(ServerPlayer player, ServerPlayer other) {
        player.experienceLevel = other.experienceLevel;
        player.experienceProgress = other.experienceProgress;
        player.experienceTotal = other.experienceTotal;
    }

    @Override
    protected UUID getUUID(ServerPlayer player) {
        return player.getUUID();
    }

    @Override
    protected void hurt(ServerPlayer player, float damage) {
        player.hurt(net.minecraft.world.damagesource.DamageSource.GENERIC, damage);
    }
}
