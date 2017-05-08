package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import server.ServerInterface;
import client.model.ViraLetrasModel;
import client.view.ViraLetrasView;

public class ViraLetrasController extends UnicastRemoteObject implements ClientInterface{
	
	private static final long serialVersionUID = 1L;

	@Override
	public void receiveChatMessage(String message){
		view.getMessageArea().append(message+"\n");
	}
	
	@Override
	public void startGame(String message){
		model.syncReceiveBoardGame(message);
		model.setMyTurn(false);
		view.chatViewFunctions(true);
		view.setButtonsTurn(false);
		view.getStartButton().setEnabled(false);
		view.resetSurrenderAndRestart();
	}
	
	@Override
	public void flipGamePiece(int index) throws RemoteException {
		model.getBoardPieces()[index].setFlipped(true);
		view.getGamePieces()[index].setText(model.getBoardPieces()[index].getFlipped());		
	}

	@Override
	public void validateGamePiece(int index) throws RemoteException {
		view.getGamePieces()[index].setEnabled(false);
		model.getBoardPieces()[index].setSelected(true);
	}
	
	@Override
	public void surrenderGame() throws RemoteException {
		model.setWinner(true);
		view.getMessageArea().append("Você venceu!!\n");
		view.setButtonsSurrender();
		showLetters();
	}

	@Override
	public boolean restartValidation() throws RemoteException {
		int value = JOptionPane.showConfirmDialog(view, "O seu adversário deseja reiniciar o jogo?", "Reiniciar...", JOptionPane.YES_NO_OPTION);
		if(value == 0)
			return true;
		else
			return false;
	}
	
	@Override
	public void restartGame() throws RemoteException {
		model.resetGameBoard();
		model.resetFlags();
		model.setScore(0, 0);
		model.setScore(1, 0);
		initVariables();
		loadButton();
		view.getMessageArea().setText("");
		view.getMessageArea().append("O jogo foi reiniciado..." + "\n");
		view.initialConfButtons();
		view.getScore().setText(model.getScore(0) + "  vs  " + model.getScore(1));
		view.setEnableBoard(true);
	}

	@Override
	public void rollDices(int value) throws RemoteException {
		view.getMessageArea().append("Seu adversário pode desvirar até " + value + " peças.\n");		
	}

	@Override
	public void nextTurn(int value) throws RemoteException {
		model.setMyTurn(true);
		model.setDicesRolled(false);
		model.setScore(1, model.getScore(1) + value);
		view.getScore().setText(model.getScore(0) + "  vs  " + model.getScore(1));
		setEnableGamePieces();
		view.setButtonsTurn(true);
	}
	
	@Override
	public boolean getTurnStatus(){
		return model.isMyTurn();
	}
	
	@Override
	public void finishGame() throws RemoteException{
		if(model.getScore(0) < model.getScore(1))
			view.getMessageArea().append("Parabéns, você ganhou!");
		else
			view.getMessageArea().append("Parabéns, você ganhou!");
		view.setButtonsSurrender();
		showLetters();
	}
	
