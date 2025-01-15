package service;
import model.Node;
import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyList;
    private Map<Integer, Node<Task>> historyMap;
    //если потребуется работать с отдельными списками других типов задач
    //необходиом  параметризировать список, передавая в качестве параметра тип задачи
    //и  использовать ограничение на верхнюю границу этого дженерика T extends Task


    //для реализации внутри данного класса двусвязного списка
    //добавлены поля для ссылки на "голову" и "хвост" - первый и последний элементы списка соответсвенно
    private Node<Task> head;
    private Node<Task> tail;
    private int size;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
        historyMap = new HashMap<>();
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private void listLast(Task task) {
        //добавляем задачу в конец (в хвост)  двусвязного списка
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode; //создали и перезаписали хвост
        if (oldTail == null) {// если хвоста не было, создаем и голову тоже
            head = newNode;
        } else { //если хвост был, обновляем ссылку на новый хвост
            oldTail.next = newNode;
        }
        size++;
        historyMap.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {//собираем в ArrayList
        historyList.clear();
        Node<Task> curNode = head;
        if (curNode != null) {
            for (int i = 0; i < size; i++) {
                historyList.add(curNode.data);
                curNode = curNode.next;
            }
        }
        //чтобы не открывать доступ к private переменной, можно historyList обернуть в new ArrayList<>()
        return new ArrayList<>(historyList);
    }

    private void removeNode(Node<Task> node) {//удааляем узел из списка
        //необходимо учесть все варианты где находится данный узел - внутри списка, голова или хвост
        //а также восстановить все ссылки на предыдущий и последующие элементы
        if (node != null) { //если null удалять нечего
            if (node == head) { //удаляем голову
                final Node<Task> newHead = node.next;
                if (newHead != null) {//если данный элемент не был последним
                    head = newHead;
                    newHead.prev = null;
                }

            } else if (node == tail) { //удаляем хвост
                final Node<Task> newTail = node.prev;
                tail = newTail;
                newTail.next = null;

            } else { //удаляем из середины
                Node<Task> prevNode = node.prev;
                Node<Task> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                node.data = null;
            }
            size--;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) return;
        //проверка на равенство null добавлено в результате сбоя теста subTaskShouldNOTBeEpic() в TaskSubTaskEpicTest
        if (historyMap.containsKey(task.getId())) {//ищем и удаляем дубликат из списка и из HashMAp
            removeNode(historyMap.get(task.getId()));
            historyMap.remove(task.getId());
        }
        listLast(task); //добавляем задачу в конец списка
    }

    @Override
    public void remove(int id) {//ищем и удаляем задачу из списка и из HashMAp
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
            historyMap.remove(id);
        }
    }

    //истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return getTasks();

    }
}
