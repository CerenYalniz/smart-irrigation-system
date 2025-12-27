package com.example.smartirrigationsystem.command;

/**
 * Command Arayüzü (Interface)
 * * Bu arayüz, Komut Tasarım Deseni'nin (Command Design Pattern) temelini oluşturur.
 * Sistemin yapacağı tüm işlemler (Sulama Başlat, Durdur vb.) bu arayüzü uygular.
 * Bu sayede ne tür bir komut gelirse gelsin, sistem sadece 'execute()' metodunu çağırarak işini yapar.
 */
public interface Command {

    /**
     * Komutu çalıştıran ana metot.
     * Bu metot çağrıldığında, ilgili komutun içeriğindeki işlem (sulama motoru gibi) tetiklenir.
     */
    void execute();
}