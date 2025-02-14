package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MagicBerries extends CreatureAffectingTrinket {

    private final Random random = new Random();
    private final List<DyeColor> shulkerColours;

    private boolean allowCreepers;
    private boolean allowFoxes;
    private boolean allowGoats;
    private boolean allowMooshrooms;
    private boolean allowPandas;
    private boolean allowKillerBunnies;
    private boolean allowVillagers;
    private boolean allowZombieVillagers;

    public MagicBerries(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "magicBerries", Power.CHANGE_CREATURE_TYPE, false, Usages.INTERACT_WITH_MULTIPLE_TYPES);
        shulkerColours = new ArrayList<>();
        initShulkerColours();
    }

    private void initShulkerColours() {
        Collections.addAll(shulkerColours, DyeColor.values());
        shulkerColours.add(null);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.SWEET_BERRIES);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Magic Berries", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("These berries have the power to"),
                Component.text("alter the appearance of those who"),
                Component.text("eat them. Careful though, all magic"),
                Component.text("comes with a price.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean transformCreature(LivingEntity entity) {
        EntityType entityType = entity.getType();
        switch (entityType) {
            case AXOLOTL -> {
                Axolotl axolotl = (Axolotl) entity;
                Axolotl.Variant variant = axolotl.getVariant();
                Axolotl.Variant newVariant;
                do {
                    newVariant = Axolotl.Variant.values()[random.nextInt(Axolotl.Variant.values().length)];
                } while (newVariant == variant);
                axolotl.setVariant(newVariant);
                return true;
            }
            case CAT -> {
                Cat cat = (Cat) entity;
                Cat.Type type = cat.getCatType();
                Cat.Type newType;
                do {
                    newType = Cat.Type.values()[random.nextInt(Cat.Type.values().length)];
                } while (newType == type);
                cat.setCatType(newType);
                return true;
            }
            case CREEPER -> {
                if (allowCreepers) {
                    Creeper creeper = (Creeper) entity;
                    creeper.setPowered(!creeper.isPowered());
                    return true;
                }
            }
            case FOX -> {
                if (allowFoxes) {
                    Fox fox = (Fox) entity;
                    Fox.Type type = fox.getFoxType();
                    Fox.Type newType;
                    do {
                        newType = Fox.Type.values()[random.nextInt(Fox.Type.values().length)];
                    } while (newType == type);
                    fox.setFoxType(newType);
                    return true;
                }
            }
            case GOAT -> {
                if (allowGoats) {
                    Goat goat = (Goat) entity;
                    goat.setScreaming(!goat.isScreaming());
                    return true;
                }
            }
            case HORSE -> {
                Horse horse = (Horse) entity;
                Horse.Color colour = horse.getColor();
                Horse.Color newColour;
                Horse.Style style = horse.getStyle();
                Horse.Style newStyle;
                do {
                    newColour = Horse.Color.values()[random.nextInt(Horse.Color.values().length)];
                    newStyle = Horse.Style.values()[random.nextInt(Horse.Style.values().length)];
                } while (newColour == colour && newStyle == style);
                horse.setColor(newColour);
                horse.setStyle(newStyle);
                return true;
            }
            case LLAMA, TRADER_LLAMA -> {
                Llama llama = (Llama) entity;
                Llama.Color colour = llama.getColor();
                Llama.Color newColour;
                do {
                    newColour = Llama.Color.values()[random.nextInt(Llama.Color.values().length)];
                } while (newColour == colour);
                llama.setColor(newColour);
                return true;
            }
            case MUSHROOM_COW -> {
                if (allowMooshrooms) {
                    MushroomCow mooshroom = (MushroomCow) entity;
                    MushroomCow.Variant variant = mooshroom.getVariant();
                    MushroomCow.Variant newVariant;
                    do {
                        newVariant = MushroomCow.Variant.values()[random.nextInt(MushroomCow.Variant.values().length)];
                    } while (newVariant == variant);
                    mooshroom.setVariant(newVariant);
                    return true;
                }
            }
            case PANDA -> {
                if (allowPandas) {
                    Panda panda = (Panda) entity;
                    Panda.Gene mainGene = panda.getMainGene();
                    Panda.Gene newMainGene;
                    Panda.Gene hiddenGene = panda.getHiddenGene();
                    Panda.Gene newHiddenGene;
                    do {
                        newMainGene = Panda.Gene.values()[random.nextInt(Panda.Gene.values().length)];
                        newHiddenGene = Panda.Gene.values()[random.nextInt(Panda.Gene.values().length)];
                    } while (newMainGene == mainGene && newHiddenGene == hiddenGene);
                    panda.setMainGene(newMainGene);
                    panda.setHiddenGene(newHiddenGene);
                    return true;
                }
            }
            case PARROT -> {
                Parrot parrot = (Parrot) entity;
                Parrot.Variant variant = parrot.getVariant();
                Parrot.Variant newVariant;
                do {
                    newVariant = Parrot.Variant.values()[random.nextInt(Parrot.Variant.values().length)];
                } while (newVariant == variant);
                parrot.setVariant(newVariant);
                return true;
            }
            case RABBIT -> {
                Rabbit rabbit = (Rabbit) entity;
                Rabbit.Type type = rabbit.getRabbitType();
                Rabbit.Type newType;
                do {
                    newType = Rabbit.Type.values()[random.nextInt(Rabbit.Type.values().length)];
                } while (newType == type || (!allowKillerBunnies && newType == Rabbit.Type.THE_KILLER_BUNNY));
                rabbit.setRabbitType(newType);
                return true;
            }
            case SHEEP -> {
                Sheep sheep = (Sheep) entity;
                DyeColor colour = sheep.getColor();
                DyeColor newColour;
                do {
                    newColour = DyeColor.values()[random.nextInt(DyeColor.values().length)];
                } while (newColour == colour);
                sheep.setColor(newColour);
                return true;
            }
            case SHULKER -> {
                Shulker shulker = (Shulker) entity;
                DyeColor colour = shulker.getColor();
                DyeColor newColour;
                do {
                    newColour = shulkerColours.get(random.nextInt(shulkerColours.size()));
                } while (newColour == colour);
                shulker.setColor(newColour);
                return true;
            }
            case TROPICAL_FISH -> {
                TropicalFish tropicalFish = (TropicalFish) entity;
                TropicalFish.Pattern pattern = tropicalFish.getPattern();
                TropicalFish.Pattern newPattern;
                DyeColor bodyColour = tropicalFish.getBodyColor();
                DyeColor newBodyColour;
                DyeColor patternColour = tropicalFish.getPatternColor();
                DyeColor newPatternColour;
                do {
                    newPattern = TropicalFish.Pattern.values()[random.nextInt(TropicalFish.Pattern.values().length)];
                    newBodyColour = DyeColor.values()[random.nextInt(DyeColor.values().length)];
                    newPatternColour = DyeColor.values()[random.nextInt(DyeColor.values().length)];
                } while (newPattern == pattern && newBodyColour == bodyColour && newPatternColour == patternColour);
                tropicalFish.setPattern(newPattern);
                tropicalFish.setBodyColor(newBodyColour);
                tropicalFish.setPatternColor(newPatternColour);
                return true;
            }
            case VILLAGER -> {
                if (allowVillagers) {
                    Villager villager = (Villager) entity;
                    Villager.Type type = villager.getVillagerType();
                    Villager.Type newType = getNewVillagerType(type);
                    villager.setVillagerType(newType);
                    return true;
                }
            }
            case ZOMBIE_VILLAGER -> {
                if (allowZombieVillagers) {
                    ZombieVillager zombieVillager = (ZombieVillager) entity;
                    Villager.Type type = zombieVillager.getVillagerType();
                    Villager.Type newType = getNewVillagerType(type);
                    zombieVillager.setVillagerType(newType);
                    return true;
                }
            }
        }
        return false;
    }

    private Villager.Type getNewVillagerType(Villager.Type type) {
        Villager.Type newType;
        do {
            newType = Villager.Type.values()[random.nextInt(Villager.Type.values().length)];
        } while (newType == type);
        return newType;
    }

    public boolean isAllowCreepers() {
        return allowCreepers;
    }

    public void setAllowCreepers(boolean allowCreepers) {
        this.allowCreepers = allowCreepers;
    }

    public boolean isAllowFoxes() {
        return allowFoxes;
    }

    public void setAllowFoxes(boolean allowFoxes) {
        this.allowFoxes = allowFoxes;
    }

    public boolean isAllowGoats() {
        return allowGoats;
    }

    public void setAllowGoats(boolean allowGoats) {
        this.allowGoats = allowGoats;
    }

    public boolean isAllowMooshrooms() {
        return allowMooshrooms;
    }

    public void setAllowMooshrooms(boolean allowMooshrooms) {
        this.allowMooshrooms = allowMooshrooms;
    }

    public boolean isAllowPandas() {
        return allowPandas;
    }

    public void setAllowPandas(boolean allowPandas) {
        this.allowPandas = allowPandas;
    }

    public boolean isAllowKillerBunnies() {
        return allowKillerBunnies;
    }

    public void setAllowKillerBunnies(boolean allowKillerBunnies) {
        this.allowKillerBunnies = allowKillerBunnies;
    }

    public boolean isAllowVillagers() {
        return allowVillagers;
    }

    public void setAllowVillagers(boolean allowVillagers) {
        this.allowVillagers = allowVillagers;
    }

    public boolean isAllowZombieVillagers() {
        return allowZombieVillagers;
    }

    public void setAllowZombieVillagers(boolean allowZombieVillagers) {
        this.allowZombieVillagers = allowZombieVillagers;
    }
}
