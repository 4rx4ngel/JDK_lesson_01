import client.ClientGUI;
import server.ServerWindow;
public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        ClientGUI clientGUI = new ClientGUI(serverWindow);
        ClientGUI clientGUI2 = new ClientGUI(serverWindow);
        int X = serverWindow.getX() + 600;
        int Y = serverWindow.getY();
        clientGUI2.setLocation(X, Y);
    }
}