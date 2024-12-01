package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.TetrisGame;
import model.User;

public class StartScreen extends JFrame {
    public StartScreen() {
        setTitle("TETRIS GAME");
        setSize(800, 490);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 화면
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 배경 이미지 로드
                ImageIcon backgroundImage = new ImageIcon("src\\main\\resources\\mainpage.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false); // 배경 패널 투명 설정
        add(panel);

        // Single 모드 버튼
        JButton singleButton = new JButton(new ImageIcon("src\\main\\resources\\single.png"));
        singleButton.setContentAreaFilled(false); // 배경 제거
        singleButton.setBorderPainted(false); // 테두리 제거
        singleButton.setFocusPainted(false); // 포커스 테두리 제거
        singleButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\single_hover.png")); // 마우스 호버
        singleButton.setBounds(300, 180, 200, 65);
        singleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // StartScreen 닫기
                new UserInputScreen(false); // DifficultySelectionScreen 열기
            }
        });
        panel.add(singleButton);
        
        // Multi 모드 버튼
        JButton multiButton = new JButton(new ImageIcon("src\\main\\resources\\multi.png"));
        multiButton.setContentAreaFilled(false); // 배경 제거
        multiButton.setBorderPainted(false); // 테두리 제거
        multiButton.setFocusPainted(false); // 포커스 테두리 제거
        multiButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\multi_hover.png")); // 마우스 호버
        multiButton.setBounds(300, 255, 200, 65);
        multiButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		dispose(); // StartScreen 닫기
        		new UserInputScreen(true); // DifficultySelectionScreen 열기
        	}
        });
        panel.add(multiButton);
        
        setVisible(true);
    }
}