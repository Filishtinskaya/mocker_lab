package de.oth.mocker;

import java.util.*;
import org.junit.*;

import static de.oth.mocker.Mocker.*;
import static org.junit.Assert.*;

public class OthMockerTest {
    @Test
    public void mockTest1() {
        List<String> mockObject = mock(ArrayList.class);
        mockObject.add("John Doe");
        mockObject.add("Max Muster");
        mockObject.add("John Doe");
        assertEquals(mockObject.size(), 0); // would return 0 as mock’s default return value
        mockObject.clear();
        verify(mockObject, times(2)).add("John Doe");
        verify(mockObject).add("Max Muster"); // same as times(1)
        verify(mockObject, never()).clear();
    }

    @Test
    public void spyTest1() {
        List<String> names = new ArrayList<>();
        List<String> spyList = spy(names);
        spyList.add("John Doe"); // really adds to ArrayList names
        spyList.add("Max Muster");
        spyList.add("John Doe");
        assertEquals(spyList.size(), 3); // would return 3
        verify(spyList, atMost(2)).add("John Doe");
        verify(spyList, atLeast(1)).add("Max Muster");
        verify(spyList, never()).clear();
        verify(spyList, times(1)).add("Max");
    }

    @Test
    public void mockInterfaceTest() {
        Collection<String> mockCollection = mock(Collection.class);
        assertFalse(mockCollection.isEmpty());
        mockCollection.add("abc");
        assertFalse(mockCollection.contains("abc"));
        System.out.println(mockCollection.toArray());
        mockCollection.add("random string");
        assertFalse(mockCollection.isEmpty());
        verify(mockCollection, never()).add("drugs");
        verify(mockCollection).toArray();
        verify(mockCollection, times(1)).contains("abc");
        verify(mockCollection, atLeast(1)).add("abc");
        verify(mockCollection, atMost(1)).isEmpty();
    }
}