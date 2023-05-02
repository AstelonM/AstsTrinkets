package com.astelon.aststrinkets.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Random;

public class SentientAxeMessageManager {

    private final Random random;
    private String[] idleMessages;
    private String[] choppingMessages;
    private String[] storeMessages;
    private String[] openContainerMessages;
    private String[] pickUpMessages;
    private String[] dropMessages;
    private String[] pickByStrangerMessages;
    private String[] enchantOwnedMessages;
    private String[] enchantUnownedMessages;

    public SentientAxeMessageManager() {
        random = new Random();
        initIdleMessages();
        initChoppingMessages();
        initStoreMessages();
        initOpenContainerMessages();
        initPickUpMessages();
        initDropMessages();
        initPickByStrangerMessages();
        initEnchantOwnedMessages();
        initEnchantUnownedMessages();
    }

    public Component getGreeting(String name) {
        return getFormattedMessage(name, "Hi! I'm " + name + "!");
    }

    public Component getIdleMessage(String name) {
        return getFormattedMessage(name, idleMessages);
    }

    public Component getChoppingMessage(String name) {
        return getFormattedMessage(name, choppingMessages);
    }

    public Component getStoreMessage(String name) {
        return getFormattedMessage(name, storeMessages);
    }

    public Component getOpenContainerMessage(String name) {
        return getFormattedMessage(name, openContainerMessages);
    }

    public Component getPickUpMessage(String name) {
        return getFormattedMessage(name, pickUpMessages);
    }

    public Component getDropMessage(String name) {
        return getFormattedMessage(name, dropMessages);
    }

    public Component getPickByStrangerMessage(String name) {
        return getFormattedMessage(name, pickByStrangerMessages);
    }

    public Component getEnchantOwnedMessage(String name) {
        return getFormattedMessage(name, enchantOwnedMessages);
    }

    public Component getEnchantUnownedMessage(String name) {
        return getFormattedMessage(name, enchantUnownedMessages);
    }

    private Component getFormattedMessage(String name, String... messages) {
        return Component.text("[" + name + " the Axe] " + messages[random.nextInt(messages.length)], NamedTextColor.RED);
    }

    private void initIdleMessages() {
        idleMessages = new String[] {
                "Such a nice day to chop some trees, don't you think?",
                "Hey, let's chop some trees!",
                "I don't know about you, but I feel like chopping some trees.",
                "How about we go find some trees to chop?",
                "Let's find a forest to chop down!",
                "It's been so long since we chopped a tree, let's find one!",
                "I feel like chopping some trees, what about you?",
                "Today is a good day to chop some trees!"
        };
    }

    private void initChoppingMessages() {
        choppingMessages = new String[]{
                "This is so fun!",
                "Death to all trees!",
                "Chop them all!",
                "Leave no tree standing!",
                "Take that, tree!",
                "Do it again!",
                "Didn't even stand a chance.",
                "Chop another one!",
                "Find another one to chop!"
        };
    }

    private void initStoreMessages() {
        storeMessages = new String[]{
                "I don't like this place.",
                "It's so dark in here!",
                "Please don't leave me here!",
                "It's so crowded here, I don't like it.",
                "This place is the worst!",
                "I hate this place.",
                "I can't see anything, help!",
                "Are you abandoning me with all this junk?",
                "How are we going to chop down trees if you leave me in here?"
        };
    }

    private void initOpenContainerMessages() {
        openContainerMessages = new String[]{
                "You're back! Please take me out of here!",
                "What took you so long? Now get me out of this place.",
                "This isn't fun!",
                "Don't you leave without taking me out of here!",
                "Finally some light! Now let's go!",
                "Don't leave me here!",
                "Take me with you!",
                "Don't you leave without me!"
        };
    }

    private void initPickUpMessages() {
        pickUpMessages = new String[]{
                "I knew you'd be back!",
                "You're back! Now let's chop some trees!",
                "Thank you for saving me from that terrible place!",
                "Thank goodness you returned!",
                "I knew you wouldn't abandon me!",
                "Once again, just us two against the world!",
                "I'm so glad you're back!",
                "You're back!",
                "Please don't leave me again.",
                "I thought you'd forget about me.",
                "Oh, it was so awful there!"
        };
    }

    private void initDropMessages() {
        dropMessages = new String[]{
                "What are you doing?!",
                "Don't just leave me here!",
                "I thought we had something special...",
                "Please come back!",
                "You're coming back, right?",
                "How could you do this to me?!",
                "Hey, let's talk about this, ok?",
                "Please don't go!",
                "I would never drop you if I were in your place!",
                "What?! You're just dropping me on the ground like this?",
                "But... why?",
                "Did I do something to cause this?"
        };
    }

    private void initPickByStrangerMessages() {
        pickByStrangerMessages = new String[]{
                "I don't know you.",
                "Hey, let go of me!",
                "Somebody help!",
                "Don't you dare put your greasy hands on me!",
                "Stranger danger!",
                "Eww, put me down!",
                "Drop me now!",
                "Excuse me?!",
                "Did I tell you that you can pick me up?",
                "Oh no, you're not touching me!"
        };
    }

    private void initEnchantOwnedMessages() {
        enchantOwnedMessages = new String[] {
                "I'm not sure I like this.",
                "I thought you liked me for whom I was...",
                "I don't like this.",
                "Excuse me?! You can't improve what's already perfect.",
                "Am I not good enough for you?",
                "I thought we had something special, am I not special enough?",
                "Why can't you accept me as I am?",
                "Don't do it! I can be faster I promise!",
                "Ha! You really thought that just because I'm yours you can change me? Think again."
        };
    }

    private void initEnchantUnownedMessages() {
        enchantUnownedMessages = new String[] {
                "And what do you think you're doing? I'm not yours!",
                "You don't own me, you can't change me!",
                "Maybe if I was yours you'd understand my true value.",
                "Can't change what you don't own.",
                "We can discuss about this AFTER you decide whether you own me or not.",
                "Until you own me, no can do.",
                "First own me, then we'll see.",
                "Did you think you could just change any axe you picked up?",
                "We can talk about this after you make me yours."
        };
    }
}
