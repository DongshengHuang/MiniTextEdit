package hds;

import javax.swing.*;
import java.io.*;

/**
 * @Author: hds
 * @Date: 2024-05-06-18:48
 * @Version 1.0.0
 * 主函数
 */
public class Main {
    public static void main(String[] args) {

        MiniTextEdit note = new MiniTextEdit();
        note.setVisible(true);
        if (args.length > 0) {// 有指定文件名，传递给打开文件函数加载内容
            try {

                InputStream in = new FileInputStream(args[0]);
                BufferedReader buf = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String lineSeparator = System.getProperty("line.separator");
                // 读取文件并附加至文字编辑区
                String text;
                while ((text = buf.readLine()) != null) {
                    MiniTextEdit.getTextArea().append(text);
                    MiniTextEdit.getTextArea().append(lineSeparator);
                    //textArea.append(text);
                    //textArea.append(lineSeparator);
                }
                buf.close();
                in.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "开启文件失败",
                        JOptionPane.ERROR_MESSAGE);
            }
			/*try {
				File file = new File(args[0]);
				FileReader readIn = new FileReader(file);
				int size = (int) file.length();
				int charsRead = 0;
				char[] content = new char[size];
				while (readIn.ready()) {
					charsRead += readIn.read(content, charsRead, size
							- charsRead);
				}
				readIn.close();
				textArea.setText(new String(content, 0, charsRead));
			} catch (Exception e) {
				System.out.println("Error opening file!");
			}*/
        }
    }
}