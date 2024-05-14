module Gobang {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens tw.mirai1129 to javafx.fxml;
    opens tw.mirai1129.domain to javafx.graphics;
    exports tw.mirai1129;
}
