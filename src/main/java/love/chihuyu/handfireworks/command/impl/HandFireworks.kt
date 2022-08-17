package love.chihuyu.handfireworks.command.impl

import love.chihuyu.handfireworks.HandFireworks.Companion.plugin
import love.chihuyu.handfireworks.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HandFireworks : Command("handfireworks") {

    override fun onCommand(sender: CommandSender, label: String, args: Array<out String>) {
        if (sender !is Player || sender.hasPermission("handfireworks.command.handfireworks")) return

        plugin.config.set("enabled", !plugin.config.getBoolean("enabled"))
        plugin.config.save(plugin.config.currentPath)
    }

    override fun onTabComplete(sender: CommandSender, label: String, args: Array<out String>): List<String>? {
        return emptyList()
    }
}