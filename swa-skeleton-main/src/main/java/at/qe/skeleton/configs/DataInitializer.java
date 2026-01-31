/**
 * For testing purposes:
 * This class is for creating Test-Data. Initializes when Maven starts.
 */
package at.qe.skeleton.configs;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import at.qe.skeleton.services.MailNotificationChannel;
import at.qe.skeleton.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@Configuration
@Profile("dev")
public class DataInitializer {

    Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    ApplicationRunner initData(ProductService productService) {
        return args -> {
            if (!productService.getAllProducts().isEmpty()) {
                return;
            }

            log.info("Initializing Test Data for Products");

            // === MEME PRODUCT ===
            createProduct(productService, "QUAK(e)", "The groundbreaking first-person quacker that quacked a generation. Yellow, squishy, and QUAK.", 9.99, 50L, 0.0, ProductCategory.PC, "/images/quak(e).png");

            // === LJONJAS FAVOURITES ===
            createProduct(productService,
                    "The Legend of Zelda: Majora's Mask",
                    "An atmospheric masterpiece where Link has only three days to save Termina from a crashing moon.",
                    49.99, 10, 0.0, ProductCategory.N64,
                    "https://upload.wikimedia.org/wikipedia/en/6/60/The_Legend_of_Zelda_-_Majora's_Mask_Box_Art.jpg");
            createProduct(productService,
                    "LEGO Star Wars: The Video Game",
                    "Play through the prequel trilogy in the fun and whimsical LEGO style.",
                    19.99, 25, 0.35, ProductCategory.PC,
                    "https://upload.wikimedia.org/wikipedia/en/8/81/Legostarwarsthevideogame.jpg");
            createProduct(productService,
                    "The Legend of Zelda: The Wind Waker (Limited Edition)",
                    "Includes the Ocarina of Time / Master Quest Bonus Disc. A GameCube classic with cel-shaded graphics.",
                    89.99, 0, 0.0, ProductCategory.GAMECUBE,
                    "https://cdn.shopify.com/s/files/1/0656/3607/3714/products/MG_8669.jpg?v=1671004665");
            createProduct(productService,
                    "The Elder Scrolls V: Skyrim",
                    "The legendary open-world RPG. Dragonborn, Fus Ro Dah, and endless adventures awaiting.",
                    14.99, 100, 0.1, ProductCategory.PS3,
                    "https://upload.wikimedia.org/wikipedia/en/1/15/The_Elder_Scrolls_V_Skyrim_cover.png");
            createProduct(productService,
                    "Garry's Mod",
                    "A physics sandbox. There are no predefined aims or goals. We give you the tools and leave you to play.",
                    9.99, 500, 0.9, ProductCategory.PC,
                    "https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/4000/header.jpg");

            // === MATTEOS FAVOURITES ===
            createProduct(productService,
                    "Panzer Dragoon Saga",
                    "Enter the world of Panzer Dragoon Saga and experience a game like no other: a fusion of classic Panzer action with the most technologically advanced RPG to come to Saturn.",
                    999.99, 2, 0.2, ProductCategory.SATURN,
                    "https://upload.wikimedia.org/wikipedia/en/6/64/PanzerDragoonSagaBox.jpg");
            createProduct(productService,
                    "Dead Space",
                    "Only the Dead Survive.",
                    9.99, 2, 0.5, ProductCategory.XBOX_360,
                    "https://upload.wikimedia.org/wikipedia/en/5/57/Dead_Space_Box_Art.jpg");
            createProduct(productService,
                    "Bayonetta",
                    "Take control of the baddest Umbra Witch in town, where her hair is her outfit and her most lethal weapon. If you’re not dodging attacks in slow motion and summoning giant demons from your ponytail, are you even really gaming?",
                    19.99, 25, 0.0, ProductCategory.XBOX_360,
                    "https://upload.wikimedia.org/wikipedia/en/8/81/Bayonetta_box_artwork.png");
            createProduct(productService,
                    "Shenmue 2",
                    "Ryo Hazuki arrives in Hong Kong, China on his continued mission to avenge his father’s death and discover the truth behind the Phoenix Mirror.",
                    49.99, 1, 0.1, ProductCategory.DREAMCAST,
                    "https://upload.wikimedia.org/wikipedia/en/e/e1/Shenmue_II.jpg");
            createProduct(productService,
                    "Metal Gear Solid 3: Snake Eater",
                    "Rival nations are secretly developing weapons that could threaten the future of mankind. Deep in the jungle, an elite soldier must use stealth to infiltrate the enemy and stop a weapon of mass destruction from triggering total war.",
                    39.99, 1, 0.0, ProductCategory.PS2,
                    "https://upload.wikimedia.org/wikipedia/en/b/b3/Mgs3box.jpg");

            // === AYLEENS FAVOURITES ===
            createProduct(productService,
                    "Shadow the Hedgehog",
                    "Join Sonic's edgiest rival as he skates through levels, fires heavy machinery, and questions his very existence. It’s the perfect game for anyone who thought Sonic Adventure needed 200% more angst and explosions.",
                    112.99, 3, 0.25, ProductCategory.PS2,
                    "https://upload.wikimedia.org/wikipedia/en/b/b2/Shadow_the_Hedgehog_Coverart.png");
            createProduct(productService,
                    "Conker's Bad Fur Day",
                    "The game that taught us that cute squirrels and hangovers go hand-in-hand. This isn't your toddler’s platformer; expect foul-mouthed opera-singing piles of poop, movie parodies, and a lot of chaotic energy.",
                    492.99, 1, 0.0, ProductCategory.N64,
                    "https://upload.wikimedia.org/wikipedia/en/9/99/Conkersbfdbox.jpg");
            createProduct(productService,
                    "The Curse of Monkey Island",
                    "Guybrush Threepwood is back, and he’s accidentally turned his girlfriend into a gold statue. Classic mistake, right? Dive into beautiful hand-drawn visuals and engage in the legendary art of insult sword fighting.",
                    5, 10, 0.1, ProductCategory.PC,
                    "https://upload.wikimedia.org/wikipedia/en/2/26/The_Curse_of_Monkey_Island_artwork.jpg");
            createProduct(productService,
                    "Yakuza 0",
                    "Whether you’re smashing bikes over thugs' heads as Kiryu or managing a cabaret club as Majima, there is never a dull moment. It’s a gripping crime drama wrapped inside a disco-dancing, karaoke-singing fever dream.",
                    14.99, 20, 0.1, ProductCategory.PS4,
                    "https://upload.wikimedia.org/wikipedia/en/b/ba/Yakuza0.jpg");
            createProduct(productService,
                    "God of War III",
                    "Kratos has a very simple \"To-Do\" list: Climb Mount Olympus and punch every god in the face. This is the peak of cinematic hack-and-slash brutality.",
                    24.99, 10, 0.0, ProductCategory.PS3,
                    "https://upload.wikimedia.org/wikipedia/en/8/8b/God_of_War_III_cover_art.jpg");

            // === CONSOLES ===
            createProduct(productService,
                    "WII U",
                    "Wii U is a brand new home videogame console from Nintendo. It's a powerful, high definition system with an extraordinary new controller that redefines the dynamic of playing games together: the Wii U GamePad.",
                    39.99, 1, 0.0, ProductCategory.WII_U,
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/Wii_U_Console_and_Gamepad.png/500px-Wii_U_Console_and_Gamepad.png");

            log.info("Test Data Initialized");
        };
    }

    private void createProduct(ProductService productService, String name, String description, double price, long stock, double discount, ProductCategory category, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock(stock);
        p.setDiscount(discount);
        p.setCategories(Set.of(category));
        p.setImageUrl(imageUrl);
        productService.saveProduct(p);
    }
}