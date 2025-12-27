package com.example.smartirrigationsystem.command;
import com.example.smartirrigationsystem.receiver.IrrigationSystem;

/**
 * StartIrrigationCommand Sınıfı
 * * Bu sınıf, sulama işlemini başlatmak için kullanılan 'Somut Komut' (Concrete Command) yapısıdır.
 * Command arayüzünü uygulayarak (implements), sulama sisteminin başlatılma mantığını kapsüller.
 */
public class StartIrrigationCommand implements Command {

    // Komutun üzerinde işlem yapacağı 'Alıcı' (Receiver) nesne
    private IrrigationSystem irrigationSystem;

    /**
     * Yapıcı Metot (Constructor)
     * @param irrigationSystem Sulama işlemlerini yürütecek olan ana sistem nesnesi.
     */
    public StartIrrigationCommand(IrrigationSystem irrigationSystem) {
        this.irrigationSystem = irrigationSystem;
    }

    /**
     * execute() Metodu
     * * Command arayüzünden gelen bu metot, komut çağrıldığında çalışır.
     * Alıcı nesne olan irrigationSystem üzerinden gerçek sulama başlatma metodunu tetikler.
     */
    @Override
    public void execute() {
        irrigationSystem.startIrrigation();
    }
}