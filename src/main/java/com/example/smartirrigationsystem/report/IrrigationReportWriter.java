package com.example.smartirrigationsystem.report;

import java.io.FileWriter;
import java.time.LocalDateTime;

/**
 * IrrigationReportWriter Sınıfı
 * * Bu sınıf, sulama analiz sonuçlarını makinelerin değil, insanların okuyabileceği
 * düz metin (.txt) formatında bir günlük (log) dosyasına kaydetmekten sorumludur.
 */
public class IrrigationReportWriter {

    /**
     * writeReport Metodu (Statik)
     * * Analiz sonuçlarını "aylik_sulama_raporu.txt" dosyasına detaylı bir liste olarak ekler.
     * * @param source Verinin kaynağı (Örn: "Kullanıcı Girişi")
     * @param moisture Ölçülen toprak nemi değeri
     * @param temperature Ölçülen hava sıcaklığı değeri
     * @param irrigated Sulama işleminin yapılıp yapılmadığı (true/false)
     * @param reason Sistemin bu kararı verme gerekçesi (Örn: "Aşırı Sıcaklık Riski")
     */
    public static void writeReport(
            String source,
            double moisture,
            double temperature,
            boolean irrigated,
            String reason
    ) {
        // 'true' parametresi dosyanın üstüne yazmak yerine altına ekleme (append) yapılmasını sağlar.
        // try-with-resources yapısı sayesinde dosya işlemi bitince otomatik kapatılır.
        try (FileWriter writer = new FileWriter("aylik_sulama_raporu.txt", true)) {

            // Raporun her bir alanı anlaşılır etiketlerle satır satır dosyaya yazdırılır
            writer.write("Tarih: " + LocalDateTime.now() + "\n");
            writer.write("Kaynak: " + source + "\n");
            writer.write("Nem: " + moisture + "\n");
            writer.write("Sıcaklık: " + temperature + "\n");

            // Boolean sonucu (true/false) kullanıcı için "EVET/HAYIR" şeklinde görselleştirilir
            writer.write("Sulama: " + (irrigated ? "EVET" : "HAYIR") + "\n");
            writer.write("Neden: " + reason + "\n");

            // Farklı zamanlarda yapılan kayıtları ayırmak için görsel bir çizgi eklenir
            writer.write("-----------------------------\n");

        } catch (Exception e) {
            // Dosya oluşturulamazsa veya yazılamazsa konsola hata mesajı verilir
            System.out.println("Rapor yazılamadı: " + e.getMessage());
        }
    }
}