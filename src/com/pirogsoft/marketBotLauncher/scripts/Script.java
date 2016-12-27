package com.pirogsoft.marketBotLauncher.scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Andrey on 22.12.2016.
 */
public class Script implements Runnable{

    /**
     * URL крипта
     */
    private URL url;

    /**
     * функция, которая выполняется каждый раз после прогона скрипта
     */
    IScriptCallback callback;

    /**
     * Включен ли скрипт
     */
    private boolean enabled;

    /**
     * Время между завершением выполнения скрипта и новым стартом
     */
    private int pause;

    /**
     * Поток, в котором выполняется запуск скрипта
     */
    private Thread thread;

    public Script(String url) {
        this(url, null);
    }

    public Script(String url, IScriptCallback callback ) {
        this(url, callback, true, 0);
    }

    public Script(String url, IScriptCallback callback, boolean enabled, int pause) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.callback = callback;
        this.enabled = enabled;
        this.pause = pause;
        thread = new Thread(this);
    }

    public String getUrl() {
        return url.getPath();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPause() {
        return pause;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    /**
     * Метод, который выполняется при запуске потока
     */
    @Override
    public void run() {
        while(true) {
            if(enabled) {
                String responce = executeScript();
                if(callback != null) {
                    callback.function(this, responce);
                }
                try {
                    thread.sleep(1000 * pause);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Метод для запуска потока
     */
    public void startThread() {
        if(!thread.isAlive())
            thread.start();
    }

    /**
     * Запускает скрипт
     * @return ответ
     */
    private String executeScript() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            connection.disconnect();
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
