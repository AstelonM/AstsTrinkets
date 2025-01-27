package com.astelon.aststrinkets.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.DyeColor;
import org.bukkit.entity.*;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

// Retrofitted from my MobHeads plugin
public class MobInfoManager {

    private final HashMap<TropicalFishType, String> tropicalFishTypes;
    private final ArrayList<String> noInfoText;
    private final Random random;

    public MobInfoManager() {
        tropicalFishTypes = new HashMap<>();
        noInfoText = new ArrayList<>();
        initFishTypes();
        initNoInfoText();
        random = new Random();
    }

    public String getMobType(Entity entity) {
        return capitalize(getRawType(entity.getType()));
    }

    public String getTypeName(EntityType entityType) {
        return capitalize(getRawType(entityType));
    }

    public String getTypeAndName(Entity entity) {
        String type = getMobType(entity);
        Component name = entity.customName();
        if (name != null)
            return type + " named " + PlainTextComponentSerializer.plainText().serialize(name);
        return type;
    }

    private String capitalize(String text) {
        return Arrays.stream(text.split("_"))
                .map(word -> word.charAt(0) + word.substring(1).toLowerCase()).collect(Collectors.joining(" "));
    }

    private String lowercase(String text) {
        return text.toLowerCase().replace('_', ' ');
    }

    private String getColourArticle(String text) {
        if (text.startsWith("o") || text.startsWith("O"))
            return "an " + text;
        return "a " + text;
    }

    private String getRawType(EntityType type) {
        return switch (type) {
            case MUSHROOM_COW -> "MOOSHROOM";
            case SNOWMAN -> "SNOW_GOLEM";
            default -> type.name();
        };
    }

    private String getNoInfoText(EntityType type) {
        String line = noInfoText.get(random.nextInt(noInfoText.size()));
        return line.replace("<name>", capitalize(getRawType(type)));
    }

    public List<String> getExtraInfo(Entity entity) {
        EntityType entityType = entity.getType();
        List<String> result = new ArrayList<>(switch (entityType) {
            case AXOLOTL -> getAxolotlInfo((Axolotl) entity);
            case CAT -> getCatInfo((Cat) entity);
            case CREEPER -> getCreeperInfo((Creeper) entity);
            case DONKEY -> getDonkeyInfo((Donkey) entity);
            case FOX -> getFoxInfo((Fox) entity);
            case GOAT -> getGoatInfo((Goat) entity);
            case HORSE -> getHorseInfo((Horse) entity);
            case LLAMA -> getLlamaInfo((Llama) entity);
            case MAGMA_CUBE -> getMagmaCubeInfo((MagmaCube) entity);
            case MUSHROOM_COW -> getMooshroomInfo((MushroomCow) entity);
            case MULE -> getMuleInfo((Mule) entity);
            case PANDA -> getPandaInfo((Panda) entity);
            case PARROT -> getParrotInfo((Parrot) entity);
            case RABBIT -> getRabbitInfo((Rabbit) entity);
            case SHEEP -> getSheepInfo((Sheep) entity);
            case SHULKER -> getShulkerInfo((Shulker) entity);
            case SLIME -> getSlimeInfo((Slime) entity);
            case SNOWMAN -> getSnowGolemInfo((Snowman) entity);
            case TROPICAL_FISH -> getTropicalFishInfo((TropicalFish) entity);
            case VILLAGER -> getVillagerInfo((Villager) entity);
            case WOLF -> getWolfInfo((Wolf) entity);
            case ZOMBIE_VILLAGER -> getZombieVillagerInfo((ZombieVillager) entity);
            default -> new ArrayList<String>(1);
        });
        if (entity instanceof Tameable tameable && !(entity instanceof Cat)) {
            if (tameable.isTamed())
                result.add("- Tamed");
            else
                result.add("- Wild");
        }
        if (entity instanceof Ageable ageable) {
            if (ageable.isAdult())
                result.add("- Adult");
            else
                result.add("- Baby");
        }
        if (entity.isInvulnerable())
            result.add("- Invulnerable");
        if (entity instanceof PiglinAbstract piglin && piglin.isImmuneToZombification() ||
                entity instanceof Hoglin hoglin && hoglin.isImmuneToZombification())
            result.add("- Immune to zombification");

        if (result.isEmpty())
            result.add(getNoInfoText(entityType));
        return result;
    }

    private List<String> getAxolotlInfo(Axolotl axolotl) {
        Axolotl.Variant variant = axolotl.getVariant();
        return List.of("- Colour: " + switch (variant) {
            case LUCY -> "Leucistic";
            case WILD -> "Brown";
            default -> capitalize(variant.name());
        });
    }

    private List<String> getCatInfo(Cat cat) {
        Cat.Type type = cat.getCatType();
        String typeName = "- Type: " + switch (type) {
            case BLACK -> "Tuxedo";
            case ALL_BLACK -> "Black";
            default -> capitalize(type.name());
        };
        if (cat.isTamed())
            return List.of(typeName, "- Tamed", "- Collar: " + capitalize(cat.getCollarColor().name()));
        else
            return List.of(typeName, "- Stray");
    }

