package ie.gmit.sw.ai;

import ie.gmit.sw.ai.nn.NeuralNetwork;
import ie.gmit.sw.ai.nn.activator.ActivatorFactory;

//This will keep all the neural networks and spiders can get handles on the right neural networks
//also reduces space complexity...
//remember to look into storing weights here
public class NeuralNetworkService {
	NeuralNetwork testeroo;
	ActivatorFactory aFactory;

	public NeuralNetworkService() {
		aFactory = ActivatorFactory.getInstance();
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
		
		
	//	testeroo = new NeuralNetwork(function, num_input_nodes, num_hidden_nodes, num_output_nodes)
	}
}
