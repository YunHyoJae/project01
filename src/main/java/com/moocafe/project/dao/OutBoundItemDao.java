package com.moocafe.project.dao;

import com.moocafe.project.entity.OutBoundItem;
import com.moocafe.project.repository.OutBoundItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OutBoundItemDao {

    private final OutBoundItemRepository outBoundItemRepository;

    public OutBoundItem save(OutBoundItem item) {
        return  outBoundItemRepository.save(item);
    }

    public Optional<OutBoundItem> findById(Integer id) {
        return outBoundItemRepository.findById(id);
    }

    public List<OutBoundItem> findAll() {
        return outBoundItemRepository.findAll();
    }

    public void delete(OutBoundItem item) {
        outBoundItemRepository.delete(item);
    }
}
