package gg.aquatic.replace

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComponentExtTest {

    @Test
    fun `test recursive placeholder replacement in components`() {
        val original = Component.text("Hello %user%!")
            .hoverEvent(HoverEvent.showText(Component.text("Click here, %user%")))

        val replaced = original.replacePlaceholders { "Larkyy" }

        // Verify main text
        assertEquals("Hello Larkyy!", (replaced as TextComponent).content())

        // Verify hover text
        val hover = replaced.style().hoverEvent()?.value() as TextComponent
        assertEquals("Click here, Larkyy", hover.content())
    }

    @Test
    fun `test findPlaceholders extracts correct names`() {
        val component = Component.text("Welcome %player% to %server%!")
        val placeholders = component.findPlaceholders()

        assertEquals(setOf("player", "server"), placeholders)
    }

    @Test
    fun `test cached placeholder replacement uses placeholder names`() {
        val component = Component.text("Welcome %player% to %server%!")
        val replaced = component.replacePlaceholders(
            mapOf(
                "player" to "Larkyy",
                "server" to "Cosmo",
            ),
        )

        assertEquals("Welcome Larkyy to Cosmo!", (replaced as TextComponent).content())
    }

    @Test
    fun `test cached placeholder replacement updates insertion`() {
        val component = Component.text("Open")
            .insertion("profile:%player%")
        val replaced = component.replacePlaceholders(mapOf("player" to "Larkyy"))

        assertEquals("profile:Larkyy", replaced.insertion())
    }
}
