package hds;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//记事本主体类
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * @Author: hds
 * @Date: 2024-05-06-18:50
 * @Version 1.0
 */

public class MiniTextEdit extends JFrame implements ActionListener {

    private JFileChooser jfc; // 文本选择
    private UndoManager um; // 撤销管理类
    private static JTextArea textArea; // 文本区域
    //public static JTextArea textArea; // 文本区域
    private JScrollPane js; // 滚动条
    private JPanel jp; // 面板对象
    private Toolkit toolKit; // 获取默认工具包。
    private Clipboard clipboard; // 获取系统剪切板
    private int option;
    //private static String path = "D:/"; //文件保存路径设置
    private static String filenameTemp;

    public static JTextArea getTextArea() {
        return textArea;
    }

    public static void setTextArea(JTextArea textArea) {
        MiniTextEdit.textArea = textArea;
    }

    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("文件(F)"), // 菜单
            edit = new JMenu("编辑(E)"),
            format = new JMenu("格式(O)"),
            view = new JMenu("查看(V)"), help = new JMenu("帮助(H)");

    JMenuItem[] menuItem = { new JMenuItem("新建(0)"), new JMenuItem("打开(1)"),
            new JMenuItem("保存(2)"), new JMenuItem("另存为(3)"),
            new JMenuItem("退出(4)"), new JMenuItem("全选(5)"),
            new JMenuItem("复制(6)"), new JMenuItem("粘贴(7)"),
            new JMenuItem("剪切(8)"), new JMenuItem("删除(9)"),
            new JMenuItem("撤销(10)"), new JMenuItem("清空(11)"),
            new JMenuItem("自动换行(12)"), new JMenuItem("字体(13)"),
            new JMenuItem("字体颜色(14)"), new JMenuItem("背景颜色(15)"),
            new JMenuItem("帮助主题(16)"), new JMenuItem("关于记事本(17)"), };

    JPopupMenu popupMenu = new JPopupMenu(); // 右键菜单
    JMenuItem[] menuItem1 = { new JMenuItem("撤销(Z)"), new JMenuItem("剪切(X)"),
            new JMenuItem("复制(C)"), new JMenuItem("粘贴(V)"),
            new JMenuItem("删除(D)"), new JMenuItem("全选(A)"), };
    //private FileFilter ;

    /**
     * MiniEdit 方法定义
     * 实现记事本初始化
     **/
    public MiniTextEdit() {

        jfc = new JFileChooser();
        um = new UndoManager();
        toolKit = Toolkit.getDefaultToolkit();
        clipboard = toolKit.getSystemClipboard();
        textArea = new JTextArea();
        js = new JScrollPane(textArea);
        jp = new JPanel();
        js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jp.setLayout(new GridLayout(1, 1));
        jp.add(js);
        textArea.setComponentPopupMenu(popupMenu); // 文本区域添加右键
        textArea.add(popupMenu);
        add(jp);
        setTitle("文本编辑器");
        filenameTemp ="";
        setFont(new Font("Times New Roman", Font.HANGING_BASELINE, 8));
        setBackground(Color.WHITE);
        setSize(600, 600);
        setJMenuBar(menuBar);
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(view);
        menuBar.add(help);
        for (int i = 0; i < 5; i++) {
            file.add(menuItem[i]);
            edit.add(menuItem[i + 5]);
        }

        for (int i = 0; i < 4; ++i) {
            format.add(menuItem[i + 12]);
        }
        edit.add(menuItem[10]);
        edit.add(menuItem[11]);
        view.add(menuItem[16]);
        menuItem[16].setEnabled(false);
        help.add(menuItem[17]);
        for (int i = 0; i < 6; ++i) {
            popupMenu.add(menuItem1[i]);
        }

        // 设置关闭时什么也不做
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // 注册各个菜单项的事件监听器
        for (int i = 0; i < menuItem.length; i++) {
            menuItem[i].addActionListener(this);
        }
        for (int i = 0; i < menuItem1.length; i++) {
            menuItem1[i].addActionListener(this);
        }
        // 编辑撤销的监听
        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {// 注册撤销可编辑监听器
                    public void undoableEditHappened(UndoableEditEvent e) {
                        um.addEdit(e.getEdit());
                    }
                });

