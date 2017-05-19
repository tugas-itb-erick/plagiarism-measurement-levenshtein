package frame;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import weapon.type.Hammer;
import weapon.type.HammerView;
import weapon.type.ToxicGasSpray;
import weapon.type.ToxicGasSprayView;
import score.HighScoreController;

/**
 * Kelas GameFrame (GameFrame.java)
 * @author NIM/Nama: 13515021, 13515087/Dewita Sonya Tarabunga, Audry Nyonata
 */
public class GameFrame extends JFrame {
  private static final long serialVersionUID = 4153332469558642589L;
  /**
   * Atribut panel utama pada Frame.
   */
  private JPanel mainPanel;
  /**
   * Atribut panel credits pada GameFrame.
   */
  private CreditsPanel creditsPanel;
  /**
   * Atribut panel help pada GameFrame.
   */
  private HelpPanel helpPanel;
  /**
   * Atribut panel game pada GameFrame.
   */
  private GamePanel gamePanel;
  /**
   * Atribut HighScoreController untuk menampilkan high score
   */
  private HighScoreController highScoreController;
  /**
   * Atribut pilihan default weapon. 
   */
  private String selectedWeapon = "hammer";
  
  /**
   * Konstruktor.
   */
  public GameFrame() {
    initMainPanel();
    helpPanel = new HelpPanel();    
    creditsPanel = new CreditsPanel();    
    highScoreController = new HighScoreController();
    setTitle("Whack A Rat");
    setSize(800, 600);
    setContentPane(new JLabel(new ImageIcon("img/grass2.jpeg")));
    setLayout(new GridBagLayout());
    helpPanel.addLabel(getBackLabel());
    creditsPanel.addLabel(getBackLabel());
    highScoreController.getView().addLabel(getBackLabel());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    add(mainPanel);
    add(helpPanel);
    add(creditsPanel);
    add(highScoreController.getView());
    helpPanel.setVisible(false);
    creditsPanel.setVisible(false);
    highScoreController.getView().setVisible(false);
    setExtendedState(Frame.MAXIMIZED_BOTH);
    setVisible(true);
  }
  
  /**
   * Menginisialisasi main panel.
   */
  public void initMainPanel() {
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridBagLayout());
    mainPanel.setOpaque(false);
    
