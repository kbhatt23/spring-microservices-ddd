package com.learning.axon.accountqueryapplication.repostiory;

import org.springframework.data.repository.CrudRepository;

import com.learning.axon.accountqueryapplication.bean.entity.AccountQueryEntity;


public interface AccountRepository extends CrudRepository<AccountQueryEntity, String> {
}