        // 窗口监听
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JOptionPane d_input = new JOptionPane();
                int res = d_input.showConfirmDialog(null, "是否保存", "",
                        d_input.YES_NO_CANCEL_OPTION);
                if (res == d_input.YES_OPTION) {
                    saveFile();
                    if (option == JFileChooser.APPROVE_OPTION) {
                        dispose();
                    }
                } else if (res == d_input.YES_NO_CANCEL_OPTION) {
                    dispose();
                } else if (res == d_input.CANCEL_OPTION) {
                    ;
                }
            }
        });
    }

    /**
     * actionPerformed 方法定义
     * 动作触发实现
     **/
    public void actionPerformed(ActionEvent e) {

        Object eventSource = e.getSource();
        if (eventSource == menuItem[0]) // 新建动作
        {
            MiniTextEdit note = new MiniTextEdit();
            note.setVisible(true);
        } else if (eventSource == menuItem[1])// 打开动作
        {
            open();
        } else if (eventSource == menuItem[2])// 保存动作
        {
            saveFile();
        } else if (eventSource == menuItem[3])// 另存为动作
        {
            saveFileAs();

        } else if (eventSource == menuItem[4]) // 退出
        {
            JOptionPane d_input = new JOptionPane();
            int res = d_input.showConfirmDialog(null, "是否保存", "",
                    d_input.YES_NO_CANCEL_OPTION, d_input.INFORMATION_MESSAGE);
            if (res == d_input.YES_OPTION) {
                saveFile();
                if (option == JFileChooser.APPROVE_OPTION) {
                    dispose();
                }
            } else if (res == d_input.YES_NO_CANCEL_OPTION) {
                dispose();
            }
        } else if (eventSource == menuItem[5] || eventSource == menuItem1[5]) // 全选动作
        {
            textArea.selectAll();
        } else if (eventSource == menuItem[6] || eventSource == menuItem1[2]) // 复制动作
        {
            String text = textArea.getSelectedText();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection, null);
        } else if (eventSource == menuItem[7] || eventSource == menuItem1[3])// 粘贴动作
        {
            Transferable contents = clipboard.getContents(this);
            if (contents == null)
                return;
            String text;
            text = "";
            try {
                text = (String) contents
                        .getTransferData(DataFlavor.stringFlavor);
            } catch (Exception ex) {
            }
            textArea.replaceRange(text, textArea.getSelectionStart(),
                    textArea.getSelectionEnd());
        } else if (eventSource == menuItem[8] || eventSource == menuItem1[1])// 剪切动作
        {
            String text = textArea.getSelectedText();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection, null);
            textArea.replaceRange("", textArea.getSelectionStart(),
                    textArea.getSelectionEnd());
        } else if (eventSource == menuItem[9] || eventSource == menuItem1[4]) // 删除动作
        {
            textArea.replaceRange("", textArea.getSelectionStart(),
                    textArea.getSelectionEnd());
        } else if ((eventSource == menuItem[10] && um.canUndo())
                || (eventSource == menuItem1[0] && um.canUndo())) // 撤销动作
        {
            um.undo();
        } else if (eventSource == menuItem[11])// 清空动作
        {
            textArea.setText("");
        } else if (eventSource == menuItem[12]) // 自动换行
        {
            if (textArea.getLineWrap())
                textArea.setLineWrap(false);
            else
                textArea.setLineWrap(true);

        } else if (eventSource == menuItem[13]) // 字体
        {// 实例化字体类
            FontDialog fontdialog = new FontDialog(new JFrame(), "字体", true);
            Font f = fontdialog.showFontDialog();
            if (fontdialog.okpressed) {
                textArea.setFont(f); // 设置字体
            }
        } else if (eventSource == menuItem[14]) // 字体颜色
        {
            JColorChooser color = new JColorChooser(); // 实例化颜色选择对话框
            Color c = color.showDialog(null, "", null);
            if (c != null) {
                textArea.setForeground(c); // 设置字体颜色
            }
        } else if (eventSource == menuItem[15]) // 背景颜色
        {
            JColorChooser color = new JColorChooser(); // 实例化颜色选择对话框
            Color c = color.showDialog(null, "", null);
            if (c != null) {
                textArea.setBackground(c);// 调用showDialog方法，能够打开颜色选择器
            }
        } else if (eventSource == menuItem[16]) // 帮助
        {
            try {
                String filePath = "C:/WINDOWS/Help/notepad.hlp";
                Runtime.getRuntime().exec("cmd.exe /c " + filePath);
            } catch (Exception ee) {
                JOptionPane.showMessageDialog(this, "打开系统的记事本帮助文件出错!", "错误信息",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (eventSource == menuItem[17]) // 关于记事本
        {
            String help = "迷你记事本  版本1.0\n操作系统：WIN & Linux \nIDE：IDEA\n版权"
                    + "所有: huangdongsheng\n最终解释权归本人所有，"
                    + "授权给：\n\nBuild By 黄东升\nQQ:201820186\n" + "\n文本编辑器";
            JOptionPane
                    .showConfirmDialog(null, help, "关于记事本",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * open 方法定义
     * 打开文件
     **/
    public void open() {
        // fileChooser 是 JFileChooser 的实例
        //文件过滤器
        FileNameExtensionFilter filter = new FileNameExtensionFilter("hds & txt", "hds","txt");
        jfc.setFileFilter(filter);
        // 显示文件选取的对话框S
        option = jfc.showOpenDialog(null);
        // 使用者按下确认键
        if (option == JFileChooser.APPROVE_OPTION) {
            try {

                InputStream in = new FileInputStream(jfc.getSelectedFile());
                BufferedReader buf = new BufferedReader(new InputStreamReader(in, "utf-8"));
                //String title = jfc.getSelectedFile().getPath()+jfc.getSelectedFile().getName() +".hds";
                // 设定文件标题
                setTitle(jfc.getSelectedFile().toString());
                // 清除前一次文件
                textArea.setText("");
                // 取得系统相依的换行字符
                String lineSeparator = System.getProperty("line.separator");
                // 读取文件并附加至文字编辑区
                String text;
                while ((text = buf.readLine()) != null) {
                    textArea.append(text);
                    textArea.append(lineSeparator);
                }
                buf.close();
                in.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "开启文件失败",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
			/*try {

				// 开启选取的文件
				BufferedReader buf = new BufferedReader(new FileReader(
						jfc.getSelectedFile()));
				//String title = jfc.getSelectedFile().getPath()+jfc.getSelectedFile().getName() +".hds";
				// 设定文件标题
				setTitle(jfc.getSelectedFile().toString());
				// 清除前一次文件
				textArea.setText("");
				// 取得系统相依的换行字符
				String lineSeparator = System.getProperty("line.separator");
				// 读取文件并附加至文字编辑区
				String text;
				while ((text = buf.readLine()) != null) {
					textArea.append(text);
					textArea.append(lineSeparator);
				}
				buf.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(), "开启文件失败",
						JOptionPane.ERROR_MESSAGE);
			}*/
    }

    /**
     * saveFile 方法定义
     * 保存文件
     **/
    private void saveFile() {
        // 从标题栏取得文件名称
        File file = new File(getTitle());
        // 若指定的文件不存在
        if (!file.exists()) {
            // 执行另存为
            saveFileAs();
        } else {
            try {
                OutputStream out = new FileOutputStream(file);
                BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(
                        out, "utf8"));
                // 开启指定的文件
                // BufferedWriter buf = new BufferedWriter(new
                // FileWriter(file));
                // 将文字编辑区的文字写入文件
                buf.write(textArea.getText());
                buf.close();
                out.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "写入文件失败",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * saveFileAs 方法定义
     * 另存
     **/
    public void saveFileAs() {
        // 显示文件对话框
        option = jfc.showSaveDialog(null);
        // 如果确认选取文件
        if (option == JFileChooser.APPROVE_OPTION) {
            // 取得选择的文件
            filenameTemp = jfc.getSelectedFile()+".hds";
            File file = new File(filenameTemp);
            //File file = jfc.getSelectedFile();
            // 在标题栏上设定文件名称
            setTitle(file.toString());
            try {
                // 建立文件
                file.createNewFile();
                // 进行文件保存
                saveFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "无法建立新文件",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}