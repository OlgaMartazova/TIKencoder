import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FanoCoder {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"));
        String alphabet = bufferedReader.readLine();
        List<String> letters = Arrays.stream(alphabet.split("\\s")).collect(Collectors.toList());
        double[] probs = Arrays
                .stream(bufferedReader.readLine().split("\\s"))
                .mapToDouble(Double::parseDouble)
                .toArray();

        int n = letters.size();
        List<Node> nodes = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> nodes.add(new Node(letters.get(i), probs[i], 0, n)));
        sortByProbs(nodes);
        fano(0, n - 1, nodes);

        printResult(n, nodes);

        System.out.print("Напишите сообщение, состоящее из букв алфавита ");
        System.out.println(alphabet);
        String text = sc.nextLine();
        while (text.isEmpty()) {
            System.out.println("Сообщение не было получено, попробуйте еще раз");
            text = sc.nextLine();
        }
        for (String ch : text.split("")) {
            if (!letters.contains(ch)) {
                System.out.println("В сообщении используются символы, которых нет в алфавите");
                return;
            }
        }
        System.out.println("Получен код:");
        System.out.print(getCode(text, n, nodes));
    }


    private static void fano(int left, int right, List<Node> nodes) {
        if (left >= right) {
            return;
        }
        if (right - left == 1) {
            setCode(left, 0, nodes);
            setCode(right, 1, nodes);
        } else {
            double total = 0;
            for (int i = left; i <= right; i++) {
                total += nodes.get(i).probability;
            }
            double group1 = nodes.get(left).probability;
            double diff1 = Math.abs(group1 - total / 2);
            int board = 1;
            group1 += nodes.get(left + board).probability;
            double diff2 = Math.abs(group1 - total / 2);
            while (diff2 < diff1) {
                diff1 = diff2;
                board++;
                group1 += nodes.get(left + board).probability;
                diff2 = Math.abs(group1 - total / 2);
            }
            board += left - 1;
            IntStream.range(left, board + 1).forEach(i -> setCode(i, 0, nodes));
            IntStream.range(board + 1, right + 1).forEach(i -> setCode(i, 1, nodes));
            fano(left, board, nodes);
            fano(board + 1, right, nodes);
        }
    }

    private static void setCode(int i, int code, List<Node> nodes) {
        nodes.get(i).code[nodes.get(i).codeLength] = code;
        nodes.get(i).codeLength += 1;
    }

    private static void sortByProbs(List<Node> nodes) {
        nodes.sort(Comparator.comparingDouble(Node::getProbability).reversed());
    }

    private static void printResult(int n, List<Node> nodes) {
        System.out.println("Symbol\tprobability\tcode");
        IntStream.range(0, n).forEach(i -> {
            System.out.print(nodes.get(i).symbol + "\t\t" + nodes.get(i).probability + "\t\t");
            IntStream.range(0, nodes.get(i).codeLength).forEach(j -> System.out.print(nodes.get(i).code[j]));
            System.out.println();
        });
    }

    private static String getCode(String text, int n, List<Node> nodes) {
        Map<String, String> code = new HashMap<>();
        IntStream.range(0, n).forEach(i -> {
            StringBuilder codeSeq = new StringBuilder();
            IntStream.range(0, nodes.get(i).codeLength).forEach(j -> codeSeq.append(nodes.get(i).code[j]));
            code.put(nodes.get(i).symbol, codeSeq.toString());
        });
        StringBuilder output = new StringBuilder();
        Arrays.stream(text.split("")).forEach(letter -> output.append(code.get(letter)));
        return output.toString();
    }
}