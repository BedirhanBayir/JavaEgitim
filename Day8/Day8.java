public class Day8 {
    public static void main(String[] args) {

        BankAccount account = new BankAccount("TR123456", "Bedirhan Bayır", 1000.0);
        account.printStatement();

        account.deposit(500);
        account.withdraw(200);
        account.withdraw(5000); // yetersiz bakiye
        account.deposit(-100);  // geçersiz tutar
        account.withdraw(0);    // geçersiz tutar

        // account.balance = -9999;  // DERLEME HATASI: balance private
        System.out.println("Getter ile bakiye: " + account.getBalance());

        account.printStatement();

        // Negatif başlangıç bakiyesi denemesi
        BankAccount invalid = new BankAccount("TR999999", "Test Kullanıcı", -50);
        invalid.printStatement();
    }
}
