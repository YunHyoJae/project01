package com.moocafe.project.config;


import com.moocafe.project.constent.Role;
import com.moocafe.project.dao.MemberDao;
import com.moocafe.project.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final MemberDao memberDao;
    private final DummyDataInitializer dummyDataInitializer;
    @Override
    public void run(String... args) throws Exception {
        String adminID ="admin";
        Optional<Member> optionalMember = memberDao.findByUserId(adminID);
        if(!optionalMember.isPresent()){
            dummyDataInitializer.setDummy(adminID);
        }else{
            System.out.println("관리자 계정이 이미 있습니다.");
        }
    }
}
