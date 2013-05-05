package femo.encog.neuralNets;

import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.Propagation;

import java.io.Serializable;

/*
convenience class which serializes both the network and the trainer so that you can save and resume training where you left off
 */
public class NeuralNetPojo implements Serializable {
    final BasicNetwork network;
    final Propagation trainer;

    public NeuralNetPojo(BasicNetwork network, Propagation trainer) {
        this.network = network;
        this.trainer = trainer;
    }
}
