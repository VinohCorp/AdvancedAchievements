package com.hm.achievement.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.hm.achievement.AdvancedAchievements;

public class AchieveTradeAnvilListener implements Listener {

	private AdvancedAchievements plugin;

	public AchieveTradeAnvilListener(AdvancedAchievements plugin) {

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClickEvent(InventoryClickEvent event) {

		if (!(event.getRawSlot() == 2) || event.getCurrentItem().getType().name().equals("AIR"))
			return;
		Player player = (Player) event.getWhoClicked();
		if (!player.hasPermission("achievement.get") || plugin.isRestrictCreative()
				&& player.getGameMode() == GameMode.CREATIVE || plugin.isInExludedWorld(player))
			return;
		String configAchievement = "";

		if (event.getInventory().getType().name().equals("MERCHANT")) {

			Integer trades = plugin.getDb().registerTrade(player);
			configAchievement = "Trades." + trades;

		} else if (event.getInventory().getType().name().equals("ANVIL")) {
			Integer anvils = plugin.getDb().registerAnvil(player);
			configAchievement = "AnvilsUsed." + anvils;
		}

		else
			return;

		if (plugin.getReward().checkAchievement(configAchievement)) {

			plugin.getAchievementDisplay().displayAchievement(player, configAchievement);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			plugin.getDb().registerAchievement(player, plugin.getConfig().getString(configAchievement + ".Name"),
					plugin.getConfig().getString(configAchievement + ".Message"), format.format(new Date()));

			plugin.getReward().checkConfig(player, configAchievement);
		}
	}
}
