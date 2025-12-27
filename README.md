# Smart Irrigation System

Bu proje, sensör verileri ve kullanıcıdan alınan çevresel bilgiler doğrultusunda
akıllı sulama kararları verebilen bir **JavaFX tabanlı Akıllı Sulama Sistemi** uygulamasıdır.

Sistem, geçmiş sensör verilerini (CSV) analiz ederek ideal toprak nemi referansı oluşturur
ve bu referans ile kullanıcı verilerini karşılaştırarak sulama başlatma veya durdurma
kararı üretir.

---

## Özellikler

- Kullanıcıdan veri alma (Sıcaklık, Hava Nemi, Toprak Nemi, Işık Süresi)
- CSV veri setinden geçmiş sensör verisi okuma
- Dinamik karar mekanizması (Decision Engine)
- Command Design Pattern kullanımı
- Otomatik sulama başlatma / durdurma
- TXT ve CSV formatında raporlama
- Aylık sulama raporu oluşturma
- Modern JavaFX arayüzü

---

## Kullanılan Teknolojiler

- Java (JDK 21)
- JavaFX
- Maven
- CSV / TXT dosya işlemleri
- Command Design Pattern

## Proje Yapısı

```
com.example.smartirrigationsystem
│
├── command → Command Pattern sınıfları
├── receiver → Sulama sisteminin kendisi
├── invoker → Komut tetikleyici
├── decision → Karar motoru
├── data → Veri modelleri ve CSV okuma
├── report → Raporlama sınıfları
├── fx → JavaFX arayüzü
└── main → Uygulama giriş noktası
```

---

## ▶Çalıştırma

### Maven ile (Önerilen)

JavaFX bağımlılıkları otomatik olarak yüklendiği için en sorunsuz yöntemdir.

```bash
mvn clean javafx:run
```

### IntelliJ IDEA Üzerinden (FxMain)

FxMain.java dosyasına sağ tık → Run

Run Configuration → VM Options alanına aşağıdaki satırı ekle:

```bash
--module-path "C:\javafx-sdk-21.x\lib" --add-modules javafx.controls,javafx.fxml
```

---

## Raporlama ve Çıktılar

Uygulama çalıştığı süre boyunca aşağıdaki rapor ve kayıt dosyalarını otomatik olarak oluşturur:

aylik_sulama_raporu.txt
→ Yapılan her analiz sonrası, sulama kararı ve gerekçesi detaylı olarak kaydedilir.

yeni_eklenen_veriler.csv
→ Kullanıcıdan alınan veriler tablo formatında saklanır.

reports/irrigation_YYYY-MM.csv
→ Aylık bazda oluşturulan CSV log arşivleri.

---

## Karar Mekanizması

Sistem aşağıdaki risk faktörlerini değerlendirerek karar verir:

- Toprak neminin ideal referansın altında olması

- Hava sıcaklığının 35°C üzerine çıkması

- Hava neminin %20’nin altına düşmesi

Bu koşullardan herhangi biri sağlandığında sulama başlatılır,
aksi halde sulama durdurulur.

---

## Arayüz Özellikleri

- JavaFX ile geliştirilmiş kullanıcı arayüzü

- Kullanıcıdan sıcaklık, nem, toprak nemi ve ışık süresi girişi

- Analiz sonucu ve karar gerekçesinin anlık gösterimi

- Terminal benzeri rapor ve log görüntüleme alanı

---

## Kullanılan Tasarım Deseni

Command Design Pattern

- Komutlar arayüzden bağımsız çalışır.

- Sulama başlatma/durdurma işlemleri soyutlanmıştır.

- Sistem genişletilebilir ve sürdürülebilir hale getirilmiştir.

### Pattern Rolleri:

Command → Command

Concrete Command → StartIrrigationCommand, StopIrrigationCommand

Invoker → CommandServer

Receiver → IrrigationSystem

---

### Sonuç

Bu proje, geçmiş sensör verileri ve kullanıcıdan alınan anlık verileri
birlikte değerlendirerek sulama ihtiyacını akıllı şekilde belirleyen
bir karar destek sistemi sunmaktadır.

JavaFX arayüzü sayesinde kullanıcı dostu bir deneyim sağlanmış,
Command Design Pattern kullanımı ile temiz ve sürdürülebilir bir mimari elde edilmiştir.

---

## Ceren Yalnız

Bu proje eğitim amaçlı geliştirilmiştir.
