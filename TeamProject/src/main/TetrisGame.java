package main;

import javax.swing.*;
import view.GamePanel;
import view.StartScreen;
import view.UserInputScreen;
import model.User;
import model.MusicPlayer;

public class TetrisGame {
    private static User currentUser;
    private static MusicPlayer musicPlayer;
    private static boolean isMultiMode; // Single or Multi 모드 여부

    public static void main(String[] args) {
    	musicPlayer = new MusicPlayer(); // MusicPlayer 초기화
    	musicPlayer.playMusic("src\\main\\resources\\bgm_main.wav"); // 메인 음악 재생
    	
        // StartScreen 열기
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartScreen();
            }
        });
    }
    
    /**
     * 게임을 시작하는 메서드
     *
     * @param user       현재 사용자
     * @param player1	 멀티 모드 사용자 1
     * @param player2	 멀티 모드 사용자 2
     * @param difficulty 선택된 난이도 ("Easy", "Normal", "Hard")
     * @param isMulti	 멀티 모드 여부
     */
    public static void startGame(User user, User player1, User player2, String difficulty, boolean isMulti) {
    	isMultiMode = isMulti; // 모드 설정
    	
        musicPlayer.stopMusic(); // 메인 음악 종료
        musicPlayer.playMusic("src\\main\\resources\\bgm_game.wav"); // 게임 음악 재생
    	
        JFrame gameFrame = new JFrame("Tetris Game - " + (isMulti ? "Multi Mode" : difficulty));
        GamePanel gamePanel;
        
        if (isMultiMode) {
        	// Multi 모드
        	gamePanel = new GamePanel(gameFrame, player1, player2, difficulty, true); // 멀티 모드 전달
        } else {
        	// Single 모드
        	gamePanel = new GamePanel(gameFrame, user, difficulty, false);
        }
        
        gameFrame.add(gamePanel);
        gameFrame.setSize(isMulti ? 800 : 400, 640); // cols * blockSize, rows * blockSize
        // gameFrame.setSize(320, 640); // 좌우 여백 포함 버전 
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // gameFrame.setResizable(false); // 리사이징 방지
        gameFrame.setLocationRelativeTo(null); // 화면 중앙에 창 배치
        gameFrame.setVisible(true);
        
        // 현재 사용자에 게임 프레임 설정
        if (isMultiMode) {
            player1.setGameFrame(gameFrame);
            player2.setGameFrame(gameFrame);
        } else {
            user.setGameFrame(gameFrame);
        }
        
        // 게임 종료 시 음악 멈추기
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                musicPlayer.stopMusic();
                super.windowClosing(e);
            }
        });
    }
    
    /**
     * 현재 사용자를 리셋하는 메서드
     */
    public static void resetCurrentUser() {
        currentUser = null;
    }

    /**
     * 현재 사용자를 반환하는 메서드
     *
     * @return 현재 사용자
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Single or Multi 모드 여부 반환하는 메서드
     * 
     * @return 멀티 모드 여부
     */
    public static boolean isMultiMode() {
    	return isMultiMode;
    }
    
    /**
     * MusicPlayer 객체 반환하는 메서드
     * 
     * @return 현재 MusicPlayer
     */
    public static MusicPlayer getMusicPlayer() {
    	return musicPlayer;
    }
}