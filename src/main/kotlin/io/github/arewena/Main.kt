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

    fun MessagetoAll(player: Player, player2: Player, kill : String, namedTextColor: NamedTextColor, tf : Boolean) {
        for (i in this.server.onlinePlayers) {
            i.sendMessage(Component.text("$player(이)가 $player2 을(를) 처치하고 ").append
                (Component.text(kill).color(namedTextColor).decoration(TextDecoration.BOLD, tf).append
                (Component.text("킬 달성!"))))
        }
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
                2 -> MessagetoAll(event.player.killer!!, event.player, "더블", NamedTextColor.YELLOW, false)
                3 -> MessagetoAll(event.player.killer!!, event.player, "트리플", NamedTextColor.YELLOW, false)
                4 -> MessagetoAll(event.player.killer!!, event.player, "쿼드라", NamedTextColor.YELLOW, false)
                5 -> MessagetoAll(event.player.killer!!, event.player, "펜타", NamedTextColor.YELLOW, true)
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

