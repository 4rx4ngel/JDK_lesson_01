package server;

import client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;


    JButton btnStart = new JButton("Start");
    JButton btnStop = new JButton("Stop");
    JTextArea log;
    JPanel bottom;
    private boolean isServerWorking;
    private static final String LOG_PATH = "src/server/log.txt";
    List<ClientGUI> clientGUIList;

    public ServerWindow() {
        clientGUIList = new ArrayList<>();

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isServerWorking) {
                    appendLog("Server has already stopped");
                } else {
                    isServerWorking = false;
                    while (!clientGUIList.isEmpty()) {
                        disconnectUser(clientGUIList.getLast());
                    }
                    appendLog("Server stopped");
                }

            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerWorking) {
                    appendLog("Server is already running");
                } else {
                    isServerWorking = true;
                    appendLog("Server is running");
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Server");
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        log = new JTextArea();
        log.setEditable(false);
        bottom = new JPanel(new GridLayout(1, 3));
        bottom.add(btnStart);
        bottom.add(new JPanel());
        bottom.add(btnStop);
        add(log);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }


    private String readLog() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);) {
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getLog() {
        return readLog();
    }

    private void saveInLog(String text) {
        try (FileWriter writer = new FileWriter(LOG_PATH, true)) {
            writer.write(text);
            writer.write("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void answerAll(String text) {
        for (ClientGUI clientGUI : clientGUIList) {
            clientGUI.answer(text);
        }
    }

    public boolean connectUser(ClientGUI clientGUI) {
        if (!isServerWorking) {
            return false;
        }
        clientGUIList.add(clientGUI);
        return true;
    }

    public void disconnectUser(ClientGUI clientGUI) {
        clientGUIList.remove(clientGUI);
        if (clientGUI != null) {
            clientGUI.disconnectFromServer();
        }
    }

    private void appendLog(String text) {
        log.append(text + "\n");
    }

    public void message(String text) {
        if (!isServerWorking) {
            return;
        }
        text += "";
        appendLog(text);
        answerAll(text);
        saveInLog(text);
    }
}
