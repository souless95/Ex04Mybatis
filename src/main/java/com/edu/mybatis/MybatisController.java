package com.edu.mybatis;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mybatis.MyBoardDTO;
import mybatis.MyMemberDTO;
import mybatis.ParameterDTO;
import mybatis.ServiceMyBoard;
import mybatis.ServiceMyMember;
import util.PagingUtil;

@Controller
public class MybatisController {

	/*
	 * Mybatis를 사용하기 위해 자바빈을 자동으로 주입받는다. 해당 자바빈은 servlet-context.xml에서 생성하고 있다.
	 */
	@Autowired
	private SqlSession sqlSession;

	// 방명록 리스트 매핑
	@RequestMapping("/mybatis/list.do")
	public String list(Model model, HttpServletRequest req) {
		/*
		 * 방명록 테이블의 게시물의 갯수를 카운트한다. 호출방식은 다음과 같다. 컨트롤러에서 서비스객체인 interface에 정의된 추상메서드를
		 * 호출한다. 그러면 Mapper에 정의된 쿼리문이 실행되는 방식으로 동작한다. 순서] Controller에서 메서드 호출 =>
		 * interface의 추상메서드 호출 => 추상메서드명과 동일한 Mapper의 id속성을 찾음 => 해당 엘리먼트에 정의된 쿼리문 실행 =>
		 * 결과 반환
		 */
		int totalRecordCount = sqlSession.getMapper(ServiceMyBoard.class).getTotalCount();

		// 게시판 페이징 처리를 위한 설정값(하드코딩으로 간단히 설정)
		int pageSize = 4;// 한 페이지당 게시물 수
		int blockPage = 2;// 한 블럭당 페이지번호 수

		// 전체 페이지수 계산
		int totalPage = (int) Math.ceil((double) totalRecordCount / pageSize);

		/*
		 * 현재 페이지 번호 설정. 목록에 처음으로 접속한 경우 파라미터가 없으므로 1로 설정하고 그 외에는 파라미터를 사용하여 설정한다.
		 */
		int nowPage = (req.getParameter("nowPage") == null || req.getParameter("nowPage").equals("")) ? 1
				: Integer.parseInt(req.getParameter("nowPage"));

		// 해당 페이지에 출력할 게시물의 구간을 계산한다.
		int start = (nowPage - 1) * pageSize + 1;
		int end = nowPage * pageSize;

		/*
		 * 현재 페이지에 출력할 게시물을 인출한다. 이때 앞에서 계산한 start, end를 인수로 전달한다. 해당 인수는 Mapper에서
		 * #{param1}과 같이 사용된다.
		 */
		ArrayList<MyBoardDTO> lists = sqlSession.getMapper(ServiceMyBoard.class).listPage(start, end);

		// 페이지번호 처리(기존의 클래스를 그대로 사용한다.)
		String pagingImg = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/mybatis/list.do?");
		model.addAttribute("pagingImg", pagingImg);

		// 내용에 대한 줄바꿈 처리(방명록은 내용보기가 별도로 없고, 목록에 내용이 바로 출력된다.)
		for (MyBoardDTO dto : lists) {
			String temp = dto.getContents().replace("\r\n", "<br/>");
			dto.setContents(temp);
		}

		// 출력할 내용을 모델객체에 저장한 후 뷰를 호출한다.
		model.addAttribute("lists", lists);
		return "07Mybatis/list";
	}
	
