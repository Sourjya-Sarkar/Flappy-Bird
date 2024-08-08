import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth = 1280;
        int boardheight = 748;
        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Flappy flappybird = new Flappy();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);
    }
}
