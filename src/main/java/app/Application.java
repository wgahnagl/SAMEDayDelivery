package app;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {
        get("/", (req, res) -> "h--hewwo??? owo;;; mistew posteman???");
    }
}