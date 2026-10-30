package com.dsx.virtualkeyboard

data class KeyboardTheme(
    val id: Int,
    val name: String,
    val category: String,
    val backgroundColor: String,
    val keyBackgroundColor: String,
    val textColor: String,
    val accentColor: String,
    val fontStyle: String
)

object ThemeRepository {
    fun getThemes(): List<KeyboardTheme> {
        val list = mutableListOf<KeyboardTheme>()
        var idCounter = 1

        // --- SPORTS THEMES (1-15) ---
        val sports = listOf("Football Turf", "Basketball Court", "Formula 1 Carbon", "Tennis Clay", "Boxing Ring", 
                            "Rugby Field", "Golf Green", "Cricket Pitch", "Athletics Track", "Baseball Diamond", 
                            "Cycling Velodrome", "Swimming Pool", "Ski Slopes", "Motorsport Rally", "Gym Fitness")
        for (name in sports) {
            list.add(KeyboardTheme(idCounter, name, "Sports", "#1A1A1A", "#333333", "#FFFFFF", "#00FF66", "SansSerif"))
            idCounter++
        }

        // --- BUSINESS & PROFESSIONAL THEMES (16-30) ---
        val business = listOf("Executive Slate", "Wall Street Gold", "Corporate Navy", "Minimalist White", "Classic Leather",
                              "Carbon Fiber", "Platinum Grey", "Bronze Executive", "Midnight Banker", "Startup Teal",
                              "Consultant Blue", "Enterprise Charcoal", "Boutique Silver", "Asset Bronze", "Global Indigo")
        for (name in business) {
            list.add(KeyboardTheme(idCounter, name, "Business", "#0F172A", "#1E293B", "#F8FAFC", "#38BDF8", "SansSerif"))
            idCounter++
        }

        // --- NEON & GAMING THEMES (31-45) ---
        val neon = listOf("Cyberpunk Neon", "Matrix Green", "Synthwave Purple", "Laser Red", "Electric Blue",
                          "Acid Lime", "Neon Orange", "Hot Pink", "UV Violet", "Plasma Cyan",
                          "Arcade Yellow", "Glow Mint", "Retro CRT", "Starlight", "Supernova")
        for (name in neon) {
            list.add(KeyboardTheme(idCounter, name, "Neon/Gaming", "#050505", "#121212", "#00FFFF", "#FF007F", "Monospace"))
            idCounter++
        }

        // --- NATURE & VIBES (46-60) ---
        val nature = listOf("Savanna Sunset", "Coffee Plantation", "Rainforest Canopy", "Ocean Deep", "Desert Sand",
                            "Mountain Fog", "Autumn Maple", "Cherry Blossom", "Arctic Glacier", "Volcanic Ash",
                            "Pine Forest", "Canyon Clay", "Tropic Lagoon", "Midnight Aurora", "Golden Wheat")
        for (name in nature) {
            list.add(KeyboardTheme(idCounter, name, "Nature & Vibes", "#1C1917", "#292524", "#FAFAF9", "#F59E0B", "SansSerif"))
            idCounter++
        }

        return list
    }
}
