

	import java.awt.BorderLayout;
	import java.awt.Color;
import java.awt.Dimension;
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
	public class ClientGUI {
		JFrame chatWindow;
		JButton chatExit;
		JButton chatSend;
		JTextField chatInput;
		JList chatMsgs;
		DefaultListModel<String> chatMsgsModel;
		JPanel chatPanel1;
		JPanel chatPanel2;
		JScrollPane chatScroll;
		/**
		 * Sets up and displays GUI
		 */
		public void draw(ChatClient chatClient) {
			chatWindow = new JFrame();
			chatWindow.setSize(500, 800);
			chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			chatWindow.setLayout(new FlowLayout());
			chatWindow.setLocationRelativeTo(null);
			
			chatSend = new JButton("Send");
			chatSend.setSize(10,10);
			chatSend.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e1) {
	                chatClient.sendMessage(chatInput.getText());
	                chatInput.setText(null);
	            }

	        });
			chatExit = new JButton("Exit");
			chatExit.setSize(10, 10);
			chatExit.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e2) {
	                chatClient.shutdownClient();
	            }

	        });
			
			chatInput = new JTextField();
			chatInput.setSize(10, 100);
			chatInput.setEditable(true);
			chatInput.setMinimumSize(new Dimension(10,100));
			
			chatMsgsModel = new DefaultListModel<>();
			
			chatMsgs = new JList(chatMsgsModel);
			chatMsgs.setBounds(0, 0, 400, 400);
			chatMsgs.setBackground(Color.white);
			chatMsgs.setPrototypeCellValue("New client connected on port 123456 from port 123456");
			chatMsgs.setVisibleRowCount(20);
			
			chatScroll =  new JScrollPane(chatMsgs);
			chatScroll.setSize(400, 400);
			chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			chatPanel1 = new JPanel();
			chatPanel1.setLayout(new BoxLayout(chatPanel1, BoxLayout.Y_AXIS));
			
			chatPanel2 = new JPanel();
			chatPanel2.setLayout(new FlowLayout());
			
			chatPanel1.add(chatScroll);
			chatPanel1.add(chatInput);
			
			chatPanel2.add(chatExit);
			chatPanel2.add(chatSend);

			chatWindow.add(chatPanel1);
			chatWindow.add(chatPanel2);
			chatWindow.pack();
			chatWindow.setVisible(true);
			
		}
		public void addMessage(String msg) {
			
			chatMsgsModel.addElement(msg + "\n");
			
		}

	}
		


