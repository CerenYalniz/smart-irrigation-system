package com.example.smartirrigationsystem.data;

/**
 * IrrigationData Sınıfı
 * * Bu sınıf, sulama sistemi için gerekli olan çevresel verileri bir arada tutan bir veri modelidir.
 * Sensörlerden gelen anlık değerleri kapsülleyerek sistemin diğer bileşenlerine (DecisionEngine vb.) iletir.
 */
public class IrrigationData {

    // Ortam sıcaklığı (Santigrat derece cinsinden)
    public double sicaklik;

    // Hava nem oranı (Yüzde % cinsinden)
    public double nem;

    // Toprağın ölçülen anlık nem oranı (Yüzde % cinsinden)
    public double toprakNemi;

    // Günlük maruz kalınan ışık süresi (Saat cinsinden)
    public double isikSuresi;

    /**
     * Yapıcı Metot (Constructor)
     * * Bu metot, tüm çevresel verileri alarak bir veri nesnesi oluşturur.
     * * @param sicaklik Ölçülen hava sıcaklığı
     * @param nem Ölçülen hava nemi
     * @param toprakNemi Ölçülen toprak nemi
     * @param isikSuresi Toplam ışık alma süresi
     */
    public IrrigationData(double sicaklik, double nem, double toprakNemi, double isikSuresi) {
        this.sicaklik = sicaklik;
        this.nem = nem;
        this.toprakNemi = toprakNemi;
        this.isikSuresi = isikSuresi;
    }
}