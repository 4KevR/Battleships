import javafx.event.EventHandler;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;


public class MouseFieldHover implements EventHandler<MouseEvent> {
    Canvas canvasHover;
    
    public MouseFieldHover(Canvas canvasHover) {
        this.canvasHover = canvasHover;
    }
    
    public void handle(MouseEvent ev){
        double x = ev.getX();
        double y = ev.getY();
        GraphicsContext hover = canvasHover.getGraphicsContext2D();
        hover.clearRect(0, 0, canvasHover.getWidth(), canvasHover.getHeight());
        hover.setFill(Color.GREEN);
        if(x > 120 && x < 840 && y > 180 && y < 900) {
            hover.fillRect(120+(72* (int)((x-120)/72)), 180+(72* (int)((y-180)/72)), 72, 72);
        }
    }
}
