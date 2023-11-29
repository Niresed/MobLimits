package me.niresed.moblimits;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MobLimits extends JavaPlugin implements Listener{

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        @NotNull LivingEntity entity = event.getEntity();
        if (event.getBreeder() instanceof Player player) {
            int maxCount = getMaxCount(entity);
            if (maxCount == -1) return;

            Chunk chunk = event.getEntity().getChunk();
            int count = 0;
            for (@NotNull Entity i : chunk.getEntities()) {
                if (i.getType().equals(entity.getType())) {
                    if (++count == maxCount) {
                        entity.remove();
                        player.sendMessage("Достигнута максимальное количество сущностей \"" + translateToRussian(entity.getType().name()) + "\" в чанке");
                        break;
                    }
                }
            }
        }

    }

    public static String translateToRussian(String animal) {
        return switch (animal.toLowerCase()) {
            case "cow" -> "Коровы";
            case "sheep" -> "Овцы";
            case "wolf" -> "Волки";
            case "chicken" -> "Курицы";
            case "pig" -> "Свиньи";
            default -> "Неизвестное животное";
        };
    }

    private int getMaxCount(Entity entity) {
        return switch (entity.getType()) {
            case COW -> getConfig().getInt("max_number_of_cows");
            case SHEEP -> getConfig().getInt("max_number_of_sheep");
            case CHICKEN -> getConfig().getInt("max_number_of_chickens");
            case PIG -> getConfig().getInt("max_number_of_pigs");
            case WOLF ->  getConfig().getInt("max_number_of_wolfs");
            default -> -1;
        };
    }

    @Override
    public void onDisable() {

    }
}
