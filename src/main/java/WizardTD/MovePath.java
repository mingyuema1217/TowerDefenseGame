package WizardTD;

import java.util.*;


/**
 * Represents a path that entities can traverse within the game map.
 * This class provides functionalities to find a start point, end point,
 * and the shortest path between them.
 */
public abstract class MovePath {
    // Load the map to find the shortest path.
    private Character[][] map;

    private Random rand = new Random();

    /**
     * Constructs a MovePath for a given map.
     *
     * @param map 2D character array representing the game map.
     */
    public MovePath(Character[][] map) {
        this.map = map;
    }

    /**
     * Finds a random start point on the game map.
     *
     * @return An array with the start point's coordinates.
     */
    public int[] findStart() {
        ArrayList<int[]> startList = new ArrayList<int[]>();
        //check if left and right side has path
        for (int i = 0; i < map.length; i++) {
            if (map[i][0] == 'X') {
                startList.add(new int[]{i, 0});
            }
            if (map[i][map.length - 1] == 'X') {
                startList.add(new int[]{i, map.length - 1});
            }

        }
        //Check if up and down side has path
        for (int j = 0; j < map[0].length; j++) {
            if (map[0][j] == 'X') {
                startList.add(new int[]{0, j});
            }
            if (map[map[0].length - 1][j] == 'X') {
                startList.add(new int[]{map[0].length - 1, j});
            }

        }
        int randomIndex = rand.nextInt(startList.size());
        return startList.get(randomIndex);
    }

    /**
     * Finds the end point on the game map.
     *
     * @return An array with the end point's coordinates or null if not found.
     */
    public int[] findEnd() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'W') {
                    int[] endpoint = {i, j};
                    return endpoint;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the given position is a valid path.
     *
     * @param current Array with the coordinates to be checked.
     * @return true if it's a valid path, false otherwise.
     */
    public boolean isValidPath(int[] current) {
        int currX = current[0];
        int currY = current[1];
        return currX >= 0 && currY >= 0 && currX < map.length && currY < map[0].length;
    }

    /**
     * Determines the shortest path from start to end on the game map.
     *
     * @return A list of int arrays, each representing a point on the path.
     */
    public List<int[]> findShortestPath() {
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] start = findStart();
        int[] end = findEnd();

        Queue<int[]> queue = new LinkedList<>();
        Map<String, int[]> parentMap = new HashMap<>();

        queue.offer(start);
        parentMap.put(Arrays.toString(start), null);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (Arrays.equals(current, end)) {
                // find target，current is destination
                List<int[]> path = new ArrayList<>();
                while (current != null) {
                    path.add(0, current);
                    current = parentMap.get(Arrays.toString(current));
                }
                return path;
            }

            // Search in four directions
            for (int[] dir : dirs) {
                int newRow = current[0] + dir[0];
                int newCol = current[1] + dir[1];
                int[] neighbor = {newRow, newCol};

                if (isValidPath(neighbor) && (map[newRow][newCol] == 'X' || map[newRow][newCol] == 'W') && !parentMap.containsKey(Arrays.toString(neighbor))) {
                    queue.offer(neighbor);
                    parentMap.put(Arrays.toString(neighbor), current);
                }
            }
        }
        // Can't find path
        return null;
    }

    /**
     * Abstract method to be implemented by subclasses. Used for updating game entities.
     */
    public abstract void tick();

}
