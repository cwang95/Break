/**
 *
 */
public class Main{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Starting node for the game tree,
        // Initial board is just constructor for Board with
        // no parameters: constructs an initial board
        GameTreeNode gameTree = new GameTreeNode(new Board());

        // Player will be player 1 (true, value = 1) at first
        boolean player = true;

        // Explore the tree with a maximum depth of 3 to build tree
        //gameTree.expand( player, 3 );

        // Game loop
        while(!gameTree.BP.gameOver(player)){
            System.out.println(gameTree.BP);

            try {
                gameTree = gameTree.getBestMove(player);    // Move game tree down to next move
            } catch (EmptyChildrenException e) {
                e.printStackTrace();
            }
            player = !player;
        }
        System.out.println(gameTree.BP);
        if (gameTree.BP.winner == 1){
            System.out.println("Player 1 has won!!");
        }
        if (gameTree.BP.winner == -1){
            System.out.println("Player 2 has won!!");
        }

    }
}
