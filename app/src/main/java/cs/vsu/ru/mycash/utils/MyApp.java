package cs.vsu.ru.mycash.utils;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String API_key = "ec6eff70-af6c-4f05-80a2-33d40914193c";
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(API_key).build();
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
