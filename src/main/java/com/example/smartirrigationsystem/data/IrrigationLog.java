package com.example.smartirrigationsystem.data;

import java.time.LocalDateTime;

/**
 * IrrigationLog Sınıfı (Veri Modeli / DTO)
 * * Bu sınıf, sistemin gerçekleştirdiği her bir sulama işleminin kaydını tutar.
 * * Nesne Yönelimli Programlamanın "Kapsülleme" (Encapsulation) prensibi kullanılarak,
 * verilerin güvenliği ve tutarlılığı sağlanmıştır.
 */
public class IrrigationLog {

    // İşlemin adı (Örn: "SULAMA BAŞLATILDI") - private yapı ile dışarıdan müdahale engellenir.
    private String action;

    // İşlemin gerçekleştiği tam zaman damgası.
    private LocalDateTime time;

    /**
     * Yapıcı Metot (Constructor)
     * * Yeni bir log nesnesi oluşturulduğunda eylem ve zaman verilerini sabitler.
     * @param action Gerçekleştirilen sistem eylemi
     * @param time Eylemin kaydedildiği anlık tarih-saat bilgisi
     */
    public IrrigationLog(String action, LocalDateTime time) {
        this.action = action;
        this.time = time;
    }

    /**
     * getAction Metodu
     * * MonthlyReport sınıfı tarafından işlem adını okumak için kullanılır.
     * * Bu metot çağrıldığında IntelliJ üzerindeki "no usages" uyarısı kaybolur.
     * @return Kayıtlı eylem metni
     */
    public String getAction() {
        return action;
    }

    /**
     * getTime Metodu
     * * İşlemin ne zaman yapıldığını raporlamak için kullanılır.
     * * LocalDateTime formatındaki veriyi raporlama katmanına taşır.
     * @return Eylemin gerçekleştiği zaman damgası
     */
    public LocalDateTime getTime() {
        return time;
    }
}