import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.*;

public class EggCatcher extends JPanel implements ActionListener, KeyListener {

    // Game objects
    Image background, eggImg;
    int basketX = 250;
    int eggX, eggY;
    int score = 0;

    Timer timer;
    Random rand = new Random();

    Clip catchSound, missSound;

    public EggCatcher() {
        setPreferredSize(new Dimension(600, 400));
        setFocusable(true);
        addKeyListener(this);

        loadImages();
        loadSounds();
        resetEgg();

        timer = new Timer(20, this);
        timer.start();
    }

    void loadImages() {
        background = new ImageIcon("resources/background.png").getImage();
        eggImg = new ImageIcon("resources/egg.png").getImage();
    }

    void loadSounds() {
        catchSound = loadSound("resources/catch.wav");
        missSound = loadSound("resources/miss.wav");
    }

    Clip loadSound(String path) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void resetEgg() {
        eggX = rand.nextInt(540); // egg width ~60
        eggY = 0;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, null);
        g.drawImage(eggImg, eggX, eggY, 60, 80, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
    }

    public void actionPerformed(ActionEvent e) {
        eggY += 5;

        if (eggY > 300 && eggX >= basketX && eggX <= basketX + 100) {
            // Egg caught
            if (catchSound != null) {
                catchSound.setFramePosition(0);
                catchSound.start();
            }
            score++;
            resetEgg();
        } else if (eggY > 400) {
            // Egg missed
            if (missSound != null) {
                missSound.setFramePosition(0);
                missSound.start();
            }
            resetEgg();
        }

        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && basketX > 0)
            basketX -= 15;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && basketX < 500)
            basketX += 15;
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Egg Catcher Game");
        EggCatcher game = new EggCatcher();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
