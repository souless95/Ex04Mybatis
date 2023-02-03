package com.edu.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import mybatis.MyBoardDTO;
import mybatis.ServiceMyBoard;

@Controller
public class CollectionController {
	
	//Mybatis를 사용하기 위한 빈 자동주입
	@Autowired
	private SqlSession sqlSession;
	
	//Map 컬렉션을 Mapper에서 사용하기 위한 매핑
	@RequestMapping("/Collection/hashMap.do")
	public String hashMap(Model model) {
		//검색어 저장을 위한 Map컬렉션을 생성한다.
		Map<String, String> hashMap = new HashMap<String, String>();
		//각 Key에 원하는 검색어를 Value로 추가한다.
		//실전에서는 파라미터를 받아와서 사용하면 된다.
		hashMap.put("key_id", "sol");
		hashMap.put("key_name", "솔");
		hashMap.put("key_contents", "잠이");
		
		//파라미터를 저장한 Map을 서비스 인터페이스를 통해 Mapper로 전달한다.
		ArrayList<MyBoardDTO> lists =
				sqlSession.getMapper(ServiceMyBoard.class)
					.hashMapUse(hashMap);
		
		//Mapper에서 사용한 쿼리문을 콘솔에 출력한다.
		String sql = sqlSession.getConfiguration().getMappedStatement("hashMapUse")
				.getBoundSql(hashMap).getSql();
		System.out.println("sql="+sql);
		
		//모델에 출력할 데이터를 저장한 후 뷰를 반환
		model.addAttribute("lists", lists);
		return "08CollectionUse/hashMapUse";
	}
	
	@RequestMapping("/Collection/arrayList.do")
	public String arrayList(Model model) {
		
		//Mapper로 전달할 List 컬렉션 생성 및 파라미터 추가
		List<String> arrayList = new ArrayList<String>();
		arrayList.add("마데카솔");
		arrayList.add("칫솔");
		arrayList.add("옜솔");
		arrayList.add("솔");
		
		//서비스 인터페이스를 통해 Mapper를 호출한다.
		ArrayList<MyBoardDTO> lists =
				sqlSession.getMapper(ServiceMyBoard.class)
					.arrayListUse(arrayList);
		
		model.addAttribute("lists", lists);
		return "08CollectionUse/ArrayListUse";
	}
}
