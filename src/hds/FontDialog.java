package hds;
//字体类
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;

/**
 * @Author: hds
 * @Date: 2024-05-06-18:50
 * @Version 1.0
 */

public class FontDialog {

    public static final String DO_NOTHING_ON_CLOSE = null;
    private Dialog fontdialog;
    private JButton okButton, cancelButton;
    private int width = 480;
    private int height = 250;
    private String name = "Serif";
    private int type = 0;
    private int size = 12;
    private Font customFont = new Font("宋体", Font.ITALIC, 12);
    boolean okpressed = false;
    private boolean cancelpressed = false;
    private JLabel lbl1 = new JLabel("字体：");
    private JLabel lbl2 = new JLabel("字形：");
    private JLabel lbl3 = new JLabel("大小：");
    private JTextArea area;
    String[] zx = { "常规", "加粗", "斜体", "基线" };
    String[] dx = { "8", "9", "10", "12", "14", "15", "16", "18", "20", "21",
            "22", "24", "26", "28", "30", "36", "48", "54", "72", "89" };
    JLabel lbl = new JLabel("字体样式Style");
    private JComboBox cb1, cb3 = new JComboBox(dx), cb2 = new JComboBox(zx);
    private String[] zt;

    public FontDialog(Frame owner, String title, boolean modal) {

        init();
        fontdialog = new Dialog(owner, title, modal);
        fontdialog.setLocation(owner.getLocation());
        fontdialog.setLayout(new FlowLayout());
        fontdialog.setSize(getWidth(), getHeight());
        fontdialog.add(lbl1);
        fontdialog.add(cb1);
        fontdialog.add(lbl2);
        fontdialog.add(cb2);
        fontdialog.add(lbl3);
        fontdialog.add(cb3);
        fontdialog.add(okButton);
        fontdialog.add(cancelButton);
        fontdialog.add(area);
        fontdialog.setResizable(false);
        fontdialog.setAlwaysOnTop(true);
        cancelButton.addActionListener(new fontListener());
        okButton.addActionListener(new fontListener());
        fontdialog.addWindowListener(new fontListener());

        cb1.addItemListener(new ItemListener() { // 字体动作
            public void itemStateChanged(ItemEvent event) { // 添加监听器获取选择用户的字体类型
                name = (String) event.getItem();
                setCustomFont(new Font(name, type, size));
            }
        });

        cb2.addItemListener(new ItemListener() { // 字形动作
            public void itemStateChanged(ItemEvent event) { // 添加监听器获取选择用户的字形

                String s = (String) event.getItem();
                if (s.equals("常规")) {
                    type = Font.PLAIN;
                    setCustomFont(new Font(name, type, size));
                } else if (s.equals("加粗")) {
                    type = Font.BOLD;
                    setCustomFont(new Font(name, type, size));
                } else if (s.equals("斜体")) {
                    type = Font.ITALIC;
                    setCustomFont(new Font(name, type, size));
                } else {
                    type = Font.CENTER_BASELINE;
                    setCustomFont(new Font(name, type, size));
                }
            }
        });

        cb3.addItemListener(new ItemListener() { // 大小动作
            public void itemStateChanged(ItemEvent event) { // 添加监听器获取选择用户的字号大小
                size = (new Integer((String) event.getItem()).intValue());
                setCustomFont(new Font(name, type, size));
            }
        });

    }

    public Font showFontDialog() {

        fontdialog.setVisible(true);
        if (okpressed) {
            return getCustomFont();
        } else {
            return customFont;
        }
    }

    private void init() { // 初始化
        okButton = new JButton("确定");
        cancelButton = new JButton("取消");
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        zt = ge.getAvailableFontFamilyNames();
        cb1 = new JComboBox(zt);
        cb1.setMaximumRowCount(6);
        area = new JTextArea(6, 30);
        cb3 = new JComboBox(dx);
        cb3.setMaximumRowCount(6);
        okButton.setFocusable(true);
        area.setEditable(false);
        area.setText(new Date().toString());
        area.setBorder(new TitledBorder("字体样式"));

    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private int getWidth() {
        return (this.width);
    }

    private int getHeight() {
        return (this.height);
    }

    private void setCustomFont(Font customFont) // 设置字体
    {
        this.customFont = customFont;
        area.setFont(customFont);
        area.revalidate();
    }

    public String toString() {
        return FontDialog.class.toString();
    }

    public Font getCustomFont() // 获取字体
    {
        return (this.customFont);
    }

    private class fontListener extends WindowAdapter implements ActionListener // 监听事件类
    {
        public void windowClosing(WindowEvent e) {
            fontdialog.dispose();
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton) {

                fontdialog.dispose();
                cancelpressed = true;
            } else if (e.getSource() == okButton) {
                okpressed = true;
                setCustomFont(new Font(name, type, size));
                fontdialog.dispose();
            }
        }

    }
}

