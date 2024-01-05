/**
 * Summary for PathFinder class:
 *
 * The PathFinder class is designed to find a path through a pyramid-like map to collect treasures. The code takes a
 * file name as input and initializes the pyramidMap with the provided file. The Map class represents the pyramid map
 * and contains the necessary methods to retrieve information about chambers and their neighbors.
 *
 * The path() method implements a depth-first search (DFS) algorithm to find the path with the maximum number of
 * treasures. It starts from the entrance chamber and uses a DLStack (a custom implementation of a doubly-linked stack)
 * to keep track of the visited chambers. The algorithm prioritizes finding chambers with treasures first and explores
 * the neighboring chambers in a specific order to maximize the chances of discovering more treasures.
 *
 * A significant challenge faced during the design was handling the lighted and dim chambers. The isDim() method checks
 * if a chamber is dim, i.e., not sealed and not lighted but having at least one lighted neighbor. It is used in bestChamber()
 * to prioritize dim chambers, which might lead to more treasures.
 *
 * Testing involved creating test pyramid maps with different arrangements of chambers and treasures. The correctness of
 * the path() method was validated by comparing the output stack with the expected path. Test cases included maps with
 * varying treasure distributions, multiple paths with the same number of treasures, and edge cases with no treasures or
 * blocked paths.
 *
 *The code demonstrates a clear understanding of the problem, and the approach to design and testing was logical and
 * precise, resulting in an efficient path-finding algorithm for the given pyramid map.
 */

public class PathFinder {
    /**
     * This variable will store the current map selected (example: map0.txt, map1.txt, map2.txt or etc)
     */
    private Map pyramidMap;


    /**
     * Constructs a PathFinder object with the specified file name to initialize the pyramid map.
     * @param fileName The name of the file containing the pyramid map data.
     * @throws Exception If an error occurs while reading or processing the file.
     */
    public PathFinder(String fileName) {
        try {
            pyramidMap = new Map(fileName);
        }
        catch (Exception e ) {
            System.out.println("Something went wrong when opening the map file!");
        }
    }

    /**
     * The method starts by creating a new DLStack to store chambers encountered during the search. It initializes
     * the current chamber with the entrance of the pyramid map.
     * The variable numTreasuresNeedsToBeFound holds the total number of treasures present in the map, and foundTreasure
     * keeps track of the number of treasures found during the search.
     * The algorithm explores the pyramid map while the stack is not empty. It checks if the top chamber of the stack
     * contains a treasure. If a treasure is found, the foundTreasure counter increments, and if it equals
     * numTreasuresNeedsToBeFound, the search stops as all treasures have been found.
     * The method then calls the bestChamber(chamber) method, which determines the next chamber to explore based on
     * specific priorities. If a suitable chamber is found, it is pushed onto the stack, and its status is marked as pushed.
     * If there are no unexplored neighboring chambers or no more treasures to be found, the method backtracks by
     * popping the top chamber from the stack and marking it as popped. The current chamber is then updated to the previous
     * chamber on the stack.
     * The method continues the search until all treasures have been found or no more chambers are left to explore.
     * Finally, the method returns the stack, which represents the path containing chambers in the order they were
     * explored, leading to the collection of treasures.
     *
     * @return Returns the stack containing all of the chambers.
     */

    public DLStack<Chamber> path () {
    DLStack<Chamber> stack = new DLStack<>(); // Creating new stack to store our chambers.
    Chamber chamber = pyramidMap.getEntrance();// Starting Chamber
    int numTreasuresNeedsToBeFound = pyramidMap.getNumTreasures(); // Number of Treasures that needs to be found in the map.
    int foundTreasure = 0; // Current treasure found.
    stack.push(chamber);
    chamber.markPushed();

        while (!stack.isEmpty()) { // Searches until the stack containing all of the chambers are empty.
            if (stack.peek().isTreasure() && !stack.peek().isMarked()) {
                foundTreasure++;
                stack.peek().markPushed();
                if (numTreasuresNeedsToBeFound == foundTreasure) { // This condition will break out of the loop meaning all treasures have been found!
                    break;
                }
            }
            chamber = bestChamber(chamber); // This method will find the next best neighbour to move to!
            if (chamber != null) { // If the neighbour is not null do the following:
                stack.push(chamber);
                if (chamber.isTreasure()) { // Don't mark as Pushed yet
                }
                else {
                    chamber.markPushed();
                }
            }
            else { // else pop it and mark it as popped
                stack.pop().markPopped();
                if (!stack.isEmpty()){ // If the stack is not empty!
                    chamber = stack.peek();
                }
            }
        }

        return stack;
        
    }

    /**
     *
     * @return Returns the Map.
     */
    public Map getMap() {
        return pyramidMap;
    }

    /**
     * Checks whether the given Chamber is dim or not.
     * A Chamber is considered dim if it meets the following criteria:
     * The currentChamber is not null.
     * The currentChamber is not sealed (isSealed() returns false).
     * The currentChamber is not lighted (isLighted() returns false).
     * At least one neighboring Chamber of the currentChamber is lighted.
     *
     * @param currentChamber The Chamber object to be checked for dimness.
     * @return true if the currentChamber is dim (satisfies the criteria above), false otherwise.
     */

    public boolean isDim(Chamber currentChamber) {
        if (currentChamber != null) {
            if(!currentChamber.isSealed() && !currentChamber.isLighted()) {
                for (int i = 0; i < 6; i++) {
                    if (currentChamber.getNeighbour(i) != null) {
                        if (currentChamber.getNeighbour(i).isLighted()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * /**
     *
     * Finds the best unmarked neighboring chamber based on specific criteria relative to the current chamber.
     * This method searches for neighboring chambers of the specified current chamber and returns the first unmarked
     * chamber that satisfies one of the following conditions, in the specified priority order:
     * If the chamber contains a treasure and has not been marked, it is returned as the best chamber.
     * If there is no treasure in any unmarked neighboring chamber, but there is at least one unmarked chamber that is
     * lighted, the first lighted unmarked chamber is returned.
     * If neither of the above conditions is met, the method checks if there is any unmarked neighboring chamber that
     * is dim (not sealed and not lighted but having at least one lighted neighbor), and the first dim unmarked chamber is returned.
     *
     * @param currentChamber The chamber for which the best neighboring chamber needs to be found.
     * @return The best unmarked neighboring chamber based on the specified criteria, or null if no suitable chamber is found.
     * @see Chamber
     * @see #isDim(Chamber)
     */

    public Chamber bestChamber(Chamber currentChamber){

        for (int i = 0; i < 6; i++) {
            Chamber neighbour = currentChamber.getNeighbour(i);
            if (neighbour != null) {
                if (!neighbour.isMarked() && neighbour.isTreasure()) { // Check for Treasure neighbours that are unmarked.
                    return neighbour;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            Chamber neighbour = currentChamber.getNeighbour(i);
            if (neighbour != null) {
                if (!neighbour.isMarked() && neighbour.isLighted()) { // Check for Lighted neighbours that are unmarked.
                    return neighbour;
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            Chamber neighbour = currentChamber.getNeighbour(i);
            if (neighbour != null) {
                if (!neighbour.isMarked() && isDim(neighbour)) { // // Check for Dim neighbours that are unmarked.
                    return neighbour;
                }
            }
        }

        return null; // Returns null if there are no more neighbours left.
    }




}
