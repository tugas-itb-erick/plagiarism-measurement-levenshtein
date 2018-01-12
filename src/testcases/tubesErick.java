package ghostgame;

import ghostgame.display.Display;

import ghostgame.gfx.Assets;

import ghostgame.gfx.GameCamera;

import ghostgame.input.KeyManager;

import ghostgame.input.MouseManager;

import ghostgame.states.MenuState;

import ghostgame.states.State;

import java.awt.Color;

import java.awt.Graphics;

import java.awt.image.BufferStrategy;

/**
 * Kelas Game merepresentasikan permainan.
 * @author Erick Wijaya - 13515057
 */

public class Game implements Runnable {

  private Display display;
  private int width;
  private int height;
  public String title;
  private boolean running = false;
  private Thread thread;
  private BufferStrategy bs;
  private Graphics gg;
  private State state;
  private KeyManager keyManager;
  private MouseManager mouseManager;
  private GameCamera gameCamera;
  private Handler handler;
  
  /**
    * Comstructor dgn parameter
    * @param title Nilai yang akan dimasukka n ke atribut shell.
    * @param width Nilai yang akan dijadikan lebarnya.
    * @param height Nilai yang akan dicari pemaen.
    */
  
  public Game(String title, int width, int height) {
    this.width = width;
    this.height = height;
    this.title = title;
    keyManager = new KeyManager();
    mouseManager = new MouseManager();
  }
  
  /**
    * Menginisialisasi semua yang dibutuhkan, termasuk display.
    */
  
  private void init() {
    display = new Display(title, width, height);
    display.getFrame().addKeyListener(keyManager);
    display.getFrame().addMouseListener(mouseManager);
    display.getFrame().addMouseMotionListener(mouseManager);
    display.getCanvas().addMouseListener(mouseManager);
    display.getCanvas().addMouseMotionListener(mouseManager);
    Assets.init();
    handler = new Handler(this);
    gameCamera = new GameCamera(handler, 0, 0);
    state = new MenuState(handler);
  }

  /**
    * Meng-update kondisi objek keyManager untuk setiap satuan waktu.
    */
  
  private void tick() {
    assert (keyManager != null);
    keyManager.tick();
    if (state != null) {
      state.tick();
    }
  }

  /**
    * Fungsi yang menampilkan gambar (frame) dari canvas.
    * @param g Nilai grafik yang mencetak gambar (frame) dari player.
    */

  private void render() {
    assert (display != null);
    bs = display.getCanvas().getBufferStrategy();
    if (bs == null) {
      display.getCanvas().createBufferStrategy(3);
      return;
    }
    gg = bs.getDrawGraphics();
    gg.clearRect(0, 0, width, height);
    gg.setColor(Color.BLACK);
    gg.fillRect(0, 0, width, height);
    if (state != null) {
      state.render(gg);
    }
    bs.show();
    gg.dispose();
  }
  
  /** 
    * Menjalankan program dengan pengaturan 60 frames per sekon.
    */
  
  public void run() { 
    init();
    int fps = 60;
    double timePerTick = 1000000000 / fps;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    long timer = 0;
    int ticks = 0;
    while (running) {
      now = System.nanoTime();
      delta += (now - lastTime) / timePerTick;
      timer += now - lastTime;
      lastTime = now;
      if (delta >= 1) {
        tick();
        render();
        ticks++;
        delta--;
      }
      if (timer >= 1000000000) {
        System.out.println("FPS: " + ticks);
        ticks = 0;
        timer = 0;
      }
    }
    stop();
  }
  
  /**
    * Mengembalikan nilai dari keyManager.
    * @return Nilai keyManager.
    */
  
  public KeyManager getKeyManager() {
    return keyManager;
  }

  /**
    * Mengembalikan nilai dari mouseManager.
    * @return nilai mouseManager.
    */
  
  public MouseManager getMouseManager() {
    return mouseManager;
  }

  /**
    * Mengembalikan nilai dari gameCamera.
    * @return nilai gameCamera.
    */
  
  public GameCamera getGameCamera() {
    return gameCamera;
  }

  /**
    * Mengembalikan nilai dari width.
    * @return nilai width.
    */
  
  public int getWidth() {
    return width;
  }

  /**
    * Mengembalikan nilai dari ukj.
    * @return nilai uk.
    */
  
  public int getHeight() {
    return height;
  }
  
  /**
    * Fungsi untuk memulai sebuah thread permainan baru.
    */
  
  public synchronized void start() {
    if (running) {
      return;
    }
    running = true;
    thread = new Thread(this);
    thread.start();
  }
  
  /**
    * Fungsi untuk memberhentikan thread.
    */
  
  public synchronized void stop() {
    if (!running) {
      return;
    }
    running = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
    * Mengembalikan state.
    * @return nilai width.
    */
  
  public State getState() {
    return state;
  }

  /**
    * I.S. atribut state sembarang.
    * F.S. atribut state terdefinisi.
    * @param state Nilai yang akan dimasukkan ke dalam state.
    */
  
  public void setState(State state) {
    this.state = state;
  }
}

package ghostgame.display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * File : Display.java.
 * Kelas Display merepresentasikan tampilan.
 * @author Erick Wijaya - 13515057
 */

public class Display {

