package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import main.TetrisGame;
import model.User;
import model.ScoreManager;
import model.FontManager;

public class GameOverScreen extends JFrame {
    private JFrame parentFrame;
    private String currentDifficulty; // 현재 난이도 저장
    
    /**
     * Constructor Overloading
     * 
     * @param parentFrame
     * @param currentUser
     * @param difficulty
     */
    public GameOverScreen(JFrame parentFrame, User currentUser, String difficulty) {
        this(parentFrame, currentUser, null, null, difficulty, false);
    }

    public GameOverScreen(JFrame parentFrame, User player1, User player2, String difficulty) {
        this(parentFrame, null, player1, player2, difficulty, true);
    }

    public GameOverScreen(JFrame parentFrame, User currentUser, User player1, User player2, String difficulty, boolean isMultiMode) {
        this.parentFrame = parentFrame; // 부모 프레임 저장
        this.currentDifficulty = difficulty;
        
        TetrisGame.getMusicPlayer().stopMusic(); // 게임 음악 종료
        TetrisGame.getMusicPlayer().playMusic("src\\main\\resources\\bgm_main.wav"); // 메인 음악 재생

        setTitle("Game Over");
        setSize(800, 490);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 동작 설정
        setLocationRelativeTo(null); // 화면 중앙에 창 배치

        // 메인 패널 생성
        JPanel mainPanel = new JPanel() {
        	@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 배경 이미지 로드
                ImageIcon backgroundImage = new ImageIcon("src\\main\\resources\\gameover.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false); // 배경 투명하게
        add(mainPanel);
        
        // 점수 나타내는 라벨
        if (isMultiMode) {
        	// Multi 모드
        	JLabel player1ScoreLabel = new JLabel(player1.getUserId() + " Score: " + player1.getScore(), SwingConstants.CENTER);
            player1ScoreLabel.setFont(FontManager.getFont(24f));
            player1ScoreLabel.setForeground(Color.BLACK);
            player1ScoreLabel.setBounds(80, 180, 300, 30);
            mainPanel.add(player1ScoreLabel);

            JLabel player2ScoreLabel = new JLabel(player2.getUserId() + " Score: " + player2.getScore(), SwingConstants.CENTER);
            player2ScoreLabel.setFont(FontManager.getFont(24f));
            player2ScoreLabel.setForeground(Color.BLACK);
            player2ScoreLabel.setBounds(80, 210, 300, 30);
            mainPanel.add(player2ScoreLabel);
            
            // 승리 메시지
            String winnerMessage;
            if (player1.getScore() > player2.getScore()) {
            	winnerMessage = player1.getUserId() + " Wins!";
            } else if (player1.getScore() < player2.getScore()) {
            	winnerMessage = player2.getUserId() + " Wins!";
            } else {
            	winnerMessage = "Draw!";
            }
            
            JLabel winnerLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
            winnerLabel.setFont(FontManager.getFont(28f));
            winnerLabel.setForeground(Color.RED);
            winnerLabel.setBounds(80, 260, 300, 30);
            mainPanel.add(winnerLabel);
        
        } else {
        	// Single 모드
        	JLabel scoreLabel = new JLabel("Your Score: " + currentUser.getScore(), SwingConstants.CENTER);
            scoreLabel.setFont(FontManager.getFont(24f));
            scoreLabel.setForeground(Color.BLACK); // 글씨 색상을 흰색으로 설정
            scoreLabel.setBounds(80, 85, 300, 30);
            mainPanel.add(scoreLabel);

            // 'Top 3 Players' 라벨
            JLabel rankLabel = new JLabel("Top 3 Players (" + currentDifficulty + ")", SwingConstants.LEFT);
            rankLabel.setFont(FontManager.getFont(20f));
            rankLabel.setForeground(Color.BLACK); // 글씨 색상을 흰색으로 설정
            rankLabel.setBounds(80, 170, 300, 30);
            mainPanel.add(rankLabel);

            // 난이도에 따른 상위 3명 가져오기
            List<User> topUsers = ScoreManager.getTopUsers(currentDifficulty);
            int yOffset = 210; // 초기 y 위치
            
            for (int i = 0; i < topUsers.size(); i++) {
                User user = topUsers.get(i);
                JLabel userLabel = new JLabel((i + 1) + ". " + user.getUserId() + " - " + user.getScore(), SwingConstants.LEFT);
                userLabel.setFont(FontManager.getFont(18f));
                userLabel.setForeground(Color.BLACK); // 글씨 색상을 흰색으로 설정
                userLabel.setBounds(80, yOffset, 300, 25);
                mainPanel.add(userLabel);
                yOffset += 30; // 다음 y 위치 변경
            }
        }

        // Retry 버튼
        JButton retryButton = new JButton(new ImageIcon("src\\main\\resources\\retry.png"));
        retryButton.setContentAreaFilled(false); // 배경 제거
        retryButton.setBorderPainted(false); // 테두리 제거
        retryButton.setFocusPainted(false); // 포커스 테두리 제거
        retryButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\retry_hover.png")); // 마우스 호버
        retryButton.setBounds(510, 105, 200, 65);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 게임 오버 창 닫기
                if (parentFrame != null) {
                    parentFrame.dispose(); // 게임 프레임 닫기
                }
                // 게임 다시 시작
                if (isMultiMode) {
                	player1.setScore(0); // 점수 초기화
                	player2.setScore(0);
                	TetrisGame.startGame(null, player1, player2, currentDifficulty, true);
                } else {
                	currentUser.setScore(0); // 점수 초기화
                    TetrisGame.startGame(currentUser, null, null, currentDifficulty, false); // 현재 난이도로 게임 시작
                }
            }
        });
        
        // 새로운 유저로 시작하기 버튼
        JButton newUserButton = new JButton(new ImageIcon("src\\main\\resources\\newuser.png"));
        newUserButton.setContentAreaFilled(false); // 배경 제거
        newUserButton.setBorderPainted(false); // 테두리 제거
        newUserButton.setFocusPainted(false); // 포커스 테두리 제거
        newUserButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\newuser_hover.png")); // 마우스 호버
        newUserButton.setBounds(510, 170, 200, 65);
        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 게임 오버 창 닫기
                if (parentFrame != null) {
                    parentFrame.dispose(); // 게임 프레임 닫기
                }
                // 새로운 유저로 게임 다시 시작
                if (isMultiMode) {
                	new UserInputScreen(true);
                } else {
                	new UserInputScreen(false); // 새로운 사용자로 난이도 선택 화면 다시 시작
                }
            }
        });
                
        // Exit 버튼
        JButton exitButton = new JButton(new ImageIcon("src\\main\\resources\\exit.png"));
        exitButton.setContentAreaFilled(false); // 배경 제거
        exitButton.setBorderPainted(false); // 테두리 제거
        exitButton.setFocusPainted(false); // 포커스 테두리 제거
        exitButton.setRolloverIcon(new ImageIcon("src\\main\\resources\\exit_hover.png")); // 마우스 호버
        exitButton.setBounds(510, 270, 200, 65);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 게임 오버 창 닫기
                System.exit(0); // 프로그램 종료
            }
        });

        // 버튼 배치
        mainPanel.add(retryButton);
        mainPanel.add(newUserButton);
        mainPanel.add(exitButton);
        
        setVisible(true); // 창 보이기
    }
}