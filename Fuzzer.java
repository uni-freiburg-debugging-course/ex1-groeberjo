import java.util.Random;

public class Fuzzer {
    private Random rand;

    public Fuzzer(){
        rand = new Random();
    }

    // Outputs a string that is a valid input for exercise 1
    public String generateFuzz() {
        String[] operators = {"+", "*", "-"};
        String returnStr = "(simplify (";
        returnStr = returnStr + operators[rand.nextInt(3)] + " ";
        int firstRandom = rand.nextInt(2000)  - 1000;
        int secondRandom = rand.nextInt(2000) - 1000;
        returnStr = returnStr + negativeFuzzer(firstRandom) + " " + negativeFuzzer(secondRandom);
        return (returnStr + "))");
    }

    // Returns num as string if num >= 0, otherwise it returns the string of num in the form (- num)
    private String negativeFuzzer(int num) {
        if (num >= 0) {
            return (String.valueOf(num));
        } else {
            return ("(- " + String.valueOf(num).replaceAll("-", "") + ")");
        }
    }
}
