package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList;

    // для реализации внутри данного класса двусвязного списка
    // добавлены поля для ссылки на "голову" и "хвост" - первый и последний элементы списка соответсвенно
    private Node<Task> head;
    private Node<Task> tail;
    private int size;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void listLast(Task task) {
        // добавляем задачу в конец (в хвост)  двусвязного списка
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode; //создали и перезаписали хвост
        if (oldTail == null) {// если хвоста не было, создаем и голову тоже
            head = newNode;
        } else { //если хвост был, обновляем ссылку на новый хвост
            oldTail.next = newNode;
        }
        size++;
        getTasks(task);//записываем в список
    }

    public void getTasks(Task task) {
        historyList.add(task);
    }

    public void removeNode(Node<Task> node) {//удааляем узел из списка
        //TODO необходимо учесть все варианты где находится данный узел - внутри списка, голова или хвост
        // а также восстановить все ссылки на предыдущий и последующие элементы
        if (node != null) {
            Node<Task> prevNode = node.prev;
            Node<Task> nextNode = node.next;
        }
    }

    public void listFirst(Task task) {
        // добавляем задачу в начало (в головной элемент) двусвязного списка
        final Node<Task> oldHead = head;
        final Node<Task> newNode = new Node<>(null, task, oldHead);
        head = newNode; // создали м перезаписали новую голову
        if (oldHead == null) {//если раньше головы не было, то хвост надо тоже создать
            tail = newNode;
        } else {// если голова была, обновляем ссылку на новуюь голову
            oldHead.prev = newNode;
        }
        size++;
        historyList.addFirst(task);//
    }

    public Task getFirst() {
        // метод реализует доступ к первому (головному) элементу двусвязного списка
        final Node<Task> curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return head.data;
    }

    public Task getLast() {
        // метод реализует доступ к последнему (хвостовому) элементу двусвязного списка
        final Node<Task> curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public int size() {
        //метод возвращает размер списка
        return this.size;
    }


    @Override
    public void add(Task task) {
//        if (historyList.size() == 10) {
//            historyList.removeFirst();
//        }
//        historyList.add(task);

        listLast(task);
        //listFirst(task);
    }

    @Override
    public void remove(int id) {
        //TODO реализовать метод remove
    }

    //истории просмотров задач
    @Override
    public List<Task> getHistory() {
        //чтобы не открывать доступ к private переменной, можно historyList обернуть в new ArrayList<>()
        return new ArrayList<>(historyList);
    }
}
