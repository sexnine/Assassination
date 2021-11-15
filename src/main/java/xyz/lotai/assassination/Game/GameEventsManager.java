package xyz.lotai.assassination.Game;

import xyz.lotai.assassination.Game.Events.*;

public class GameEventsManager {
    private final GracePeriodEvents gracePeriodEvents;
    private final MainGameEvents mainGameEvents;
    private final RunningEvents runningEvents;
    private final StartingEvents startingEvents;
    private final WaitingEvents waitingEvents;

    public GameEventsManager() {
        gracePeriodEvents = new GracePeriodEvents();
        mainGameEvents = new MainGameEvents();
        runningEvents = new RunningEvents();
        startingEvents = new StartingEvents();
        waitingEvents = new WaitingEvents();
    }

    public void stopAll() {
        gracePeriodEvents.stop();
        mainGameEvents.stop();
        runningEvents.stop();
        startingEvents.stop();
        waitingEvents.stop();
    }

    public GracePeriodEvents getGracePeriodEvents() {
        return gracePeriodEvents;
    }

    public MainGameEvents getMainGameEvents() {
        return mainGameEvents;
    }

    public RunningEvents getRunningEvents() {
        return runningEvents;
    }

    public StartingEvents getStartingEvents() {
        return startingEvents;
    }

    public WaitingEvents getWaitingEvents() {
        return waitingEvents;
    }
}
