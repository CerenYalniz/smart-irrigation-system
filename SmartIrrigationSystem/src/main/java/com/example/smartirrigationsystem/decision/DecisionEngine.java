package com.example.smartirrigationsystem.decision;

import com.example.smartirrigationsystem.command.*;
import com.example.smartirrigationsystem.data.IrrigationData;
import com.example.smartirrigationsystem.receiver.IrrigationSystem;

/**
 * DecisionEngine Sınıfı
 * * Bu sınıf, sistemin 'Mantıksal Karar Merkezi'dir.
 * Sensörlerden gelen verileri ve veri setinden gelen referans değerleri kıyaslayarak
 * sulamanın başlayıp başlamayacağına karar verir.
 */
public class DecisionEngine {

    /**
     * decide Metodu
     * * Gelen verilere göre uygun Komut (Command) nesnesini oluşturur.
     * * @param inputData Sensörlerden gelen anlık sıcaklık, nem ve ışık verileri.
     * @param referenceMoisture CsvSensorReader tarafından hesaplanan ideal toprak nemi.
     * @param system Komutların uygulanacağı ana sulama sistemi nesnesi.
     * @return Çalıştırılmaya hazır bir Command (Start veya Stop) nesnesi döndürür.
     */
    public Command decide(IrrigationData inputData, double referenceMoisture, IrrigationSystem system) {

        // --- ŞARTLARIN TANIMLANMASI (Risk Analizi) ---

        // 1. Şart: Ölçülen toprak nemi, o hava koşulları için hesaplanan idealin altındaysa risk vardır.
        boolean toprakSusuz = inputData.toprakNemi < referenceMoisture;

        // 2. Şart: Hava sıcaklığı kritik eşik olan 35 derecenin üstündeyse bitki koruma amaçlı risk vardır.
        boolean asiriSicak = inputData.sicaklik > 35.0;

        // 3. Şart: Hava nemi %20'nin altına düşerse aşırı kuraklık riski oluşur.
        boolean havaCokKuru = inputData.nem < 20.0;

        // --- KARAR VE NEDEN BELİRLEME ---

        // 'Veya' (||) mantığıyla: Bu üç riskten HERHANGİ BİRİ gerçekleşirse sulama tetiklenir.
        if (toprakSusuz || asiriSicak || havaCokKuru) {

            String sebep = "";

            /**
             * ÖNCELİK SIRALAMASI:
             * Raporlarda en kritik durumun görünmesi için önce sıcaklık, sonra toprak nemi kontrol edilir.
             */
            if (asiriSicak) {
                sebep = "Aşırı Sıcaklık Riski";
            } else if (toprakSusuz) {
                sebep = "Düşük Toprak Nemi";
            } else {
                sebep = "Düşük Hava Nemi (Kuraklık)";
            }

            // Konsola bilgilendirme mesajı yazdırılır.
            System.out.println("Otomatik Karar: SULAMA BAŞLATILDI. Neden: " + sebep);

            // Command Pattern gereği başlatma komutu nesnesi oluşturulup döndürülür.
            return new StartIrrigationCommand(system);

        } else {
            // Hiçbir risk faktörü bulunmuyorsa sistem güvenli durumdadır.
            System.out.println("Otomatik Karar: SULAMA GEREKSİZ. Koşullar uygun.");

            // Mevcut sulamayı durduracak veya kapalı tutacak komut nesnesi döndürülür.
            return new StopIrrigationCommand(system);
        }
    }
}