package com.example.smartirrigationsystem.report;

import com.example.smartirrigationsystem.data.IrrigationLog;
import java.util.List;

/**
 * MonthlyReport Sınıfı
 * * Bu sınıf, sistemin çalışma süresi boyunca (session) biriktirdiği
 * sulama kayıtlarını terminal ekranına profesyonel bir formatta basar.
 */
public class MonthlyReport {

    /**
     * printReport Metodu (Statik)
     * * Bu metot, IrrigationLog nesnelerinden oluşan bir listeyi parametre olarak alır.
     * * Nesne yönelimli programlama (OOP) prensiplerine uygun olarak nesne metotlarını tetikler.
     * * @param logs FxMain tarafından biriktirilen sulama günlüğü listesi.
     */
    public static void printReport(List<IrrigationLog> logs) {
        // Rapor başlangıcını belirten görsel başlık
        System.out.println("\n--- 📊 PROFESYONEL SİSTEM DENETİM RAPORU ---");

        // Liste içindeki her bir 'IrrigationLog' nesnesi için döngü başlatılır
        for (IrrigationLog log : logs) {
            /**
             * USAGE (KULLANIM) NOKTASI:
             * Burada log.getAction() ve log.getTime() metotları bizzat çağrılır.
             * Bu sayede IntelliJ'deki "no usages" (kullanım yok) uyarısı ortadan kalkar.
             */
            System.out.println(
                    "EYLEM: " + log.getAction() + " | ZAMAN DAMGASI: " + log.getTime()
            );
        }

        // Raporun sonunda toplam işlem adedi özet bilgi olarak sunulur
        System.out.println("Toplam Takip Edilen İşlem Sayısı: " + logs.size());
        System.out.println("-------------------------------------------\n");
    }
}