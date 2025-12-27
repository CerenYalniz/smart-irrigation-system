package com.example.smartirrigationsystem.invoker;
import com.example.smartirrigationsystem.command.Command;

/**
 * CommandServer Sınıfı (Invoker)
 * * Bu sınıf, Komut Tasarım Deseni'ndeki 'Yürütücü' (Invoker) rolündedir.
 * Komut nesnelerini (Command) alır ve onları ne zaman çalıştıracağına karar verir.
 * Arayüz (UI) ile iş mantığı (Command) arasında bir köprü görevi görür.
 */
public class CommandServer {
    // Yürütülecek olan komutu tutan referans
    private Command command;

    /**
     * setCommand Metodu
     * * Çalıştırılmak istenen komutu sisteme yükler.
     * @param command StartIrrigationCommand veya StopIrrigationCommand nesnesi olabilir.
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * executeProcess Metodu
     * * Yüklü olan komutu tetikler.
     * null kontrolü yaparak sistemin hata almasını (NullPointerException) önler.
     */
    public void executeProcess() {
        if (command != null) {
            // Komut arayüzü üzerinden execute() metodunu çağırır.
            // Bu çağrı, gerçekte sulama sisteminin başlatılmasını veya durdurulmasını sağlar.
            command.execute();
        }
    }
}