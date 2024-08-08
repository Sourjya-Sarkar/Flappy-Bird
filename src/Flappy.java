import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Flappy extends JPanel implements ActionListener, KeyListener {
    int boardwidth = 1280;
    int boardheight = 748;
    // images
    Image backgroundImage;
    Image birdimage;
    Image ToppipeImage;
    Image BottompipeImage;
    // bird
    int birdx = boardwidth / 8;
    int birdy = boardheight / 2;
    int birdwidth = 34;
    int birdheight = 24;

    class Bird {
        int x = birdx;
        int y = birdy;
        int width = birdwidth;
        int height = birdheight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // pipe
    int pipex = boardwidth;
    int pipey = 0;
    int pipewidth = 64;
    int pipeheight = 512;

    class Pipe {
        int x = pipex;
        int y = pipey;
        int width = pipewidth;
        int height = pipeheight;
        Image img;
        boolean pass = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // game logic
    Bird bird;
    Timer gameloop;
    Timer placepipeTimer;
    int velocityX = -4;// for pipe
    int velocityY = 0;// for bird
    int gravity = 1;
    boolean gameover = false;
    double score = 0;
    ArrayList<Pipe> pipes = new ArrayList<>();
    Random random = new Random();

    Flappy() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);
        // load images
        backgroundImage = new ImageIcon(getClass().getResource("./gaERSZ.png")).getImage();
        birdimage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        ToppipeImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        BottompipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird = new Bird(birdimage);
        placepipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        placepipeTimer.start();// start the pipes
        gameloop = new Timer(1000 / 60, this);
        gameloop.start();// start the game loop

    }

    public void placePipe() {
        int randompipey = (int) (pipey - pipeheight / 4 - Math.random() * (pipeheight / 2));
        Pipe topPipe = new Pipe(ToppipeImage);
        Pipe bottompipe = new Pipe(BottompipeImage);
        topPipe.y = randompipey;
        pipes.add(topPipe);
        int opening = boardheight / 4;
        bottompipe.y = topPipe.y + pipeheight + opening;
        pipes.add(bottompipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, boardwidth, boardheight, null);
        g.drawImage(birdimage, bird.x, bird.y, bird.width, bird.height, null);
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(ToppipeImage, pipe.x, pipe.y, pipewidth, pipeheight, null);
            // score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            if (gameover) {
                g.drawString("GAME OVER : " + String.valueOf((int) score), 10, 35);
            } else {
                g.drawString(String.valueOf((int) score), 10, 35);
            }
        }
    }

    public void move() {
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);// top pipe
        // pipe
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            if (!pipe.pass && bird.x > pipe.x + pipewidth) {
                pipe.pass = true;
                score += 0.5;
            }
            if (collision(bird, pipe)) {
                gameover = true;
            }

            if (bird.y > boardheight)
                gameover = true;

        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameover) {
            gameloop.stop();
            placepipeTimer.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            // restart
            if (gameover) {
                bird.y = birdy;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameover = false;
                gameloop.start();
                placepipeTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}