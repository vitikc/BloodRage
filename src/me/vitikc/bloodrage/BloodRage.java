package me.vitikc.bloodrage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;

/**
 * Created by Vitikc on 11/Oct/15.
 */
public class BloodRage extends JavaPlugin{
    private FileConfiguration config;
    private File configFile;

    private BloodRagePlayerListener playerListener;

    private String MainPermission = "br.all";

    private int speedED = 5;
    private int damageResistanceED = 5;
    private int increaseDamageED = 5;
    private int regenerationED = 5;

    private double regenerationStartPoint = 2.4;
    private double ratio = 6.5;

    public double rageStartPoint = 12;

    public void onEnable(){
        playerListener = new BloodRagePlayerListener(this);
        getLogger().info("Enabled");
        createConfigFile();
        setDefaultConfig();
        loadUserConfig();
    }

    public void onDisable(){
        getLogger().info("Disabled");
    }


    private void createConfigFile(){
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            try {
                config.save(configFile);
                getLogger().info("Generating new config file...");
            } catch (IOException ex) {
                throw new IllegalStateException("Unable to create config file ", ex);
            }
        }
    }
    private void setDefaultConfig(){
        configFile = new File(getDataFolder(), "config.yml");
        if(!configFile.exists()){return;}
        getLogger().info("Setting default config file...");
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("rage_start_point")){
            config.set("rage_start_point", rageStartPoint);
        }
        if(!config.isSet("effects_duration")) {
            config.createSection("effects_duration");
            config.getConfigurationSection("effects_duration").set("speed", speedED);
            config.getConfigurationSection("effects_duration").set("damage_resistance", damageResistanceED);
            config.getConfigurationSection("effects_duration").set("damage_increase", increaseDamageED);
            config.getConfigurationSection("effects_duration").set("regeneration", regenerationED);
        }
        if (!config.isSet("regeneration_start_point")){
            config.set("regeneration_start_point", regenerationStartPoint);
        }
        if (!config.isSet("effects_ratio")){
            config.set("effects_ratio",ratio);
        }
        try{
            config.save(configFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void loadUserConfig(){
        getLogger().info("Loading user config file...");
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        speedED = config.getConfigurationSection("effects_duration").getInt("speed");
        damageResistanceED = config.getConfigurationSection("effects_duration").getInt("damage_resistance");
        increaseDamageED = config.getConfigurationSection("effects_duration").getInt("damage_increase");
        regenerationED = config.getConfigurationSection("effects_duration").getInt("regeneration");
        rageStartPoint = config.getDouble("rage_start_point");
        regenerationStartPoint = config.getDouble("regeneration_start_point");
        ratio = config.getDouble("effects_ratio");
    }
    public void setRageMode(String name, double health){
        Player player = getServer().getPlayer(name);
        if (player == null){
            return;
        }
        double multiplier = (1 / health * ratio);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,speedED * 20,(int)multiplier));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,damageResistanceED * 20,(int)multiplier));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, increaseDamageED * 20, (int) multiplier));
        if (health <= regenerationStartPoint)
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,regenerationED*20,1));
    }
    public void showInfo(String name){
        Player player = getServer().getPlayer(name);
        player.sendMessage(ChatColor.GOLD+"|-----------"+ChatColor.BOLD+"[BloodRage]"+ChatColor.GOLD+"-------------|");
        player.sendMessage(ChatColor.GOLD+" |             Developed by "+ChatColor.DARK_AQUA+ChatColor.BOLD+"Vitikc"+ChatColor.GOLD+"             |");
        player.sendMessage(ChatColor.GOLD+"  |-------------"+ChatColor.BOLD+"[v1.0]"+ChatColor.GOLD+"--------------|");

    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission(MainPermission)||player.isOp()) {
                if (command.getName().equalsIgnoreCase("br")) {
                    if (args.length>0){
                        showInfo(player.getName());
                    } else {
                        setRageMode(player.getName(), player.getHealth());
                        player.sendMessage(ChatColor.DARK_PURPLE+"[BloodRage]Rage effect enabled!!!");
                    }
                }
            } else {
                player.sendMessage("You don't have permission");
            }
        }
        return true;
    }
}
