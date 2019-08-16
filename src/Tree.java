import java.util.ArrayList;
import java.util.List;

public class Tree<T extends Comparable<T>> {

    private Node<T> nil = new Node<T>();
    private Node<T> root = nil;

    public Tree() {
        root.setLeft(nil);
        root.setRight(nil);
        root.setParent(nil);
    }

    private void leftRotate(Node<T> x) {

        leftRotateFixup(x);

        Node<T> y;
        y = x.getRight();
        x.setRight(y.getLeft());

        if (!isNil(y.getLeft()))
            y.getLeft().setParent(x);
        y.setParent(x.getParent());

        if (isNil(x.getParent()))
            root = y;

        else if (x.getParent().getLeft() == x)
            x.getParent().setLeft(y);

        else
            x.getParent().setRight(y);


        y.setLeft(x);
        x.setParent(y);
    }


    private void leftRotateFixup(Node x) {

        // Case 1: Only x, x.right and x.right.right always are not nil.
        if (isNil(x.getLeft()) && isNil(x.getRight().getLeft())) {
            x.setNumLeft(0);
            x.setNumRight(0);
            x.getRight().setNumLeft(1);
        }

        // Case 2: x.right.left also exists in addition to Case 1
        else if (isNil(x.getLeft()) && !isNil(x.getRight().getLeft())) {
            x.setNumLeft(0);
            x.setNumRight(1 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());
            x.getRight().setNumLeft(1 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());
        }

        // Case 3: x.left also exists in addition to Case 1
        else if (!isNil(x.getLeft()) && isNil(x.getRight().getLeft())) {
            x.setNumRight(0);
            x.getRight().setNumLeft(2 + x.getLeft().getNumLeft() + x.getLeft().getNumRight());
        }
        // Case 4: x.left and x.right.left both exist in addtion to Case 1
        else {
            x.setNumRight(1 + x.getRight().getLeft().getNumLeft() +
                    x.getRight().getLeft().getNumRight());

            x.getRight().setNumLeft(3 + x.getLeft().getNumLeft() + x.getLeft().getNumRight() +
                    x.getRight().getLeft().getNumLeft() + x.getRight().getLeft().getNumRight());
        }

    }


    private void rightRotate(Node<T> y) {
        rightRotateFixup(y);

        Node<T> x = y.getLeft();
        y.setLeft(x.getRight());

        if (!isNil(x.getRight()))
            x.getRight().setParent(y);
        x.setParent(y.getParent());

        if (isNil(y.getParent()))
            root = x;
        else if (y.getParent().getRight() == y)
            y.getParent().setRight(x);
        else
            y.getParent().setLeft(x);

        x.setRight(y);
        y.setParent(x);

    }

