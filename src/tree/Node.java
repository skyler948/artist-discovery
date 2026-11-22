package tree;

import artist.Artist;

public class Node {

    Artist data;
    Node left, right;

    Node(Artist data) {
        this.data = data;
        left = null;
        right = null;
    }

}
