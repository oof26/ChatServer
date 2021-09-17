import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.Border;
import javax.swing.*;  
/**
 * 
 * @author Anonymous?
 * @version 1.0
 * @release 14/3/2019
 *
 */
public class ServerGUI {
	JFrame window;
	JButton exit;
	JList msgs;
	DefaultListModel<String> msgsModel;
	JPanel panel1;
	JPanel panel2;
	JScrollPane scroll;
	/**
	 * Sets up and displays GUI
	 */
	public void draw(ChatServer chatServer) {
		window = new JFrame();
		window.setSize(500, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		window.setLocationRelativeTo(null);
		
		exit = new JButton("Exit");
		exit.setSize(10, 10);
		exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chatServer.shutdownServer();
            }

        });
		msgsModel = new DefaultListModel<>();
		
		msgs = new JList(msgsModel);
		msgs.setBounds(0, 0, 400, 400);
		msgs.setBackground(Color.white);
		msgs.setPrototypeCellValue("New client connected on port 123456 from port 123456");
		msgs.setVisibleRowCount(20);
		
		scroll =  new JScrollPane(msgs);
		scroll.setSize(400, 400);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		
		panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		
		panel1.add(scroll);
		panel2.add(exit);

		window.add(panel1);
		window.add(panel2);
		window.pack();
		window.setVisible(true);
		
	}
	public void addMessage(String msg) {
		
		msgsModel.addElement(msg + "\n");
		
	}

}
	