	//기존 리스트에 검색기능을 추가한 컨트롤러 메서드
	@RequestMapping("/mybatis/listSearch.do")
	public String listSearch(Model model, HttpServletRequest req) {
		
		/*
		리스트에서 검색어를 입력한 후 submit하면 아래 파라미터를 받아서
		DTO에 저장해야 한다. 이 부분은 커맨드객체를 사용하면 아래 코드 없이
		한꺼번에 폼값을 받아 저장할 수 있다.
		*/
		ParameterDTO parameterDTO = new ParameterDTO();
		parameterDTO.setSearchField(req.getParameter("searchField"));
		parameterDTO.setSearchTxt(req.getParameter("searchTxt"));
		
		//검색어가 있는 경우 파라미터를 저장한 DTO를 Mapper로 전달한다.
		//이를 통해 where절을 <if>문으로 조건부로 삽입할 수 있다.
		int totalRecordCount = sqlSession.getMapper(ServiceMyBoard.class).getTotalCountSearch(parameterDTO);
		
		//페이지 관련 설정값
		int pageSize = 4;// 한 페이지당 게시물 수
		int blockPage = 2;// 한 블럭당 페이지번호 수
		
		//전체 페이지수 계산
		//int totalPage = (int) Math.ceil((double) totalRecordCount / pageSize);
		
		//현재 페이지번호 가져오기
		int nowPage = (req.getParameter("nowPage") == null || req.getParameter("nowPage").equals("")) ? 1
				: Integer.parseInt(req.getParameter("nowPage"));
		
		//출력할 페이지의 구간 계산
		int start = (nowPage - 1) * pageSize + 1;
		int end = nowPage * pageSize;
		//계산된 값은 DTO객체에 저장
		parameterDTO.setStart(start);
		parameterDTO.setEnd(end);
		
		//서비스 인터페이스를 통해 Mapper를 호출한다.
		ArrayList<MyBoardDTO> lists = sqlSession.getMapper(ServiceMyBoard.class).listPageSearch(parameterDTO);
		
		//페이지 바로가기 문자열 처리
		String pagingImg = PagingUtil.pagingImg(totalRecordCount, pageSize, blockPage, nowPage,
				req.getContextPath() + "/mybatis/list.do?");
		model.addAttribute("pagingImg", pagingImg);
		
		//내용에 대해 줄바꿈 처리
		for (MyBoardDTO dto : lists) {
			String temp = dto.getContents().replace("\r\n", "<br/>");
			dto.setContents(temp);
		}
		
		//출력할 내용을 모델에 저장한 후 뷰 경로 반환
		model.addAttribute("lists", lists);
		return "07Mybatis/list_search";
	}
	
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	// 글쓰기 페이지 매핑
	@RequestMapping("/mybatis/write.do")
	public String write(Model model, HttpSession session, HttpServletRequest req) {
		/*
		 * 컨트롤러에서 session 내장객체를 사용하고 싶다면 위와같이 매개변수로 선언해주기만 하면 메서드에서 즉시 사용할 수 있다.
		 */
		// 세션영역에 인증정보가 없다면 로그아웃 상태로 판단한다.
		if (session.getAttribute("siteUserInfo") == null) {
			/*
			 * 현재상태는 글쓰기를 위해 버튼을 출력했으므로, 만약 로그인이 된 후에는 글쓰기 페이지로 다시 이동하는것이 좋다. 따라서 backUrl이라는
			 * 속성값에 쓰기페이지에 대한 View경로를 저장하여 리다이렉트(이동) 시킨다.
			 */
			model.addAttribute("backUrl", "07Mybatis/write");
			/*
			 * 모델객체에 특정 속성을 저장한 후 redirect 시키면 URL뒤에 쿼리스트링 형태로 추가된다. 즉 login.do?backUrl=값 과
			 * 같이 리다이렉트 된다.
			 */
			return "redirect:login.do";
		}
		// 로그인이 완료된 상태라면 쓰기페이지로 진입한다.
		return "07Mybatis/write";
	}

	@RequestMapping("/mybatis/login.do")
	public String login(Model model) {
		return "07Mybatis/login";
	}
	
	//로그인 처리 : session 내장객체 사용
	@RequestMapping("/mybatis/loginAction.do")
	public ModelAndView loginAction(HttpServletRequest req, HttpSession session) {
		//폼값으로 전송된 id, pass를 login()메서드로 전달하여 Mapper를 호출한다.
		MyMemberDTO dto = sqlSession.getMapper(ServiceMyMember.class).login(req.getParameter("id"),
				req.getParameter("pass"));
		
		//Model객체에 속성저장과 View반환을 동시에 처리할 수 있는 객체
		ModelAndView mv = new ModelAndView();
		//Mapper에서 회원인증에 실패한 경우(로그인 실패)
		if (dto == null) {
			//로그인 실패 메세지를 모델객체에 저장한다.
			mv.addObject("LoginNG", "아이디/패스워드가 틀렸습니다.");
			//로그인 페이지를 화면에 출력한다.
			mv.setViewName("07Mybatis/login");
			return mv;
		} else {
			//로그인에 성공한 경우에는 세션영역에 DTO객체를 저장한다. 
			session.setAttribute("siteUserInfo", dto);
		}
		
		/*
		로그인 처리 후 backUrl이 있는 경우라면 해당 페이지로 이동시킨다.
		만약 값이 없다면 로그인 페이지로 이동시킨다.
		backUrl은 글쓰기 페이지로 진입시 로그인 정보가 없는 경우 파라미터로
		전달된 쓰기페이지의 View경로이다.
		
		*/
		String backUrl = req.getParameter("backUrl");
		if (backUrl == null || backUrl.equals("")) {
			mv.setViewName("07Mybatis/login");
		} else {
			mv.setViewName(backUrl);
		}
		return mv;
	}
	
