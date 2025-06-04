package com.moocafe.project.service;

import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.dao.MemberStoreDao;
import com.moocafe.project.dto.CustomUserDetails;
import com.moocafe.project.dto.LoginDto;
import com.moocafe.project.entity.Member;
import com.moocafe.project.entity.MemberStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberDao memberDao;
    private final MemberStoreDao memberStoreDao;

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        Optional<Member> user = memberDao.findByUserId(userID);
        if (user.isPresent()) {
            Member member = user.get();
            List<MemberStore> memberStore = memberStoreDao.findByMemberId(member.getId());
            LoginDto loginDto = LoginDto.toDto(memberStore.getFirst());
            System.out.println("==========="+loginDto);
            return new CustomUserDetails(member,loginDto);
        }
        throw new UsernameNotFoundException("아이디 패스워드 입력해주세요.");
    }
}