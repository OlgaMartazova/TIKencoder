import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        String line1 = sc.nextLine();
        String line1 = "a b c d e f";
        String[] letters = line1.split("\\s");
//        String line2 = sc.nextLine();
        String line2 = "0.36 0.18 0.18 0.12 0.09 0.07";
        double[] probs = Arrays.stream(line2.split("\\s")).mapToDouble(Double::parseDouble).toArray();
        int n = letters.length;
        List<Node> nodes = new ArrayList<>();

        String line3 = sc.nextLine();

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(letters[i], probs[i], 0, n));
        }

        sortByProbs(n, nodes);
        fano(0, n - 1, nodes);
        printResult(line3, n, nodes);
    }


    private static void fano(int left, int right, List<Node> nodes) {
        double group1 = 0;
        double total = 0;
        double diff1 = 0;
        if (left == right || left > right) {
            return;
        }
        if (right - left == 1) {
            nodes.get(left).code[nodes.get(left).topPosition] = 0;
            nodes.get(left).topPosition += 1;
            nodes.get(right).code[nodes.get(right).topPosition] = 1;
            nodes.get(right).topPosition += 1;
        } else {
            for (int i = left; i <= right; i++) {
                total += nodes.get(i).probability;
            }
            group1 = nodes.get(left).probability;
            diff1 = Math.abs(group1 - total/2);
            int board = 1;
            group1 += nodes.get(left + board).probability;
            double diff2 = Math.abs(group1 - total/2);
            while (diff2 < diff1) {
                diff1 = diff2;
                board++;
                group1 += nodes.get(left + board).probability;
                diff2 = Math.abs(group1 - total/2);
            }
            board--;
            board = board + left;
            for (int i = left; i <= board; i++) {
                nodes.get(i).code[nodes.get(i).topPosition] = 0;
                nodes.get(i).topPosition += 1;
            }
            for (int i = board + 1; i <= right; i++) {
                nodes.get(i).code[nodes.get(i).topPosition] = 1;
                nodes.get(i).topPosition += 1;
            }
            fano(left, board, nodes);
            fano(board + 1, right, nodes);
        }
    }

    private static int sumFromTo(int left, int right, List<Node> nodes) {
        int sum = 0;
        for (int i = left; i < right; i++) {
            sum += nodes.get(i).probability;
        }
        return sum;
    }


    private static void sortByProbs(int n, List<Node> nodes) {
        Node current = new Node();
        for (Node node : nodes) {
            for (int i = 0; i < n - 1; i++) {
                if (nodes.get(i).probability < nodes.get(i + 1).probability) {
//                swap
                    current.probability = nodes.get(i).probability;
                    current.symbol = nodes.get(i).symbol;

                    nodes.get(i).probability = nodes.get(i + 1).probability;
                    nodes.get(i).symbol = nodes.get(i + 1).symbol;

                    nodes.get(i + 1).probability = current.probability;
                    nodes.get(i + 1).symbol = current.symbol;
                }
            }
        }
    }

    private static void printResult(String text, int n, List<Node> nodes) {
        System.out.println("Symbol\tprobability\tcode");
        Map<String, String> code = new HashMap<>();
        for (int i = 0; i < n; i++) {
            System.out.print(nodes.get(i).symbol + "\t\t" + nodes.get(i).probability + "\t\t");
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < nodes.get(i).topPosition; j++) {
                builder.append(nodes.get(i).code[j]);
                System.out.print(nodes.get(i).code[j]);
            }
            System.out.println();
            code.put(nodes.get(i).symbol, builder.toString());
        }
        for (char ch: text.toCharArray()) {
            System.out.print(code.get(String.valueOf(ch)));
        }
    }
}