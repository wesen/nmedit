package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JModDisplayLabel extends JComponent {
    JModParameterObject jmpo = null;
    JModParameterObject jmpo2 = null;
    
    int val1 = 0;
    int val2 = 0;
    
    JModDisplayLabel _this = null;
    String text = null;
    
    public JModDisplayLabel() {
        super();
    }

    public JModDisplayLabel(int x, int y, int w, JModParameterObject new_jmpo, JModParameterObject new_jmpo2) {
        super();
        _this = this;
        jmpo = new_jmpo;
        jmpo2 = new_jmpo2;
        
        this.setFont(new Font("Dialog", Font.PLAIN, 10));
        
        val1 = jmpo.getValue();
        if (new_jmpo2!=null)
            val2 = jmpo2.getValue();
        
        repaint();
        
        this.setSize(w, 13);
        this.setLocation(x-1, y-3);
        
        jmpo.addChangeListener(new cl());
        jmpo.par.addChangeListener(new cl());
        
        if (jmpo2!=null) {
            jmpo2.addChangeListener(new cl2());
            jmpo2.par.addChangeListener(new cl2());
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.BLUE.darker());
        g.fillRoundRect(0, 0, this.getSize().width, this.getSize().height-1, 2, 2);
        
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, this.getSize().width-1, this.getSize().height-1, 2, 2);
        
        FontMetrics fontMetrics=getFontMetrics(g.getFont());
        Rectangle2D r = fontMetrics.getStringBounds(text, g);

        g.setColor(Color.WHITE);
        g.drawString(text, (int)((getWidth()/2)-r.getCenterX()), 10);
    }
    
    class cl implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
//          if (e.getSource().getClass().getSimpleName().equalsIgnoreCase("JModKnob"))
            if (e.getSource() instanceof Parameter)
                val1 = ((Parameter) e.getSource()).getValue();
            else
                if (e.getSource() instanceof JModParameterObject)
                    val1 = ((JModParameterObject) e.getSource()).getValue();
            repaint();
        }
    }
    
    class cl2 implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
//          if (e.getSource().getClass().getSimpleName().equalsIgnoreCase("JModKnob"))
            if (e.getSource() instanceof Parameter)
                val2 = ((Parameter) e.getSource()).getValue();
            else
                if (e.getSource() instanceof JModParameterObject)
                    val2 = ((JModParameterObject) e.getSource()).getValue();
            repaint();
        }
    }
    
    public void repaint() {
        if (jmpo2==null)
            this.text = String.valueOf(val1);
        else
            this.text = String.valueOf(val1) + "." + String.valueOf(val2);
        super.repaint();
    }
}
