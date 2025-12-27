package com.example.smartirrigationsystem.command;
import com.example.smartirrigationsystem.receiver.IrrigationSystem;

/**
 * StopIrrigationCommand Sınıfı
 * * Bu sınıf, devam eden sulama işlemini durdurmak için kullanılan 'Somut Komut' (Concrete Command) yapısıdır.
 * Command arayüzünü uygulayarak, sulama sisteminin kapatılma mantığını bağımsız bir nesne haline getirir.
 */
public class StopIrrigationCommand implements Command {

    // İşlemin uygulanacağı hedef sistem (Alıcı/Receiver)
    private IrrigationSystem irrigationSystem;

    /**
     * Yapıcı Metot (Constructor)
     * * @param irrigationSystem Sulama komutunun iletileceği ana sistem nesnesi.
     */
    public StopIrrigationCommand(IrrigationSystem irrigationSystem) {
        this.irrigationSystem = irrigationSystem;
    }

    /**
     * execute() Metodu
     * * Command arayüzünden devralınan bu metot, komut sunucu (invoker) tarafından çağrıldığında çalışır.
     * irrigationSystem nesnesinin stopIrrigation() metodunu tetikleyerek sulamayı sonlandırır.
     */
    @Override
    public void execute() {
        irrigationSystem.stopIrrigation();
    }
}