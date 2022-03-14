package javase.thread.exec;

public class TestThreadNumber {
    public static void main(String[] args) {
        ShowEvenNumbers showEvenNumbers = new ShowEvenNumbers();
        ShowOddNumber showOddNumber = new ShowOddNumber();

        Thread thread = new Thread(showOddNumber);
        showEvenNumbers.start();
        thread.start();


    }
}
