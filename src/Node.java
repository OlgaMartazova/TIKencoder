public class Node {
    String symbol;
    double probability = 0;
    int[] code;
    int topPosition = 0;

    public Node(String symbol, double probability, int topPosition, int length) {
        this.symbol = symbol;
        this.probability = probability;
        this.topPosition = topPosition;
        code = new int[length];
    }

    public Node() {
    }
}