    String[] url = {"img/whack a rat1.png","img/whack a rat2.png","img/whack a rat3.png"};
    try {
      TimerImageSwapper tHeader = new TimerImageSwapper(url,600);
    
      JPanel control = initControl();
      JPanel weapon = initWeapon();
      
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      mainPanel.add(tHeader,gbc);
      gbc.gridx = 0;
      gbc.gridy = 1;
      mainPanel.add(control,gbc);
      gbc.gridx = 0;
      gbc.gridy = 2;
      mainPanel.add(weapon,gbc);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Mengembalikan label yang berfungsi untuk kembali ke main panel.
   * @return label back.
   */
  public JLabel getBackLabel() {
    final JLabel back = new JLabel(new ImageIcon("img/back1.png"));
    back.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent mo) {
        back.setIcon(new ImageIcon("img/back2.png"));
      }
      public void mouseClicked(MouseEvent mo) {
        back.setIcon(new ImageIcon("img/back3.png"));
        mainPanel.setVisible(true);
        if (creditsPanel!=null){
          creditsPanel.setVisible(false);
        }
        if (helpPanel!=null){
          helpPanel.setVisible(false);
        }
        if (highScoreController!=null){
          highScoreController.getView().setVisible(false);
        }
        if (gamePanel!=null){
          gamePanel.setVisible(false);
        }
        setLayout(new GridBagLayout());
      }
      public void mouseExited(MouseEvent mo) {
        back.setIcon(new ImageIcon("img/back1.png"));
      }
    });   
    return back;
  }
  
  /**
   * Menginisialisasi control untuk pergi ke panel lain.
   * @return Panel untuk mengatur control.
   */
  public JPanel initControl() {
    JPanel control = new JPanel();
    control.setLayout(new BoxLayout(control, BoxLayout.Y_AXIS));
    control.setOpaque(false);
    
    final JLabel start = new JLabel(new ImageIcon("img/start1.png"));
    control.add(start);
    
    final JLabel highScore = new JLabel("High Score");
    control.add(highScore);
    final JLabel help = new JLabel("Help");
    control.add(help);
    final JLabel credits = new JLabel("Credits");
    control.add(credits);
    
    start.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent mo) {
        start.setIcon(new ImageIcon("img/start2.png"));
      }
      public void mouseClicked(MouseEvent mo) {
        start.setIcon(new ImageIcon("img/start3.png"));
        mainPanel.setVisible(false);
        if (selectedWeapon == "hammer") {
          gamePanel = new GamePanel(new HammerView(), new Hammer(), highScoreController);
        } else {
          gamePanel = new GamePanel(new ToxicGasSprayView(), new ToxicGasSpray(), highScoreController);
        }
        setLayout(new BorderLayout());
        add(gamePanel);
        gamePanel.setVisible(true);
        Timer done = new Timer(60000, new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
            gamePanel.setVisible(false);
            final JLabel up = new JLabel(new ImageIcon("img/up.png"));
            setLayout(new GridBagLayout());
            up.setText("Your Score : " + Integer.toString(gamePanel.getScore()));
            up.setHorizontalTextPosition(JLabel.CENTER);
            up.setVerticalTextPosition(JLabel.BOTTOM);
            up.setFont(new Font("Purisa", Font.BOLD, 30));
            up.setForeground(Color.YELLOW);
            add(up);
            Timer ups = new Timer(2000, new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                up.setVisible(false);
                highScoreController.add("anonymous", gamePanel.getScore());
                mainPanel.setVisible(true);
              }
            });
            ups.setRepeats(false);
            ups.start();
          }
        });
        done.setRepeats(false);
        done.start();
      }
      public void mouseExited(MouseEvent mo) {
        start.setIcon(new ImageIcon("img/start1.png"));
      }
    });
    
    MouseListener cl = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        JLabel temp= (JLabel)e.getSource();
        temp.setForeground(Color.black);
        mainPanel.setVisible(false);
        if (temp == highScore) {
          highScoreController.getView().setVisible(true);
        } else if (temp == help) {
          helpPanel.setVisible(true);
        } else if (temp == credits) {
          creditsPanel.setVisible(true);
        }
      }
      public void mouseEntered(MouseEvent e) {
        JLabel temp= (JLabel)e.getSource();
        temp.setForeground(Color.red);
      }
      public void mouseExited(MouseEvent e) {
        JLabel temp= (JLabel)e.getSource();
        temp.setForeground(Color.orange);
      }
    };
    for (int i = 0; i < control.getComponents().length; ++i) {
      if (control.getComponent(i) instanceof JLabel) {
        JLabel label = (JLabel)control.getComponent(i);
        label.setForeground(Color.orange);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22);
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.addMouseListener(cl);
      }
    }
    return control;
  }
  
  /**
   * Menginisialisasi label untuk pemilihan senjata.
   * @return Panel untuk mengatur senjata yang dipilih.
   */
  public JPanel initWeapon() {
    JPanel weapon = new JPanel(new GridLayout(1, 2));
    weapon.setOpaque(false);
    
    final JLabel hammer = new JLabel(new ImageIcon(new ImageIcon("img/hammer2.png").getImage().getScaledInstance(80, 80, 1)));
    weapon.add(hammer);
    
    final JLabel spray = new JLabel(new ImageIcon(new ImageIcon("img/spray2grey.png").getImage().getScaledInstance(80, 80, 1)));
    weapon.add(spray);
    
    MouseListener WeaponPanelListener = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        JLabel temp = (JLabel)e.getSource();
        if (temp == hammer) {
          hammer.setIcon(new ImageIcon(new ImageIcon("img/hammer2.png").getImage().getScaledInstance(80, 80,1)));        
          spray.setIcon(new ImageIcon(new ImageIcon("img/spray2grey.png").getImage().getScaledInstance(80, 80,1)));
          selectedWeapon = "hammer";
        } else {
          hammer.setIcon(new ImageIcon(new ImageIcon("img/hammer2grey.png").getImage().getScaledInstance(80, 80,1)));        
          spray.setIcon(new ImageIcon(new ImageIcon("img/spray2.png").getImage().getScaledInstance(80, 80,1)));
          selectedWeapon = "spray";
        }
      }
    };
    
    Component[] component = weapon.getComponents();
    for(int i =0; i<component.length; i++) {
      if (component[i] instanceof JLabel) {
        JLabel label = (JLabel)component[i];
        label.addMouseListener(WeaponPanelListener);
      }
    }
    return weapon;
  }
}

package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import animal.Animal;
import animal.AnimalController;
import animal.AnimalView;
import animal.species.Chick;
import animal.species.ChickView;
import animal.species.Cockroach;
import animal.species.CockroachView;
import animal.species.Hamster;
import animal.species.HamsterView;
import animal.species.Rat;
import animal.species.RatView;

import score.HighScoreController;
import score.Score;
import score.ScoreController;
import score.ScoreView;

import weapon.Weapon;
import weapon.WeaponController;
import weapon.WeaponView;

/**
 * Kelas GamePanel (GamePanel.java)
 * @author NIM/Nama: 13515087/Audry Nyonata
 */
public class GamePanel extends JPanel {
  private static final long serialVersionUID = 3086601523332143745L;
  /**
   * Atribut WeaponView pada GamePanel.
   */
  private WeaponView cursor;
  /**
   * Atribut ScoreView pada GamePanel.
   */
  private ScoreView score;
  /**
   * Atribut Score pada GamePanel.
   */
  private Score skor;
  /**
   * Atribut WeaponController pada GamePanel.
   */
  private WeaponController wco;
  /**
   * Atribut ScoreController pada GamePanel.
   */
  private ScoreController sco;
  /**
   * Atribut controller highscore.
   */
  private HighScoreController hsco;
  /**
   * Atribut counter pada GamePanel.
   */
  private int cnt = 60;
  
