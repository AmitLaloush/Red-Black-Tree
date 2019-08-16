class Node<T extends Comparable<T>> {

    private T key;

    private Node<T> parent;
    private Node<T> left;
    private Node<T> right;

    private int numLeft = 0;
    private int numRight = 0;
    private int color;
    public static final int BLACK = 0;
    public static final int RED = 1;

    Node() {
        color = BLACK;
        numLeft = 0;
        numRight = 0;
        parent = null;
        left = null;
        right = null;
    }

    Node(T key) {
        this();
        this.key = key;
    }

    public T getKey() {
        return key;
    }


    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void setNumLeft(int numLeft) {
        this.numLeft = numLeft;
    }

    public void setNumRight(int numRight) {
        this.numRight = numRight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNumLeft() {
        return numLeft;
    }

    public int getNumRight() {
        return numRight;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }


    public Node<T> getParent() {
        return parent;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }


}