  private JFrame frame;
  private Canvas canvas;
  private String title;
  private int width;
  private int height;
  
  /**
   * Constructor.
   * @param title Nama display.
   * @param width Lebar display.
   * @param height Tinggi display.
   */

  public Display(String title, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
    
    createDisplay();
  }
  
  /**
   * Membuat display.
   */

  private void createDisplay() {
    frame = new JFrame(title);
    frame.setSize(width, height);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(width, height));
    canvas.setMaximumSize(new Dimension(width, height));
    canvas.setMinimumSize(new Dimension(width, height));
    canvas.setFocusable(false);
    
    frame.add(canvas);
    frame.pack();
  }

  /**
   * Mengembalikan canvas.
   * @return canvas.
   */

  public Canvas getCanvas() {
    return canvas;
  }
  
  /**
   * Mengembalikan frame.
   * @return frame.
   */
  
  public JFrame getFrame() {
    return frame;
  }
  
}

package ghostgame.ui;

/**
 * File : ClickListener.java.
 * Kelas ClickListener merepresentasikan click listener.
 * @author 
 */

public interface ClickListener {
  
  /**
   * Mengubah state ketika click.
   */
  
  public void onClick();

}

package ghostgame.ui;

/**
  * File : UIimage.java.
  * Kelas UIimage merepresentasikan objek gambar.
  * @author Veren Iliana K - 13515057.
  */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** 
 * File : UIimage.java.
 * Kelas yang merepresentasikan object gambar interface yang ada pada game ini.
 * @author
 */

public class UIimage extends UIobject {

  private BufferedImage image;
  
  /**
   * Constructor dengan parameter.
   * @param x Posisi sumbu x.
   * @param y Posisi sumbu y.
   * @param width Lebar UIimage.
   * @param height Tinggi UIimage.
   * @param image gambar UIimage.
   */

  public UIimage(float x, float y, int width, int height, BufferedImage image) {
    super(x, y, width, height);
    this.image = image;
  }

  /**
   * Mengupdate kondisi state setiap satuan waktu.
   */

  @Override
  public void tick() {}

  /**
   * Menampilkan gambar sesuai dengan jenisnya.
   * @param g Gambar.
   */

  @Override
  public void render(Graphics g) {
    g.drawImage(image, (int) posX, (int) posY, width, height, null);
  }

  /**
   * Mengubah state ketika click.
   */

  @Override
  public void onClick() {
  }

}

package ghostgame.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/** 
 * File : UIobject.java.
 * Kelas yang merepresentasikan object interface yang ada pada game ini.
 * @author
 */

public abstract class UIobject {
  
  protected float posX;
  protected float posY;
  protected int width;
  protected int height;
  protected Rectangle bounds;
  protected boolean hovering = false;
  
  /**
   * @param posX Posisi sumbu x.
   * @param posY Posisi sumbu y.
   * @param width lebah UI Object.
   * @param height tinggi UI Object.
   */
  
  public UIobject(float posX, float posY, int width, int height) {
    this.posX = posX;
    this.posY = posY;
    this.width = width;
    this.height = height;
    bounds = new Rectangle((int) posX, (int) posY, width, height);
  }
  
  /**
   * Mengupdate kondisi state setiap satuan waktu.
   */
  
  public abstract void tick();
  
  /**
   * Menampilkan gambar sesuai dengan jenisnya.
   * @param g Gambar.
   */
  
  public abstract void render(Graphics g);
  
  /**
   * Mengubah state ketika click.
   */
  
  public abstract void onClick();
  
  /**
   * Mengubah state ketika mouse bergerak.
   * @param e MouseEvent.
   */
  
  public void onMouseMove(MouseEvent e) {
    if (bounds.contains(e.getX(), e.getY())) {
      hovering = true;
    } else {
      hovering = false;
    }
  }
  
  /**
   * Mengubah state ketika mouse dilepas.
   * @param e MouseEvent.
   */
  
  public void onMouseRelease(MouseEvent e) {
    if (hovering) {
      onClick();
    }
  }

  /**
   * Mengembalikan x.
   * @return x.
   */
  
  public float getX() {
    return posX;
  }
  
  /**
   * Mengubah x
   * @param x Posisi x.
   */
  
  public void setX(float x) {
    this.posX = x; 
  }

  /**
   * Mengembalikan y.
   * @return y.
   */
   
  public float getY() {
    return posY;
  }

  /**
    * Mengubah y
    * @param y Posisi y.
    */
  
  public void setY(float y) {
    this.posY = y;
  }

  /**
   * Mengembalikan width.
   * @return width.
   */
  
  public int getWidth() {
    return width;
  }

  /**
   * Mengubah width.
   * @param width Lebar.
   */
  
  public void setWidth(int width) {
    this.width = width;
  }
  
  /**
   * Mengembalikan height.
   * @return height.
   */
  
  public int getHeight() {
    return height;
  }

  /**
   * Mengubah height.
   * @param height Tinggi.
   */
  
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Mengembalikan kondisi hovering.
   * @return true jika mouse menyentuh object.
   */
  
  public boolean isHovering() {
    return hovering;
  }

  /**
   * Mengubah kondisi hovering
   * @param hovering Kondisi mouse da object (bersentuhan atau tidak).
   */
  public void setHovering(boolean hovering) {
    this.hovering = hovering;
  }

}