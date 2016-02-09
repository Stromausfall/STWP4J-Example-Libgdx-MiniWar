package net.matthiasauer.stwp4j.libgdx.miniwar.model;

import java.util.Random;

import net.matthiasauer.stwp4j.ChannelInPort;
import net.matthiasauer.stwp4j.ChannelOutPort;
import net.matthiasauer.stwp4j.LightweightProcess;
import net.matthiasauer.stwp4j.libgdx.miniwar.controller.WorldInteraction;

/**
 * This process modifies the world according to WorldInteraction objects and
 * periodically creates WorldSnapshot instances which will be used to display
 * the world
 */
public class WorldProcess extends LightweightProcess {
    private final Random random = new Random();
    private final ChannelInPort<WorldInteraction> worldInteractionChannel;
    private final ChannelOutPort<WorldSnapShot> worldSnapShotChannel;
    private final PlayerData player1 = new PlayerData(10, 100);
    private final PlayerData player2 = new PlayerData(10, 100);
    private int round;
    private int progress;

    public WorldProcess(ChannelInPort<WorldInteraction> worldInteractionChannel,
            ChannelOutPort<WorldSnapShot> worldSnapShotChannel) {
        this.worldInteractionChannel = worldInteractionChannel;
        this.worldSnapShotChannel = worldSnapShotChannel;
        this.round = 0;
        this.progress = 50;
    }

    @Override
    protected void preIteration() {
        this.worldSnapShotChannel.offer(new WorldSnapShot(this.round, this.progress, this.player1, this.player2));
    }

    @Override
    protected void execute() {
        WorldInteraction worldInteraction = null;

        while ((worldInteraction = this.worldInteractionChannel.poll()) != null) {
            switch (worldInteraction) {
            case Attack:
                this.attack(this.player1, this.player2);
                break;
            case IncreaseArmy:
                this.increaseArmy(this.player1);
                break;
            case IncreaseIndustry:
                this.increaseIndustry(this.player1);
                break;
            default:
                throw new NullPointerException("Unknown world interaction : " + worldInteraction);
            }

            this.performComputerTurn();

            this.progress = (int) ((100 * this.player2.armySize)
                    / ((double) this.player1.armySize + this.player2.armySize));
            this.round++;
        }
    }

    private void performComputerTurn() {
        double attackChance = 100;
        double increaseIndustryChance = 100;
        double increaseArmyChance = 100;

        // don't attack if our army is smaller !
        if (this.player1.armySize > this.player2.armySize) {
            attackChance *= 0.50;
        }

        // build more factories
        if (this.player1.factories > this.player2.factories) {
            increaseIndustryChance *= 1.50;
        }

        // if our army is MUCH smaller ...
        if (this.player1.armySize > 1.25 * this.player2.armySize) {
            attackChance *= 0.25;
            increaseArmyChance *= 2.50;
        }

        double chance = this.random.nextDouble() * (increaseArmyChance + increaseIndustryChance + attackChance);

        if (chance <= attackChance) {
            this.attack(this.player2, this.player1);
            return;
        }
        chance -= attackChance;

        if (chance <= increaseIndustryChance) {
            this.increaseIndustry(this.player2);
            return;
        }
        chance -= increaseIndustryChance;

        this.increaseArmy(this.player2);
    }

    private void attack(PlayerData attacker, PlayerData defender) {
        // efficency between 10% and 20%
        double attackEfficency = 10 + this.random.nextDouble() * 10;
        double defenceEfficency = 10 + this.random.nextDouble() * 10;
        double absoluteAttackDamage = (attackEfficency * attacker.armySize) / 100;
        double absoluteDefenceDamage = (defenceEfficency * defender.armySize) / 100;

        // subtract
        attacker.armySize -= (int) absoluteDefenceDamage;
        defender.armySize -= (int) absoluteAttackDamage;

        // limit
        attacker.armySize = Math.max(attacker.armySize, 0);
        defender.armySize = Math.max(defender.armySize, 0);
    }

    private void increaseArmy(PlayerData player) {
        player.armySize += player.factories;
    }

    private void increaseIndustry(PlayerData player) {
        player.factories++;
    }
}
