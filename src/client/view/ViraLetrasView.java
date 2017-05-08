package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class ViraLetrasView extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public static boolean WINDOWS_STATE = true;
	
	//TODO mudar para JEditorPane para usar multifonts
	private static JTextArea messageArea;
	
	
	public static Font fontSystem = new Font("Serif", Font.ITALIC, 13);
	public static Font fontUser = new Font("Arial", Font.PLAIN, 15);
	
	private JPanel chatPanel, gameBoardPanel, functionsPanel;
	private JTextField messageSend;
	private JButton gamePieces[];
	private JButton sendButton, surrenderButton, endTurnButton, restartButton, dicesButton, startButton, finishButton;
	private JLabel diceOneLabel, diceTwoLabel, score;

	public ViraLetrasView() {
		setWindowsProperties();
		
		gameBoardPanel = setGameBoard(new JPanel(new GridLayout(8,8)));
		chatPanel = setChatPanel(new JPanel(new GridBagLayout()));
		functionsPanel = setExtraPanel(new JPanel());
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(functionsPanel);
		rightPanel.add(chatPanel);
		
		this.add(gameBoardPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);
		
		initialConfButtons();
	}
	
	private void setWindowsProperties(){
		this.setTitle("Vira-Letras");
		this.setSize(1000, 622);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
	}
	
	private JPanel setChatPanel(JPanel panel){
		GridBagConstraints gc = new GridBagConstraints();
		panel. setPreferredSize(new Dimension(400, 250));
		
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(messageArea);
		scrollPane.setPreferredSize(new Dimension(394, 208)); 
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		DefaultCaret caret = (DefaultCaret)messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		messageSend = new JTextField();
		messageSend.setPreferredSize(new Dimension(318, 40));
		
		sendButton = new JButton("Enviar");
		sendButton.setPreferredSize(new Dimension(80, 40));

		//scrollPane settings
		gc.gridwidth = 2;
		gc.gridheight = 2;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weighty = 0.5;
		gc.anchor = GridBagConstraints.PAGE_START;
		panel.add(scrollPane, gc);
		//messageSend settings
		gc.gridwidth = 2;
		gc.gridheight = 1;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.LAST_LINE_START;
		panel.add(messageSend, gc);
		//sendButton settings
		gc.gridwidth = 1;
		gc.gridheight = 1;
		gc.gridx = 1;
		gc.gridy = 1;
		gc.weightx = 0.5;
		gc.anchor = GridBagConstraints.LAST_LINE_END;
		panel.add(sendButton, gc);
		return panel;
	}
	
	private JPanel setGameBoard(JPanel panel){
		panel.setPreferredSize(new Dimension(600, 600));
		panel.setBackground(new Color(34, 167, 240));
		gamePieces = new JButton[64];
		
		for(int i=0; i<64; i++){
			gamePieces[i] = new JButton();
			gamePieces[i].setFont(new Font("Serif", Font.ITALIC, 16));
			gamePieces[i].putClientProperty("index", i);
//			gamePieces[i].setEnabled(false);
			panel.add(gamePieces[i]);
		}
		return panel;
	}
	
	private JPanel setExtraPanel(JPanel panel){
		panel.setPreferredSize(new Dimension(400, 350));
		panel.setLayout(new FlowLayout());
		
		startButton = new JButton("Iniciar");
		startButton.setPreferredSize(new Dimension(92, 72));
		
		restartButton = new JButton("Reiniciar!");
		restartButton.setPreferredSize(new Dimension(92, 72));
		
		surrenderButton = new JButton("Desistir");
		surrenderButton.setPreferredSize(new Dimension(92, 72));
		
		finishButton = new JButton("Terminar");
		finishButton.setPreferredSize(new Dimension(92, 72));
		
		dicesButton = new JButton("Jogar Dados");
		dicesButton.setPreferredSize(new Dimension(189, 72));
		
		endTurnButton = new JButton("Fim de Turno");
		endTurnButton.setPreferredSize(new Dimension(189, 72));
		
		JPanel northPanel = new JPanel(new FlowLayout(SwingConstants.CENTER, 5, 5));
		northPanel.setPreferredSize(new Dimension(400, 154));
		northPanel.add(startButton);
		northPanel.add(restartButton);
		northPanel.add(surrenderButton);
		northPanel.add(finishButton);
		northPanel.add(dicesButton);
		northPanel.add(endTurnButton);

		score = new JLabel( 0 + " vs " + 0, SwingConstants.CENTER);
		score.setFont(new Font("Arial", Font.BOLD, 14));
		score.setPreferredSize(new Dimension(400, 50));
		score.setBackground(new Color(255,182,30));
		score.setOpaque(true);
		
		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.setPreferredSize(new Dimension(400, 90));
		centerPanel.add(score);
		
		diceOneLabel = new JLabel("-");
		diceTwoLabel = new JLabel("-");
		diceOneLabel.setBackground(Color.WHITE);
		
		
		
		
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
//		panel.add(endTurnButton, BorderLayout.CENTER);
//		panel.add(dicesButton, BorderLayout.CENTER);
		
//		panel.add(diceOneLabel, BorderLayout.SOUTH);
//		panel.add(diceTwoLabel, BorderLayout.SOUTH);
		
		return panel;
	}

	/*
	 * Eventos dos botões!
	 */
	public void addSendChatMessageControl(ActionListener actionListener){
		sendButton.addActionListener(actionListener);
	}
	
	public void addFlipPieceControl(ActionListener actionListener, MouseListener mouseListener){
		for(int i=0; i<64; i++){
			gamePieces[i].addActionListener(actionListener);
			gamePieces[i].addMouseListener(mouseListener);
		}
	}
	
	public void addEndTurnControl(ActionListener actionListener){
		endTurnButton.addActionListener(actionListener);
	}
	
	public void addSurrenderControl(ActionListener actionListener){
		surrenderButton.addActionListener(actionListener);
	}
	
	public void addDicesControl(ActionListener actionListener){
		dicesButton.addActionListener(actionListener);
	}
	
	public void addStartControl(ActionListener actionListener){
		startButton.addActionListener(actionListener);
	}
	
	public void addRestartControl(ActionListener actionListener){
		restartButton.addActionListener(actionListener);
	}
	
	public void addFinishControl(ActionListener actionListener){
		finishButton.addActionListener(actionListener);
	}

	/*
	 * Getters and Setters
	 */
	public JTextArea getMessageArea() {
		return messageArea;
	}

	public JTextField getMessageSend() {
		return messageSend;
	}

	public JButton[] getGamePieces() {
		return gamePieces;
	}
	
	public JButton getDicesButton() {
		return dicesButton;
	}

	public JLabel getDiceOneLabel() {
		return diceOneLabel;
	}

	public void setDiceOneLabel(int value) {
		this.diceOneLabel.setText(value + "");
	}

	public JLabel getDiceTwoLabel() {
		return diceTwoLabel;
	}

	public void setDiceTwoLabel(int value) {
		this.diceTwoLabel.setText(value + "");
	}
	public JButton getStartButton() {
		return startButton;
	}
	
	public JLabel getScore(){
		return score;
	}
	
	public void setButtonsTurn(boolean bool){
//		this.setEnableBoard(bool);
		this.finishButton.setEnabled(bool);
		this.endTurnButton.setEnabled(bool);
		this.dicesButton.setEnabled(bool);
	}
	
	public void initialConfButtons(){
		setButtonsTurn(false);
		this.startButton.setEnabled(true);
		this.restartButton.setEnabled(false);
		this.surrenderButton.setEnabled(false);
		this.finishButton.setEnabled(false);
		chatViewFunctions(false);
	}
	
	public void setButtonsSurrender(){
//		this.setEnableBoard(false);
		this.endTurnButton.setEnabled(false);
		this.dicesButton.setEnabled(false);
		this.startButton.setEnabled(false);
		this.surrenderButton.setEnabled(false);
	}
	
	public void resetSurrenderAndRestart(){
		this.restartButton.setEnabled(true);
		this.surrenderButton.setEnabled(true);
	}
	/**
	 * Habilita ou desabilita os botões do painel de jogo.
	 * @param bool - true, se deseja habilitá-los. Caso contrário, false.
	 */
	public void setEnableBoard(boolean bool){
		for(JButton b : gamePieces){
				b.setEnabled(bool);
		}
	}
	
	public void chatViewFunctions(boolean bool){
		this.messageSend.setEditable(bool);		//não pode escrever.
		this.sendButton.setEnabled(bool);		//não pode apertar o botão.
	}
}
