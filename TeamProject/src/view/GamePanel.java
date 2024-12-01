package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Board;
import model.User;
import model.FontManager;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
	private Board board;
	private Board board1;
    private Board board2;
    private Timer timer;
    
    private User currentUser;
    private User player1;
    private User player2;
    private String difficulty;
    
    private JFrame parentFrame; // 부모 프레임 참조
    
    private boolean isTimerRunning = false; // 타이머 상태 추적
    private boolean isMultiMode = false; // 멀티 모드 여부

    /**
     * Single 모드 생성자
     * 생성자 수정: 난이도 추가
     *
     * @param frame      부모 프레임
     * @param user       현재 사용자
     * @param difficulty 선택된 난이도
     * @param isMulti	 멀티 모드 여부
     */
    public GamePanel(JFrame frame, User user, String difficulty, boolean isMulti) {
        this.parentFrame = frame;
        this.currentUser = user;
        this.difficulty = difficulty;
        this.isMultiMode = isMulti;
        
        // 보드 초기화
        if (!isMultiMode) {
        	board = new Board(currentUser, difficulty);
        }
        
        initializePanel();
    }
    
    /**
     * Multi 모드 생성자
     *
     * @param frame      부모 프레임
     * @param player1    사용자 1
     * @param player2	 사용자 2
     * @param difficulty 선택된 난이도
     * @param isMulti	 멀티 모드 여부
     */
    public GamePanel(JFrame frame, User player1, User player2, String difficulty, boolean isMulti) {
        this.parentFrame = frame;
        this.player1 = player1;
        this.player2 = player2;
        this.difficulty = difficulty;
        this.isMultiMode = isMulti;

        // 보드 초기화
        if (isMultiMode) {
            board1 = new Board(player1, difficulty);
            board2 = new Board(player2, difficulty);
        }

        initializePanel();
    }
    
    /**
     * 패널 초기화
     */
    private void initializePanel() {        
        addKeyListener(this);
        setFocusable(true);

        int delay = 500; // Normal 난이도 기본 지연 시간

        switch (difficulty) {
            case "Easy":
                delay = 500; // Easy 난이도 기본 속도
                break;
            case "Hard":
                delay = 350; // Hard 난이도 빠른 속도
                break;
            default:
                delay = 500; // Normal 난이도
        }

        // 타이머 초기화 및 시작
        timer = new Timer(delay, this);
        timer.start();
        isTimerRunning = true;

        // 디버깅을 위한 로그 출력 (선택 사항)
        System.out.println("Selected Difficulty: " + difficulty + ", Timer Delay: " + delay + "ms");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 배경 이미지 로드
        ImageIcon backgroundImage;
        if (isMultiMode) {
            backgroundImage = new ImageIcon("src\\main\\resources\\multi_background.png");
        } else {
            backgroundImage = new ImageIcon("src\\main\\resources\\single_background.png");
        }
        g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        
        // 폰트 설정
        Font scoreFont = FontManager.getFont(20f); // score 폰트
        Font gameOverFont = FontManager.getFont(48f); // Game Over 폰트
        
        if (isMultiMode) {
        	// 두 보드를 나란히 그리기
        	Graphics2D g2 = (Graphics2D) g.create();
        	int mid = getWidth() / 2;
        	int width = board1.getCols() * board1.getBlockSize();
        	
        	// Player1 보드
        	g2.translate((mid - width) / 2, 0);
        	board1.draw(g2, width, getHeight());
        	g2.dispose();
            
            // Player2 보드
        	g2 = (Graphics2D) g.create();
        	g2.translate(mid + (mid - width) / 2, 0);
            board2.draw(g2, width, getHeight());
            g2.dispose();
            
            // 점수 표시
            g.setColor(Color.BLACK);
            g.setFont(scoreFont);
            g.drawString(player1.getUserId() + " Score: " + board1.getScore(), 10, 20);
            g.drawString(player2.getUserId() + " Score: " + board2.getScore(), getWidth() / 2 + 10, 20);
        
        } else {
        	// Single 모드
            if ("Easy".equalsIgnoreCase(difficulty)) {
                drawGrid(g); 
            }
            
            board.draw(g, getWidth(), getHeight());
            
            // 점수 표시
            g.setColor(Color.BLACK);
            g.setFont(scoreFont);
            String scoreText = "Score: " + board.getScore();
            g.drawString(scoreText, 10, 20);
        }
        
        // Game Over 표시
        if ((isMultiMode && (board1.isGameOver() || board2.isGameOver())) || (!isMultiMode && board.isGameOver())) {
            g.setColor(new Color(0, 0, 0, 150)); // 반투명한 검은색
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setFont(gameOverFont);
            FontMetrics fm = g.getFontMetrics();
            String gameOverText = "GAME OVER";
            
            int textWidth = 0;
            // 문자 간격을 포함한 전체 너비 계산
            for (char c : gameOverText.toCharArray()) {
                textWidth += fm.charWidth(c) + 12;
            }
            textWidth -= 12; // 마지막 자간 제외
            
            int startX = (getWidth() - textWidth) / 2; // 문자가 시작되는 x 좌표
            int y = getHeight() / 2;
            
            int x = startX;
            // 문자를 순차적으로 출력하며 자간 적용
            for (char c : gameOverText.toCharArray()) {
            	g.drawString(String.valueOf(c), x, y);
            	x += fm.charWidth(c) + 12;
            }
        }
    }

    // 격자를 그리는 메서드
    private void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY); // 격자 색상 설정
        g2.setStroke(new BasicStroke(1)); // 선 두께 설정

        int blockSize = board.getBlockSize();
        int cols = board.getCols();
        int rows = board.getRows();
        
        int playAreaWidth = cols * blockSize;
        int playAreaHeight = rows * blockSize;
        
        // 오프셋 계산
        int offsetX = (getWidth() - playAreaWidth) / 2; // 화면 중앙에 정렬
        int offsetY = (getHeight() - playAreaHeight) / 2; // 세로 정렬

        // 세로 선 그리기
        for (int x = 0; x <= cols; x++) {
            int px = offsetX + x * blockSize;
            g2.drawLine(px, offsetY, px, offsetY + rows * blockSize);
        }
        // 가로 선 그리기
        for (int y = 0; y <= rows; y++) {
            int py = y * blockSize;
            g2.drawLine(offsetX, py, offsetX + cols * blockSize, py);
        }
    }

    // 타이머 이벤트 처리 (블록이 내려감)
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (!isMultiMode) {
    		// Single 모드
    		if (!board.isGameOver()) {
                board.moveBlockDown(difficulty);
            }
    	} else {
    		// Multi 모드
    		if (!board1.isGameOver()) {
                board1.moveBlockDown(difficulty);
            }
    		if (!board2.isGameOver()) {
                board2.moveBlockDown(difficulty);
            }
    	}
    	
    	if ((isMultiMode && (board1.isGameOver() || board2.isGameOver())) || (!isMultiMode && board.isGameOver())) {
    		timer.stop();
            isTimerRunning = false; // 타이머 정지 상태로 설정
            repaint();

            // 게임 오버 창 띄우기
            if (isMultiMode) {
                new GameOverScreen(parentFrame, player1, player2, difficulty);
            } else {
                new GameOverScreen(parentFrame, currentUser, difficulty);
            }
    	}
        repaint();
    }

    // 키 상태를 추적하기 위한 배열
    private boolean[] keyStates = new boolean[256]; // 256개의 키 상태를 추적

    @Override
    public void keyPressed(KeyEvent e) {
        // 키 상태를 true로 설정 (키가 눌렸을 때)
        keyStates[e.getKeyCode()] = true;

        if (isMultiMode) {
            // Player1 (WASD 키)
            if (!board1.isGameOver()) {
                if (keyStates[KeyEvent.VK_A]) {
                    board1.moveBlockLeft();
                }
                if (keyStates[KeyEvent.VK_D]) {
                    board1.moveBlockRight();
                }
                if (keyStates[KeyEvent.VK_S]) {
                    board1.moveBlockDown(difficulty);
                }
                if (keyStates[KeyEvent.VK_W]) {
                    board1.rotateBlock();
                }
            }

            // Player2 (방향키)
            if (!board2.isGameOver()) {
                if (keyStates[KeyEvent.VK_LEFT]) {
                    board2.moveBlockLeft();
                }
                if (keyStates[KeyEvent.VK_RIGHT]) {
                    board2.moveBlockRight();
                }
                if (keyStates[KeyEvent.VK_DOWN]) {
                    board2.moveBlockDown(difficulty);
                }
                if (keyStates[KeyEvent.VK_UP]) {
                    board2.rotateBlock();
                }
            }
        } else {
            // Single 모드
            if (!board.isGameOver()) {
                if (keyStates[KeyEvent.VK_LEFT]) {
                    board.moveBlockLeft();
                }
                if (keyStates[KeyEvent.VK_RIGHT]) {
                    board.moveBlockRight();
                }
                if (keyStates[KeyEvent.VK_DOWN]) {
                    board.moveBlockDown(difficulty);
                }
                if (keyStates[KeyEvent.VK_UP]) {
                    board.rotateBlock();
                }
            }
        }
        repaint(); // 화면을 새로 고침
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 키가 떼어졌을 때 해당 키의 상태를 false로 설정
        keyStates[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    	// 인터페이스의 모든 메서드가 구현되어야 하므로 추가
    }
}
