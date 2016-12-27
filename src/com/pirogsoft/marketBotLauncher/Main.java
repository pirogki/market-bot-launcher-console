package com.pirogsoft.marketBotLauncher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pirogsoft.marketBotLauncher.scripts.IScriptCallback;
import com.pirogsoft.marketBotLauncher.scripts.Script;
import com.pirogsoft.marketBotLauncher.scripts.ScriptContainer;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static final String SETTINGS_FILE_NAME = "script_settings.json";

    public static void main(String[] args) {

        List<Script> scripts = new ArrayList<Script>();
        IScriptCallback callBack = new IScriptCallback() {
            @Override
            public void function(Script script, String response) {
                System.out.println("Запрос на адрес: " + script.getUrl());
                System.out.println("Ответ: " + response);
            }
        };

        ObjectMapper mapper = new ObjectMapper();


        List<JsonScriptRecord> myJsonScripts;
        try {
            myJsonScripts = mapper.readValue(new File(SETTINGS_FILE_NAME), new TypeReference<List<JsonScriptRecord>>(){});
        } catch (IOException e) {
            throw(new RuntimeException(e));
        }

        for(JsonScriptRecord jsonScriptRecord : myJsonScripts) {
            scripts.add(new Script(jsonScriptRecord.url, callBack, jsonScriptRecord.enabled, jsonScriptRecord.pause));
        }
        ScriptContainer sc = new ScriptContainer(scripts);
        sc.runScripts();
    }

    public static class JsonScriptRecord {
        public String url;
        public int pause;
        public boolean enabled;
    }

}
