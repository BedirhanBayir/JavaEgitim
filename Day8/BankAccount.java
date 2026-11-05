public class BankAccount {

    private String accountNumber;
    private String owner;
    private double balance;

    public BankAccount(String accountNumber, String owner, double initialBalance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        if (initialBalance < 0) {
            System.out.println("Uyarı: Başlangıç bakiyesi negatif olamaz. 0 atandı.");
            this.balance = 0;
        } else {
            this.balance = initialBalance;
        }
    }

    // Getter'lar (balance için bilerek setter yok)
    public String getAccountNumber() { return accountNumber; }
    public String getOwner()         { return owner; }
    public double getBalance()       { return balance; }

    // Para yatırma
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Hata: Yatırılan tutar pozitif olmalıdır.");
            return;
        }
        balance += amount;
        System.out.printf("%.2f TL yatırıldı. Yeni bakiye: %.2f TL%n", amount, balance);
    }

    // Para çekme
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Hata: Çekilen tutar pozitif olmalıdır.");
            return;
        }
        if (amount > balance) {
            System.out.println("Hata: Yetersiz bakiye.");
            return;
        }
        balance -= amount;
        System.out.printf("%.2f TL çekildi. Kalan bakiye: %.2f TL%n", amount, balance);
    }

    // Hesap özeti
    public void printStatement() {
        System.out.println("============================");
        System.out.println("Hesap No : " + accountNumber);
        System.out.println("Sahip    : " + owner);
        System.out.printf("Bakiye   : %.2f TL%n", balance);
        System.out.println("============================");
    }
}
