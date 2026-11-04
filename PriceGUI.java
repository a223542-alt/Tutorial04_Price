
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PriceGUI extends JFrame implements ActionListener {

    private JTextField customerField, salaryField;
    private JLabel itemLabel, priceLabel;

    private JButton smartphoneButton, laptopButton, carButton;
    private JButton checkButton, resetButton;

    private JTextArea resultArea;

    private String selectedItem = "SmartPhone";

    public PriceGUI() {

        setTitle("Price Management System");
        setSize(760, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Container pane = getContentPane();
        pane.setLayout(new BorderLayout(15, 15));

        // Title
        JLabel title = new JLabel(
                "PRICE MANAGEMENT SYSTEM",
                SwingConstants.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(25, 85, 150));

        JLabel subtitle = new JLabel(
                "Select an item and check customer eligibility",
                SwingConstants.CENTER);

        JPanel headings = new JPanel(new GridLayout(2, 1));
        headings.add(title);
        headings.add(subtitle);

        // Product selection buttons
        smartphoneButton = new JButton("SmartPhone");
        laptopButton = new JButton("Laptop");
        carButton = new JButton("Car");

        JPanel choices = new JPanel(new GridLayout(1, 3, 10, 10));
        choices.add(smartphoneButton);
        choices.add(laptopButton);
        choices.add(carButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 12));

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 5, 25));

        topPanel.add(headings, BorderLayout.NORTH);
        topPanel.add(choices, BorderLayout.SOUTH);

        pane.add(topPanel, BorderLayout.NORTH);

        // Input fields
        itemLabel = new JLabel("Iphone 14 ProMax");
        priceLabel = new JLabel("RM5500.00");

        customerField = new JTextField("Sally");
        salaryField = new JTextField("3000");

        JPanel inputs = new JPanel(new GridLayout(4, 2, 12, 12));

        inputs.add(new JLabel("Selected Item:"));
        inputs.add(itemLabel);

        inputs.add(new JLabel("Item Price:"));
        inputs.add(priceLabel);

        inputs.add(new JLabel("Customer Name:"));
        inputs.add(customerField);

        inputs.add(new JLabel("Customer Salary (RM):"));
        inputs.add(salaryField);

        inputs.setBorder(
                BorderFactory.createEmptyBorder(15, 35, 15, 35));

        pane.add(inputs, BorderLayout.CENTER);

        // Action buttons
        checkButton = new JButton("CHECK PRICE");
        resetButton = new JButton("RESET");

        JPanel actions = new JPanel();
        actions.add(checkButton);
        actions.add(resetButton);

        // Result area
        resultArea = new JTextArea(7, 40);
        resultArea.setFont(
                new Font("Monospaced", Font.PLAIN, 14));

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        resultArea.setBackground(new Color(235, 245, 255));

        resultArea.setBorder(
                BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        bottomPanel.setPreferredSize(new Dimension(0, 245));

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 35, 20, 35));

        bottomPanel.add(actions, BorderLayout.NORTH);
        bottomPanel.add(
                new JScrollPane(resultArea), BorderLayout.CENTER);

        pane.add(bottomPanel, BorderLayout.SOUTH);

        // Register ActionListener
        smartphoneButton.addActionListener(this);
        laptopButton.addActionListener(this);
        carButton.addActionListener(this);
        checkButton.addActionListener(this);
        resetButton.addActionListener(this);

        setVisible(true);
    }

    // Select product and load sample values
    private void chooseItem(String item) {

        selectedItem = item;

        if (item.equals("SmartPhone")) {

            itemLabel.setText("Iphone 14 ProMax");
            priceLabel.setText("RM5500.00");

            customerField.setText("Sally");
            salaryField.setText("3000");

        } else if (item.equals("Laptop")) {

            itemLabel.setText("Lenovo Yoga");
            priceLabel.setText("RM6000.00");

            customerField.setText("Aaron");
            salaryField.setText("8000");

        } else {

            itemLabel.setText("BMW");
            priceLabel.setText("RM200000.00");

            customerField.setText("Simon");
            salaryField.setText("10000");
        }

        resultArea.setText("");
    }

    // Capture output from the original getPrice() method
    private String captureResult(Price price, Customer customer) {

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        PrintStream originalOut = System.out;

        try (PrintStream capture = new PrintStream(buffer)) {

            System.setOut(capture);
            price.getPrice(customer);

        } finally {

            System.setOut(originalOut);
        }

        return buffer.toString();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == smartphoneButton) {

            chooseItem("SmartPhone");

        } else if (e.getSource() == laptopButton) {

            chooseItem("Laptop");

        } else if (e.getSource() == carButton) {

            chooseItem("Car");

        } else if (e.getSource() == resetButton) {

            chooseItem("SmartPhone");

        } else if (e.getSource() == checkButton) {

            try {

                String name = customerField.getText().trim();

                double salary = Double.parseDouble(
                        salaryField.getText().trim());

                if (name.isEmpty() ||
                    !Double.isFinite(salary) ||
                    salary < 0) {

                    throw new IllegalArgumentException(
                            "Please enter a valid name and salary.");
                }

                Customer customer = new Customer(name, salary);

                Price price;

                if (selectedItem.equals("SmartPhone")) {

                    price = new SmartPhone(
                            "SmartPhone",
                            "Iphone 14 ProMax",
                            5500.00);

                } else if (selectedItem.equals("Laptop")) {

                    price = new Laptop(
                            "Laptop",
                            "Lenovo Yoga",
                            6000.00);

                } else {

                    price = new Car("BMW", 200000);
                }

                String result = captureResult(price, customer);

                resultArea.setText(result);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid number for salary.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new PriceGUI();
            }
        });
    }
}
