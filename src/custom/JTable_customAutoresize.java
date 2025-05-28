
package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JTable_customAutoresize extends JTable {
    
    private int selectedRow = -1;

    public JTable_customAutoresize() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean bln, boolean blnl, int i, int il) {
                TablezHeader header = new TablezHeader(o + "");
                if(il==10){
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(noFocusBorder);

                if (isSelected) {
                    com.setBackground(new Color(204, 153, 255));
                    com.setForeground(Color.BLACK);
                } else {
                    com.setBackground(Color.WHITE);
                    com.setForeground(Color.BLACK); 
                }

                return com;
            }
        });




        }public void addRow(Object[] row) {
            DefaultTableModel model = (DefaultTableModel) getModel();
            model.addRow(row);
        }


    public class TablezHeader extends JLabel {
        public TablezHeader(String text) {
            super(text);
            setOpaque(true);
            setBackground(new Color(228, 88, 88));
            setFont(new Font("SansSerif", Font.PLAIN, 12));
            setForeground(new Color(0, 0, 0));
            setBorder(new EmptyBorder(5, 10, 5, 10));

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(230, 230, 230));
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }

    }

}
