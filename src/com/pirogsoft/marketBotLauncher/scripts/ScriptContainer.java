package com.pirogsoft.marketBotLauncher.scripts;

import java.util.List;

/**
 * Created by Andrey on 22.12.2016.
 */
public class ScriptContainer {

    /**
     * Список скриптов
     */
    private List<Script> scripList;

    public ScriptContainer(List<Script> scripList) {
        this.scripList = scripList;
    }

    /**
     * Запуск всех скриптов
     */
    public void runScripts() {
        for (Script script : scripList) {
            script.startThread();
        }
    }
}
