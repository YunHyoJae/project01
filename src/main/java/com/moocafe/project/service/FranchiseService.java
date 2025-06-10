package com.moocafe.project.service;

import com.moocafe.project.dao.FranchiseDao;
import com.moocafe.project.dao.FranchiseReplyDao;
import com.moocafe.project.dto.FranchiseBoardDto;
import com.moocafe.project.dto.FranchiseBoardSaveDto;
import com.moocafe.project.dto.FranchiseReplySaveDto;
import com.moocafe.project.entity.FranchiseBoard;
import com.moocafe.project.entity.FranchiseReply;
import com.moocafe.project.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseDao dao;
    private final FranchiseReplyDao replyDao;
    public int save(FranchiseBoardSaveDto dto){
        return dao.save(FranchiseBoardSaveDto.toEntity(dto));
    }
    public List<FranchiseBoardDto> list(){
        return dao.list().stream().map(FranchiseBoardDto::toDto).toList();
    }
    public FranchiseBoardDto get(int id){
        Optional<FranchiseBoard> entity = dao.findById(id);
        return entity.map(FranchiseBoardDto::toDto).orElse(null);
    }
    @Transactional
    public FranchiseBoardDto update(int id, Member member, String state){
        Optional<FranchiseBoard> entity = dao.findById(id);
        entity.ifPresent(board -> {
            if(!state.equals(board.getState())&&!board.getState().equals("상담완료")){
                dao.save(board.maskAsState(state));
                FranchiseReply reply=FranchiseReply.builder().board(board).member(member).state("확인중").build();
                replyDao.save(reply);
            }
        });
        return entity.map(FranchiseBoardDto::toDto).orElse(null);
    }
    @Transactional
    public int update2(FranchiseReplySaveDto dto, String state){
        Optional<FranchiseReply> entity = replyDao.findById(dto.getId());
        if(entity.isPresent()){
            FranchiseReply reply = entity.get();
            if(!state.equals(reply.getState())&&!reply.getState().equals("상담완료")){
                replyDao.save(reply.saveAsSaveDto(dto,"상담완료"));
                dao.save(reply.getBoard().maskAsState(state));
                return 1;
            }
        }
        return 0;
    }
    public Page<FranchiseBoardDto> page(String type, String keyword, Pageable pageable){
        Page<FranchiseBoard> franchiseBoardPage;
        List<FranchiseBoardDto> dtoList = switch (type) {
            case "name" -> {
                franchiseBoardPage = dao.findByName(keyword, pageable);
                yield franchiseBoardPage.getContent().stream()
                        .map(FranchiseBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "tel" -> {
                franchiseBoardPage = dao.findByTel(keyword, pageable);
                yield franchiseBoardPage.getContent().stream()
                        .map(FranchiseBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            case "state" -> {
                franchiseBoardPage = dao.findByState(keyword, pageable);
                yield franchiseBoardPage.getContent().stream()
                        .map(FranchiseBoardDto::toDto)
                        .collect(Collectors.toList());
            }
            default -> {
                franchiseBoardPage = dao.findAll(pageable);
                yield franchiseBoardPage.getContent().stream()
                        .map(FranchiseBoardDto::toDto)
                        .collect(Collectors.toList());
            }
        };
        return new PageImpl<>(dtoList, pageable, franchiseBoardPage.getTotalElements());
    }
}