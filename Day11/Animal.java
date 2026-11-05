public abstract class Animal {

    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    // Her alt sınıf kendi sesini tanımlamak zorunda
    public abstract void makeSound();

    // Tüm hayvanlarda ortak davranış
    public void breathe() {
        System.out.println(name + " nefes alıyor...");
    }

    public String getName() { return name; }
}