	private class SendChatMessage implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String message = view.getMessageSend().getText();
				if(!message.isEmpty() && !message.trim().isEmpty()){
					server.sendChatMessage(message);
					view.getMessageSend().setText("");
					view.getMessageSend().requestFocus();
				}
			} catch (IOException ex) {
				view.getMessageArea().append("A mensagem não pode ser enviada...\n");
				view.getMessageSend().setText("");
				view.getMessageSend().requestFocus();
			}
		}
	}
	
	private class FlipPiece implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			int i=  (int) b.getClientProperty("index");
			
			try {
				if (counter < sum) {
					if (!model.getBoardPieces()[i].isFlipped() && !model.getBoardPieces()[i].isSelected()) {
						model.getBoardPieces()[i].setFlipped(true);
						view.getGamePieces()[i].setText(model.getBoardPieces()[i].getFlipped());
						flippedPieces.add(i);

						server.flipPiece(i);
						counter++;
					}
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class ValidatePiece implements MouseListener{
		long start, end;

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			start = System.currentTimeMillis();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				end = System.currentTimeMillis();
				JButton b = (JButton) e.getSource();
				int i = (int) b.getClientProperty("index");

				if ((end - start) > 400 && model.getBoardPieces()[i].isFlipped() && model.isMyTurn() && !model.getBoardPieces()[i].isSelected()) {
					selectedPieces.add(i);
					server.validatePiece(i);
				}
			} catch (RemoteException e1) {

			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	private class Surrender implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int i = JOptionPane.showConfirmDialog( view, "Deseja desistir deste jogo?", "Pense bem...", JOptionPane.YES_NO_OPTION);
				if (i == 0 && server.getGameStatus()) { //TODO verify!
					server.surrender(ViraLetrasController.this);
					model.setWinner(false);
					view.getMessageArea().append("Você perdeu!!\n");
					view.setButtonsSurrender();
					showLetters();
				}
			} catch (RemoteException e1){ }
		}
	}
	
	private class RollDice implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!model.isDicesRolled()) { // TODO verify
				int diceOne = model.rollDice();
				int diceTwo = model.rollDice();
				model.setDices(diceOne, diceTwo);
				model.setDicesRolled(true);

				counter = 0;
				sum = model.getSumDices();

				// view.setDiceOneLabel(model.getDices(0));
				// view.setDiceTwoLabel(model.getDices(1));
				view.getDicesButton().setEnabled(false);
				view.getMessageArea().append(
						"Você pode desvirar até " + model.getSumDices() + " peças\n");
				try {
					server.rollDices(ViraLetrasController.this, model.getSumDices());
				} catch (RemoteException e1) { 
					System.out.println("Remote exception in RollDice class");
				}
			}
		}
	}
	
	private class StartGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(server.getClientSize() == 2) {
					view.chatViewFunctions(true);
					model.setMyTurn(true);
//					setEnableGamePieces(true);
					setEnableGamePieces();
					server.startGame(ViraLetrasController.this, model.syncSendBoardGame());
					view.setButtonsTurn(true);
					view.getStartButton().setEnabled(false);
					view.resetSurrenderAndRestart();
				} else {
					view.getMessageArea().append("Esperando outro jogador conectar...\n");
				}
			} catch (RemoteException e1) { }
		}
	}
	
	private class Restart implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				int value = JOptionPane.showConfirmDialog(view, "Deseja realmente reiniciar o jogo?", "Reiniciar...", JOptionPane.YES_NO_OPTION);
				boolean rival = server.restartValidation(ViraLetrasController.this);
				if(value == 0 && rival){
					server.restartGame();
				}
			} catch(RemoteException e1){}
		}
	}
	
	private class EndTurn implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if(model.isDicesRolled() && model.isMyTurn()){
					int score = selectedPieces.size();
					model.setScore(0, model.getScore(0) + score);
					model.setMyTurn(false);
					view.getScore().setText(model.getScore(0) + "  vs  " + model.getScore(1));
					view.setButtonsTurn(false);
					setEnableGamePieces();
					flippedPieces = new ArrayList<Integer>();
					selectedPieces = new ArrayList<Integer>();
					server.endTurn(ViraLetrasController.this, score);
				} else{
					view.getMessageArea().append("É sua vez de jogar os dados!\n");
				}
			} catch(RemoteException e1){
				System.out.println("Exception in EndTurn class");
			}
		}
	}
	
	private class FinishGame implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				server.finishGame();
			} catch (RemoteException e1) { }
		}
	}
	
	private ViraLetrasView view;
	private ViraLetrasModel model;
	private ServerInterface server;
	
	private int counter, sum;
	private List<Integer> flippedPieces, selectedPieces;
	
	public ViraLetrasController(ViraLetrasView view, ViraLetrasModel model, ServerInterface server) throws RemoteException{
		this.view = view;
		this.model = model;
		this.server = server;
		
		this.initVariables();
		
		view.addSendChatMessageControl(new SendChatMessage());
		view.addFlipPieceControl(new FlipPiece(), new ValidatePiece());
		view.addSurrenderControl(new Surrender());
		view.addDicesControl(new RollDice());
		view.addStartControl(new StartGame());
		view.addRestartControl(new Restart());
		view.addEndTurnControl(new EndTurn());
		view.addFinishControl(new FinishGame());
		
		this.loadButton();
	}
	
	public void initVariables(){
		this.counter = 0;
		this.sum = 0;
		this.flippedPieces = new ArrayList<Integer>();
		this.selectedPieces = new ArrayList<Integer>();
	}
	
	private void loadButton(){
		for(int i=0; i< 64; i++){
			String letras = model.getBoardPieces()[i].getUnflipped();
			view.getGamePieces()[i].setText(letras);
		}
	}
	
	public void setEnableGamePieces(){
		for(int i=0; i<64; i++){
			if(model.getBoardPieces()[i].isSelected() && model.getBoardPieces()[i].isFlipped()){
				view.getGamePieces()[i].setEnabled(false);
				view.getGamePieces()[i].setText(" ");
			}
			
			if(!model.getBoardPieces()[i].isSelected() && model.getBoardPieces()[i].isFlipped()){
//				view.getGamePieces()[i].setEnabled(true);
				view.getGamePieces()[i].setText(model.getBoardPieces()[i].getUnflipped());
				model.getBoardPieces()[i].setFlipped(false);
			}
		}
	}
	
	private void showLetters(){
		for(int i=0; i< 64; i++){
			String letras = model.getBoardPieces()[i].getFlipped();
			view.getGamePieces()[i].setText(letras);
		}
	}
}