package edu.uchicago.gerber._05gui.P10_19;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantBillApp {
    public static void main(String[] args) {
        JFrame frame = new RestaurantBillFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class RestaurantBillFrame extends JFrame {
    private JTextArea billArea;
    private JTextField itemNameField;
    private JTextField priceField;
    private double total = 0;

    private ArrayList<Dish> notPopularDishes;
    private HashMap<String, Integer> popularDishCountMap;
    private HashMap<String, Double> popularDishPriceMap;
    public RestaurantBillFrame() {
        setTitle("Restaurant Bill");
        setSize(400, 400);
        setLayout(new BorderLayout());

        // Panel for popular items
        JPanel popularItemsPanel = new JPanel();
        popularItemsPanel.setLayout(new GridLayout(5, 2)); // Use BoxLayout
        popularDishPriceMap = new HashMap<>();
        addButton(popularItemsPanel, "Hamburger", 5.99);
        popularDishPriceMap.put("Hamburger", 5.99);
        addButton(popularItemsPanel, "Chicken Caesar Salad", 6.99);
        popularDishPriceMap.put("Chicken Caesar Salad", 6.99);
        addButton(popularItemsPanel, "Margherita Pizza", 8.99);
        popularDishPriceMap.put("Margherita Pizza", 8.99);
        addButton(popularItemsPanel, "Sushi", 11.99);
        popularDishPriceMap.put("Sushi", 11.99);
        addButton(popularItemsPanel, "Pasta", 7.99);
        popularDishPriceMap.put("Pasta", 7.99);
        addButton(popularItemsPanel, "Soda", 1.99);
        popularDishPriceMap.put("Soda", 1.99);
        addButton(popularItemsPanel, "Coffee", 2.99);
        popularDishPriceMap.put("Coffee", 2.99);
        addButton(popularItemsPanel, "BBR ribs", 15.99);
        popularDishPriceMap.put("BBR ribs", 15.99);
        addButton(popularItemsPanel, "Vegetable Curry", 3.99);
        popularDishPriceMap.put("Vegetable Curry", 3.99);
        addButton(popularItemsPanel, "Beef Stir-Fry", 5.99);
        popularDishPriceMap.put("Beef Stir-Fry", 5.99);
        popularDishCountMap = new HashMap<>();

        // Panel for custom items
        JPanel customItemsPanel = new JPanel();
        itemNameField = new JTextField(10);
        priceField = new JTextField(5);
        JButton addItemButton = new JButton("Add Item");
        notPopularDishes = new ArrayList<>();
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem(itemNameField.getText(), Double.parseDouble(priceField.getText()));
                notPopularDishes.add(new Dish(itemNameField.getText(), Double.parseDouble(priceField.getText())));
                itemNameField.setText("");
                priceField.setText("");
            }
        });
        customItemsPanel.add(new JLabel("Item:"));
        customItemsPanel.add(itemNameField);
        customItemsPanel.add(new JLabel("Price:"));
        customItemsPanel.add(priceField);
        customItemsPanel.add(addItemButton);

        billArea = new JTextArea(10, 40);
        billArea.setPreferredSize(new Dimension(480, 300));
        billArea.setEditable(false);

        add(popularItemsPanel, BorderLayout.NORTH);
        add(customItemsPanel, BorderLayout.CENTER);
        add(new JScrollPane(billArea), BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String itemName, double price) {
        JButton button = new JButton(itemName + " - $" + price);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popularDishCountMap.containsKey(itemName)) {
                    popularDishCountMap.put(itemName, popularDishCountMap.get(itemName) + 1);
                } else {
                    popularDishCountMap.put(itemName, 1);
                }
                addItem(itemName, price);
            }
        });
        panel.add(button);
    }

    private void addItem(String itemName, double price) {
        total += price;
        double tax = total * 0.105;
        double suggestedTip = total * 0.15;
        double grandTotal = total + tax + suggestedTip;

        DecimalFormat df = new DecimalFormat("#.##");
        billArea.setText("Items:\n");
        for (String key: popularDishCountMap.keySet()) {
            Integer count = popularDishCountMap.get(key);
            billArea.append(key + " - " + count + " - " + df.format(popularDishPriceMap.get(key) * count) + "\n");
        }
//        billArea.append(itemName + " - $" + df.format(price) + "\n");
        for (Dish dish: notPopularDishes) {
            billArea.append(dish.getDishName() + " - " + 1 + " - " + df.format(dish.getDishPrice()) + "\n");
        }

        billArea.append("\nTotal: $" + df.format(total));
        billArea.append("\nTax (10%): $" + df.format(tax));
        billArea.append("\nSuggested Tip (15%): $" + df.format(suggestedTip));
        billArea.append("\nGrand Total: $" + df.format(grandTotal));
    }
}
class Dish {
    private String dishName;
    private double dishPrice;
    public Dish(String dishName, double dishPrice) {
        this.dishName = dishName;
        this.dishPrice = dishPrice;
    }
    public String getDishName() {
        return this.dishName;
    }
    public double getDishPrice() {
        return this.dishPrice;
    }
}