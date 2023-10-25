package com.learning.commondataaccess.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestrauntEntityId implements Serializable{

	private static final long serialVersionUID = 6455791611197096336L;

	private UUID restrauntId;

	private UUID productId;
}
