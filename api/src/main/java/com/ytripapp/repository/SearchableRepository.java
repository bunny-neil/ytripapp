package com.ytripapp.repository;

import com.ytripapp.repository.support.Page;
import com.ytripapp.repository.support.PageRequest;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface SearchableRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

    Page<T> search(Optional<String> queryString, PageRequest pageRequest);

}
