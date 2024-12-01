package model;

import javax.swing.JFrame;

public class User {
    private String userId;
    private int score;
    private JFrame gameFrame; // 게임 프레임 참조 추가

    /**
     * 사용자 생성자
     *
     * @param userId 사용자의 ID
     */
    public User(String userId) {
        this.userId = userId;
        this.score = 0;
    }

    /**
     * 사용자 생성자 (점수 초기화 포함)
     *
     * @param userId 사용자의 ID
     * @param score 초기 점수
     */
    public User(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(JFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    /**
     * 사용자 정보를 파일에 저장할 문자열 형식으로 변환합니다.
     * 형식: userId,score
     *
     * @return 직렬화된 사용자 정보 문자열
     */
    @Override
    public String toString() {
        return userId + "," + score;
    }

    /**
     * 문자열에서 사용자 정보를 파싱하여 User 객체를 생성합니다.
     * 문자열 형식: userId,score
     *
     * @param line 사용자 정보가 저장된 문자열
     * @return 파싱된 User 객체
     * @throws IllegalArgumentException 형식이 잘못된 경우 예외 발생
     */
    public static User fromString(String line) throws IllegalArgumentException {
        String[] parts = line.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid user data: " + line);
        }
        String userId = parts[0];
        int score;
        try {
            score = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid score for user " + userId + ": " + parts[1]);
        }
        return new User(userId, score);
    }

    /**
     * 사용자 객체의 비교를 위해 equals 및 hashCode 메서드를 오버라이드합니다.
     * userId를 기준으로 동일성을 판단합니다.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}