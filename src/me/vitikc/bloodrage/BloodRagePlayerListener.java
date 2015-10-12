package me.vitikc.bloodrage;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Vitikc on 11/Oct/15.
 */
public class BloodRagePlayerListener implements Listener {
    private BloodRage plugin;

    public BloodRagePlayerListener(BloodRage plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    private double damageReduced(EntityEquipment equipment){
        double reduce = 0;
        if (equipment==null){
            return reduce;
        }
        ItemStack helmet = equipment.getHelmet();
        ItemStack boots = equipment.getBoots();
        ItemStack chestplate = equipment.getChestplate();
        ItemStack leggings = equipment.getLeggings();

        if (helmet != null)
        switch (helmet.getType()){
            case LEATHER_HELMET: reduce += 0.04;
                break;
            case GOLD_HELMET: reduce += 0.08;
                break;
            case CHAINMAIL_HELMET: reduce += 0.08;
                break;
            case IRON_HELMET: reduce += 0.08;
                break;
            case DIAMOND_HELMET: reduce += 0.12;
                break;
            default: break;
        }
        if (boots != null)
        switch (boots.getType()){
            case LEATHER_BOOTS: reduce += 0.04;
                break;
            case GOLD_BOOTS: reduce += 0.04;
                break;
            case CHAINMAIL_BOOTS: reduce += 0.04;
                break;
            case IRON_BOOTS: reduce += 0.08;
                break;
            case DIAMOND_BOOTS: reduce += 0.12;
                break;
            default: break;
        }
        if (chestplate != null)
        switch (chestplate.getType()){
            case LEATHER_CHESTPLATE: reduce += 0.12;
                break;
            case GOLD_CHESTPLATE: reduce += 0.20;
                break;
            case CHAINMAIL_CHESTPLATE: reduce += 0.20;
                break;
            case IRON_CHESTPLATE: reduce += 0.24;
                break;
            case DIAMOND_CHESTPLATE: reduce += 0.32;
                break;
            default: break;
        }
        if (leggings != null)
        switch (leggings.getType()){
            case LEATHER_LEGGINGS: reduce += 0.08;
                break;
            case GOLD_LEGGINGS: reduce += 0.12;
                break;
            case CHAINMAIL_LEGGINGS: reduce += 0.16;
                break;
            case IRON_LEGGINGS: reduce += 0.20;
                break;
            case DIAMOND_LEGGINGS: reduce += 0.24;
                break;
            default: break;
        }

        return reduce;
    }
    @EventHandler
    public void onPlayerTakeDamage(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player player = (Player) entity;
            int damage = (int)event.getDamage();
            double playerHealth = player.getHealth();
            double reducedDamage = damageReduced(player.getEquipment());
            double realDamage = damage - damage*reducedDamage;
            if (playerHealth - realDamage + 0.5 <= plugin.rageStartPoint){
                plugin.setRageMode(player.getName(), playerHealth);
            }
        }
    }

}
