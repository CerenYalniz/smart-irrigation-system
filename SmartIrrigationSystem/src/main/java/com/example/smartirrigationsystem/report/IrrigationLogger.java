package com.example.smartirrigationsystem.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * IrrigationLogger Sınıfı
 * * Bu sınıf, sistemin tarihsel kayıtlarını (loglarını) tutan gelişmiş raporlama birimidir.
 * Verileri aylık bazda CSV dosyalarına ayırarak düzenli bir arşivleme sağlar.
 */
public class IrrigationLogger {

    // Dosya isimleri için ay formatı (Örn: 2023-10)
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    // Kayıtlar içindeki tam zaman damgası formatı (Örn: 2023-10-25 14:30:05)
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * log Metodu (Statik)
     * * Sistemin gerçekleştirdiği her işlemi detaylıca bir dosyaya yazar.
     * @param source Verinin kaynağı (Örn: Kullanıcı Girişi)
     * @param moisture Kaydedilen toprak nemi
     * @param temperature Kaydedilen sıcaklık
     * @param decision Sistemin verdiği karar (SULAMA BAŞLATILDI / DURDURULDU)
     */
    public static void log(String source, double moisture, double temperature, String decision) {
        try {
            // Mevcut ayın ismini alarak dosya isimlendirmesinde kullanıyoruz
            String month = LocalDate.now().format(MONTH_FMT);

            // 'reports' adında bir klasör oluşturulup oluşturulmadığını kontrol ediyoruz
            File folder = new File("reports");
            if (!folder.exists()) folder.mkdirs();

            // Aylık dosya nesnesini oluşturuyoruz (Örn: reports/irrigation_2023-10.csv)
            File file = new File(folder, "irrigation_" + month + ".csv");
            boolean newFile = !file.exists();

            // Dosyayı 'append' (üstüne ekleme) modunda açıyoruz
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

                // Eğer dosya ilk kez oluşturuluyorsa, CSV başlıklarını (Header) ekliyoruz
                if (newFile) {
                    writer.write("timestamp,source,moisture,temperature,decision");
                    writer.newLine();
                }

                // Kayıt anındaki tam tarih ve saati alıyoruz
                String time = LocalDateTime.now().format(DATE_TIME_FMT);

                // Verileri virgülle ayırarak (CSV formatında) dosyaya satır olarak yazıyoruz
                writer.write(String.format("%s,%s,%.2f,%.2f,%s",
                        time, source, moisture, temperature, decision));
                writer.newLine();
            }

        } catch (Exception e) {
            // Herhangi bir dosya yazma hatasında kullanıcıyı konsoldan bilgilendiriyoruz
            System.out.println("Log yazma hatası: " + e.getMessage());
        }
    }
}