	//글쓰기 처리
	@RequestMapping(value="/mybatis/writeAction.do",
			method=RequestMethod.POST)
	public String writeAction(Model model, HttpServletRequest req, HttpSession session) {
		/*
        글쓰기 페이지에 오랫동안 머물러 세션이 끊어지는 경우가 있으므로
        글쓰기 처리 직전에 반드시 세션을 확인한 후 처리해야한다.
	     */
		if(session.getAttribute("siteUserInfo")==null) {
			return "redirect:login.do";
		}
		
		/*
		전송된 파라미터를 이용해서 쓰기 처리를 위해 write()메서드를 호출한다.
		이때 아이디의 경우 세션 영역에 저장되어 있으므로 형변환 후 getter()를 통해 아이디를 읽어온다.
		메서드 실행 후 정수형 반환값을 통해 입력 성공/실패 여부를 확인 할 수 있다.
		*/
		int applyRow = sqlSession.getMapper(ServiceMyBoard.class).write(
				req.getParameter("name"),
				req.getParameter("contents"),
				((MyMemberDTO)session.getAttribute("siteUserInfo")).getId()
				);
		
		System.out.println("입력된행의갯수:"+applyRow);
		
		//글쓰기가 완료되면 리스트로 이동한다. 
		return "redirect:list.do";
	}
	
	//수정페이지 매핑
	@RequestMapping("/mybatis/modify.do")
	public String modify(Model model, HttpServletRequest req, HttpSession session) {
		/*
		작성자 본인에게만 수정, 삭제 버튼이 노출되므로 수정페이지 진입시에도
		로그인을 확인해야한다. 여기서는 단순히 로그인 여부만 확인했지만,
		필요하다면 작성자 본인이 맞는지까지 확인하면 더욱 좋다.
		*/
		if(session.getAttribute("siteUserInfo")==null) {
			//로그인이 안된상태라면 로그인 페이지로 리다이렉트한다.
			return "redirect:login.do";
		}
		
		/*
		파라미터를 전달하는 또 하나의 방법으로 DTO(혹은 VO)객체에 파라미터를 저장한 후
		Mapper로 전달한다. 폼값을 한꺼번에 받을 수 있는 커맨드객체와 비슷한 개념이다.
		*/
		ParameterDTO parameterDTO = new ParameterDTO();
		//게시물의 일련번호 저장
		parameterDTO.setBoard_idx(req.getParameter("idx"));
		//세션영역에 있는 사용자 아이디 저장
		parameterDTO.setUser_id(((MyMemberDTO)session.getAttribute("siteUserInfo")).getId());
		
		//인터페이스를 통해 Mapper의 view 메서드를 호출한다. 이때 앞에서 준비한 DTO객체를 인수로 전달한다.
		MyBoardDTO dto = sqlSession.getMapper(ServiceMyBoard.class).view(parameterDTO);
		
		//인출한 레코드는 모델객체에 저장한 후 뷰를 호출한다.
		model.addAttribute("dto", dto);
		return "07Mybatis/modify";
	}
	
	@RequestMapping("/mybatis/modifyAction.do")
	public String modifyAction(HttpSession session,
			MyBoardDTO myBoardDTO) {
		//수정페이지에서 전송된 폼값을 한꺼번에 받기 위해 커맨드객체인
		//MyBoardDTO를 매개변수로 선언한다.
		
		//수정 처리 전 로그인 확인
		if(session.getAttribute("siteUserInfo")==null) {
			return "redirect:login.do";
		}
		
		//수정처리를 위해 Mapper의 modify메서드 호출
		int applyRow = sqlSession.getMapper(ServiceMyBoard.class).modify(myBoardDTO);
		
		System.out.println("수정된행의갯수:"+applyRow);
		
		//방명록 게시판은 상세보기 페이지가 없으므로 수정완료시 리스트로 이동하면 된다.
		return "redirect:list.do";
	}
	
	@RequestMapping("/mybatis/delete.do")
	public String delete(HttpServletRequest req,
			HttpSession session) {
		//삭제는 본인만 가능하므로 로그인 확인을 진행한다.
		if(session.getAttribute("siteUserInfo")==null) {
			return "redirect:login.do";
		}
		/*
		Service인터페이스를 통해 Mapper의 delete 메서드를 호출한다.
		특히 아이디의 경우 session영역에 저장되어있으므로 DTO로 형변환 후
		getter를 통해 아이디를 읽어와 인수로 전달한다.
		*/
		int applyRow = sqlSession.getMapper(ServiceMyBoard.class).
				delete(req.getParameter("idx"),
						((MyMemberDTO)session.getAttribute("siteUserInfo")).getId());
		/* session영역에 속성을 저장하면 Object타입으로 변환되므로
		꺼내서 사용할때는 원래의 타입으로 형변환(다운캐스팅) 해야한다.
		코드의 우선순위를 따른 이슈가 있으면 소괄호를 적절히 사용해야한다. */
		System.out.println("삭제된행의갯수:"+applyRow);
		
		return "redirect:list.do";
	}
}
