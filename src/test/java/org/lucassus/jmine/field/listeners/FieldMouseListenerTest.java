package org.lucassus.jmine.field.listeners;

import java.awt.event.MouseEvent;
import org.lucassus.jmine.enums.GameIcon;
import org.lucassus.jmine.field.Field;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;

@Test
public class FieldMouseListenerTest {

    @Mock
    private Field fieldMock;

    @Mock
    private MouseEvent mouseEventMock;

    @Mock
    private IFieldObserver observerMock;

    private FieldMouseListener instance;

    @BeforeMethod
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(fieldMock.getObserver()).thenReturn(observerMock);
        instance = new FieldMouseListener(fieldMock);
    }

    @Test
    public void leftMouseButtonClicked() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
        when(fieldMock.hasMine()).thenReturn(true);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).setDetonated(true);
        verify(fieldMock).setIcon(GameIcon.MINE_DETONATED.getIcon());
        verify(observerMock).mineWasDetonated();
    }

    @Test
    public void rightMouseButtonClicked() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON3);
        when(fieldMock.isDetonated()).thenReturn(false);
        when(fieldMock.hasFlag()).thenReturn(false);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).setHasFlag(true);
        verify(observerMock).flagWasSet();
    }

    @Test
    public void middleMouseButtonClickedTest() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON2);
        when(fieldMock.isDetonated()).thenReturn(true);
        when(fieldMock.getNeighborFlagsCount()).thenReturn(3);
        when(fieldMock.getNeighborMinesCount()).thenReturn(3);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).detonateNeighbourFields();
        verify(observerMock).fieldWasDetonated();
    }

}
