package ie.gmit.sw.ai.sprites;

import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Trainator;
import ie.gmit.sw.ai.nn.activator.Activator.ActivationFunction;

public class SpiderNNService {
	//thread safe, nn doesn't need to be trained 9000 times
	private static SpiderNNService ss = new SpiderNNService();
	private NeuralNetwork bernard;
	private double[] inputs = new double[5];
	private SpiderNNService() {
		//dfndPickup, friend, attckSpartan, attackEnmy, lowHealth 	
		double[][] trainingData = { 
				{ 1, 0, 0, 0, 0},//sees pickup
				{ 0, 1, 0, 0, 1},//sees friend and health is low
				{ 0, 1, 0, 0, 0},//sees friend and health is fine
				{ 1, 0, 1, 0, 1},//sees pickup spartan and health is low
				{ 0, 1, 1, 0, 1},//sees friend spartan and health is low
				{ 1, 0, 1, 0, 0},//sees pickup spartan
				{ 0, 1, 1, 0, 0},//sees friend spartan
				{ 0, 1, 1, 1, 0},//sees friend spartan enemy
				{ 0, 1, 0, 1, 0},//sees friend enemy
				{ 0, 0, 1, 1, 0},//sees enemy spartan
				{ 1, 1, 1, 1, 0},//sees pickup friend spartan enemy
				{ 1, 1, 1, 1, 1},//sees pickup friend spartan enemy low health
				{ 0, 0, 0, 1, 1},//sees enemy low health
				{ 0, 0, 0, 1, 0},//sees enemy health fine
				{ 0, 0, 1, 0, 1},//sees spartan low health
				{ 1, 1, 0, 0, 0},//sees pickup friend health fine
				{ 1, 1, 0, 0, 1},//sees pickup friend health low
				{ 1, 1, 1, 0, 0},//sees pickup friend spartan health fine
				{ 1, 1, 1, 0, 1},//sees pickup friend spartan health low
				{ 1, 1, 0, 1, 0},//sees pickup friend enemy health fine
				{ 1, 1, 0, 1, 1}//sees pickup friend enemy health low
		};
		//dfndPickup, friend, attckSpartan, attackEnmy, do what you want spider
		double[][] expected = { 
				{ 1, 0, 0, 0, 0},//sees pickup
				{ 0, 1, 0, 0, 0},//sees friend and health is low
				{ 0, 0, 0, 0, 1},//sees friend and health is fine
				{ 1, 0, 0, 0, 0},//sees pickup spartan and health is low
				{ 0, 1, 0, 0, 0},//sees friend spartan and health is low
				{ 0, 0, 1, 0, 0},//sees pickup spartan
				{ 0, 0, 1, 0, 0},//sees friend spartan
				{ 0, 1, 0, 0, 0},//sees friend spartan enemy
				{ 0, 0, 0, 1, 0},//sees friend enemy
				{ 0, 0, 0, 1, 0},//sees enemy spartan
				{ 1, 0, 0, 0, 0},//sees pickup friend spartan enemy
				{ 0, 1, 0, 0, 0},//sees pickup friend spartan enemy low health
				{ 0, 0, 0, 0, 1},//sees enemy low health
				{ 0, 0, 0, 1, 0},//sees enemy health fine
				{ 0, 0, 0, 0, 1},//sees spartan low health
				{ 1, 0, 0, 0, 0},//sees pickup friend health fine
				{ 0, 1, 0, 0, 0},//sees pickup friend health low
				{ 0, 0, 1, 0, 0},//sees pickup friend spartan health fine
				{ 0, 1, 0, 0, 0},//sees pickup friend spartan health low
				{ 0, 0, 0, 1, 0},//sees pickup friend enemy health fine
				{ 0, 1, 0, 0, 0}};//sees pickup friend enemy health low
				
		System.out.println("training");
		bernard = new NeuralNetwork(ActivationFunction.HyperbolicTangent, inputs.length, 6, inputs.length);
		Trainator t = new BackpropagationTrainer(bernard);
		t.train(trainingData, expected, .01, 10000);
	}

	public static SpiderNNService getInstance() {
		return ss;
	}
	public double[] testNN(double[] actions)
	{
		try {
			return bernard.process(actions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new double[5];
	}	
}
