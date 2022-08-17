package love.chihuyu.handfireworks

import love.chihuyu.handfireworks.utils.runTaskTimer
import org.bukkit.*
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

class HandFireworks : JavaPlugin(), Listener {

    companion object {
        lateinit var plugin: JavaPlugin
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)
        saveDefaultConfig()
    }

    override fun onDisable() { }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        val damager = event.damager

        if (entity !is Player) return

        if (damager is Firework) event.isCancelled = true
    }

    @EventHandler
    fun onUse(event: PlayerInteractEvent) {
        val FIREWORKS_PROB = 0.13

        val player = event.player
        val action = event.action
        val item = event.item

        if (!player.hasPermission("handfireworks.use")) return
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) return

        val color = when (item?.type) {
            Material.RED_DYE -> Triple(Color.RED, Color.ORANGE, Color.NAVY)
            Material.LIME_DYE -> Triple(Color.LIME, Color.GREEN, Color.OLIVE)
            Material.LIGHT_BLUE_DYE -> Triple(Color.AQUA, Color.BLUE, Color.TEAL)
            Material.PINK_DYE -> Triple(Color.FUCHSIA, Color.PURPLE, Color.WHITE)
            Material.YELLOW_DYE -> Triple(Color.YELLOW, Color.ORANGE, Color.SILVER)
            else -> return
        }

        item.amount--

        var tried = 0
        this.runTaskTimer(0, 1) {
            val randomResult = Math.random() <= FIREWORKS_PROB

            if (randomResult) {
                val entity = player.world.spawn(player.location.apply { y += 2 }, Firework::class.java)
                val meta = entity.fireworkMeta
                val effect = FireworkEffect.builder().trail(true).flicker(true).withColor(color.first).withColor(color.second).withColor(color.third)
                    .build()

                meta.power = 0
                meta.addEffect(effect)
                entity.fireworkMeta = meta
                entity.detonate()
            }

            tried++
            if (tried == 120) {
                cancel()
            }
        }
    }
}