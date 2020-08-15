package com.learning.cloudstream.streamconsumer.bean.ddd.aggregator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GreetingRepository extends JpaRepository<GreetingAggregator, Integer>{

}
