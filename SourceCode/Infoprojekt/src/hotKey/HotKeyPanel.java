package hotKey;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.App;
import main.InputHandler;
import main.LoadStoreHotKeys;

public class HotKeyPanel extends JPanel implements Runnable {
    /**
     * generic width of the buttons in the starting menu
     */
    private final int buttonWidth = 230;

    /**
     * generic height of the buttons in the starting menu
     */
    private final int buttonHeight = 35;

    /**
     * desired frame rate of the game, it can't be higher but can drop under load
     */
    private final int frameRate;

    /**
     * to be done
     */
    private HotKeyInputHandler hotKeyInputHandler;

    /**
     * button to return to main menu
     */
    private JButton returnToMainMenu;

    /**
     * to be done
     */
    private HashMap<String, JButton> buttons;

    /**
     * to be done
     */
    private HashMap<String, JLabel> actions;

    /**
     * to be done
     */
    private String nameOfActionToChange;

    /**
     * to be done
     */
    private HashMap<String, Character> iMN;

    /**
     * to be done
     */
    private HashMap<String, Character> iMNH;

    /**
     * to be done
     */
    private HashMap<String, Character> iMH;

    /**
     * thread that keeps the game running
     */
    private Thread thread;

    /**
     * Constructor of the starting panel.
     * Instantiates the buttons, adds listeners to them and
     * sets important values of the frame.
     * 
     * @param pFR the desired frame rate of the starting menu
     */
    public HotKeyPanel(int pFR) {
        frameRate = pFR;
        InputHandler inputHandler = new InputHandler();
        addKeyListener(inputHandler);
        hotKeyInputHandler = new HotKeyInputHandler(inputHandler);
        this.setLayout(null);
        this.setPreferredSize(null);
        this.setDoubleBuffered(true);
        this.setOpaque(true);
        this.setFocusable(true);
        returnToMainMenu = new JButton("return to main menu");
        returnToMainMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                App.loadStartingScreen();
                thread = null;
            }
        });
        returnToMainMenu.setFont(new Font("Arial", Font.BOLD, 15));
        add(returnToMainMenu);

        HashMap<String, HashMap<String, Character>> tempMap = LoadStoreHotKeys.loadHotKeys();
        iMN = tempMap.get("normal");
        iMNH = tempMap.get("not_holdable");
        iMH = tempMap.get("holdable");

        HashMap<String, Character> unionInputMap = new HashMap<String, Character>();
        unionInputMap.putAll(iMN);
        unionInputMap.putAll(iMNH);
        unionInputMap.putAll(iMH);

        actions = new HashMap<String, JLabel>(11);
        buttons = new HashMap<String, JButton>(11);
        JButton temp;
        JLabel textField;
        for (Entry<String, Character> inputMapping : unionInputMap.entrySet()) {
            textField = new JLabel(inputMapping.getKey() + ":");
            textField.setFont(new Font("Arial", Font.BOLD, 15));
            actions.put(inputMapping.getKey(), textField);
            add(textField);

            temp = new JButton(String.valueOf(charToHumanReadableString(inputMapping.getValue())));
            temp.setFont(new Font("Arial", Font.BOLD, 15));
            temp.setRequestFocusEnabled(false);
            temp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changeKeyOf(inputMapping.getKey());
                }
            });
            buttons.put(inputMapping.getKey(), temp);
            add(temp);
        }
    }

    /**
     * Updates the postion of the buttons so that they are centered and have the
     * correct height and width.
     */
    public void updateGraphicElements() {
        returnToMainMenu.setBounds(10, 10, buttonWidth, buttonHeight);

        int actionCount = actions.size();
        int height = this.getHeight();
        int width = this.getWidth();
        int i = 0;

        ArrayList<String> listOfActions = new ArrayList<>(actions.keySet());
        listOfActions.sort(null);
        for (String key : listOfActions) {
            JLabel textField = actions.get(key);
            textField.setBounds(width / 2 - 135, (height - (actionCount - i * 2) * 30 + 5) / 2, 150, 25);
            JButton button = buttons.get(key);
            button.setBounds(width / 2 + 15, (height - (actionCount - i * 2) * 30 + 5) / 2, 120, 25);
            i++;
        }
        this.validate();
        this.setVisible(true);
    }

    /**
     * Starts the thread to keep updating the window.
     */
    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Executed upon starting the thread.
     * Starts the game loop.
     * Controls the maximum frame rate and calls @moveButtons to update their
     * position.
     */
    @Override
    public void run() {
        double frameDisplayTime = 1000000000 / frameRate;
        long lastTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        while (thread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / frameDisplayTime;
            lastTime = currentTime;
            if (delta >= 1) {
                checkForNewKey();
                updateGraphicElements();
                repaint();
                delta--;
            }
        }
    }

    private void checkForNewKey() {
        Character pressed = hotKeyInputHandler.getPressedChar();
        if (pressed != null && nameOfActionToChange != null) {
            if (iMN.containsValue(pressed) || iMNH.containsValue(pressed)
                    || iMH.containsValue(pressed)) {
                nameOfActionToChange = null;
                return;
            }
            if (iMN.containsKey(nameOfActionToChange)) {
                iMN.replace(nameOfActionToChange, pressed);
            } else if (iMNH.containsKey(nameOfActionToChange)) {
                iMNH.replace(nameOfActionToChange, pressed);
            } else {
                iMH.replace(nameOfActionToChange, pressed);
            }
            buttons.get(nameOfActionToChange).setText(charToHumanReadableString(pressed));
            nameOfActionToChange = null;
            LoadStoreHotKeys.storeHotKeys(iMN, iMNH, iMH);
        }
    }

    private void changeKeyOf(String action) {
        nameOfActionToChange = action;
    }

    private String charToHumanReadableString(char c) {
        // '\u001B' esc, '\u0020' space, '\u007F' delete
        switch (c) {
            case '\u001B':
                return "escape";
            case '\u0020':
                return "space";
            case '\u007F':
                return "delete";
            default:
                return String.valueOf((char) c);
        }
    }

    /**
     * to be done
     * 
     * @param g to be done
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.dispose();
    }
}