package service.handlerbase;

import com.google.gson.reflect.TypeToken;
import model.Task;

import java.util.List;

// класс адаптер для сериализации\десериализации дженериков
class TaskListTypeToken extends TypeToken<List<Task>> {
}