package net.matthiasauer.stwp4j.libgdx.miniwar.model;

public class WorldSnapShot {
    public final double progress;
    public final int player1Factories;
    public final int player2Factories;
    public final int player1Army;
    public final int player2Army;

    public WorldSnapShot(double progress, int player1Factories, int player2Factories, int player1Army,
            int player2Army) {
        this.progress = progress;
        this.player1Army = player1Army;
        this.player2Army = player2Army;
        this.player1Factories = player1Factories;
        this.player2Factories = player2Factories;
    }
}
