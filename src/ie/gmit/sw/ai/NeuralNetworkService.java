package ie.gmit.sw.ai;

import ie.gmit.sw.ai.nn.BackpropagationTrainer;
import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.Trainator;
import ie.gmit.sw.ai.nn.activator.Activator.ActivationFunction;
import ie.gmit.sw.ai.nn.activator.ActivatorFactory;

//This will keep all the neural networks and spiders can get handles on the right neural networks
//also reduces space complexity...
//remember to look into storing weights here
public class NeuralNetworkService {
	private static NeuralNetworkService nns;
	private NeuralNetwork spartanNN;


	public static NeuralNetworkService getInstance(){
		if(nns == null)
			nns = new NeuralNetworkService();
		return nns;
	}
	
	public NeuralNetwork getSpartanNeuralNetwork(){
		return spartanNN;
	}
	
	private NeuralNetworkService() {

		//input layer ideas
		//health
		//anger?
		//these nearby ideas need to be nailed down to if they are just booleans or number of or distance
		//or we can have an input node for distance to nearest and a separate one for number?
		//also consider how to train it, ie what numbers you would pass it and what you want the spider to do in that event
		//nearby enemies (could also include enemy spiders?)
		//nearby friends (determines how confident they are?)
		//nearby treasure (something to guard?)
		
		
		//output node ideas
		//attack nearest enemy
		//flee
		//guard nearest treasure
		//follow nearest friend? probably need to do that idea where some spiders are leader types, then this friend thing becomes a
		//nearest leader. otherwise spiders would clump up and not move until the player comes by, which could be an idea too
		
		
		// ************************************************************************
				// start off training a NN for now, then move elsewhere later
				// pass in nearby treasure
				// need to pass in enemies later
				double[][] trainingData = { { 1, 0, 0, 0, 0 }, // only a sword nearby
						{ 0, 1, 0, 0, 0 }, // only help nearby, doesnt have a map
						{ 0, 1, 0, 0, 1 }, // help nearby, already has a map
						{ 0, 0, 1, 0, 0 }, // bomb nearby
						{ 0, 0, 0, 1, 0 }, // hmb nearby
						{ 1, 1, 0, 0, 0 }, // sword and help nearby, has no map
						{ 1, 0, 1, 0, 0 }, //
						{ 0, 1, 0, 1, 0 }, // no map
						{ 0, 1, 1, 0, 0 }, // help and bomb, nearby no map
						{ 0, 1, 1, 0, 1 }, // help nearby has map
						{ 0, 1, 0, 1, 0 }, // no map
						{ 0, 0, 1, 1, 0 }, //// bomb and hbomb nearby
						{ 0, 0, 0, 1, 0 }, // hbomb nearby
						{ 1, 1, 1, 1, 0 }, // all nearby, but no map
						{ 0, 0, 0, 0, 0 }// nothing in sight, wander around like a
											// tourist
				};
				// {getsword, gethlp, getbmb, gethmb, wander}
				double[][] expected = { { 1, 0, 0, 0, 0 }, // only a sword nearby
						{ 0, 1, 0, 0, 0 }, // only help nearby, doesnt have a map
						{ 0, 0, 0, 0, 1 }, // help nearby, already has a map
						{ 0, 0, 1, 0, 0 }, // bomb nearby
						{ 0, 0, 0, 1, 0 }, // hmb nearby
						{ 0, 1, 0, 0, 0 }, // sword and help nearby, has no map
						{ 1, 0, 0, 0, 0 }, // sword and bomb nearby
						{ 0, 0, 0, 0, 1 }, // no map
						{ 0, 1, 0, 0, 0 }, // help and bomb, nearby no map
						{ 0, 0, 0, 0, 1 }, // help nearby has map
						{ 0, 1, 0, 0, 0 }, // no map
						{ 0, 0, 0, 1, 0 }, //// bomb and hbomb nearby
						{ 0, 0, 0, 1, 0 }, // hbomb nearby
						{ 0, 1, 0, 0, 0 }, // all nearby, but no map
						{ 0, 0, 0, 0, 1 } };

				spartanNN = new NeuralNetwork(ActivationFunction.HyperbolicTangent, 5, 6, 5);
				Trainator t = new BackpropagationTrainer(spartanNN);
				t.train(trainingData, expected, .01, 10000);
				// training complete
				// ***************************************************************************
	}
}
