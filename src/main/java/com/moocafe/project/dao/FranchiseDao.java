package com.moocafe.project.dao;

import com.moocafe.project.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FranchiseDao {
    private final FranchiseRepository franchiseRepository;
}
