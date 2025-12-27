package com.example.smartirrigationsystem.fx;

// Projenin farklı katmanlarından (Logic, Data, Command) gerekli sınıfların import edilmesi
import com.example.smartirrigationsystem.command.Command;
import com.example.smartirrigationsystem.data.CsvSensorReader;
import com.example.smartirrigationsystem.data.IrrigationData;
import com.example.smartirrigationsystem.data.IrrigationLog;
import com.example.smartirrigationsystem.decision.DecisionEngine;
import com.example.smartirrigationsystem.invoker.CommandServer;
import com.example.smartirrigationsystem.receiver.IrrigationSystem;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * FxMain Sınıfı: Uygulamanın grafiksel arayüzünü (GUI) yöneten ana sınıftır.
 * Katmanlı mimari yapısındaki tüm bileşenleri burada birleştirir.
 */
public class FxMain extends Application {

    // Alt sistemlerin (Core Components) nesne örneklerinin oluşturulması
    private final IrrigationSystem sistem = new IrrigationSystem(); // İşlemleri yapan Alıcı (Receiver)
    private final DecisionEngine motor = new DecisionEngine();     // Karar mekanizması (Beyin)
    private final CsvSensorReader okuyucu = new CsvSensorReader();   // Mendeley Veri Seti Okuyucusu
    private final CommandServer sunucu = new CommandServer();       // Komutları tetikleyen Yürütücü (Invoker)

    // Uygulama açık olduğu sürece yapılan işlemleri bellekte tutan liste (Usage uyarısını önler)
    private final List<IrrigationLog> loglar = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        // --- ANA PANEL DÜZENLEMESİ ---
        VBox anaPanel = new VBox(15);
        anaPanel.setPadding(new Insets(20));
        anaPanel.setAlignment(Pos.TOP_CENTER);
        anaPanel.setStyle("-fx-background-color: #e4f3e4;"); // Açık yeşil tema (Doğa dostu görünüm)

        // --- BAŞLIK ALANI ---
        Label baslik = new Label("AKILLI SULAMA KONTROL PANELİ");
        baslik.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        baslik.setTextFill(Color.web("#1e7d34"));

