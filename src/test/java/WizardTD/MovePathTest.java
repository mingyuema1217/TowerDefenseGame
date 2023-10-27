package WizardTD;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MovePathTest {

    private MovePath movePath;
    private Character[][] testMap;

    @BeforeEach
    public void setUp() {
        testMap = new Character[][]{
                {'O', 'X', 'O', 'O', 'O'},
                {'O', 'X', 'O', 'O', 'O'},
                {'O', 'X', 'O', 'O', 'O'},
                {'O', 'X', 'X', 'X', 'W'},
                {'O', 'O', 'O', 'O', 'O'}
        };

        movePath = new MovePath(testMap) {
            @Override
            public void tick() {
                // Mock implementation
            }
        };
    }

    @Test
    public void testFindStart() {
        int[] startPoint = movePath.findStart();
        assertTrue((startPoint[0] == 0 && (startPoint[1] == 0 || startPoint[1] == 1)) ||
                (startPoint[1] == 0 && ( startPoint[0] == 1 || startPoint[0] == 2)));
    }

    @Test
    public void testFindEnd() {
        int[] endPoint = movePath.findEnd();
        assertEquals(3, endPoint[0]);
        assertEquals(4, endPoint[1]);
    }

    @Test
    public void testIsValidPath() {
        assertTrue(movePath.isValidPath(new int[]{2, 1}));
        assertFalse(movePath.isValidPath(new int[]{10, 5}));
    }

    @Test
    public void testFindShortestPath() {
        List<int[]> shortestPath = movePath.findShortestPath();
        assertNotNull(shortestPath);
        assertEquals(7, shortestPath.size());

        // Check the start and end of the path
        assertArrayEquals(new int[]{0, 1}, shortestPath.get(0));
        assertArrayEquals(new int[]{3, 4}, shortestPath.get(shortestPath.size() - 1));
    }
}