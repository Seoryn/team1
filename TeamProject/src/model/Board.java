package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Board {
    private int[][] board; // 보드의 상태를 저장하는 2D 배열
    private final int rows = 20;
    private final int cols = 10;
    
    private final int blockSize = 30; // 각 블록의 픽셀 크기

    private User currentUser;
    private Block currentBlock;
    private String difficulty;
    
    private boolean gameOver = false;

    public Board(User user, String difficulty) {
        this.currentUser = user;
        this.difficulty = difficulty;
        board = new int[rows][cols];
        spawnBlock();
    }
    
    // 게임 오버 시 호출되는 메서드
    public void setGameOver() {
        gameOver = true;
        // ScoreManager.addScore(user) 호출 필요 없
        // 점수는 이미 ScoreManager.addScore(user, linesCleared)로 점수 업데이트 
    }

    // 새로운 블록을 생성하고 배치
    public void spawnBlock() {
        currentBlock = Block.getRandomBlock();
        currentBlock.setPosition(cols / 2 - 2, 0);

        if (checkCollision(currentBlock, 0, 0)) {
            setGameOver();
        }
    }

    // 블록을 아래로 한 칸 이동
    public void moveBlockDown(String difficulty) {
        if (gameOver) {
            return;
        }
        if (!checkCollision(currentBlock, 0, 1)) {
            currentBlock.move(0, 1);
        } else {
            mergeBlockToBoard();
            int linesCleared = clearLines();
            
            // linesCleared가 1, 2, 3, 4인 경우에만 점수를 추가
            if (linesCleared >= 0 && linesCleared <= 4) {
                ScoreManager.addScore(currentUser, linesCleared, difficulty);
            }
            spawnBlock();
        }    
    }
    
    // 블록을 왼쪽으로 이동
    public void moveBlockLeft() {
        if (!checkCollision(currentBlock, -1, 0)) {
            currentBlock.move(-1, 0);
        }
    }

    // 블록을 오른쪽으로 이동
    public void moveBlockRight() {
        if (!checkCollision(currentBlock, 1, 0)) {
            currentBlock.move(1, 0);
        }
    }

    // 블록을 회전
    public void rotateBlock() {
        currentBlock.rotate();
        if (checkCollision(currentBlock, 0, 0)) {
            // 회전으로 인해 충돌이 발생하면 회전을 원상복구
            currentBlock.rotate(); // 반시계 방향으로 한 번 더 회전하여 원상복귀
            currentBlock.rotate();
            currentBlock.rotate();
        }
    }

    // 충돌 검사
    private boolean checkCollision(Block block, int dx, int dy) {
        int[][] shape = block.getShape();
        int xPos = block.getPosition().x + dx;
        int yPos = block.getPosition().y + dy;

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    int newX = xPos + x;
                    int newY = yPos + y;

                    if (newX < 0 || newX >= cols || newY < 0 || newY >= rows) {
                        return true; // 벽에 충돌
                    }
                    if (board[newY][newX] != 0) {
                        return true; // 다른 블록에 충돌
                    }
                }
            }
        }
        return false;
    }

    // 블록을 보드에 병합
    private void mergeBlockToBoard() {
        int[][] shape = currentBlock.getShape();
        int xPos = currentBlock.getPosition().x;
        int yPos = currentBlock.getPosition().y;

        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if (shape[y][x] != 0) {
                    board[yPos + y][xPos + x] = 1; // 블록이 있는 위치를 1로 설정
                }
            }
        }
    }

    // 완성된 라인 제거 및 점수 계산
    // 1줄, 2줄, 3줄, 4줄 제거 시 점수를 추가하고, 그 외의 경우는 점수를 추가하지 않음
    private int clearLines() {
        int linesCleared = 0;
        for (int y = rows - 1; y >= 0; y--) {
            boolean isFull = true;
            for (int x = 0; x < cols; x++) {
                if (board[y][x] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                removeLine(y);
                y++; // 제거된 줄 위의 줄들을 한 칸씩 내림
                linesCleared++;
            }
        }
        return linesCleared;
    }


    // 한 줄 제거하고 위의 블록들을 한 줄씩 내림
    private void removeLine(int line) {
        for (int y = line; y > 0; y--) {
            System.arraycopy(board[y - 1], 0, board[y], 0, cols);
        }
        board[0] = new int[cols]; // 맨 윗줄은 빈 줄로 설정
    }

    // 보드와 현재 블록을 그리기
    public void draw(Graphics g, int panelWidth, int panelHeight) {
    	Graphics2D g2 = (Graphics2D) g;
    	
    	int playAreaWidth = cols * blockSize;
    	int playAreaHeight = rows * blockSize;
    	
    	// 오프셋 계산
    	int offsetX = (panelWidth - playAreaWidth) / 2; // 화면 중앙에 배치
    	int offsetY = (panelHeight - playAreaHeight) / 2; // 상단 여백
    	
    	// 플레이 영역에 배경 추가
    	g2.setColor(new Color(255, 255, 255, 70)); // 옅은 흰색
        g2.fillRect(offsetX, offsetY, playAreaWidth, playAreaHeight);
    	
        // 보드에 쌓인 블록 그리기
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (board[y][x] != 0) {
                    Color blockColor = Color.GRAY;
                    g.setColor(blockColor);
                    
                    int px = offsetX + x * blockSize;
                    int py = offsetY + y * blockSize;
                    g.fillRect(px, py, blockSize, blockSize);
                    
                    // 하이라이트 (위, 왼쪽)
                    g.setColor(blockColor.brighter());
                    g.fillRect(px, py, blockSize, blockSize / 4); // 위 하이라이트
                    g.fillRect(px, py, blockSize / 4, blockSize); // 왼쪽 하이라이트

                    // 그림자 (오른쪽, 아래)
                    g.setColor(blockColor.darker());
                    g.fillRect(px + blockSize - blockSize / 4, py, blockSize / 4, blockSize); // 오른쪽 그림자
                    g.fillRect(px, py + blockSize - blockSize / 4, blockSize, blockSize / 4); // 아래 그림자
                }
            }
        }

        // 현재 블록 그리기
        if (currentBlock != null) {
            currentBlock.draw(g2, blockSize, offsetX, offsetY);
        }
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getScore() {
        return currentUser.getScore();
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    // 접근자 메서드 추가
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getBlockSize() {
        return blockSize;
    }
}