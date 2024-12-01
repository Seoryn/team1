package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreManager {
    private static final String DIRECTORY = "rankings"; // 랭킹 파일을 저장할 디렉토리
    private static Map<String, List<User>> topUsersByDifficulty = new HashMap<>();

    static {
        // 디렉토리 존재 확인 및 생성
        File dir = new File(DIRECTORY); //"rankings" 폴더  
        // 존재 안하면 폴더 생성 
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 난이도별로 초기화 및 파일에서 기존 데이터 로드
        topUsersByDifficulty.put("Easy", loadTopUsersFromFile("Easy"));
        topUsersByDifficulty.put("Normal", loadTopUsersFromFile("Normal"));
        topUsersByDifficulty.put("Hard", loadTopUsersFromFile("Hard"));
    }

    /**
     * 사용자가 제거한 줄 수에 따라 점수를 추가하고, 해당 난이도의 상위 3명의 사용자를 업데이트합니다.
     *
     * @param user         점수를 추가할 사용자
     * @param linesCleared 제거한 줄 수 (1, 2, 3, 4)
     * @param difficulty   게임의 난이도 ("Easy", "Normal", "Hard")
     */
    public static void addScore(User user, int linesCleared, String difficulty) {
        int points;
        if (linesCleared == 1) {
            points = calculateScore();
        } else {
            points = calculateScore(linesCleared);
        }

        // 계산된 점수를 사용자 점수에 추가
        if (points > 0) {
            user.setScore(user.getScore() + points);
        }

        // 해당 난이도의 topUsers 리스트 가져오기
        List<User> topUsers = topUsersByDifficulty.get(difficulty);
        if (topUsers == null) {
            // 예외 처리: 유효하지 않은 난이도일 경우
            topUsers = new ArrayList<>();
            topUsersByDifficulty.put(difficulty, topUsers);
        }

        // topUsers 리스트에서 해당 사용자가 이미 존재하는지 확인
        boolean updated = false;
        for (User u : topUsers) {
            if (u.getUserId().equals(user.getUserId())) {
                // 현재 점수가 기존 점수보다 높을 경우 업데이트
                if (user.getScore() > u.getScore()) {
                    u.setScore(user.getScore());
                }
                updated = true;
                break;
            }
        }

        // 사용자가 topUsers에 없으면 추가
        if (!updated) {
            topUsers.add(user); // 동일한 User 객체를 참조
        }

        // 점수 내림차순으로 정렬
        topUsers.sort(Comparator.comparingInt(User::getScore).reversed());

        // 상위 3명만 유지
        if (topUsers.size() > 3) {
            topUsers = new ArrayList<>(topUsers.subList(0, 3));
            topUsersByDifficulty.put(difficulty, topUsers);
        }

        // 파일에 업데이트된 랭킹 저장
        saveTopUsersToFile(difficulty, topUsers);
    }

    public static int calculateScore(int linesCleared) {
        int points = 0;
        switch (linesCleared) {
            // case 1: // 싱글
            //     points = 100;
            //     break;
            case 2: // 더블
                points = 300;
                break;
            case 3: // 트리플
                points = 500;
                break;
            case 4: // 테트리스
                points = 800;
                break;
            default: // 1~4줄 이외의 경우 점수 추가하지 않음
                break;
        }

        return points;
    }

    public static int calculateScore() {
        int points = 100;

        return points;
    }

    /**
     * 특정 난이도의 상위 3명의 사용자 목록을 반환합니다.
     *
     * @param difficulty 게임의 난이도 ("Easy", "Normal", "Hard")
     * @return 해당 난이도의 상위 3명의 사용자 리스트
     */
    public static List<User> getTopUsers(String difficulty) {
        List<User> topUsers = topUsersByDifficulty.get(difficulty);
        if (topUsers == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(topUsers); // 외부에서 리스트 수정 방지
    }

    /**
     * 파일에서 특정 난이도의 상위 사용자 목록을 로드합니다.
     *
     * @param difficulty 게임의 난이도
     * @return 상위 사용자 리스트
     */
    private static List<User> loadTopUsersFromFile(String difficulty) {
        List<User> users = new ArrayList<>();
        File file = new File(DIRECTORY, difficulty + ".txt");

        if (!file.exists()) {
            // 파일이 없으면 빈 리스트 반환
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null && users.size() < 3) {
                try {
                    User user = User.fromString(line);
                    users.add(user);
                } catch (IllegalArgumentException e) {
                    // 잘못된 형식의 라인은 무시
                    System.err.println("Invalid line in " + file.getName() + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
        }

        return users;
    }

    /**
     * 특정 난이도의 상위 사용자 목록을 파일에 저장합니다.
     *
     * @param difficulty 게임의 난이도
     * @param users      상위 사용자 리스트
     */
    private static void saveTopUsersToFile(String difficulty, List<User> users) {
        File file = new File(DIRECTORY, difficulty + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // overwrite mode
            for (User user : users) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + file.getName() + ": " + e.getMessage());
        }
    }
}