        // --- GİRİŞ KARTI (INPUT CARD) ---
        VBox kart = new VBox(12);
        kart.setAlignment(Pos.CENTER);
        kart.setPadding(new Insets(18));
        kart.setMaxWidth(520);
        // Modern kart tasarımı için gölge efekti ve yuvarlatılmış köşeler
        kart.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 4);");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(14);
        inputGrid.setVgap(12);
        inputGrid.setAlignment(Pos.CENTER);

        // Kullanıcıdan alınacak 4 kritik veri alanı
        TextField tSicak = createField("Örn: 28");
        TextField tHavaN = createField("Örn: 45");
        TextField tToprakN = createField("Örn: 25");
        TextField tIsik = createField("Örn: 12");

        // Etiketlerin ve metin alanlarının ızgara yapısına yerleştirilmesi
        inputGrid.add(makeLabel("Sıcaklık (°C):"), 0, 0);
        inputGrid.add(tSicak, 1, 0);

        inputGrid.add(makeLabel("Hava Nemi (%):"), 0, 1);
        inputGrid.add(tHavaN, 1, 1);

        inputGrid.add(makeLabel("Toprak Nemi (%):"), 0, 2);
        inputGrid.add(tToprakN, 1, 2);

        inputGrid.add(makeLabel("Işık Süresi (saat):"), 0, 3);
        inputGrid.add(tIsik, 1, 3);

        // --- ETKİLEŞİM BUTONLARI ---
        HBox butonlar = new HBox(16);
        butonlar.setAlignment(Pos.CENTER);

        Button btnBaslat = new Button("📊 ANALİZİ BAŞLAT");
        Button btnRapor = new Button("📄 RAPORU GÖRÜNTÜLE");

        styleBtn(btnBaslat, "#2ecc71"); // Onay yeşili
        styleBtn(btnRapor, "#2d7df6");  // Bilgi mavisi

        butonlar.getChildren().addAll(btnBaslat, btnRapor);

        // --- ANLIK DURUM VE GEREKÇE ETİKETLERİ ---
        Label lDurum = new Label("Durum: Beklemede");
        lDurum.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lDurum.setTextFill(Color.web("#333"));

        Label lGerekce = new Label("Gerekçe: -");
        lGerekce.setFont(Font.font("Arial", 13));
        lGerekce.setTextFill(Color.web("#0a7aa3"));

        kart.getChildren().addAll(inputGrid, butonlar, lDurum, lGerekce);

        // --- TERMİNAL EKRANI (LOG İZLEME) ---
        TextArea terminal = new TextArea();
        terminal.setEditable(false);
        terminal.setWrapText(true);
        terminal.setPrefHeight(260);
        terminal.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-font-family: 'Consolas';" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: #111;"
        );

        // --- ANALİZ BUTONU AKSİYONU ---
        btnBaslat.setOnAction(e -> {
            try {
                // 1. Arayüzden verilerin sayısal olarak okunması
                double s = Double.parseDouble(tSicak.getText().trim());
                double h = Double.parseDouble(tHavaN.getText().trim());
                double tn = Double.parseDouble(tToprakN.getText().trim());
                double is = Double.parseDouble(tIsik.getText().trim());

                // 2. Verilerin Data sınıfına kapsüllenmesi
                IrrigationData veri = new IrrigationData(s, h, tn, is);

                // 3. Mendeley Veri Seti Üzerinden Dinamik Referans (Zeka) Belirleme
                // Kullanıcının girdiği sıcaklık ve neme göre veri setinden ideal toprak nemini bulur.
                double ideal = okuyucu.getDynamicReference(s, h, is);

                // 4. Command Pattern: Karar mekanizmasının çalıştırılması
                Command komut = motor.decide(veri, ideal, sistem);
                sunucu.setCommand(komut);
                sunucu.executeProcess();

                // 5. Karar Gerekçesinin Belirlenmesi (Açıklanabilir Mantık)
                String gerekce;
                if (s > 35.0) {
                    gerekce = "Kritik Sıcaklık Koruması Aktif";
                } else if (tn < ideal) {
                    gerekce = "Toprak Nemi İdeal Seviyenin Altında (İdeal: " + String.format("%.2f", ideal) + "%)";
                } else if (h < 20.0) {
                    gerekce = "Düşük Hava Nemi (Kuraklık Riski)";
                } else {
                    gerekce = "Koşullar Bitki İçin İdeal";
                }

                // 6. Sonuçların Veri Tabanı / Dosya Sistemine Kaydedilmesi
                // Bu metot TXT ve CSV çıktılarını aynı anda üretir.
                sistem.saveAndReport(veri, gerekce);

                String durum = sistem.getStatus();

                // 7. Arayüz Elemanlarının Güncellenmesi
                lDurum.setText("Durum: " + durum);
                lGerekce.setText("Gerekçe: " + gerekce);

                // 8. Terminale Detaylı Log Yazdırılması
                terminal.appendText(
                        "\n[" + LocalDateTime.now().withNano(0) + "]\n" +
                                "Girilen Veriler -> Sıcaklık: " + s + "°C, Hava Nemi: " + h + "%, Toprak Nemi: " + tn + "%, Işık: " + is + " saat\n" +
                                "Hesaplanan İdeal Nem -> " + String.format("%.2f", ideal) + "%\n" +
                                "Karar -> " + durum + "\n" +
                                "Gerekçe -> " + gerekce + "\n" +
                                "---------------------------------------------\n"
                );

                // 9. Oturum Logu: MonthlyReport modülü için nesne oluşturma (Usage Bağlantısı)
                loglar.add(new IrrigationLog(durum + " (" + gerekce + ")", LocalDateTime.now()));

            } catch (Exception ex) {
                terminal.appendText("\n[HATA] Lütfen tüm alanlara sayısal değer giriniz.\n");
            }
        });

        // --- RAPOR BUTONU AKSİYONU ---
        btnRapor.setOnAction(e -> {
            // Kayıtlı dosyaları ve oturum loglarını terminale basar
            terminal.setText(buildReportText());
        });

        anaPanel.getChildren().addAll(baslik, kart, terminal);

        // Sahne kurulumu ve pencere gösterimi
        Scene scene = new Scene(anaPanel, 620, 720);
        stage.setTitle("Smart Irrigation AI v3.5");
        stage.setScene(scene);
        stage.show();
    }

    // --- Görsel Bileşen Oluşturucu Yardımcı Metotlar ---

    private TextField createField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefWidth(220);
        f.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: #dcdcdc;" +
                        "-fx-padding: 10;"
        );
        return f;
    }

    private Label makeLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        l.setTextFill(Color.web("#333"));
        return l;
    }

    private void styleBtn(Button b, String color) {
        b.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 10 16;" +
                        "-fx-cursor: hand;"
        );
    }

    // --- Sistem Verilerini ve Kayıtlı Dosyaları Okuyup Raporlayan Metot ---
    private String buildReportText() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== SİSTEM RAPOR PANELİ =====\n\n");

        // 1) Kalıcı TXT raporu bilgisi
        sb.append("[1] TXT RAPOR DOSYASI: aylik_sulama_raporu.txt\n");
        sb.append("Bu dosyaya her analiz sonrası detaylı kayıt eklenir.\n\n");

        // 2) Dinamik CSV dosyası okuma işlemi
        sb.append("[2] CSV KAYITLARI: yeni_eklenen_veriler.csv\n");
        sb.append("Format: zaman,sicaklik,nem,toprakNemi,isikSuresi,karar\n\n");

        sb.append("--- CSV İÇERİĞİ ---\n");
        try (BufferedReader br = new BufferedReader(new FileReader("yeni_eklenen_veriler.csv"))) {
            String line;
            int satir = 0;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                satir++;
                // TextArea'yı yormamak için limitlendirme
                if (satir >= 200) {
                    sb.append("\n... (devamı dosyada)\n");
                    break;
                }
            }
        } catch (Exception ex) {
            sb.append("Henüz kayıt yok (dosya oluşmadı) veya okunamadı.\n");
        }

        // 3) RAM üzerinde tutulan oturum günlüğü (MonthlyReport mantığı)
        sb.append("\n--- OTURUM İŞLEMLERİ (RAM LOG) ---\n");
        if (loglar.isEmpty()) {
            sb.append("Bu oturumda henüz işlem yapılmadı.\n");
        } else {
            for (IrrigationLog log : loglar) {
                sb.append(log.getTime().withNano(0)).append(" -> ").append(log.getAction()).append("\n");
            }
        }

        sb.append("\n===============================\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args); // JavaFX uygulamasını başlatır
    }
}