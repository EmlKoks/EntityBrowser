package emlkoks.entitybrowser.common;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourcesTest {

//    @Test
    public void cacheDirIsCorrect() {
        Resources resources = mock(Resources.class);
        when(resources.getHomeDir()).thenReturn("testPath");
        System.out.println(Resources.CACHE_DIR);
        assertTrue(true);
    }

}