  /**
   * Konstruktor dengan parameter.
   * @param vi WeaponView pada GamePanel.
   * @param we Weapon pada GamePanel.
   * @param hco Controller highscore pada GamePanel.
   */
  public GamePanel(WeaponView vi, Weapon we, HighScoreController hco) {
    super();
    setLayout(new BorderLayout());
    setOpaque(false);
    cursor = vi;
    hsco = hco;
    add(cursor, BorderLayout.CENTER);
    score = new ScoreView(0);
    skor = new Score(0);
    vi.setLayout(new BorderLayout());
    vi.add(score, BorderLayout.NORTH);
    final JLabel times = new JLabel();
    ImageIcon kayu = new ImageIcon("img/kayu.png");
    Image img = kayu.getImage();
    Image newimg = img.getScaledInstance(100, 50, java.awt.Image.SCALE_SMOOTH);
    kayu = new ImageIcon(newimg);
    times.setIcon(kayu);
    times.setText("01:00");
    times.setHorizontalTextPosition(JLabel.CENTER);
    times.setVerticalTextPosition(JLabel.CENTER);
    times.setFont(new Font("Purisa", Font.BOLD, 30));
    times.setOpaque(false);
    times.setForeground(Color.WHITE);
    vi.add(times, BorderLayout.SOUTH);
    wco = new WeaponController(we, vi);
    sco = new ScoreController(skor, score);
    final Random rand = new Random();
    Timer appearTimer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        int y = rand.nextInt(cursor.getHeight());
        int animal = rand.nextInt(4);
        cursor.setLayout(null);
        AnimalView av;
        Animal an;
        if (animal == 0) {
          av = new HamsterView();
          an = new Hamster();
        } else if (animal == 1) {
          av = new ChickView();
          an = new Chick();
        } else if (animal == 2) {
          av = new CockroachView();
          an = new Cockroach();
        } else {
          av = new RatView();
          an = new Rat();
        }
        new AnimalController(an, av);
        av.setLocation(0, y);
        av.addMouseListener(wco.getListener());
        av.addMouseListener(sco.control(an.getScore()));
        cursor.add(av);
      }
    });
    Timer time = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        --cnt;
        times.setText("00:" + Integer.toString(cnt));
      }
    });
    appearTimer.start();
    time.start();
  }
  
  /**
   * Mengembalikan WeaponView pada GamePanel.
   * @return cursor. 
   */
  public WeaponView getWeaponView() {
    return cursor;
  }
  
  /**
   * Mengembalikan ScoreView pada GamePanel.
   * @return score.
   */
  public ScoreView getScoreView() {
    return score;
  }
  
  /**
   * Menambahkan label pada GamePanel.
   * @param jl label yang akan ditambahkan ke GamePanel.
   */
  public void addLabel(JLabel jl) {
    cursor.add(jl, BorderLayout.SOUTH);
  }
  
  /**
   * Membuat GamePanel terlihat.
   */
  public void setVisible(boolean bo) {
    super.setVisible(bo);
    cursor.setVisible(bo);
  }
  
  /**
   * Mengembalikan score pada permainan.
   * @return score permainan.
   */
  public int getScore() {
    return sco.getScore();
  }
}

package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Kelas TimerImageSwapper (TimerImageSwapper.java)
 * @author NIM/Nama: 13515087/Audry Nyonata
 */
public class TimerImageSwapper extends JLabel{
  private static final long serialVersionUID = 4978187277656439360L;
  private int iconIndex = 0;

  /**
   * Konstruktor dengan parameter.
   * @param url array of string yang akan saling berganti.
   * @param delay nilai waktu delay.
   */
  public TimerImageSwapper(final String[] url, int delay) {
    super();
    final ImageIcon[] icons = new ImageIcon[url.length];
    for (int i = 0; i < icons.length; i++) {
        icons[i] = new ImageIcon(url[i]);
    }
    setIcon(icons[iconIndex ]);
    new Timer(delay, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        iconIndex++;
        iconIndex %= url.length;
        setIcon(icons[iconIndex]);
          }
    }).start();
  }
  
  /**
   * Konstruktor dengan parameter.
   * @param url array of string yang akan saling berganti.
   * @param delay nilai waktu delay.
   * @param width nilai lebar dari image.
   * @param height nilai panjang dari image.
   */
  public TimerImageSwapper(final String[] url, int delay, int width, int height) {
    super();
    final ImageIcon[] icons = new ImageIcon[url.length];
    for (int i = 0; i < icons.length; i++) {
        icons[i] = new ImageIcon(new ImageIcon(url[i]).getImage().getScaledInstance(width, height, 1));
    }
    setIcon(icons[iconIndex ]);
    new Timer(delay, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        iconIndex++;
        iconIndex %= url.length;
        setIcon(icons[iconIndex]);
          }
    }).start();
  }
}