
public class Chessboard {

    int arr[][] = new int[5][5];
    int userChessmanCount = 0;
    int machineChessmanCount = 0;
    int turn = 1;

    public Chessboard(int arr[][], int turn) {//dau vao la mang bieu dien ban co va luot choi, mang phai co kich thuoc 5 x 5, va phai co du lieu chuan
        this.turn = turn;
        for (int i = 0; i < 5; i++) {
            System.arraycopy(arr[i], 0, this.arr[i], 0, 5);
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (arr[i][j] == 1) {
                    userChessmanCount++;
                } else if (arr[i][j] == 2) {
                    machineChessmanCount++;
                }
            }
        }
    }

    public Chessboard() {
        int i;
        for (i = 0; i < 5; i++) {
            arr[0][i] = 2;
            arr[4][i] = 1;
        }
        arr[1][0] = 2;
        arr[2][0] = 1;
        arr[3][0] = 1;
        arr[1][4] = 2;
        arr[2][4] = 2;
        arr[3][4] = 1;
        userChessmanCount = 8;
        machineChessmanCount = 8;
    }

    public int evaluate() {
        return machineChessmanCount - userChessmanCount;
    }

    public boolean move(int r1, int c1, int r2, int c2) {
        if (!inBoard(r1, c1)
                || !inBoard(r2, c2)
                || !canMove(r1, c1, r2, c2)
                || Math.abs(r1 - r2) > 1
                || Math.abs(c1 - c2) > 1
                || (r1 == r2 && c1 == c2)) {
            return false;
        }
        if (arr[r1][c1] > 0 && arr[r2][c2] == 0) {
            arr[r2][c2] = arr[r1][c1];
            arr[r1][c1] = 0;
            capture(r2, c2);
            if(turn == 1)
                turn =2;
            else 
                turn =1;
            return true;
        }
        return false;
    }
    
    private boolean canMove(int r1, int c1, int r2, int c2) {//kiểm tra có thể di chuyển từ một vị trí đến một vị trí khác
        return r1 == r2
                || c1 == c2
                || (r1 == c1 && r2 == c2)
                || (r1 + c1 == 4 && r2 + c2 == 4)
                || (r1 + c1 == 2 && r2 + c2 == 2)
                || (r1 + c1 == 6 && r2 + c2 == 6)
                || (r1 - c1 == 2 && r2 - c2 == 2)
                || (r1 - c1 == -2 && r2 - c2 == -2);
    }

    private void capture(int r, int c) {//kiem tra va an tat ca cac quan doi phuong co the khi da dat quan vao vi tri (r, c)
        //gánh
        int current = arr[r][c];
        checkNip(r - 1, c, r + 1, c, current);
        checkNip(r, c - 1, r, c + 1, current);
        checkNip(r - 1, c - 1, r + 1, c + 1, current);
        checkNip(r + 1, c - 1, r - 1, c + 1, current);
        //vây
        // kiểm tra tất cả các nút có thể di chuyển tới và xem xét có phải đó là quân đối phương bị vây
        if (canMove(r, c, r - 1, c - 1)) {
            checkBlockade(r - 1, c - 1);
        }
        checkBlockade(r - 1, c);
        if (canMove(r, c, r - 1, c + 1)) {
            checkBlockade(r - 1, c + 1);
        }
        checkBlockade(r, c + 1);
        if (canMove(r, c, r + 1, c + 1)) {
            checkBlockade(r + 1, c + 1);
        }
        checkBlockade(r + 1, c);
        if (canMove(r, c, r + 1, c - 1)) {
            checkBlockade(r + 1, c - 1);
        }
        checkBlockade(r, c - 1);
    }

    private void checkNip(int r1, int c1, int r2, int c2, int current) {//quân hiện tại (ở giữa) đã biết, kiểm tra hai bên nếu có thể gánh
        if (inBoard(r1, c1) && inBoard(r2, c2) && canMove(r1, c1, r2, c2)) {
            if (arr[r1][c1] == arr[r2][c2] && arr[r1][c1] > 0 && arr[r1][c1] != current) {
                arr[r1][c1] = arr[r2][c2] = current;
                if (current == 1) {
                    userChessmanCount += 2;
                    machineChessmanCount -= 2;
                } else {
                    machineChessmanCount += 2;
                    userChessmanCount -= 2;
                }
                capture(r1, c1);
                capture(r2, c2);
            }
        }
    }

    private boolean inBoard(int r, int c) {//kiểm tra một vị trí có ở trong bàn cờ
        return r >= 0 && r < 5 && c >= 0 && c < 5;
    }

    private void checkBlockade(int r, int c) {//kiểm tra một vị trí chưa biết, nếu là vị trí của một quân cờ và bị vây thì chuyển thành quân đối phương
        int current, opponent;
        if (inBoard(r, c)) {
            current = arr[r][c];
            if (current > 0) {
                if (current == 1) {
                    opponent = 2;
                } else {
                    opponent = 1;
                }
                if (checkAround(r, c, opponent)) {//kiểm tra xung quanh nếu bị bao vây
                    arr[r][c] = opponent;
                    if (opponent == 1) {
                        userChessmanCount++;
                        machineChessmanCount--;
                    } else {
                        machineChessmanCount++;
                        userChessmanCount--;
                    }
                }
            }
        }
    }

    private boolean checkAround(int r, int c, int opponent) {//Kiểm tra nếu một quân có bị bao vây bởi quân đối phương không
        if (inBoard(r - 1, c - 1) && canMove(r, c, r - 1, c - 1) && arr[r - 1][c - 1] != opponent) {
            return false;
        }
        if (inBoard(r - 1, c) && arr[r - 1][c] != opponent) {
            return false;
        }
        if (inBoard(r - 1, c + 1) && canMove(r, c, r - 1, c + 1) && arr[r - 1][c + 1] != opponent) {
            return false;
        }
        if (inBoard(r, c + 1) && arr[r][c + 1] != opponent) {
            return false;
        }
        if (inBoard(r + 1, c + 1) && canMove(r, c, r + 1, c + 1) && arr[r + 1][c + 1] != opponent) {
            return false;
        }
        if (inBoard(r + 1, c) && arr[r + 1][c] != opponent) {
            return false;
        }
        if (inBoard(r + 1, c - 1) && canMove(r, c, r + 1, c - 1) && arr[r + 1][c - 1] != opponent) {
            return false;
        }
        if (inBoard(r, c - 1) && arr[r][c - 1] != opponent) {
            return false;
        }
        return true;
    }
}
