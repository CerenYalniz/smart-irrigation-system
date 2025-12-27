package com.example.smartirrigationsystem.receiver;

import com.example.smartirrigationsystem.data.IrrigationData;
import com.example.smartirrigationsystem.report.IrrigationReportWriter;
import java.io.*;
import java.time.LocalDateTime;

/**
 * IrrigationSystem Sınıfı (Receiver)
 * * Bu sınıf, sulama sisteminin kendisini temsil eder ve gerçek eylemleri gerçekleştirir.
 * Komutların (Command) ulaştığı 'Alıcı' (Receiver) nesnesidir.
 * Hem sulama durumunu yönetir hem de analiz sonuçlarını dosyalara kaydeder.
 */
public class IrrigationSystem {
    // Sistemin anlık çalışma durumunu tutar
    private String currentStatus = "DURDURULDU";

    // Analizlerin kaydedileceği yeni CSV dosyasının adı
    private final String logCsvFile = "yeni_eklenen_veriler.csv";

    /**
     * Sulama işlemini başlatır ve durumu günceller.
     */
    public void startIrrigation() {
        this.currentStatus = "SULAMA BAŞLATILDI";
    }

    /**
     * Sulama işlemini durdurur ve durumu günceller.
     */
    public void stopIrrigation() {
        this.currentStatus = "SULAMA DURDURULDU";
    }

    /**
     * @return Sistemin o anki durumunu (Başlatıldı/Durduruldu) döndürür.
     */
    public String getStatus() { return currentStatus; }

    /**
     * saveAndReport Metodu
     * * Yapılan analizi ve sistemin verdiği kararı iki farklı dosyaya raporlar.
     * @param data Kullanıcının girdiği çevresel veriler.
     * @param reason Sistemin bu kararı verme gerekçesi (Örn: Aşırı Sıcaklık).
     */
    public void saveAndReport(IrrigationData data, String reason) {
        // --- 1. Yeni CSV'ye Kaydet (Tablo Formatında Saklama) ---
        // 'true' parametresi dosyanın üstüne yazmak yerine altına ekleme (append) yapılmasını sağlar.
        try (PrintWriter pw = new PrintWriter(new FileWriter(logCsvFile, true))) {
            pw.printf("%s,%.2f,%.2f,%.2f,%.2f,%s\n",
                    LocalDateTime.now(), // İşlem zamanı
                    data.sicaklik,      // Kullanıcı sıcaklık girişi
                    data.nem,           // Kullanıcı hava nemi girişi
                    data.toprakNemi,    // Kullanıcı toprak nemi girişi
                    data.isikSuresi,    // Kullanıcı ışık süresi girişi
                    currentStatus);     // Sistemin verdiği son karar
        } catch (IOException e) { e.printStackTrace(); }

        // --- 2. Rapor Dosyasına (TXT) Detaylı Yaz ---
        // Bu adımda yardımcı bir sınıf olan IrrigationReportWriter çağrılır.
        IrrigationReportWriter.writeReport(
                "Kullanıcı Girişi",
                data.toprakNemi,
                data.sicaklik,
                currentStatus.contains("BAŞLATILDI"), // Sulama yapıldı mı? (Boolean)
                reason
        );
    }

    /**
     * getMonthlyReport Metodu
     * * Kaydedilen tüm analiz verilerini (yeni_eklenen_veriler.csv) okuyarak
     * arayüzde (FxMain) gösterilmek üzere tek bir metin (String) haline getirir.
     * @return Kayıtlı tüm verilerin listesi.
     */
    public String getMonthlyReport() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(logCsvFile))) {
            String line;
            // Dosyayı sonuna kadar satır satır oku
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            // Dosya henüz oluşturulmamışsa (ilk çalıştırma) bu mesaj döner.
            return "Rapor henüz oluşmadı.";
        }
        return sb.toString();
    }
}