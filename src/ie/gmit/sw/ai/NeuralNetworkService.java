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

	public static NeuralNetworkService getInstance() {
		if (nns == null)
			nns = new NeuralNetworkService();
		return nns;
	}

	public NeuralNetwork getSpartanNeuralNetwork() {
		return spartanNN;
	}

	private NeuralNetworkService() {

		// input layer ideas
		// health
		// anger?
		// these nearby ideas need to be nailed down to if they are just
		// booleans or number of or distance
		// or we can have an input node for distance to nearest and a separate
		// one for number?
		// also consider how to train it, ie what numbers you would pass it and
		// what you want the spider to do in that event
		// nearby enemies (could also include enemy spiders?)
		// nearby friends (determines how confident they are?)
		// nearby treasure (something to guard?)

		// output node ideas
		// attack nearest enemy
		// flee
		// guard nearest treasure
		// follow nearest friend? probably need to do that idea where some
		// spiders are leader types, then this friend thing becomes a
		// nearest leader. otherwise spiders would clump up and not move until
		// the player comes by, which could be an idea too

		// ************************************************************************
		// start off training a NN for now, then move elsewhere later
		// pass in nearby treasure
		// need to pass in enemies later

		// inputs[0] = swrdNearby > 0 ? 1 : 0;
		// inputs[1] = hlpNearby > 0 ? 1 : 0;
		// inputs[2] = bmbNearby > 0 ? 1 : 0;
		// inputs[3] = hbmbNearby > 0 ? 1 : 0;
		// inputs[4] = hasmap > 0 ? 1 : 0;
		//5 spider near bomb
		//spider near hbomb

		double[][] trainingData = { 
			//   sw hp bm hbm m  sb sh
				{ 1, 0, 0, 0, 0, 0, 0 }, // 0 only a sword nearby
				{ 0, 1, 0, 0, 0, 0, 0 }, // 1 only help nearby, doesnt have a map
				{ 0, 1, 0, 0, 1, 0, 0 }, // 2 help nearby, already has a map
				{ 0, 0, 1, 0, 0, 0, 0 }, // 3 bomb nearby
				{ 0, 0, 0, 1, 0, 0, 0 }, // 4 hmb nearby
				{ 1, 1, 0, 0, 0, 0, 0 }, // 5 sword and help nearby, has no map
				{ 1, 0, 1, 0, 0, 0, 0 }, // 6 sword and bomb nearby
				{ 0, 1, 0, 1, 0, 0, 0 }, // 7 help and hbmb nearby no map
				{ 0, 1, 1, 0, 0, 0, 0 }, // 8 help and bomb no map
				{ 0, 1, 1, 0, 1, 0, 0 }, // 9 help and bomb nearby has map
				{ 0, 1, 0, 1, 0, 0, 0 }, // 10 help and hbomb, no map
				{ 0, 0, 1, 1, 0, 0, 0 }, // 11 bomb and hbomb nearby
				{ 0, 0, 0, 1, 0, 0, 0 }, // 12 hbomb nearby
				{ 1, 1, 1, 1, 0, 0, 0 }, // 13 all nearby, but no map
				{ 0, 0, 0, 0, 0, 0, 0 }, // 14 nothing in sight, wander around
				{ 0, 0, 1, 1, 1, 0, 0 }, // 15 bomb and hbomb nearby, has map
				{ 0, 0, 0, 0, 1, 0, 0 }, // 16 nothing in sight, has map
				{ 0, 0, 1, 0, 0, 1, 0 }, // 17 bomb nearby w/spd
				{ 1, 0, 1, 0, 0, 1, 0 }, // 18 swd, bmb nearby w/spd
				{ 0, 1, 1, 0, 0, 1, 0 }, // 19 hlp bmb nearby w/spd
				{ 1, 0, 0, 1, 0, 0, 1 }, // 20 swd, bmb nearby w/spd
				{ 0, 1, 0, 1, 0, 0, 1 }  // 21 hlp bmb nearby w/spd
		};
		// {getsword, gethlp, getbmb, gethmb, wander, exit}
		double[][] expected = { 
				{ 1, 0, 0, 0, 0, 0 }, // 0 get the sword
				{ 0, 1, 0, 0, 0, 0 }, // 1 get the help
				{ 0, 0, 0, 0, 0, 1 }, // 2 goto exit
				{ 0, 0, 0, 0, 1, 0 }, // 3 wander
				{ 0, 0, 0, 0, 1, 0 }, // 4 wander
				{ 0, 1, 0, 0, 0, 0 }, // 5 get help
				{ 1, 0, 0, 0, 0, 0 }, // 6 get sword
				{ 0, 1, 0, 0, 0, 0 }, // 7 get help
				{ 0, 1, 0, 0, 0, 0 }, // 8 get help
				{ 0, 0, 0, 0, 0, 1 }, // 9 exit
				{ 0, 1, 0, 0, 0, 0 }, // 10 help
				{ 0, 0, 0, 0, 1, 0 }, // 11 wander
				{ 0, 0, 0, 0, 1, 0 }, // 12 wander
				{ 0, 1, 0, 0, 0, 0 }, // 13 get help
				{ 0, 0, 0, 0, 1, 0 }, // 14 wander
				{ 0, 0, 0, 0, 0, 1 }, // 15 get out
				{ 0, 0, 0, 0, 0, 1 }, // 16 get out
				{ 0, 0, 1, 0, 0, 0 }, // 17 det bomb
				{ 0, 0, 1, 0, 0, 0 }, // 18 det bomb
				{ 0, 0, 1, 0, 0, 0 }, // 19 det bomb
				{ 0, 0, 1, 0, 0, 0 }, // 17 det bomb
				{ 0, 0, 0, 1, 0, 0 }, // 18 det hbomb
				{ 0, 0, 0, 1, 0, 0 }, // 19 det hbomb
		};

		spartanNN = new NeuralNetwork(ActivationFunction.HyperbolicTangent, 7, 8, 6);
		Trainator t = new BackpropagationTrainer(spartanNN);
		t.train(trainingData, expected, .01, 10000);
		// training complete
		// ***************************************************************************
	}
}
