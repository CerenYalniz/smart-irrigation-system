package com.example.smartirrigationsystem.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * CsvSensorReader Sınıfı
 * resources içindeki sensor_veri_seti.csv dosyasını okur.
 * Kullanıcı verisine benzer kayıtları bularak ideal toprak nemi referansı üretir.
 */
public class CsvSensorReader {

    // resources içindeki dosya adı
    private static final String RESOURCE_NAME = "/sensor_veri_seti.csv";

    /**
     * Kullanıcının anlık verisine benzer geçmiş kayıtları bulur ve onların toprak nemi ortalamasını döndürür.
     * Benzerlik toleransları:
     * - Sıcaklık: ±5
     * - Hava Nemi: ±15
     * - Işık Süresi: ±2 saat
     *
     * @return ideal toprak nemi referansı (bulunamazsa 35.0)
     */
    public double getDynamicReference(double userTemp, double userHum, double userLight) {
        double totalMoisture = 0;
        int count = 0;

        try {
            InputStream is = CsvSensorReader.class.getResourceAsStream(RESOURCE_NAME);
            if (is == null) {
                // Dosya bulunamazsa güvenli varsayılan
                return 35.0;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                br.readLine(); // header satırı
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] v = line.split(",");
                    // CSV kolon sırası varsayımı:
                    // 0:tarih, 1:sicaklik, 2:havaNemi, 3:toprakNemi, 4:isikSuresi
                    if (v.length < 5) continue;

                    double rowTemp = Double.parseDouble(v[1].trim());
                    double rowHum = Double.parseDouble(v[2].trim());
                    double rowMoisture = Double.parseDouble(v[3].trim());
                    double rowLight = Double.parseDouble(v[4].trim());

                    boolean tempClose = Math.abs(rowTemp - userTemp) <= 5.0;
                    boolean humClose = Math.abs(rowHum - userHum) <= 15.0;
                    boolean lightClose = Math.abs(rowLight - userLight) <= 2.0;

                    if (tempClose && humClose && lightClose) {
                        totalMoisture += rowMoisture;
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            return 35.0;
        }

        return count > 0 ? (totalMoisture / count) : 35.0;
    }
}
