package view;
import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

    private JPanel mainPanel;

    private JButton gameStartNormalModeButton;
    private JButton exitButton;
    private JButton gameStartVersusModeButton;

    private JButton[] buttons = new JButton[]{
            gameStartNormalModeButton, gameStartVersusModeButton, exitButton
    };

    private int buttonSelectorIndex = 0;

    // 생성자
    public MainPage() {
        // 초기화
        initialize();
    }

    private void initialize() {
        this.setTitle("Main");
        this.setSize(500, 500);

        // 'mainPanel' 초기화: 레이아웃과 버튼들 추가
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1)); // 세로로 버튼을 나열하기 위해 GridLayout 사용

        // 각 버튼 초기화 및 추가
        gameStartNormalModeButton = new JButton("Game Start - Normal Mode");
        gameStartVersusModeButton = new JButton("Game Start - Versus - NormalMode");
        exitButton = new JButton("Exit");

        // 버튼들을 JPanel에 추가
        mainPanel.add(gameStartNormalModeButton);
        mainPanel.add(gameStartVersusModeButton);
        mainPanel.add(exitButton);

        // JFrame에 JPanel을 추가
        this.add(mainPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setLocationRelativeTo(null); // 화면 중앙에 위치

        // 포커스를 이 화면에 맞춰서 키 이벤트 받게 만듦
        requestFocus();
        setFocusable(true);
    }
}
