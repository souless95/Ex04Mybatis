package mybatis;

import org.springframework.stereotype.Service;

//회원인증 처리를 위한 서비스 객체
@Service
public interface ServiceMyMember {
   //아이디, 패스워드를 통해 로그인 처리를 위한 추상메서드
   public MyMemberDTO login(String id, String pass);
}