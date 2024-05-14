package tw.mirai1129.domain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class GobangUI extends Application {

    private static final int BOARD_SIZE = 15; // 棋盤大小
    private char[][] board; // 棋盤狀態
    private boolean isPlayer1Turn = true; // 紀錄當前玩家是否為玩家1（黑棋）的回合
    private boolean gameOver = false; // 紀錄遊戲是否結束

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gobang"); // 設置應用程式視窗標題
        primaryStage.setResizable(false);

        GridPane gridPane = new GridPane(); // 使用 GridPane 佈局來擺放棋盤上的格子
        board = new char[BOARD_SIZE][BOARD_SIZE]; // 初始化棋盤

        // 創建棋盤格子，這裡使用 Button 作為格子
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button btn = new Button();
                btn.setStyle("-fx-background-color: rgb(234, 215, 176); -fx-border-color: #D98D40;"); // 棋盤顏色
                btn.setMinSize(30, 30); // 設置每個按鈕的大小
                int finalRow = row;
                int finalCol = col;
                btn.setOnAction(event -> handleButtonClick(btn, finalRow, finalCol)); // 設置按鈕點擊事件處理函數
                gridPane.add(btn, col, row); // 將按鈕添加到 GridPane 中的特定位置
            }
        }

        Scene scene = new Scene(gridPane, 30 * BOARD_SIZE, 30 * BOARD_SIZE); // 創建場景，設置大小為 450x450
        primaryStage.setScene(scene); // 將場景設置到視窗中
        primaryStage.show(); // 顯示視窗
    }

    // 按鈕點擊事件處理函數
    private void handleButtonClick(Button btn, int row, int col) {
        if (!gameOver && board[row][col] == 0) {
            // 根據當前玩家下棋
            char symbol = isPlayer1Turn ? 'X' : 'O';
            board[row][col] = symbol; // 在該位置標記下棋的玩家
            isPlayer1Turn = !isPlayer1Turn; // 切換玩家回合

            // 設置按鈕背景色以表示玩家下的棋子顏色
//            Color color = symbol == 'X' ? Color.BLACK : Color.GRAY;
//            BackgroundFill fill = new BackgroundFill(color, null, null);
//            btn.setBackground(new Background(fill));

            Circle circle = new Circle(15); // 創建一個半徑為25的圓形
            circle.setFill(symbol == 'X' ? Color.BLACK : Color.WHITE); // 設置圓形填充顏色
            btn.setGraphic(circle); // 將圓形設置為按鈕的圖形

            // 檢查是否有玩家獲勝
            if (checkWin(row, col, symbol)) {
                String winner = symbol == 'X' ? "黑棋" : "白棋";
                showAlert(winner + "獲勝！");
                gameOver = true; // 設置遊戲結束標誌為 true
                Platform.exit(); // 結束應用程式
            } else if (checkDraw()) {
                showAlert("和局！");
                gameOver = true; // 設置遊戲結束標誌為 true
                Platform.exit(); // 結束應用程式
            }
        }
    }

    // 檢查是否有玩家在指定位置（row, col）獲勝的方法
    private boolean checkWin(int row, int col, char player) {
        // 遍歷周圍的八個方向
        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                // 略過（0, 0）方向
                if (rowDelta == 0 && colDelta == 0) {
                    continue;
                }
                // 檢查單個方向是否連成五子
                if (checkDirection(row, col, player, rowDelta, colDelta)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 檢查單個方向上是否有五子連成的方法
    private boolean checkDirection(int row, int col, char player, int rowDelta, int colDelta) {
        // 初始化連子計數器為1，因為起點位置已經有一顆棋子
        int count = 1;
        // 從指定位置的相鄰位置開始向特定方向遍歷
        int r = row + rowDelta;
        int c = col + colDelta;
        // 向前遍歷，計算連續相同玩家的棋子數目
        while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
            count++;
            r += rowDelta;
            c += colDelta;
        }
        // 將位置重置為指定位置，向後遍歷
        r = row - rowDelta;
        c = col - colDelta;
        // 向後遍歷，計算連續相同玩家的棋子數目
        while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
            count++;
            r -= rowDelta;
            c -= colDelta;
        }
        // 如果連子數目達到五子或以上，則返回true，否則返回false
        return count >= 5;
    }

    // 檢查是否和局的方法
    private boolean checkDraw() {
        // 檢查棋盤是否已滿
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 0) {
                    return false; // 還有空位，遊戲還未和局
                }
            }
        }
        return true; // 棋盤已滿，遊戲和局
    }

    // 顯示彈窗的方法
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("遊戲結束");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
