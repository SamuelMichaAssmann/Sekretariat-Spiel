package de.jspll.data.objects.game.tasks;

import de.jspll.handlers.GameObjectHandler;

public interface iTaskReaction {

    int goodSelection(GameObjectHandler gOH);
    int badSelection(GameObjectHandler gOH);
}
