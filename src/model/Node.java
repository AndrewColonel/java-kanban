// класс реализующий узел списка
package model;

public class Node<E> {
    public E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public Node(E data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

}
