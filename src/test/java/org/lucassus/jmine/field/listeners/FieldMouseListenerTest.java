package org.lucassus.jmine.field.listeners;

import java.awt.event.MouseEvent;
import junit.framework.TestCase;
import org.lucassus.jmine.enums.GameIcon;
import org.lucassus.jmine.field.Field;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class FieldMouseListenerTest extends TestCase {

    @Mock
    private Field fieldMock;

    @Mock
    private MouseEvent mouseEventMock;

    @Mock
    private IFieldObserver observerMock;

    private FieldMouseListener instance;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);
        when(fieldMock.getObserver()).thenReturn(observerMock);
        instance = new FieldMouseListener(fieldMock);
    }

    public void testLeftMouseButtonClicked() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON1);
        when(fieldMock.hasMine()).thenReturn(true);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).setDetonated(true);
        verify(fieldMock).setIcon(GameIcon.MINE_DETONATED.getIcon());
        verify(observerMock).mineWasDetonated();
    }

    public void testRightMouseButtonClicked() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON3);
        when(fieldMock.isDetonated()).thenReturn(false);
        when(fieldMock.hasFlag()).thenReturn(false);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).setHasFlag(true);
        verify(observerMock).flagWasSet();
    }

    public void testMiddleMouseButtonClickedTest() {
        when(mouseEventMock.getButton()).thenReturn(MouseEvent.BUTTON2);
        when(fieldMock.isDetonated()).thenReturn(true);
        when(fieldMock.getNeighborFlagsCount()).thenReturn(3);
        when(fieldMock.getNeighborMinesCount()).thenReturn(3);

        instance.mouseClicked(mouseEventMock);

        verify(fieldMock).detonateNeighbourFields();
        verify(observerMock).fieldWasDetonated();
    }

}
