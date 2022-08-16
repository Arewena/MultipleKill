package io.github.arewena

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask


class Main : JavaPlugin(), Listener {
    private val killPoint = hashMapOf<String, Int>()
    var task: BukkitTask? = null
    var pentaTask: BukkitTask? = null
    fun count(player: Player) {
        task = Bukkit.getScheduler().runTaskLater(this, Runnable {
            killPoint[player.name] = 0
        }, 200L)
    }
    fun pentaCount(player: Player) {
        pentaTask = Bukkit.getScheduler().runTaskLater(this, Runnable {
            killPoint[player.name] = 0
        }, 400L)
    }
    override fun onEnable() { this.server.pluginManager.registerEvents(this, this) }



    @EventHandler
    fun join(event: PlayerJoinEvent) {
        killPoint[event.player.name] = 0
    }

    @EventHandler
    fun quit(event: PlayerQuitEvent) {
        killPoint.remove(event.player.name)
    }

    @EventHandler
    fun kill(event: PlayerDeathEvent) {
        if (event.player.killer is Player) {
            task?.cancel()
            pentaTask?.cancel()
            killPoint.put(event.player.killer!!.name, killPoint.get(event.player.killer!!.name)!! +1)



            when (killPoint[event.player.killer!!.name]) {
                2 -> event.deathMessage(Component.text(event.deathMessage + " 더블 킬!").color(NamedTextColor.YELLOW))
                3 -> event.deathMessage(Component.text(event.deathMessage + " 트리플 킬!").color(NamedTextColor.YELLOW))
                4 -> event.deathMessage(Component.text(event.deathMessage + " 쿼드라 킬!").color(NamedTextColor.YELLOW))
                5 -> event.deathMessage(Component.text(event.deathMessage + " 펜타 킬!").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, true))
            }

            if (killPoint[event.player.killer!!.name]!! < 5) {
                count(event.player.killer!!)
            }
            else if (killPoint[event.player.name]!! == 5) {
                pentaCount(event.player.killer!!)
                killPoint[event.player.killer!!.name] = 0
            }
            else {
                killPoint[event.player.killer!!.name] = 0
            }

        }
    }
}

