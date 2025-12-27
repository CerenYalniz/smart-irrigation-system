package com.example.smartirrigationsystem.main;
import com.example.smartirrigationsystem.fx.FxMain;
import javafx.application.Application;

/**
 * MainApp Sınıfı
 * * Bu sınıf, uygulamanın 'Giriş Noktası'dır (Entry Point).
 * Tüm projeyi başlatan ana main metodu burada yer alır.
 */
public class MainApp {

    /**
     * main Metodu
     * * Uygulama çalıştırıldığında ilk tetiklenen metottur.
     * @param args Komut satırı argümanları (varsa).
     */
    public static void main(String[] args) {
        /**
         * Application.launch: JavaFX yaşam döngüsünü başlatır.
         * FxMain.class: Arayüz tasarımının ve uygulama mantığının bulunduğu
         * ana JavaFX sınıfını (FxMain) parametre olarak alır ve ekrana getirir.
         */
        Application.launch(FxMain.class, args);
    }
}