    private void rightRotateFixup(Node y) {

        // Case 1: Only y, y.left and y.left.left exists.
        if (isNil(y.getRight()) && isNil(y.getLeft().getRight())) {
            y.setNumRight(0);
            y.setNumLeft(0);
            y.getLeft().setNumRight(1);
        }

        // Case 2: y.left.right also exists in addition to Case 1
        else if (isNil(y.getRight()) && !isNil(y.getLeft().getRight())) {
            y.setNumRight(0);
            y.setNumLeft(1 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
            y.getLeft().setNumRight(2 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
        }

        // Case 3: y.right also exists in addition to Case 1
        else if (!isNil(y.getRight()) && isNil(y.getLeft().getRight())) {
            y.setNumLeft(0);
            y.getLeft().setNumRight(2 + y.getRight().getNumRight() +
                    y.getRight().getNumLeft());
        }

        // Case 4: y.right & y.left.right exist in addition to Case 1
        else {
            y.setNumLeft(1 + y.getLeft().getRight().getNumRight() +
                    y.getLeft().getRight().getNumLeft());
            y.getLeft().setNumRight(3 + y.getRight().getNumRight() + y.getRight().getNumLeft() +
                    y.getLeft().getRight().getNumRight() + y.getLeft().getRight().getNumLeft());
        }

    }

    public void insert(T key) {
        insert(new Node<T>(key));
    }

    private void insert(Node<T> z) {

        // Create a reference to root & initialize a node to nil
        Node<T> y = nil;
        Node<T> x = root;

        // While we haven't reached a the end of the tree keep
        // tryint to figure out where z should go
        while (!isNil(x)) {
            y = x;

            // if z.key is < than the current key, go left
            if (z.getKey().compareTo(x.getKey()) < 0) {
                x.setNumLeft(x.getNumLeft() + 1);
                x = x.getLeft();
            } else {
                x.setNumRight(x.getNumRight() + 1);
                x = x.getRight();
            }
        }
        // y will hold z's parent
        z.setParent(y);

        // Depending on the value of y.key, put z as the left or
        // right child of y
        if (isNil(y))
            root = z;
        else if (z.getKey().compareTo(y.getKey()) < 0)
            y.setLeft(z);
        else
            y.setRight(z);

        z.setLeft(nil);
        z.setRight(nil);
        z.setColor(Node.RED);
        insertFixup(z);
    }

    private void insertFixup(Node<T> z) {
        Node<T> y = nil;
        // While there is a violation of the RedBlackTree properties..
        while (z.getParent().getColor() == Node.RED) {

            // If z's parent is the the left child of it's parent.
            if (z.getParent() == z.getParent().getParent().getLeft()) {

                // Initialize y to z 's cousin
                y = z.getParent().getParent().getRight();

                // Case 1: if y is red...recolor
                if (y.getColor() == Node.RED) {
                    z.getParent().setColor(Node.BLACK);
                    y.setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                }
                // Case 2: if y is black & z is a right child
                else if (z == z.getParent().getRight()) {

                    // leftRotaet around z's parent
                    z = z.getParent();
                    leftRotate(z);
                }

                // Case 3: else y is black & z is a left child
                else {
                    // recolor and rotate round z's grandpa
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    rightRotate(z.getParent().getParent());
                }
            }

            // If z's parent is the right child of it's parent.
            else {

                // Initialize y to z's cousin
                y = z.getParent().getParent().getLeft();

                // Case 1: if y is red...recolor
                if (y.getColor() == Node.RED) {
                    z.getParent().setColor(Node.BLACK);
                    y.setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    z = z.getParent().getParent();
                }

                // Case 2: if y is black and z is a left child
                else if (z == z.getParent().getLeft()) {
                    // rightRotate around z's parent
                    z = z.getParent();
                    rightRotate(z);
                }
                // Case 3: if y  is black and z is a right child
                else {
                    // recolor and rotate around z's grandpa
                    z.getParent().setColor(Node.BLACK);
                    z.getParent().getParent().setColor(Node.RED);
                    leftRotate(z.getParent().getParent());
                }
            }
        }
        // Color root black at all times
        root.setColor(Node.BLACK);

    }

    public Node<T> treeMinimum(Node<T> node) {
        while (!isNil(node.getLeft()))
            node = node.getLeft();
        return node;
    }

    public Node<T> treeSuccessor(Node<T> x) {

        // if x.left is not nil, call treeMinimum(x.right) and
        // return it's value
        if (!isNil(x.getLeft()))
            return treeMinimum(x.getRight());

        Node<T> y = x.getParent();

        // while x is it's parent's right child...
        while (!isNil(y) && x == y.getRight()) {
            // Keep moving up in the tree
            x = y;
            y = y.getParent();
        }
        return y;
    }

    public void remove(Node<T> v) {

        Node<T> z = search(v.getKey());

        // Declare variables
        Node<T> x;
        Node<T> y;

        // if either one of z's children is nil, then we must remove z
        if (isNil(z.getLeft()) || isNil(z.getRight()))
            y = z;

            // else we must remove the successor of z
        else y = treeSuccessor(z);

        // Let x be the left or right child of y (y can only have one child)
        if (!isNil(y.getLeft()))
            x = y.getLeft();
        else
            x = y.getRight();

        // link x's parent to y's parent
        x.setParent(y.getParent());

        // If y's parent is nil, then x is the root
        if (isNil(y.getParent()))
            root = x;

            // else if y is a left child, set x to be y's left sibling
        else if (!isNil(y.getParent().getLeft()) && y.getParent().getLeft() == y)
            y.getParent().setLeft(x);

            // else if y is a right child, set x to be y's right sibling
        else if (!isNil(y.getParent().getRight()) && y.getParent().getRight() == y)
            y.getParent().setRight(x);

        // if y != z, trasfer y's satellite data into z.
        if (y != z) {
            z.setKey(y.getKey());
        }

        // Update the numLeft and numRight numbers which might need
        // updating due to the deletion of z.key.
        fixNodeData(x, y);

        // If y's color is black, it is a violation of the
        // RedBlackTree properties so call removeFixup()
        if (y.getColor() == Node.BLACK)
            removeFixup(x);
    }

    private void fixNodeData(Node<T> x, Node<T> y) {


        Node<T> current;
        Node<T> track;


        // if x is nil, then we will start updating at y.parent
        // Set track to y, y.parent's child
        if (isNil(x)) {
            current = y.getParent();
            track = y;
        }

        // if x is not nil, then we start updating at x.parent
        // Set track to x, x.parent's child
        else {
            current = x.getParent();
            track = x;
        }

        // while we haven't reached the root
        while (!isNil(current)) {
            // if the node we deleted has a different key than
            // the current node
            if (y.getKey() != current.getKey()) {

                // if the node we deleted is greater than
                // current.key then decrement current.numRight
                if (y.getKey().compareTo(current.getKey()) > 0)
                    current.setNumRight(current.getNumRight() - 1);

                // if the node we deleted is less than
                // current.key thendecrement current.numLeft
                if (y.getKey().compareTo(current.getKey()) < 0)
                    current.setNumLeft(current.getNumLeft() - 1);
            }

            // if the node we deleted has the same key as the
            // current node we are checking
            else {
                // the cases where the current node has any nil
                // children and update appropriately
                if (isNil(current.getLeft()))
                    current.setNumLeft(current.getNumLeft() - 1);
                else if (isNil(current.getRight()))
                    current.setNumRight(current.getNumRight() - 1);

                    // the cases where current has two children and
                    // we must determine whether track is it's left
                    // or right child and update appropriately
                else if (track == current.getRight())
                    current.setNumRight(current.getNumRight() - 1);
                else if (track == current.getLeft())
                    current.setNumLeft(current.getNumLeft() - 1);
            }

            // update track and current
            track = current;
            current = current.getParent();

        }

    }//end fixNodeData()

    private void removeFixup(Node<T> x) {

        Node<T> w;

        // While we haven't fixed the tree completely...
        while (x != root && x.getColor() == Node.BLACK) {

            // if x is it's parent's left child
            if (x == x.getParent().getLeft()) {

                // set w = x's sibling
                w = x.getParent().getRight();

                // Case 1, w's color is red.
                if (w.getColor() == Node.RED) {
                    w.setColor(Node.BLACK);
                    x.getParent().setColor(Node.RED);
                    leftRotate(x.getParent());
                    w = x.getParent().getRight();
                }

                // Case 2, both of w's children are black
                if (w.getLeft().getColor() == Node.BLACK &&
                        w.getRight().getColor() == Node.BLACK) {
                    w.setColor(Node.RED);
                    x = x.getParent();
                }
                // Case 3 / Case 4
                else {
                    // Case 3, w's right child is black
                    if (w.getRight().getColor() == Node.BLACK) {
                        w.getLeft().setColor(Node.BLACK);
                        w.setColor(Node.RED);
                        rightRotate(w);
                        w = x.getParent().getRight();
                    }
                    // Case 4, w = black, w.right = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Node.BLACK);
                    w.getRight().setColor(Node.BLACK);
                    leftRotate(x.getParent());
                    x = root;
                }
            }
            // if x is it's parent's right child
            else {

                // set w to x's sibling
                w = x.getParent().getLeft();

                // Case 1, w's color is red
                if (w.getColor() == Node.RED) {
                    w.setColor(Node.BLACK);
                    x.getParent().setColor(Node.RED);
                    rightRotate(x.getParent());
                    w = x.getParent().getLeft();
                }

                // Case 2, both of w's children are black
                if (w.getRight().getColor() == Node.BLACK &&
                        w.getLeft().getColor() == Node.BLACK) {
                    w.setColor(Node.RED);
                    x = x.getParent();
                }

                // Case 3 / Case 4
                else {
                    // Case 3, w's left child is black
                    if (w.getLeft().getColor() == Node.BLACK) {
                        w.getRight().setColor(Node.BLACK);
                        w.setColor(Node.RED);
                        leftRotate(w);
                        w = x.getParent().getLeft();
                    }

                    // Case 4, w = black, and w.left = red
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(Node.BLACK);
                    w.getLeft().setColor(Node.BLACK);
                    rightRotate(x.getParent());
                    x = root;
                }
            }
        }// end while

        // set x to black to ensure there is no violation of
        // RedBlack tree Properties
        x.setColor(Node.BLACK);
    }

    public Node<T> search(T key) {

        // Initialize a pointer to the root to traverse the tree
        Node<T> current = root;

        while (!isNil(current)) {

            // If we have found a node with a key equal to key
            if (current.getKey().equals(key))

                // return that node and exit search(int)
                return current;

                // go left or right based on value of current and key
            else if (current.getKey().compareTo(key) < 0)
                current = current.getRight();

                // go left or right based on value of current and key
            else
                current = current.getLeft();
        }
        // we have not found a node whose key is "key"
        return null;
    }

    public int numGreater(T key) {

        // Call findNumGreater(root, key) which will return the number
        // of nodes whose key is greater than key
        return findNumGreater(root, key);

    }

    public int numSmaller(T key) {

        // Call findNumSmaller(root,key) which will return
        // the number of nodes whose key is greater than key
        return findNumSmaller(root, key);

    }

    public int findNumGreater(Node<T> node, T key) {

        // Base Case: if node is nil, return 0
        if (isNil(node))
            return 0;
            // If key is less than node.key, all elements right of node are
            // greater than key, add this to our total and look to the left
        else if (key.compareTo(node.getKey()) < 0)
            return 1 + node.getNumRight() + findNumGreater(node.getLeft(), key);

            // If key is greater than node.key, then look to the right as
            // all elements to the left of node are smaller than key
        else
            return findNumGreater(node.getRight(), key);

    }

    public List<T> getGreaterThan(T key, Integer maxReturned) {
        List<T> list = new ArrayList<T>();
        getGreaterThan(root, key, list);
        return list.subList(0, Math.min(maxReturned, list.size()));
    }

    private void getGreaterThan(Node<T> node, T key,
                                List<T> list) {
        if (isNil(node)) {
            return;
        } else if (node.getKey().compareTo(key) > 0) {
            getGreaterThan(node.getLeft(), key, list);
            list.add(node.getKey());
            getGreaterThan(node.getRight(), key, list);
        } else {
            getGreaterThan(node.getRight(), key, list);
        }
    }

    public int findNumSmaller(Node<T> node, T key) {

        // Base Case: if node is nil, return 0
        if (isNil(node)) return 0;

            // If key is less than node.key, look to the left as all
            // elements on the right of node are greater than key
        else if (key.compareTo(node.getKey()) <= 0)
            return findNumSmaller(node.getLeft(), key);

            // If key is larger than node.key, all elements to the left of
            // node are smaller than key, add this to our total and look
            // to the right.
        else
            return 1 + node.getNumLeft() + findNumSmaller(node.getRight(), key);

    }

    private boolean isNil(Node node) {

        // return appropriate value
        return node == nil;

    }

    public int size() {

        // Return the number of nodes to the root's left + the number of
        // nodes on the root's right + the root itself.
        return root.getNumLeft() + root.getNumRight() + 1;
    }
}