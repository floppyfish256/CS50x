import javax.swing.*;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Island Adventure");
		
		Screen sc = new Screen();
		frame.add(sc);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
        frame.setResizable(false);
		frame.setVisible(true);

        sc.animate();
    }
}