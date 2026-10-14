package lab.scd.net.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class SimpleClient {

    private static Socket socket = null;
    private static BufferedReader in = null;
    private static PrintWriter out = null;

    public static void main(String[] args) {
        try {

            InetAddress address = InetAddress.getByName("192.168.1.37"); // server IP
            socket = new Socket(address, 1900);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // GUI
            createAndShowGUI();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Could not connect to the server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Network Calculator - Client");
        frame.setSize(380, 220);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2, 8, 8));

        JLabel lblNum1 = new JLabel(" First number:");
        JTextField txtNum1 = new JTextField();

        JLabel lblNum2 = new JLabel(" Second number:");
        JTextField txtNum2 = new JTextField();

        JLabel lblOp = new JLabel(" Select operation:");
        JPanel panelButtons = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton btnAdd = new JButton("+");
        JButton btnSub = new JButton("-");
        JButton btnMul = new JButton("*");
        JButton btnDiv = new JButton("/");
        panelButtons.add(btnAdd);
        panelButtons.add(btnSub);
        panelButtons.add(btnMul);
        panelButtons.add(btnDiv);

        JLabel lblRes = new JLabel(" Server response:");
        JTextField txtRes = new JTextField();
        txtRes.setEditable(false);

        frame.add(lblNum1);
        frame.add(txtNum1);
        frame.add(lblNum2);
        frame.add(txtNum2);
        frame.add(lblOp);
        frame.add(panelButtons);
        frame.add(lblRes);
        frame.add(txtRes);

        // Action listener for all 4 buttons
        ActionListener calcAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String n1 = txtNum1.getText().trim();
                String n2 = txtNum2.getText().trim();
                String op = ((JButton) e.getSource()).getText();

                if (n1.isEmpty() || n2.isEmpty()) {
                    txtRes.setText("Please fill in both numbers!");
                    return;
                }

                try {
                    // Send command to server (e.g., "12 + 4")
                    out.println(n1 + " " + op + " " + n2);

                    // Read response sent by server
                    String response = in.readLine();
                    txtRes.setText(response);
                } catch (Exception ex) {
                    txtRes.setText("Communication error!");
                }
            }
        };

        btnAdd.addActionListener(calcAction);
        btnSub.addActionListener(calcAction);
        btnMul.addActionListener(calcAction);
        btnDiv.addActionListener(calcAction);

        frame.setVisible(true);
    }
}