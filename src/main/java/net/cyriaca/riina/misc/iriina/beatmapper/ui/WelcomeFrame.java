package net.cyriaca.riina.misc.iriina.beatmapper.ui;

import net.cyriaca.riina.misc.iriina.beatmapper.IRiina;
import net.cyriaca.riina.misc.iriina.beatmapper.IRiina.View;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeEvent;
import net.cyriaca.riina.misc.iriina.beatmapper.LocaleChangeListener;
import net.cyriaca.riina.misc.iriina.generic.localization.Locale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class WelcomeFrame extends JFrame implements IViewFrame, LocaleChangeListener, ActionListener {

    private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 120);

    private static final int IMG_TARGET_HEIGHT = 350;
    private static final float IMG_SHOW_PERC_W = 0.8f;
    private static final float IMG_SHOW_PERC_H = 0.7f;

    private static final String KEY_FRAME_WELCOME_TITLE = "frame_welcome_title";
    private static final String KEY_FRAME_WELCOME_TEXT_CREATE_NEW_PROJECT = "frame_welcome_text_create_new_project";
    private static final String KEY_FRAME_WELCOME_TEXT_IMPORT_MAP = "frame_welcome_text_import_map";
    private static final String KEY_FRAME_WELCOME_TEXT_OPEN_PROJECT = "frame_welcome_text_open_project";

    private static final String KEY_MENU_FILE = "menu_file";
    private static final String KEY_MENU_FILE_NEW_PROJECT = "menu_file_new_project";
    private static final String KEY_MENU_FILE_IMPORT_MAP = "menu_file_import_map";
    private static final String KEY_MENU_FILE_OPEN_PROJECT = "menu_file_open_project";
    private static final String KEY_MENU_HELP = "menu_help";
    private static final String KEY_MENU_HELP_ABOUT = "menu_help_about";
    private static final String KEY_MENU_HELP_PREFERENCES = "menu_help_preferences";

    private IRiina parent;
    private Image tempImg;

    private JLabel welcomeLabel;
    private JPanel welcomePanel;
    private JButton createNewProjectButton;
    private JButton importMapButton;
    private JButton openProjectButton;
    private JPanel contentPane;
    private JPanel buttonPanel;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newProjectItem;
    private JMenuItem openProjectItem;
    private JMenuItem importMapItem;
    private JMenu helpMenu;
    private JMenuItem aboutItem;
    private JMenuItem preferencesItem;

    public WelcomeFrame(IRiina parent) {
        super();
        this.parent = parent;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.tempImg = parent.getTempImg();

        contentPane = new JPanel();
        contentPane = new WelcomeBackgroundPanel();
        setContentPane(contentPane);
        welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setOpaque(false);
        welcomeLabel = new JLabel("IRiina");
        welcomeLabel.setFont(TITLE_FONT);
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.CENTER);
        contentPane.add(welcomePanel);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        createNewProjectButton = new JButton();
        buttonPanel.add(createNewProjectButton);
        createNewProjectButton.addActionListener(this);
        importMapButton = new JButton();
        buttonPanel.add(importMapButton);
        importMapButton.addActionListener(this);
        openProjectButton = new JButton();
        buttonPanel.add(openProjectButton);
        openProjectButton.addActionListener(this);
        add(buttonPanel, BorderLayout.SOUTH);

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        menuBar.add(fileMenu);
        JMenuItem menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        newProjectItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        openProjectItem = menuItem;
        fileMenu.addSeparator();
        menuItem = new JMenuItem();
        fileMenu.add(menuItem);
        menuItem.addActionListener(this);
        importMapItem = menuItem;
        helpMenu = new JMenu();
        menuBar.add(helpMenu);
        menuItem = new JMenuItem();
        helpMenu.add(menuItem);
        menuItem.addActionListener(this);
        aboutItem = menuItem;
        menuItem = new JMenuItem();
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        helpMenu.add(menuItem);
        menuItem.addActionListener(this);
        preferencesItem = menuItem;
        setJMenuBar(menuBar);

        setResizable(false);
    }

    public void setupAndShowFrame() {
        setSize(800, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void cleanupAndHideFrame() {
        setVisible(false);
    }

    public void localeChangePerformed(LocaleChangeEvent event) {
        localizeStrings(event.getNewLocale());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createNewProjectButton || e.getSource() == newProjectItem) {
            parent.setView(View.NewProjectDialogFrame);
        } else if (e.getSource() == importMapButton || e.getSource() == importMapItem) {
            parent.setView(View.MapImportFrame);
        } else if (e.getSource() == openProjectButton || e.getSource() == openProjectItem) {
            parent.setView(View.OpenProjectDialogFrame);
        } else if (e.getSource() == aboutItem) {
            parent.showAboutFrame();
        } else if (e.getSource() == preferencesItem)
            parent.showPreferencesFrame();
    }

    public void localizeStrings(Locale l) {
        setTitle(l.getKey(KEY_FRAME_WELCOME_TITLE));
        createNewProjectButton.setText(l.getKey(KEY_FRAME_WELCOME_TEXT_CREATE_NEW_PROJECT));
        importMapButton.setText(l.getKey(KEY_FRAME_WELCOME_TEXT_IMPORT_MAP));
        openProjectButton.setText(l.getKey(KEY_FRAME_WELCOME_TEXT_OPEN_PROJECT));
        fileMenu.setText(l.getKey(KEY_MENU_FILE));
        newProjectItem.setText(l.getKey(KEY_MENU_FILE_NEW_PROJECT));
        openProjectItem.setText(l.getKey(KEY_MENU_FILE_OPEN_PROJECT));
        importMapItem.setText(l.getKey(KEY_MENU_FILE_IMPORT_MAP));
        helpMenu.setText(l.getKey(KEY_MENU_HELP));
        aboutItem.setText(l.getKey(KEY_MENU_HELP_ABOUT));
        preferencesItem.setText(l.getKey(KEY_MENU_HELP_PREFERENCES));
    }

    class WelcomeBackgroundPanel extends JPanel {

        WelcomeBackgroundPanel() {
            setLayout(new BorderLayout());
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int targetHeight = IMG_TARGET_HEIGHT;
            int targetWidth = IMG_TARGET_HEIGHT * tempImg.getWidth(null) / tempImg.getHeight(null);
            g2.drawImage(tempImg, getWidth() - (int) (targetWidth * IMG_SHOW_PERC_W), getHeight() - (int) (targetHeight * IMG_SHOW_PERC_H), targetWidth, targetHeight, null);
        }
    }
}
