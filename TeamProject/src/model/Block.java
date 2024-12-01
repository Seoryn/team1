package model;

import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Block {
    // 기존 코드 유지
    public enum Type {
        I, O, T, S, Z, J, L
    }

    private Type type;
    private int[][][] shapes;
    private int rotationState;
    private Color color;

    // 추가: 블록의 위치 (보드 상에서의 x, y 좌표)
    private Point position;

    // 생성자
    public Block(Type type) {
        this.type = type;
        this.shapes = getShapesByType(type);
        this.rotationState = 0;
        this.color = getColorByType(type);
        this.position = new Point(3, 0); // 기본 시작 위치 (보드 중앙 상단)
    }

    // 블록의 현재 형태 반환
    public int[][] getShape() {
        return shapes[rotationState];
    }

    // 블록을 오른쪽으로 90도 회전
    public void rotate() {
        rotationState = (rotationState + 1) % shapes.length;
    }

    // 블록의 색상 반환
    public Color getColor() {
        return color;
    }

    // 블록의 위치 반환
    public Point getPosition() {
        return position;
    }

    // 블록의 위치 설정
    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    // 블록의 위치 이동
    public void move(int dx, int dy) {
        position.translate(dx, dy);
    }

    // 그래픽 컨텍스트에 블록을 그림
    public void draw(Graphics g, int blockSize, int offsetX, int offsetY) {
        int[][] shape = getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    int x = offsetX + (position.x + j) * blockSize;
                    int y = offsetY + (position.y + i) * blockSize;
                    
                    g.setColor(color);
                    g.fillRect(x, y, blockSize, blockSize);

                    // 하이라이트 (위, 왼쪽)
                    g.setColor(color.brighter());
                    g.fillRect(x, y, blockSize, blockSize / 4); // 위 하이라이트
                    g.fillRect(x, y, blockSize / 4, blockSize); // 왼쪽 하이라이트

                    // 그림자 (오른쪽, 아래)
                    g.setColor(color.darker());
                    g.fillRect(x + blockSize - blockSize / 4, y, blockSize / 4, blockSize); // 오른쪽 그림자
                    g.fillRect(x, y + blockSize - blockSize / 4, blockSize, blockSize / 4); // 아래 그림자

                    // 테두리 없는 게 더 나아보여서 우선 제외
                    // g.setColor(Color.BLACK); // 블록 테두리
                    // g.drawRect(x, y, blockSize, blockSize);
                }
            }
        }
    }

    // 블록 타입에 따른 색상 지정
    private Color getColorByType(Type type) {
        switch (type) {
            case I: return new Color(0x82cbc4);
            case O: return new Color(0xffc700);
            case T: return new Color(0xa8c439);
            case S: return new Color(0x48b2ff);
            case Z: return new Color(0xff5151);
            case J: return new Color(0x3358b7);
            case L: return new Color(0xd78a43);
            default: return Color.GRAY;
        }
    }

 // 블록 타입에 따른 회전 형태 정의
    private int[][][] getShapesByType(Type type) {
        switch (type) {
            case I:
                return new int[][][] {
                    {
                        {1, 1, 1, 1}
                    },
                    {
                        {1},
                        {1},
                        {1},
                        {1}
                    }
                };
            case O:
                return new int[][][] {
                    {
                        {1, 1},
                        {1, 1}
                    }
                };
            case T:
                return new int[][][] {
                    {
                        {0, 1, 0},
                        {1, 1, 1}
                    },
                    {
                        {1, 0},
                        {1, 1},
                        {1, 0}
                    },
                    {
                        {1, 1, 1},
                        {0, 1, 0}
                    },
                    {
                        {0, 1},
                        {1, 1},
                        {0, 1}
                    }
                };
            case S:
                return new int[][][] {
                    {
                        {0, 1, 1},
                        {1, 1, 0}
                    },
                    {
                        {1, 0},
                        {1, 1},
                        {0, 1}
                    }
                };
            case Z:
                return new int[][][] {
                    {
                        {1, 1, 0},
                        {0, 1, 1}
                    },
                    {
                        {0, 1},
                        {1, 1},
                        {1, 0}
                    }
                };
            case J:
                return new int[][][] {
                    {
                        {1, 0, 0},
                        {1, 1, 1}
                    },
                    {
                        {1, 1},
                        {1, 0},
                        {1, 0}
                    },
                    {
                        {1, 1, 1},
                        {0, 0, 1}
                    },
                    {
                        {0, 1},
                        {0, 1},
                        {1, 1}
                    }
                };
            case L:
                return new int[][][] {
                    {
                        {0, 0, 1},
                        {1, 1, 1}
                    },
                    {
                        {1, 0},
                        {1, 0},
                        {1, 1}
                    },
                    {
                        {1, 1, 1},
                        {1, 0, 0}
                    },
                    {
                        {1, 1},
                        {0, 1},
                        {0, 1}
                    }
                };
            default:
                return new int[][][] {{{0}}};
        }
    }

    // 무작위로 블록 생성하는 정적 메소드 (필요한 경우)
    public static Block getRandomBlock() {
        Random random = new Random();
        Type[] types = Type.values();
        Type randomType = types[random.nextInt(types.length)];
        return new Block(randomType);
    }
}