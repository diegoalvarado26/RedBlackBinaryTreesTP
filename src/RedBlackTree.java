public class RedBlackTree<T> {

    private Node<T> current, parent, grand, great, header;
    private static Node nullNode;

    static {
        nullNode = new Node(null);
        nullNode.left = nullNode;
        nullNode.right = nullNode;
    }

    static final int BLACK = 1;
    static final int RED = 0;

}