    private List<String> getCreeperInfo(Creeper creeper) {
        if (creeper.isPowered())
            return List.of("- Charged");
        return List.of();
    }

    private List<String> getDonkeyInfo(Donkey donkey) {
        if (donkey.getInventory().getSaddle() != null) {
            if (donkey.isCarryingChest())
                return List.of("- Has a saddle and a chest");
            else
                return List.of("- Has a saddle");
        } else if (donkey.isCarryingChest())
                return List.of("- Has a chest");
        return List.of();
    }

    private List<String> getFoxInfo(Fox fox) {
        String type = "- Type: " + capitalize(fox.getType().name());
        if (fox.getFirstTrustedPlayer() != null || fox.getSecondTrustedPlayer() != null)
            return List.of(type, "- Trusts someone");
        return List.of(type);
    }

    private List<String> getGoatInfo(Goat goat) {
        if (goat.isScreaming())
            return List.of("- Screaming");
        return List.of();
    }

    private List<String> getHorseInfo(Horse horse) {
        String colour = "- Colour: " + capitalize(horse.getColor().name());
        Horse.Style markings = horse.getStyle();
        String markingsName = "- Markings: " + switch (markings) {
            case NONE -> "None";
            case WHITE -> "White stockings and blaze";
            case WHITEFIELD -> "White field";
            case WHITE_DOTS -> "White dots";
            case BLACK_DOTS -> "Black dots";
        };
        HorseInventory inventory = horse.getInventory();
        ItemStack armour = inventory.getArmor();
        if (inventory.getSaddle() != null) {
            if (armour != null)
                return List.of(colour, markingsName, "- Has a saddle and " + lowercase(armour.getType().name()));
            else
                return List.of(colour, markingsName, "- Has a saddle");
        } else if (armour != null)
                return List.of(colour, markingsName, "- Has " + lowercase(armour.getType().name()));
        return List.of(colour, markingsName);
    }

    private List<String> getLlamaInfo(Llama llama) {
        String colour = "- Colour: " + capitalize(llama.getColor().name());
        ItemStack carpetItem = llama.getInventory().getDecor();
        if (carpetItem != null) {
            if (llama.isCarryingChest())
                return List.of(colour, "- Has " + getColourArticle(lowercase(carpetItem.getType().name()) + " and a chest"));
            else
                return List.of(colour, "- Has " + getColourArticle(lowercase(carpetItem.getType().name())));
        } else if (llama.isCarryingChest()) {
            return List.of(colour, "- Has a chest");
        }
        return List.of(colour);
    }

    private List<String> getMagmaCubeInfo(MagmaCube cube) {
        return List.of("- Size: " + cube.getSize());
    }

    private List<String> getMooshroomInfo(MushroomCow mooshroom) {
        return List.of("- Variant: " + capitalize(mooshroom.getVariant().name()));
    }

    private List<String> getMuleInfo(Mule mule) {
        if (mule.getInventory().getSaddle() != null) {
            if (mule.isCarryingChest())
                return List.of("- Has a saddle and a chest");
            else
                return List.of("- Has a saddle");
        } else if (mule.isCarryingChest())
            return List.of("- Has a chest");
        return List.of();
    }

    private List<String> getPandaInfo(Panda panda) {
        Panda.Gene mainGene = panda.getMainGene();
        Panda.Gene hiddenGene = panda.getHiddenGene();
        String type = "- Type: ";
        if (mainGene == Panda.Gene.WEAK || mainGene == Panda.Gene.BROWN) {
            if (mainGene == hiddenGene)
                type += capitalize(mainGene.name());
            else
                type += "Normal";
        } else
            type += capitalize(mainGene.name());
        return List.of(type);
    }

    private List<String> getParrotInfo(Parrot parrot) {
        return List.of("- Variant: " + capitalize(parrot.getVariant().name()));
    }

    private List<String> getRabbitInfo(Rabbit rabbit) {
        Rabbit.Type type = rabbit.getRabbitType();
        String typeText = "- Type: ";
        if (type == Rabbit.Type.THE_KILLER_BUNNY)
            typeText += "Killer Bunny";
        else
            typeText += capitalize(rabbit.getRabbitType().name());
        return List.of(typeText);
    }

    private List<String> getSheepInfo(Sheep sheep) {
        DyeColor colour = sheep.getColor();
        if (colour == null)
            colour = DyeColor.WHITE;
        return List.of("- Colour: " + capitalize(colour.name()));
    }

    private List<String> getShulkerInfo(Shulker shulker) {
        DyeColor colour = shulker.getColor();
        if (colour == null)
            return List.of("- Colour: None");
        return List.of("- Colour: " + capitalize(colour.name()));
    }

    private List<String> getSlimeInfo(Slime slime) {
        return List.of("- Size: " + slime.getSize());
    }

