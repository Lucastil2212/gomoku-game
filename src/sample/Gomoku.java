package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class that represents the design and gameplay of the game Gomoku
 *
 * @author Lucas Tilford
 */
public class Gomoku extends Application {

    //borderpane to hold grid and buttons
    private BorderPane borderPane;

    //grid pane to hold buttons
    private GridPane gridPane;

    //the reset button
    private Button reset;

    //settings button
    private Button settings;

    //apply button
    private Button apply;

    //menu button in settings used for changing the board color
    private MenuButton menu;

    //continue button(used after illegal move)
    private Button cont;

    //Vbox to hold setting options
    VBox settingBox;

    //buttons on the grid
    private Button[][] buttons;

    //pieces on the grid , 0 is none, 1 is black, 2 is white
    private int[][] pieces;

    //number of rows on board
    private static int row = 19;

    //number of columns on board
    private static int col = 19;

    // 1 is black, 2 is white
    private int currentPlayer = 1;

    //number of pieces players are trying to get in a row
    private static int COUNT = 5;

    //state of game, true = not playing
    private boolean pause = false;

    //the option selected in the menu button in the settings box
    private int menuOption = 1;

    /**
     * start javaFX
     *
     * @param primaryStage main stage
     */
    @Override
    public void start(Stage primaryStage) {

        setButtons(new Button[col][row]);
        setPieces(new int[col][row]);

        createButtons();

        setUpGrid();

        reset = new Button("Reset");
        apply = new Button("Apply");

        //create menu items
        MenuItem gOption = new MenuItem("Green (default)");
        MenuItem bOption = new MenuItem("Light Blue (Pretty)");
        MenuItem oOption = new MenuItem("Orange (Halloween)");
        MenuItem yOption = new MenuItem("Purple (Color)");

        gOption.setOnAction(e -> {
            menuOption = 1;
        });
        bOption.setOnAction(e -> {
            menuOption = 2;
        });
        oOption.setOnAction(e -> {
            menuOption = 3;
        });
        yOption.setOnAction(e -> {
            menuOption = 4;
        });
        menu = new MenuButton("Color", null, gOption, bOption, oOption, yOption);


        //add buttons to setting box
        settingBox = new VBox(menu, apply);

        reset.setOnAction(e -> {
            pause = false;
            borderPane.setCenter(gridPane);
            borderPane.setTop(null);

            borderPane.setBottom(settings);
            BorderPane.setAlignment(settings, Pos.CENTER);

            //loop through the buttons and set them back to default
            for (int i = 0; i < getButtons().length; i++) {
                for (int j = 0; j < getButtons()[i].length; j++) {
                    getButtons()[i][j].setBackground(new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1))));
                    getPieces()[i][j] = 0;
                }
            }
        });

        apply.setOnAction(e -> {
            pause = false;
            borderPane.setLeft(null);
            changeColor();
        });

        settings = new Button("Change Color");
        settings.setOnAction(e -> {
            pause = true;
            borderPane.setLeft(settingBox);
        });

        borderPane.setBottom(settings);
        BorderPane.setAlignment(settings, Pos.CENTER);

        Scene scene = new Scene(borderPane);

        //stage
        primaryStage.setTitle("Gomoku by Lucas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * get buttons
     *
     * @return buttons to get
     */
    public Button[][] getButtons() {
        return buttons;
    }

    /**
     * set buttons
     *
     * @param buttons to set
     */
    public void setButtons(Button[][] buttons) {
        this.buttons = buttons;
    }

    /**
     * get pieces
     *
     * @return pieces to set
     */
    public int[][] getPieces() {
        return pieces;
    }

    /**
     * set pieces
     *
     * @param pieces to set
     */
    public void setPieces(int[][] pieces) {
        this.pieces = pieces;
    }

    /**
     * get currentplayer
     *
     * @return the current player (1 black, 2 white)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * set the current player
     *
     * @param currentPlayer to set (1 black, 2 white)
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * create all the buttons on the grid along with their action events
     * userData stores if the button is an empty tile(0), black piece(1), or white piece(2)
     */
    public void createButtons() {

        //loop through button array and create a new button for each element
        for (int i = 0; i < getButtons().length; i++) {
            for (int j = 0; j < getButtons()[i].length; j++) {

                getButtons()[i][j] = new Button();
                getButtons()[i][j].setBackground(new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1))));
                getButtons()[i][j].setPrefSize(30, 30);

                getButtons()[i][j].setOnAction(e -> {
                    if (!pause) {

                        Background black = new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1)), new BackgroundFill(Color.BLACK, new CornerRadii(20), new Insets(3)));
                        Background white = new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1)), new BackgroundFill(Color.WHITE, new CornerRadii(20), new Insets(3)));

                        Button b = (Button) e.getSource();

                        //row of clicked button in array
                        int bRow = 0;

                        //column of clicked button in array
                        int bCol = 0;

                        //find position in array of the button
                        for (int i1 = 0; i1 < getButtons().length; i1++) {
                            for (int j1 = 0; j1 < getButtons()[i1].length; j1++) {
                                if (getButtons()[i1][j1] == b) {
                                    bRow = i1;
                                    bCol = j1;
                                }
                            }
                        }
                        //current player name
                        String possibleWinner;

                        if (currentPlayer == 1) {
                            possibleWinner = "Black";
                        } else {
                            possibleWinner = "White";
                        }
                        if (getPieces()[bRow][bCol] == 0 && isLegal(bRow, bCol)) {
                            if (getCurrentPlayer() == 1) {
                                b.setBackground(black);
                                getPieces()[bRow][bCol] = 1;

                                if (won(bRow, bCol)) {
                                    endGame(possibleWinner + " Won!");
                                }
                                setCurrentPlayer(2);
                            } else {
                                b.setBackground(white);
                                getPieces()[bRow][bCol] = 2;

                                if (won(bRow, bCol)) {
                                    endGame(possibleWinner + " Won!");
                                }
                                setCurrentPlayer(1);
                            }
                            if (isFull()) {
                                endGame("Draw!");
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * checks if the player won with that piece
     *
     * @param i row of piece
     * @param j column of piece
     * @return
     */
    public boolean won(int i, int j) {

        //count left + count right
        if (numberInLine(getPieces(), i, j, -1, 0) + numberInLine(getPieces(), i, j, 1, 0) == COUNT + 1)
            return true;

        //count up and down
        if (numberInLine(getPieces(), i, j, 0, -1) + numberInLine(getPieces(), i, j, 0, 1) == COUNT + 1)
            return true;

        //count diagonal left up and right down
        if (numberInLine(getPieces(), i, j, -1, -1) + numberInLine(getPieces(), i, j, 1, 1) == COUNT + 1)
            return true;

        //count diagonal left down and right up
        if (numberInLine(getPieces(), i, j, -1, 1) + numberInLine(getPieces(), i, j, 1, -1) == COUNT + 1)
            return true;

        return false;
    }

    /**
     * counts the number in line of the peice just played
     *
     * @param pieces array that stores location of pieces
     * @param i      row of piece
     * @param j      column of piece
     * @param dX     x direction to count
     * @param dY     y direction to count
     * @return
     */
    public int numberInLine(int[][] pieces, int i, int j, int dX, int dY) {

        //number of pieces in a row
        int count = 0;

        //row counting
        int row = i;

        //column counting
        int col = j;

        //while we are within range of the board and current piece belongs to the current player, keep counting
        while (row >= 0 && row < getPieces().length && col >= 0 && col < getPieces()[row].length && getPieces()[row][col] == currentPlayer) {
            count++;
            row += dX;
            col += dY;
        }
        return count;
    }

    /**
     * check if a space is open by specifying direction
     *
     * @param pieces
     * @param i      currently played pieces row
     * @param j      currently played pieces col
     * @param dX     direction X to check
     * @param dY     direction Y to check
     * @return true of the space is open, false otherwise
     */
    public boolean isOpen(int[][] pieces, int i, int j, int dX, int dY) {

        //number of pieces in a row
        boolean open = false;

        //row counting
        int row = i + dX;

        //column counting
        int col = j + dY;

        //while we are within range of the board and current piece belongs to the current player, keep counting
        if (getPieces()[row][col] == 0)
            open = true;
        return open;
    }

    /**
     * check if the piece trying to be placed violates the "three-three" or "four-four" rule
     *
     * @param i the row of the piece trying to be placed
     * @param j the column of the piece trying to be placed
     * @return false if placing the piece violates one of the rules and displays an illegal move message, and true otherwise
     */
    public boolean isLegal(int i, int j) {

        Text t = new Text("Illegal Move! Press Continue to Try Again!");
        t.setFont(new Font(20));
        cont = new Button("Continue");
        cont.setOnAction(e -> {
            borderPane.setTop(null);
            borderPane.setBottom(settings);
            BorderPane.setAlignment(settings, Pos.CENTER);
            pause = false;
        });

        if (threethree(i, j) || fourfour(i, j)) {
            borderPane.setTop(t);
            borderPane.setBottom(cont);
            BorderPane.setAlignment(cont, Pos.CENTER);
            BorderPane.setAlignment(t, Pos.CENTER);
            pause = true;
            return false;
        }
        return true;
    }

    /**
     * check if the three-three rule is violated
     *
     * @param row of piece user is trying to place
     * @param col of piece user is trying to place
     * @return true if the rule is violated
     */
    public boolean threethree(int row, int col) {

        //groups of one less than the number to get in a row to win(COUNT)
        int groups = 0;

        getPieces()[row][col] = currentPlayer;

        //loop through each piece placed on the board and find the groups of four
        for (int i = 0; i < getPieces().length; i++) {
            for (int j = 0; j < getPieces()[i].length; j++) {

                if (numberInLine(getPieces(), i, j, 0, 1) == COUNT - 2 && (isOpen(getPieces(), i, j, 0, -1) && (isOpen(getPieces(), i, j + 2, 0, 1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 0, -1) == COUNT - 2 && (isOpen(getPieces(), i, j, 0, 1) && (isOpen(getPieces(), i, j - 2, 0, 1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, 1) == COUNT - 2 && (isOpen(getPieces(), i, j, -1, -1) && (isOpen(getPieces(), i + 2, j + 2, 1, 1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, -1) == COUNT - 2 && (isOpen(getPieces(), i, j, -1, 1) && (isOpen(getPieces(), i + 2, j - 2, 1, -1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, 1) == COUNT - 2 && (isOpen(getPieces(), i, j, 1, -1) && (isOpen(getPieces(), i - 2, j + 2, -1, 1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, -1) == COUNT - 2 && (isOpen(getPieces(), i, j, 1, 1) && (isOpen(getPieces(), i - 2, j - 2, -1, -1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, 0) == COUNT - 2 && (isOpen(getPieces(), i, j, 1, 0) && (isOpen(getPieces(), i - 2, j, -1, 1)))) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, 0) == COUNT - 2 && (isOpen(getPieces(), i, j, -1, 0) && (isOpen(getPieces(), i + 2, j, 1, 0)))) {
                    groups++;
                }
            }
        }
        if (groups > 2) {
            getPieces()[row][col] = 0;
            return true;
        }
        return false;
    }

    /**
     * check if the four-four rule is violated
     *
     * @param row of piece user is trying to place
     * @param col of piece user is trying to place
     * @return true if the rule is violated
     */
    public boolean fourfour(int row, int col) {

        //groups of one less than the number to get in a row to win(COUNT)
        int groups = 0;

        getPieces()[row][col] = currentPlayer;

        //loop through each piece placed on the board and find the groups of four
        for (int i = 0; i < getPieces().length; i++) {
            for (int j = 0; j < getPieces()[i].length; j++) {

                if (numberInLine(getPieces(), i, j, 0, 1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 0, -1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, 1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, -1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, 1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, -1) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, -1, 0) == COUNT - 1) {
                    groups++;
                }
                if (numberInLine(getPieces(), i, j, 1, 0) == COUNT - 1) {
                    groups++;
                }
            }
        }
        if (groups > 2) {
            getPieces()[row][col] = 0;
            return true;
        }
        return false;
    }

    /**
     * sets up the grid of button
     */
    public void setUpGrid() {

        borderPane = new BorderPane();
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(1));
        borderPane.setCenter(gridPane);

        //add buttons to the grid
        for (int i = 0; i < getButtons().length; i++) {
            for (int j = 0; j < getButtons()[i].length; j++) {
                gridPane.add(getButtons()[i][j], i, j);
            }
        }
    }

    /**
     * end the game, display victory message, display reset button
     *
     * @param winner message to display
     */
    public void endGame(String winner) {

        pause = true;

        Text t = new Text(winner);
        t.setFont(new Font(20));

        borderPane.setTop(t);
        BorderPane.setAlignment(t, Pos.CENTER);

        borderPane.setBottom(reset);
        BorderPane.setAlignment(reset, Pos.CENTER);

    }

    /**
     * change the color of board
     */
    public void changeColor() {
        for (int i = 0; i < getButtons().length; i++) {
            for (int j = 0; j < getButtons()[i].length; j++) {

                if (getPieces()[i][j] == 1) {
                    getButtons()[i][j].setBackground(new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1)), new BackgroundFill(Color.BLACK, new CornerRadii(20), new Insets(3))));
                } else if (getPieces()[i][j] == 2) {
                    getButtons()[i][j].setBackground(new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1)), new BackgroundFill(Color.WHITE, new CornerRadii(20), new Insets(3))));
                } else {
                    getButtons()[i][j].setBackground(new Background(new BackgroundFill(getColor(), new CornerRadii(1), new Insets(1))));

                }
            }
        }

    }

    /**
     * check if the board is full
     *
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < getPieces().length; i++) {
            for (int j = 0; j < getPieces()[i].length; j++) {
                if (getPieces()[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * get the current selected color from the menu
     *
     * @return the Color to get
     */
    public Color getColor() {
        if (menuOption == 2)
            return Color.LIGHTBLUE;
        if (menuOption == 3)
            return Color.ORANGE;
        if (menuOption == 4)
            return Color.PURPLE;
        return Color.GREEN;
    }

    public static void main(String[] args) {

        if (args.length == 3) {
            try {
                COUNT = Integer.parseInt(args[0]);
                col = Integer.parseInt(args[1]);
                row = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.print("You can specify number over pieces to get in a row as the first argument,\n or the number of rows and columns of the board in the second and third arguments as integers");
            }
        } else {
            try {
                col = Integer.parseInt(args[0]);
                row = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.print("You can specify number over pieces to get in a row as the first argument,\n or the number of rows and columns of the board in the second and third arguments as integers");
            }
        }
        launch(args);
    }
}
