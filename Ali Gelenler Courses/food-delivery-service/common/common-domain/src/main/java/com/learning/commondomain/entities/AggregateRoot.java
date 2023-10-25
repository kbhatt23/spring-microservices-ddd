package com.learning.commondomain.entities;

public abstract class AggregateRoot<ID> extends BaseEntity<ID>{

	protected AggregateRoot(ID id) {
		super(id);
	}

}
