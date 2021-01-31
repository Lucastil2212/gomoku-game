package sample;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * test the gomoku game helper methods
 *
 * @author Lucas Tilford
 */
public class GomokuTester {
    @Test
    public void numberInLinetest() {

        Gomoku go = new Gomoku();
        go.setCurrentPlayer(1);

        /**test right and left for first, middle and last*/
        go.setPieces(new int[10][10]);
        go.getPieces()[1][0] = 1;
        go.getPieces()[2][0] = 1;
        go.getPieces()[3][0] = 1;
        go.getPieces()[4][0] = 1;
        go.getPieces()[5][0] = 1;

        //right test first
        int result = go.numberInLine(go.getPieces(), 1, 0, 1, 0);
        assertEquals("Should return 5", 5, result);

        //right test middle
        result = go.numberInLine(go.getPieces(), 3, 0, 1, 0);
        assertEquals("Should return 3", 3, result);

        //right test last
        result = go.numberInLine(go.getPieces(), 5, 0, 1, 0);
        assertEquals("Should return 1", 1, result);

        //left test first
        result = go.numberInLine(go.getPieces(), 1, 0, -1, 0);
        assertEquals("Should return 1", 1, result);

        //left test middle
        result = go.numberInLine(go.getPieces(), 3, 0, -1, 0);
        assertEquals("Should return 3", 3, result);

        //left test last
        result = go.numberInLine(go.getPieces(), 5, 0, -1, 0);
        assertEquals("Should return 5", 5, result);

        /**test up and down for first, middle and last*/
        //set new pieces
        go.getPieces()[0][1] = 1;
        go.getPieces()[0][2] = 1;
        go.getPieces()[0][3] = 1;
        go.getPieces()[0][4] = 1;
        go.getPieces()[0][5] = 1;

        //up test first
        result = go.numberInLine(go.getPieces(), 0, 1, 0, -1);
        assertEquals("Should return 1", 1, result);

        //up test middle
        result = go.numberInLine(go.getPieces(), 0, 3, 0, -1);
        assertEquals("Should return 3", 3, result);

        //up test last
        result = go.numberInLine(go.getPieces(), 0, 5, 0, -1);
        assertEquals("Should return 5", 5, result);

        //down test first
        result = go.numberInLine(go.getPieces(), 0, 1, 0, 1);
        assertEquals("Should return 5", 5, result);

        //down test middle
        result = go.numberInLine(go.getPieces(), 0, 3, 0, 1);
        assertEquals("Should return 3", 3, result);

        //down test last
        result = go.numberInLine(go.getPieces(), 0, 5, 0, 1);
        assertEquals("Should return 1", 1, result);

        /**test diagonal left up, left down, right up, and right down for first, last, and many*/
        //set new pieces
        go.setCurrentPlayer(2);

        //pieces create an "X" on the board
        go.getPieces()[0][0] = 2;
        go.getPieces()[4][0] = 2;
        go.getPieces()[1][1] = 2;
        go.getPieces()[3][1] = 2;
        go.getPieces()[2][2] = 2;
        go.getPieces()[1][3] = 2;
        go.getPieces()[3][3] = 2;
        go.getPieces()[0][4] = 2;
        go.getPieces()[4][4] = 2;

        //diagonal left up test first
        result = go.numberInLine(go.getPieces(), 4, 4, -1, -1);
        assertEquals("Should return 5", 5, result);

        //diagonal left up test middle
        result = go.numberInLine(go.getPieces(), 2, 2, -1, -1);
        assertEquals("Should return 3", 3, result);

        //diagonal left up test last
        result = go.numberInLine(go.getPieces(), 0, 4, -1, -1);
        assertEquals("Should return 1", 1, result);

        //diagonal left down test first
        result = go.numberInLine(go.getPieces(), 4, 0, -1, 1);
        assertEquals("Should return 5", 5, result);

        //diagonal left down test middle
        result = go.numberInLine(go.getPieces(), 2, 2, -1, 1);
        assertEquals("Should return 3", 3, result);

        //diagonal left down test last
        result = go.numberInLine(go.getPieces(), 0, 4, -1, 1);
        assertEquals("Should return 1", 1, result);

        //diagonal right up test first
        result = go.numberInLine(go.getPieces(), 0, 4, 1, -1);
        assertEquals("Should return 5", 5, result);

        //diagonal right up test middle
        result = go.numberInLine(go.getPieces(), 2, 2, 1, -1);
        assertEquals("Should return 3", 3, result);

        //diagonal right up test last
        result = go.numberInLine(go.getPieces(), 4, 0, 1, -1);
        assertEquals("Should return 1", 1, result);

        //diagonal right down test first
        result = go.numberInLine(go.getPieces(), 0, 0, 1, 1);
        assertEquals("Should return 5", 5, result);

        //diagonal right down test middle
        result = go.numberInLine(go.getPieces(), 2, 2, 1, 1);
        assertEquals("Should return 3", 3, result);

        //diagonal right down test last
        result = go.numberInLine(go.getPieces(), 4, 4, 1, 1);
        assertEquals("Should return 1", 1, result);
    }

    @Test
    public void testWon() {
        Gomoku go = new Gomoku();
        go.setCurrentPlayer(1);

        go.setPieces(new int[10][10]);
        go.getPieces()[1][0] = 1;
        go.getPieces()[2][0] = 1;
        go.getPieces()[3][0] = 1;
        go.getPieces()[4][0] = 1;
        go.getPieces()[5][0] = 1;

        //test last
        boolean result = go.won(5, 0);
        assertEquals(true, result);

        //test middle
        result = go.won(3, 0);
        assertEquals(true, result);

        //test first
        result = go.won(1, 0);
        assertEquals(true, result);

        //test first
        result = go.won(0, 1);
        assertEquals(false, result);
    }

    @Test
    public void testThreeThree() {
        Gomoku go = new Gomoku();
        go.setCurrentPlayer(1);

        go.setPieces(new int[10][10]);

        //two groups of three with empty ends
        go.getPieces()[2][0] = 1;
        go.getPieces()[3][0] = 1;
        go.getPieces()[4][0] = 1;
        go.getPieces()[0][2] = 1;
        go.getPieces()[0][3] = 1;
        go.getPieces()[0][4] = 1;

        boolean result = go.threethree(0, 4);
        assertEquals(true, result);

        //now two groups of three but with a blocked end
        go.getPieces()[1][0] = 2;

        result = go.threethree(0, 4);
        assertEquals(false, result);

        //now two groups of three both ends are blocked
        go.getPieces()[5][0] = 2;

        assertEquals(false, result);
    }

    @Test
    public void testFourFour() {

        Gomoku go = new Gomoku();
        go.setCurrentPlayer(1);

        go.setPieces(new int[10][10]);

        //two groups of four
        go.getPieces()[1][0] = 1;
        go.getPieces()[2][0] = 1;
        go.getPieces()[3][0] = 1;
        go.getPieces()[4][0] = 1;
        go.getPieces()[1][1] = 1;
        go.getPieces()[2][1] = 1;
        go.getPieces()[3][1] = 1;
        go.getPieces()[4][1] = 1;

        boolean result = go.fourfour(4, 1);
        assertEquals(true, result);

        //get rid of one group
        go.getPieces()[1][1] = 0;
        go.getPieces()[2][1] = 0;
        go.getPieces()[3][1] = 0;
        go.getPieces()[4][1] = 0;

        result = go.fourfour(4, 1);
        assertEquals(false, result);
    }
}
