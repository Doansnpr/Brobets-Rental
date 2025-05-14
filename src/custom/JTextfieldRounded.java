
package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalTextFieldUI;

public class JTextfieldRounded extends JTextField {

    private TextFieldUI textUI;

    public JTextfieldRounded() {
        // Create and set the custom UI for the text field
        textUI = new TextFieldUI(this);
        setUI(textUI);
    }

    private class TextFieldUI extends MetalTextFieldUI {
        private JTextField textfield;
        private Border border;
        private int round = 15;
        private List<String> items = new ArrayList<>();
        
        public int getRound() {
            return round;
        }

        public void setRound(int round) {
            this.round = round;
            border.setRound(round);
            // border.setR (komentar ini kemungkinan sebelumnya ingin mengatur radius border)
            textfield.repaint();
        }


        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }
        
        public TextFieldUI(JTextField textfield) {
            this.textfield = textfield;
            border = new Border(10);
            border.setRound(round); // 'round' sudah dideklarasikan sebelumnya
            textfield.setBorder(border);
            textfield.setOpaque(false);
            textfield.setSelectionColor(Color.red);
            textfield.setSelectedTextColor(Color.YELLOW);
            textfield.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    border.setColor(Color.cyan);
                    textfield.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    border.setColor(Color.pink);
                    textfield.repaint();
                }
            });
            
        }
        
        @Override
        protected void paintBackground(Graphics g) {
            if (textfield.isOpaque()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(textfield.getBackground());
                g2.fillRoundRect(0, 0, textfield.getWidth() - 1, textfield.getHeight() - 1, round, round);
                g2.dispose();
            }
        }


        private class Border extends EmptyBorder {
            private Color focusColor = Color.BLUE;
            private Color color = Color.GREEN;
            private int round;

            public Color getFocusColor() {
                return focusColor;
            }

            public void setFocusColor(Color focusColor) {
                this.focusColor = focusColor;
            }

            public Color getColor() {
                return color;
            }

            public void setColor(Color color) {
                this.color = color;
            }

            public int getRound() {
                return round;
            }

            public void setRound(int round) {
                this.round = round;
            }

            public Border(int border) {
                super(border, border, border, border);
            }
            
            public Border() {
                this(5);
            }

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (c.isFocusOwner()) {
                    g2.setColor(focusColor);
                } else {
                    g2.setColor(color);
                }

                g2.drawRoundRect(x, y, width - 1, height - 1, round, round);
                g2.dispose();
            }


        }


    }
}
