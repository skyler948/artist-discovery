package tree;

import artist.Artist;

public class BinaryTree {

    private Node root = null;

    public void insert(Artist value) {
        root = insert(root, value);
    }

    private Node insert(Node node, Artist value) {
        if (node == null) {
            node = new Node(value);
            return node;
        }

        if (value.getScore() <= node.data.getScore()) {
            node.left = insert(node.left, value);
        } else {
            node.right = insert(node.right, value);
        }

        return node;
    }

    public Artist nearest(float value) {
        return nearestSearch(value).data;
    }

    private Node nearestSearch(float value) {
        Node node = root;
        Node nearest = node;
        float closest = Integer.MAX_VALUE;

        while (node != null) {
            float distance = Math.abs(node.data.getScore() - value);

            if (distance == 0 && node.data.getScore() != root.data.getScore()) return node;

            if (distance < closest) {
                closest = distance;
                nearest = node;
            }

            if (value < node.data.getScore()) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        return nearest;
    }

}
