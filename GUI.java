import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField firstTAField;
	private JTextField secondTAField;
	private JTextField EDWField;
	private double firstPError;
	private double secondPError;
	private int numOfEDW;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setTitle("Tests Application");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 356, 436);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel firstTALabel = new JLabel("First teaching assistant P error:");
		firstTALabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		firstTALabel.setBounds(31, 43, 324, 13);
		contentPane.add(firstTALabel);

		firstTAField = new JTextField();
		firstTAField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstPError = Double.parseDouble(firstTAField.getText());
				if (firstPError < 0.1 || firstPError > 0.9 || (firstPError / 0.01) != (int) (firstPError * 100))
					firstPError = 0.2;
			}
		});
		firstTAField.setBounds(31, 66, 147, 41);
		contentPane.add(firstTAField);
		firstTAField.setColumns(10);

		JLabel secondTALabel = new JLabel("Second teaching assistant P error:");
		secondTALabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		secondTALabel.setBounds(31, 117, 282, 13);
		contentPane.add(secondTALabel);

		secondTAField = new JTextField();
		secondTAField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondPError = Double.parseDouble(secondTAField.getText());
				if (secondPError < 0.1 || secondPError > 0.9 || (secondPError / 0.01) != (int) (secondPError * 100))
					secondPError = 0.2;
			}
		});
		secondTAField.setColumns(10);
		secondTAField.setBounds(31, 140, 147, 41);
		contentPane.add(secondTAField);

		JLabel EDWLabel = new JLabel("Number of EDW:");
		EDWLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		EDWLabel.setBounds(31, 202, 282, 13);
		contentPane.add(EDWLabel);

		EDWField = new JTextField();
		EDWField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numOfEDW = Integer.parseInt(EDWField.getText());
				if (numOfEDW < 1 || numOfEDW > 3)
					numOfEDW = 2;
			}
		});
		EDWField.setColumns(10);
		EDWField.setBounds(31, 225, 147, 41);
		contentPane.add(EDWField);

		JButton startButton = new JButton("START");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Data data = new Data("C:\\Users\\Doron\\לימודים\\קורסים\\פתמ''ע\\עבודה 4\\12StudentsData.txt",
						firstPError, secondPError, numOfEDW);
			}
		});
		startButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		startButton.setBounds(194, 298, 122, 41);
		contentPane.add(startButton);

		JButton exitButton = new JButton("EXIT");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		exitButton.setBounds(31, 298, 122, 41);
		contentPane.add(exitButton);
	}

} // GUI
