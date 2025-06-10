package com.moocafe.project.dao;

import com.moocafe.project.entity.OutBound;
import com.moocafe.project.repository.OutBoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OutBoundDao {

    private final OutBoundRepository outBoundRepository;

    public OutBound save(OutBound outBound) {
        return outBoundRepository.save(outBound);
    }

    public Optional<OutBound> findById(Integer id) {
        return outBoundRepository.findById(id);
    }

    public List<OutBound> findAll() {
        return outBoundRepository.findAll();
    }

    public void delete(OutBound outBound) {
        outBoundRepository.delete(outBound);
    }
}
