/**
 * For testing purposes:
 * This class is for creating Test-Data. Initializes when Maven starts.
 */
package at.qe.skeleton.configs;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import at.qe.skeleton.services.ProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    ApplicationRunner initData(ProductService productService) {
        return args -> {
            if (!productService.getAllProducts().isEmpty()) {
                return;
            }

            System.out.println("--- Initializing Test Data for Products ---");

            // QUAK - Dummy Product
            Product p1 = new Product();
            p1.setName("QUAK(e)");
            p1.setDescription("The groundbreaking first-person quacker that quacked a generation. Yellow, squishy, and QUAK.");
            p1.setPrice(9.99);
            p1.setStock(50L);
            p1.setDiscount(0.0);
            p1.setImageUrl("/images/quak(e).png");
            p1.setCategories(Set.of(ProductCategory.PC));
            productService.saveProduct(p1);

            // --- Ljonja's favourites ---
            createProduct(productService,
                    "The Legend of Zelda: Majora's Mask",
                    "An atmospheric masterpiece where Link has only three days to save Termina from a crashing moon.",
                    49.99, 10, ProductCategory.N64,
                    "https://upload.wikimedia.org/wikipedia/en/6/60/The_Legend_of_Zelda_-_Majora%27s_Mask_Box_Art.jpg"
            );
            createProduct(productService,
                    "LEGO Star Wars: The Video Game",
                    "Play through the prequel trilogy in the fun and whimsical LEGO style.",
                    19.99, 25, ProductCategory.PC,
                    "https://upload.wikimedia.org/wikipedia/en/8/81/Legostarwarsthevideogame.jpg"
            );
            createProduct(productService,
                    "The Legend of Zelda: The Wind Waker (Limited Edition)",
                    "Includes the Ocarina of Time / Master Quest Bonus Disc. A GameCube classic with cel-shaded graphics.",
                    89.99, 0, ProductCategory.GAMECUBE,
                    "https://cdn.shopify.com/s/files/1/0656/3607/3714/products/MG_8669.jpg?v=1671004665"
            );
            createProduct(productService,
                    "The Elder Scrolls V: Skyrim",
                    "The legendary open-world RPG. Dragonborn, Fus Ro Dah, and endless adventures awaiting.",
                    14.99, 100, ProductCategory.PS3,
                    "https://upload.wikimedia.org/wikipedia/en/1/15/The_Elder_Scrolls_V_Skyrim_cover.png"
            );
            createProduct(productService,
                    "Garry's Mod",
                    "A physics sandbox. There are no predefined aims or goals. We give you the tools and leave you to play.",
                    9.99, 500, ProductCategory.PC,
                    "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/4000/header.jpg"
            );


            // --- 2. Nintendo Classics ---

            createProduct(productService, "Super Mario 64", "The 3D platformer that defined a genre.", 39.99, 15, ProductCategory.N64, "https://upload.wikimedia.org/wikipedia/en/6/6a/Super_Mario_64_box_cover.jpg");
            createProduct(productService, "Super Metroid", "The definitive action-adventure platformer.", 59.99, 5, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/e/e4/Super_Metroid_box_art.jpg");
            createProduct(productService, "The Legend of Zelda: A Link to the Past", "A timeless adventure in Hyrule.", 45.00, 8, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/2/21/The_Legend_of_Zelda_A_Link_to_the_Past_SNES_Game_Cover.jpg");
            createProduct(productService, "Super Mario Bros.", "The classic that started it all.", 99.99, 2, ProductCategory.NES, "https://upload.wikimedia.org/wikipedia/en/0/03/Super_Mario_Bros._box.png");
            createProduct(productService, "Mario Kart 8 Deluxe", "The ultimate racing experience on Switch.", 59.99, 200, ProductCategory.SWITCH, "https://upload.wikimedia.org/wikipedia/en/b/b5/MarioKart8Boxart.jpg");
            createProduct(productService, "Breath of the Wild", "Open air adventure that reinvented Zelda.", 59.99, 150, ProductCategory.SWITCH, "https://upload.wikimedia.org/wikipedia/en/c/c6/The_Legend_of_Zelda_Breath_of_the_Wild.jpg");
            createProduct(productService, "Super Smash Bros. Melee", "Competitive fighting game phenomenon.", 69.99, 12, ProductCategory.GAMECUBE, "https://upload.wikimedia.org/wikipedia/en/1/1d/Super_Smash_Bros._Melee_Box_Art.png");
            createProduct(productService, "Luigi's Mansion", "Luigi searches for Mario in a haunted mansion.", 35.00, 10, ProductCategory.GAMECUBE, "https://upload.wikimedia.org/wikipedia/en/9/93/Luigis_Mansion_Coverart.png");
            createProduct(productService, "Metroid Prime", "First-person adventure in the depths of Tallon IV.", 29.99, 14, ProductCategory.GAMECUBE, "https://upload.wikimedia.org/wikipedia/en/2/25/Metroid_Prime_box_art.jpg");
            createProduct(productService, "F-Zero GX", "High-speed futuristic racing.", 49.99, 5, ProductCategory.GAMECUBE, "https://upload.wikimedia.org/wikipedia/en/8/87/F-Zero_GX_Coverart.png");
            createProduct(productService, "Star Fox 64", "Do a barrel roll!", 24.99, 20, ProductCategory.N64, "https://upload.wikimedia.org/wikipedia/en/6/63/Star_Fox_64_box_cover.jpg");
            createProduct(productService, "GoldenEye 007", "The seminal console shooter.", 29.99, 10, ProductCategory.N64, "https://upload.wikimedia.org/wikipedia/en/3/36/GoldenEye007_box.jpg");
            createProduct(productService, "Pokemon Red", "Catch them all in Kanto.", 89.99, 4, ProductCategory.GB, "https://upload.wikimedia.org/wikipedia/en/f/f1/Bulbasaur_Pok%C3%A9mon_Red_and_Blue_Box_Art.jpg");
            createProduct(productService, "Tetris", "From Russia with fun.", 19.99, 100, ProductCategory.GB, "https://upload.wikimedia.org/wikipedia/en/2/23/Tetris_Game_Boy_cover_art.png");
            createProduct(productService, "Donkey Kong Country", "Pre-rendered graphics that wowed the world.", 25.00, 15, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/c/c1/Donkey_Kong_Country_SNES_cover.png");
            createProduct(productService, "EarthBound", "A quirky RPG with a cult following.", 299.99, 1, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/1/1f/Earthbound_Box_Art.jpg");
            createProduct(productService, "Super Mario World", "Dinosaur land awaits.", 35.00, 20, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/3/32/Super_Mario_World_Coverart.png");
            createProduct(productService, "Chrono Trigger", "Time traveling RPG masterpiece.", 120.00, 3, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/a/a7/Chrono_Trigger.jpg");
            createProduct(productService, "Fire Emblem: Awakening", "Saved the franchise.", 39.99, 10, ProductCategory.NINTENDO_3DS, "https://upload.wikimedia.org/wikipedia/en/4/40/Fire_Emblem_Awakening_box_art.png");
            createProduct(productService, "Animal Crossing: New Leaf", "Relaxing village life.", 29.99, 15, ProductCategory.NINTENDO_3DS, "https://upload.wikimedia.org/wikipedia/en/8/87/Animal_Crossing_New_Leaf_NA_cover.png");


            // --- 3. PlayStation Ecosystem ---

            createProduct(productService, "Final Fantasy VII", "Cloud Strife vs Sephiroth.", 29.99, 50, ProductCategory.PLAYSTATION_1, "https://upload.wikimedia.org/wikipedia/en/c/c2/Final_Fantasy_VII_Box_Art.jpg");
            createProduct(productService, "Metal Gear Solid", "Tactical Espionage Action.", 34.99, 25, ProductCategory.PLAYSTATION_1, "https://upload.wikimedia.org/wikipedia/en/3/33/Metal_Gear_Solid_cover_art_NA.png");
            createProduct(productService, "Castlevania: SotN", "Metroidvania perfection.", 49.99, 10, ProductCategory.PLAYSTATION_1, "https://upload.wikimedia.org/wikipedia/en/6/6b/Castlevania_SotN_PAL_box_art.jpg");
            createProduct(productService, "Resident Evil 2", "Survival horror in Raccoon City.", 29.99, 20, ProductCategory.PLAYSTATION_1, "https://upload.wikimedia.org/wikipedia/en/7/77/Resident_Evil_2_Box_Art.jpg");
            createProduct(productService, "Grand Theft Auto: San Andreas", "Ah shit, here we go again.", 19.99, 100, ProductCategory.PS2, "https://upload.wikimedia.org/wikipedia/en/c/c4/GTASACover.jpg");
            createProduct(productService, "Shadow of the Colossus", "Epic battles against giants.", 39.99, 15, ProductCategory.PS2, "https://upload.wikimedia.org/wikipedia/en/b/b0/Shadow_of_the_Colossus_PAL_box_art.jpg");
            createProduct(productService, "God of War II", "Kratos seeks revenge.", 15.99, 30, ProductCategory.PS2, "https://upload.wikimedia.org/wikipedia/en/7/7e/God_of_War_II_North_American_Box_Art.png");
            createProduct(productService, "Kingdom Hearts II", "Disney meets Final Fantasy.", 24.99, 40, ProductCategory.PS2, "https://upload.wikimedia.org/wikipedia/en/4/4a/Kingdom_Hearts_II.jpg");
            createProduct(productService, "Persona 5 Royal", "Steal their hearts.", 59.99, 30, ProductCategory.PS4, "https://upload.wikimedia.org/wikipedia/en/b/b0/Persona_5_Royal_cover_art.jpg");
            createProduct(productService, "The Last of Us Part II", "Emotional rollercoaster survival.", 49.99, 25, ProductCategory.PS4, "https://upload.wikimedia.org/wikipedia/en/4/44/The_Last_of_Us_Part_II_cover_art.png");
            createProduct(productService, "Bloodborne", "Fear the old blood.", 19.99, 50, ProductCategory.PS4, "https://upload.wikimedia.org/wikipedia/en/6/68/Bloodborne_Cover_Wallpaper.png");
            createProduct(productService, "Spider-Man: Miles Morales", "Be greater. Be yourself.", 49.99, 35, ProductCategory.PS5, "https://upload.wikimedia.org/wikipedia/en/a/a3/Spider-Man_Miles_Morales.jpeg");
            createProduct(productService, "Demon's Souls", "The remake of the classic.", 69.99, 20, ProductCategory.PS5, "https://upload.wikimedia.org/wikipedia/en/5/5e/Demon%27s_Souls_2020_cover_art.jpg");
            createProduct(productService, "Uncharted 2: Among Thieves", "Drake's greatest adventure.", 14.99, 40, ProductCategory.PS3, "https://upload.wikimedia.org/wikipedia/en/4/47/Uncharted_2_Among_Thieves_cover_art.jpg");
            createProduct(productService, "Metal Gear Solid 4", "Old Snake's final mission.", 19.99, 15, ProductCategory.PS3, "https://upload.wikimedia.org/wikipedia/en/1/1d/Metal_Gear_Solid_4_European_Box_Art.jpg");


            // --- 4. Xbox Ecosystem ---

            createProduct(productService, "Halo: Combat Evolved", "Master Chief's debut.", 19.99, 50, ProductCategory.XBOX, "https://upload.wikimedia.org/wikipedia/en/8/80/Halo_-_Combat_Evolved_Coverart.png");
            createProduct(productService, "Halo 3", "Finish the fight.", 14.99, 60, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/b/b4/Halo_3_final_boxshot.JPG");
            createProduct(productService, "Gears of War", "Cover based shooter revolution.", 9.99, 45, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/8/87/Gears_of_War_cover_art.jpg");
            createProduct(productService, "Mass Effect", "Space Opera RPG.", 14.99, 30, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/e/e8/MassEffect.jpg");
            createProduct(productService, "Forza Horizon 5", "Driving in Mexico.", 59.99, 0, ProductCategory.XBOX_SERIES, "https://upload.wikimedia.org/wikipedia/en/8/86/Forza_Horizon_5_cover_art.jpg");
            createProduct(productService, "Starfield", "Skyrim in space.", 69.99, 100, ProductCategory.XBOX_SERIES, "https://upload.wikimedia.org/wikipedia/en/6/6d/Starfield_cover_art.jpg");
            createProduct(productService, "Fable", "For every choice, a consequence.", 12.99, 20, ProductCategory.XBOX, "https://upload.wikimedia.org/wikipedia/en/6/6d/Fable_box_art.jpg");
            createProduct(productService, "Knights of the Old Republic", "Legendary Star Wars RPG.", 19.99, 25, ProductCategory.XBOX, "https://upload.wikimedia.org/wikipedia/en/2/21/Star_Wars_KOTOR_box_art.jpg");
            createProduct(productService, "Red Dead Redemption", "Cowboy open world.", 29.99, 20, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/a/a7/Red_Dead_Redemption.jpg");
            createProduct(productService, "Bioshock", "No Gods or Kings. Only Man.", 14.99, 35, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/6/6d/BioShock_cover.jpg");
            createProduct(productService, "Left 4 Dead 2", "Zombie co-op shooter.", 19.99, 40, ProductCategory.XBOX_360, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/550/header.jpg");
            createProduct(productService, "Cuphead", "Don't deal with the devil.", 19.99, 30, ProductCategory.XBOX_ONE, "https://upload.wikimedia.org/wikipedia/en/0/05/Cuphead_cover_art.png");
            createProduct(productService, "Sunset Overdrive", "Colorful chaos.", 14.99, 20, ProductCategory.XBOX_ONE, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/847370/header.jpg");


            // --- 5. PC Gaming ---

            createProduct(productService, "Half-Life 2", "Rise and shine, Mr. Freeman.", 9.99, 100, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/220/header.jpg");
            createProduct(productService, "Portal 2", "Thinking with portals.", 9.99, 120, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/620/header.jpg");
            createProduct(productService, "Counter-Strike 2", "Tactical competitive shooter.", 0.00, 1000, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/730/header.jpg");
            createProduct(productService, "The Witcher 3: Wild Hunt", "Geralt searches for Ciri.", 29.99, 80, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/292030/header.jpg");
            createProduct(productService, "Cyberpunk 2077", "Wake up Samurai.", 59.99, 90, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1091500/header.jpg");
            createProduct(productService, "Baldur's Gate 3", "D&D RPG masterpiece.", 59.99, 60, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1086940/header.jpg");
            createProduct(productService, "Elden Ring", "Oh, Elden Ring.", 59.99, 75, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1245620/header.jpg");
            createProduct(productService, "Stardew Valley", "Farming simulation.", 14.99, 200, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/413150/header.jpg");
            createProduct(productService, "Terraria", "2D Minecraft but more.", 9.99, 150, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/105600/header.jpg");
            createProduct(productService, "Hades", "Escaping hell repeatedly.", 24.99, 40, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/1145360/header.jpg");
            createProduct(productService, "Factorio", "The factory must grow.", 30.00, 50, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/427520/header.jpg");
            createProduct(productService, "RimWorld", "Colony sim generator.", 34.99, 40, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/294100/header.jpg");
            createProduct(productService, "Dota 2", "MOBA classic.", 0.00, 500, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/570/header.jpg");
            createProduct(productService, "Team Fortress 2", "Class based hat simulator.", 0.00, 300, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/440/header.jpg");
            createProduct(productService, "Age of Empires II: DE", "Wololo.", 19.99, 60, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/813780/header.jpg");
            createProduct(productService, "Civilization VI", "Just one more turn...", 59.99, 40, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/289070/header.jpg");
            createProduct(productService, "Doom Eternal", "Rip and tear.", 39.99, 45, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/782330/header.jpg");


            // --- 6. SEGA & Retro ---

            createProduct(productService, "Sonic the Hedgehog 2", "Gotta go fast.", 9.99, 50, ProductCategory.GENESIS, "https://upload.wikimedia.org/wikipedia/en/0/0c/Sonic_2_US_Cover.jpg");
            createProduct(productService, "Streets of Rage 2", "Beat 'em up classic.", 14.99, 20, ProductCategory.GENESIS, "https://upload.wikimedia.org/wikipedia/en/0/01/Streets_of_Rage_2_Box_Art.jpg");
            createProduct(productService, "Sonic Adventure", "Open your heart.", 19.99, 25, ProductCategory.DREAMCAST, "https://upload.wikimedia.org/wikipedia/en/9/9e/Sonic_Adventure_box_art.png");
            createProduct(productService, "Shenmue", "Do you know where sailors hang out?", 39.99, 0, ProductCategory.DREAMCAST, "https://upload.wikimedia.org/wikipedia/en/d/df/Shenmue_box_art.jpg");
            createProduct(productService, "Crazy Taxi", "Ya ya ya ya ya!", 19.99, 20, ProductCategory.DREAMCAST, "https://upload.wikimedia.org/wikipedia/en/3/30/Crazy_Taxi_cover_art.jpg");
            createProduct(productService, "Jet Set Radio", "Graffiti and rollerblades.", 24.99, 15, ProductCategory.DREAMCAST, "https://upload.wikimedia.org/wikipedia/en/4/4c/Jet_Set_Radio_cover.png");
            createProduct(productService, "Nights into Dreams", "Flying through dreams.", 29.99, 10, ProductCategory.SATURN, "https://upload.wikimedia.org/wikipedia/en/b/b5/Nights_into_Dreams..._box_art.jpg");
            createProduct(productService, "Panzer Dragoon Saga", "Rare RPG gem.", 499.99, 0, ProductCategory.SATURN, "https://upload.wikimedia.org/wikipedia/en/c/c2/Panzer_Dragoon_Saga_Box_Art.jpg");
            createProduct(productService, "Alex Kidd in Miracle World", "Pre-Sonic mascot.", 19.99, 15, ProductCategory.MASTER_SYSTEM, "https://upload.wikimedia.org/wikipedia/en/6/6d/Alex_Kidd_in_Miracle_World_box_cover.jpg");


            // --- 7. Handhelds & Misc ---

            createProduct(productService, "Pokemon Emerald", "Hoenn region enhanced.", 99.99, 10, ProductCategory.GBA, "https://upload.wikimedia.org/wikipedia/en/5/58/Pokemon_Emerald_Box_Art.jpg");
            createProduct(productService, "Metroid Fusion", "Horror atmosphere on GBA.", 49.99, 12, ProductCategory.GBA, "https://upload.wikimedia.org/wikipedia/en/c/c5/Metroid_Fusion_box.jpg");
            createProduct(productService, "Advance Wars", "Tactical turn-based war.", 29.99, 20, ProductCategory.GBA, "https://upload.wikimedia.org/wikipedia/en/6/66/Advance_Wars_Box_Art.jpg");
            createProduct(productService, "Golden Sun", "RPG with djinn magic.", 34.99, 15, ProductCategory.GBA, "https://upload.wikimedia.org/wikipedia/en/0/00/Golden_Sun_box_art.jpg");
            createProduct(productService, "Castlevania: Aria of Sorrow", "Soul collecting action.", 59.99, 8, ProductCategory.GBA, "https://upload.wikimedia.org/wikipedia/en/c/c4/Castlevania_-_Aria_of_Sorrow_Cover.jpg");
            createProduct(productService, "God of War: Chains of Olympus", "Kratos on the go.", 19.99, 25, ProductCategory.PSP, "https://upload.wikimedia.org/wikipedia/en/1/1a/God_of_War_Chains_of_Olympus_Recall_Boxart.jpg");
            createProduct(productService, "Metal Gear Solid: Peace Walker", "Building Mother Base.", 24.99, 20, ProductCategory.PSP, "https://upload.wikimedia.org/wikipedia/en/2/23/Metal_Gear_Solid_Peace_Walker_cover.jpg");
            createProduct(productService, "Persona 4 Golden", "Mystery in Inaba.", 29.99, 40, ProductCategory.PS_VITA, "https://upload.wikimedia.org/wikipedia/en/5/52/Persona_4_Golden_Cover.jpg");
            createProduct(productService, "Uncharted: Golden Abyss", "Drake on Vita.", 19.99, 20, ProductCategory.PS_VITA, "https://upload.wikimedia.org/wikipedia/en/3/3d/Uncharted_Golden_Abyss_Box_Art.jpg");


            // --- 8. Filling the rest ---

            createProduct(productService, "Monkey Island 2", "Guybrush Threepwood returns.", 14.99, 10, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/2/23/Monkey_Island_2_LeChuck%27s_Revenge_artwork.jpg");
            createProduct(productService, "SimCity 2000", "Reticulating splines.", 9.99, 10, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/b/b8/SimCity_2000_cover.jpg");
            createProduct(productService, "Diablo II", "Stay a while and listen.", 19.99, 30, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/d/d5/Diablo_II_Coverart.png");
            createProduct(productService, "StarCraft", "We require more vespene gas.", 14.99, 40, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/9/93/StarCraft_box_art.jpg");
            createProduct(productService, "World of Warcraft", "The king of MMOs.", 14.99, 1000, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/6/65/World_of_Warcraft.png");
            createProduct(productService, "System Shock 2", "Look at you, hacker.", 9.99, 15, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/238210/header.jpg");
            createProduct(productService, "Deus Ex", "I never asked for this.", 9.99, 20, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/6910/header.jpg");
            createProduct(productService, "Thief II", "The metal age.", 9.99, 15, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/211740/header.jpg");
            createProduct(productService, "Vampire: The Masquerade - Bloodlines", "Cult RPG classic.", 19.99, 10, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/2600/header.jpg");
            createProduct(productService, "Fallout: New Vegas", "War never changes.", 14.99, 50, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/22380/header.jpg");
            createProduct(productService, "Hollow Knight", "Bug souls.", 14.99, 60, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/367520/header.jpg");
            createProduct(productService, "Celeste", "Climb the mountain.", 19.99, 40, ProductCategory.SWITCH, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/504230/header.jpg");
            createProduct(productService, "Undertale", "Determination.", 9.99, 80, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/391540/header.jpg");
            createProduct(productService, "Minecraft", "Blocks everywhere.", 29.99, 500, ProductCategory.PC, "https://upload.wikimedia.org/wikipedia/en/5/51/Minecraft_cover.png");
            createProduct(productService, "Among Us", "Sus.", 4.99, 200, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/945360/header.jpg");
            createProduct(productService, "Rocket League", "Soccer with cars.", 0.00, 300, ProductCategory.PC, "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/252950/header.jpg");
            createProduct(productService, "Street Fighter II", "Hadouken!", 19.99, 25, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/0/02/Street_Fighter_II_SNES_box_art.jpg");
            createProduct(productService, "Mortal Kombat II", "Fatality.", 19.99, 20, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/1/13/Mortal_Kombat_II_box_art.png");
            createProduct(productService, "Mega Man X", "Jump and shoot.", 24.99, 15, ProductCategory.SNES, "https://upload.wikimedia.org/wikipedia/en/e/ed/Mega_Man_X_cover.jpg");
            createProduct(productService, "Silent Hill 2", "In my restless dreams...", 49.99, 10, ProductCategory.PS2, "https://upload.wikimedia.org/wikipedia/en/b/bb/Silent_Hill_2_cover.png");

            System.out.println("--- Test Data Initialized ---");
        };
    }

    private void createProduct(ProductService productService, String name, String description, double price, int stock, ProductCategory category, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock((long) stock);
        p.setDiscount(0.0);
        p.setCategories(Set.of(category));
        p.setImageUrl(imageUrl);
        productService.saveProduct(p);
    }
}