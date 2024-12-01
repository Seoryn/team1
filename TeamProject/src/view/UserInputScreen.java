package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.TetrisGame;
import model.User;
import model.FontManager;

public class UserInputScreen extends JFrame {
    private JTextField userIdField;
    private JTextField player1IdField;
    private JTextField player2IdField;
    private JRadioButton easyButton;
    private JRadioButton normalButton;
    private JRadioButton hardButton;
    private ButtonGroup difficultyGroup;
    private boolean isMultiMode;
    
    public UserInputScreen(boolean isMultiMode) {
    	this.isMultiMode = isMultiMode;
        setTitle(isMultiMode ? "Enter Player ID" : "Select Difficulty and Enter User ID");
        setSize(800, 490);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 배치

        // 메인 패널 설정
        JPanel mainPanel = new JPanel() {
        	@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 배경 이미지 로드
                ImageIcon backgroundImage = new ImageIcon("src\\main\\resources\\background.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null); // 절대 위치로 레이아웃 설정
        mainPanel.setOpaque(false); // 배경 패널 투명 설정
        add(mainPanel);
        
        // 폰트 설정
        Font labelFont = FontManager.getFont(16f); // label 폰트
        Font textFieldFont = FontManager.getFont(14f); // 입력 필드 폰트
        
        if (isMultiMode) {
        	// Player1 ID 입력
        	JLabel player1IdLabel = new JLabel("Enter Player 1 ID:");
        	player1IdLabel.setFont(labelFont);
        	player1IdLabel.setBounds(200, 50, 300, 25);
            mainPanel.add(player1IdLabel);

            player1IdField = new JTextField();
            player1IdField.setFont(textFieldFont);
            player1IdField.setBounds(200, 80, 400, 35);
            mainPanel.add(player1IdField);
            
        	// Player2 ID 입력
            JLabel player2IdLabel = new JLabel("Enter Player 2 ID:");
            player2IdLabel.setFont(labelFont);
            player2IdLabel.setBounds(200, 130, 300, 25);
            mainPanel.add(player2IdLabel);

            player2IdField = new JTextField();
            player2IdField.setFont(textFieldFont);
            player2IdField.setBounds(200, 160, 400, 35);
            mainPanel.add(player2IdField);
            
        } else {
        	// 사용자 ID 입력
            JLabel userIdLabel = new JLabel("Enter User ID:");
            userIdLabel.setFont(labelFont);
            userIdLabel.setBounds(200, 50, 300, 25);
            mainPanel.add(userIdLabel);

            userIdField = new JTextField();
            userIdField.setFont(textFieldFont);
            userIdField.setBounds(200, 80, 400, 35);
            mainPanel.add(userIdField);
            
            // 난이도 선택
            JLabel difficultyLabel = new JLabel("Select Difficulty:");
            difficultyLabel.setFont(labelFont);
            difficultyLabel.setBounds(200, 130, 300, 50);
            mainPanel.add(difficultyLabel);

            JPanel difficultyPanel = new JPanel();
            difficultyPanel.setLayout(new FlowLayout());
            difficultyPanel.setOpaque(false); // 난이도 패널 투명 설정
            difficultyPanel.setBounds(200, 170, 400, 40);

            easyButton = new JRadioButton("Easy");
            easyButton.setFont(labelFont);
            easyButton.setOpaque(false); // 버튼 배경 투명 설정
            normalButton = new JRadioButton("Normal", true); // 기본 선택
            normalButton.setFont(labelFont);
            normalButton.setOpaque(false); // 버튼 배경 투명 설정
            hardButton = new JRadioButton("Hard");
            hardButton.setFont(labelFont);
            hardButton.setOpaque(false); // 버튼 배경 투명 설정
            
            difficultyGroup = new ButtonGroup();
            difficultyGroup.add(easyButton);
            difficultyGroup.add(normalButton);
            difficultyGroup.add(hardButton);

            difficultyPanel.add(easyButton);
            difficultyPanel.add(normalButton);
            difficultyPanel.add(hardButton);
            mainPanel.add(difficultyPanel);
        }

        // 시작 버튼
        JButton startButton = new JButton(new ImageIcon("src\\main\\resources\\start.png"));
        startButton.setContentAreaFilled(false); // 배경 제거
        startButton.setBorderPainted(false); // 테두리 제거
        startButton.setFocusPainted(false); // 포커스 테두리 제거
        startButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\start_hover.png")); // 마우스 호버 이미지
        startButton.setBounds(300, 250, 200, 65);
        mainPanel.add(startButton);

        // 시작 버튼 이벤트 핸들러
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = null;
                String player1Id = null;
                String player2Id = null;
                String difficulty = "Normal"; // 기본값으로 Normal 난이도 설정
                
                if (isMultiMode) {
                	player1Id = player1IdField.getText().trim();
                	player2Id = player2IdField.getText().trim();
                	
                	if (player1Id.isEmpty() || player2Id.isEmpty()) {
                		JOptionPane.showMessageDialog(UserInputScreen.this, "Please enter both Player IDs.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                	}
                } else {
                	userId = userIdField.getText().trim();
                	
                	if (userId.isEmpty()) {
                        JOptionPane.showMessageDialog(UserInputScreen.this, "Please enter a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                	
                	// 난이도 설정
                    if (easyButton.isSelected()) {
                        difficulty = "Easy";
                    } else if (hardButton.isSelected()) {
                        difficulty = "Hard";
                    }
                }
                
                // 사용자 객체 생성
                if (isMultiMode) {
                    User player1 = new User(player1Id);
                    User player2 = new User(player2Id);
                    dispose(); // 창 닫기
                    TetrisGame.startGame(null, player1, player2, difficulty, true); // 난이도는 항상 "Normal"
                } else {
                    User user = new User(userId);
                    dispose(); // 창 닫기
                    TetrisGame.startGame(user, null, null, difficulty, false);
                }
            }
        });

        setVisible(true);
    }
}