    public List<String> getSnowGolemInfo(Snowman snowGolem) {
        if (snowGolem.isDerp())
            return List.of("- Has no pumpkin");
        return List.of();
    }

    private List<String> getTropicalFishInfo(TropicalFish tropicalFish) {
        DyeColor bodyColour = tropicalFish.getBodyColor();
        DyeColor patternColour = tropicalFish.getPatternColor();
        TropicalFish.Pattern pattern = tropicalFish.getPattern();
        TropicalFishType fishType = new TropicalFishType(bodyColour, patternColour, pattern);
        String fishName = tropicalFishTypes.get(fishType);
        String type = "- Type: ";
        if (fishName != null)
            return List.of(type + fishName);
        String colourName;
        if (bodyColour == patternColour)
            colourName = capitalize(bodyColour.name());
        else
            colourName = capitalize(bodyColour.name()) + "-" + capitalize(patternColour.name());
        type += colourName + " " + capitalize(pattern.name());
        return List.of(type);
    }

    private List<String> getVillagerInfo(Villager villager) {
        String profession = "- Profession: " + getProfessionName(villager.getProfession());
        String type = "- Type: " + capitalize(villager.getVillagerType().name());
        return List.of(profession, type);
    }

    private String getProfessionName(Villager.Profession profession) {
        if (profession == Villager.Profession.NONE)
            return "Unemployed";
        return capitalize(profession.name());
    }

    private List<String> getWolfInfo(Wolf wolf) {
        if (wolf.isTamed())
            return List.of("- Collar: " + capitalize(wolf.getCollarColor().name()));
        return List.of();
    }

    private List<String> getZombieVillagerInfo(ZombieVillager zombieVillager) {
        Villager.Profession profession = zombieVillager.getVillagerProfession();
        // The IDE just can't decide if this can be null or not, so I'll just add this check to make sure
        //noinspection ConstantConditions
        if (profession == null)
            profession = Villager.Profession.NONE;
        String professionName = getProfessionName(profession);
        String type = capitalize(zombieVillager.getVillagerType().name());
        return List.of(professionName, type);
    }

    private void initFishTypes() {
        tropicalFishTypes.put(new TropicalFishType(DyeColor.ORANGE, DyeColor.GRAY, TropicalFish.Pattern.STRIPEY), "Anemone");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.GRAY, DyeColor.GRAY, TropicalFish.Pattern.FLOPPER), "Black Tang");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.GRAY, DyeColor.BLUE, TropicalFish.Pattern.FLOPPER), "Blue Tang");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.GRAY, TropicalFish.Pattern.CLAYFISH), "Butterfly Fish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.BLUE, DyeColor.GRAY, TropicalFish.Pattern.SUNSTREAK), "Cichlid");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.ORANGE, DyeColor.WHITE, TropicalFish.Pattern.KOB), "Clownfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.PINK, DyeColor.LIGHT_BLUE, TropicalFish.Pattern.SPOTTY), "Cotton Candy Betta");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.PURPLE, DyeColor.YELLOW, TropicalFish.Pattern.BLOCKFISH), "Dottyback");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.RED, TropicalFish.Pattern.CLAYFISH), "Emperor Red Snapper");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.YELLOW, TropicalFish.Pattern.CLAYFISH), "Goatfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.GRAY, TropicalFish.Pattern.GLITTER), "Moorish Idol");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.ORANGE, TropicalFish.Pattern.CLAYFISH), "Ornate Butterflyfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.CYAN, DyeColor.PINK, TropicalFish.Pattern.DASHER), "Parrotfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.LIME, DyeColor.LIGHT_BLUE, TropicalFish.Pattern.BRINELY), "Queen Angelfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.RED, DyeColor.WHITE, TropicalFish.Pattern.BETTY), "Red Cichlid");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.GRAY, DyeColor.RED, TropicalFish.Pattern.SNOOPER), "Red Lipped Blenny");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.RED, DyeColor.WHITE, TropicalFish.Pattern.BLOCKFISH), "Red Snapper");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.WHITE, DyeColor.YELLOW, TropicalFish.Pattern.FLOPPER), "Threadfin");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.RED, DyeColor.WHITE, TropicalFish.Pattern.KOB), "Tomato Clownfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.GRAY, DyeColor.WHITE, TropicalFish.Pattern.SUNSTREAK), "Triggerfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.CYAN, DyeColor.YELLOW, TropicalFish.Pattern.DASHER), "Yellowtail Parrotfish");
        tropicalFishTypes.put(new TropicalFishType(DyeColor.YELLOW, DyeColor.YELLOW, TropicalFish.Pattern.FLOPPER), "Yellow Tang");
    }

    private void initNoInfoText() {
        noInfoText.add("- Just an average <name>.");
        noInfoText.add("- Nothing special about it.");
        noInfoText.add("- Looks like any other <name>.");
        noInfoText.add("- Nothing to see here.");
        noInfoText.add("- If it's different, you can't tell.");
    }

    public record TropicalFishType(DyeColor bodyColor, DyeColor patternColor, TropicalFish.Pattern pattern) {}
}
