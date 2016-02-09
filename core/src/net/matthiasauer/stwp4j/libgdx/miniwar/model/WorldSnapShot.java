package net.matthiasauer.stwp4j.libgdx.miniwar.model;

public class WorldSnapShot {
    public int round;
    public final double progress;
    public final int player1Factories;
    public final int player2Factories;
    public final int player1Army;
    public final int player2Army;

    WorldSnapShot(int round, double progress, PlayerData player1, PlayerData player2) {
        this.round = round;
        this.progress = progress;
        this.player1Army = player1.armySize;
        this.player2Army = player2.armySize;
        this.player1Factories = player1.factories;
        this.player2Factories = player2.factories;
    }
}
