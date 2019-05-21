public class Node<T> {

    Node<T> left, right;
    T element;
    int color;

    public Node(T elem, Node lt, Node rt){
        left = lt;
        right = rt;
        color = 1;
        element = elem;
    }

    public Node(T elem){
        this(elem,null,null);
    }

}
