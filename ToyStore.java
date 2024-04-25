// Задание
 
// Необходимо написать программу – розыгрыша игрушек в магазине детских товаров.
// Стараемся применять ООП и работу с файлами.
// Если какой-то пункт не изучали и не знаете, как сделать, то можете сделать своим способом. Например, у кого в курсе не было ООП, то применяем списки\массивы\словари
 
// Желательный функционал программы:
// В программе должен быть минимум один класс со следующими свойствами:
// id игрушки,
// текстовое название,
// количество
// частота выпадения игрушки (вес в % от 100)
 
// Метод добавление новых игрушек и возможность изменения веса (частоты выпадения игрушки)
// Возможность организовать розыгрыш игрушек.

// Например, следующим образом:
// С помощью метода выбора призовой игрушки – мы получаем эту призовую игрушку и записываем в список\массив.
// Это список призовых игрушек, которые ожидают выдачи.
// Еще у нас должен быть метод – получения призовой игрушки.
// После его вызова – мы удаляем из списка\массива первую игрушку и сдвигаем массив. А эту игрушку записываем в текстовый файл.
// Не забываем уменьшить количество игрушек



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Класс для представления игрушек
class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight; // Частота выпадения игрушки в %

    // Конструктор для создания игрушки
    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    // Геттеры для свойств игрушки
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    // Сеттер для изменения частоты выпадения игрушки
    public void setWeight(double weight) {
        this.weight = weight;
    }

    // Метод для уменьшения количества игрушек
    public void decreaseQuantity() {
        quantity--;
    }
}

// Основной класс магазина игрушек
public class ToyStore {
    private List<Toy> toys = new ArrayList<>();

    // Метод для добавления новой игрушки в магазин
    public void addToy(Toy toy) {
        toys.add(toy);
    }

    // Метод для обновления частоты выпадения игрушки
    public void updateWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    // Метод для розыгрыша призовой игрушки
    public Toy drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum(); // Вычисляем суммарный вес всех игрушек
        double randomNumber = Math.random() * totalWeight; // Генерируем случайное число от 0 до суммарного веса
        double weightSum = 0;
        for (Toy toy : toys) {
            weightSum += toy.getWeight();
            if (randomNumber <= weightSum) { // Если случайное число меньше или равно суммарному весу, выбираем эту игрушку
                Toy selectedToy = toy;
                selectedToy.decreaseQuantity(); // Уменьшаем количество выбранной игрушки
                toys.remove(toy); // Удаляем выбранную игрушку из списка
                return selectedToy;
            }
        }
        return null; // Если нет доступных игрушек для розыгрыша
    }

    // Метод для записи призовой игрушки в файл
    public void writeToFile(Toy toy) {
        File file = new File("prize_toys.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("ID: " + toy.getId() + ", Name: " + toy.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод main для тестирования функционала магазина игрушек
    public static void main(String[] args) {
        ToyStore store = new ToyStore();
        // Добавляем игрушки в магазин
        store.addToy(new Toy(1, "Кукла", 10, 30));
        store.addToy(new Toy(2, "Машинка", 15, 20));
        store.addToy(new Toy(3, "Мяч", 20, 50));

        // Устанавливаем новый вес (частоту выпадения) для игрушки
        store.updateWeight(2, 10);

        // Розыгрыш и запись призовой игрушки в файл
        Toy prizeToy = store.drawToy();
        if (prizeToy != null) {
            System.out.println("Получена призовая игрушка: " + prizeToy.getName());
            store.writeToFile(prizeToy);
        } else {
            System.out.println("Нет доступных призовых игрушек");
        }
    }
}
