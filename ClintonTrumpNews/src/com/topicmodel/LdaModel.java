package com.topicmodel;

import java.io.Serializable;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;

public class LdaModel implements Serializable{

	InstanceList instances = null;
	ParallelTopicModel model = null;
	
	
	public ParallelTopicModel getModel() {
		return model;
	}
	public void setModel(ParallelTopicModel model) {
		this.model = model;
	}
	public InstanceList getInstances() {
		return instances;
	}
	public void setInstances(InstanceList instances) {
		this.instances = instances;
	}
	
}
