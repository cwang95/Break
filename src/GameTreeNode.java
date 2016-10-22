import java.util.LinkedList;

/**
 * GameTreeNode: a point in a game tree
 */
public class GameTreeNode {
    public LinkedList<GameTreeNode> children;  // Each possible resulting tree
    public Board BP;                    // Board position to calculate GTN on
    public boolean leafNode;

    // Constructor
    public GameTreeNode(Board BP) {
        children = new LinkedList<GameTreeNode>();
        this.BP = BP;
        leafNode = true;
    }

    public void expand(boolean player, int maxDepth) {
        if (maxDepth <= 0) {   // Reached max depth of tree we want to expand
            return;
        }

        if (leafNode) {
            LinkedList<Board> moves = BP.getMoves(player);
            for (Board b : moves) {
                children.add(new GameTreeNode(b));  // Calculate children for leaf node
            }
            leafNode = false;                       // Do not recalculate on next iteration
        }
        for (GameTreeNode GTN : children) {
            GTN.expand(!player, maxDepth - 1);      // Expand until max depth is reached
        }

    }

    /**
     *
     * @param player
     * @return
     */
    public double minimax(boolean player) {
        if (leafNode) {                             // Leaf node means we've reached the end of the tree
            if (player)
                return BP.boardValueOffensive();
            return BP.boardValue();        // Return value to be compared in minimax
        }

        if (player) {                               // Player 1 will want a HIGH score from Board heuristic
            double currMax = Double.MIN_VALUE;      // Start the MIN at the smallest possible value
            for (GameTreeNode GTN : children) {     // Cycle through all of the possible resulting moves
                currMax = Math.max(currMax, GTN.minimax(!player));
            }
            return currMax;
        } else {                                    // Player 2 will want a LOW score from Board heuristic
            double currMin = Double.MAX_VALUE;      // Start the MIN at the highest possible value
            for (GameTreeNode GTN : children) {     // Cycle through all of the possible resulting moves
                currMin = Math.min(currMin, GTN.minimax(!player));
            }
            return currMin;
        }
    }

    public GameTreeNode getBestMove(boolean player) throws EmptyChildrenException {
        expand(player, 4);

        if (children.isEmpty()) {
            throw new EmptyChildrenException("Board:"+BP.toString());
        }

        double playNum = 0;
        double maxScore = 0;

        if (player) {
            playNum = 1;
            maxScore = Double.MIN_VALUE;
        } else {
            playNum = -1;
            maxScore = Double.MAX_VALUE;
        }

        GameTreeNode best = null;

        for (GameTreeNode GTN : children) {
            // Find minimax depending on player
            double value = GTN.minimax(player);
            if (best == null || value * playNum > maxScore * playNum) {
                maxScore = value;
                best = GTN;
            }
        }

        return best;
    }
}
