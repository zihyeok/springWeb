package com.jdbc.util;

public class MyUtil {
	
	//전체페이지수                           
	public int getPageCount(int numPerPage, int dataCount) {
		
		int pageCount = 0;
		pageCount = dataCount / numPerPage;
		
		if(dataCount%numPerPage!=0) {
			pageCount++;
		}
		
		return pageCount;
	}
	
	//페이지 처리
	public String pageIndexList(int currentPage, int totalPage, 
			String listUrl) {
		
		int numPerBlock = 5;//◀이전  6  7  8  9  10  ▶다음 6~10여기의 개수
		int currentPageSetup;//◀이전 <-이거
		int page;//6,7,8,9,10 이 값이 들어가는 숫자값. 일명 for문의 i
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage==0 || totalPage==0) {
			return "";
		}
		
		//listUrl에 list.jsp값을 넣어줌.
		//검색X = list.jsp
		//검색O = list.jsp?searchKey=subject&serachValue=3
		
		//?가 있다면(검색을 했다면) &를 붙여주고 -> list.jsp?searchKey=subject&serachValue=3&
		//아니라면(검색을 하지 않았다면) ?를 붙여준다. -> list.jsp?
		//그래야 뒤에 pageNum을 붙일 수 있다
		if(listUrl.indexOf("?")!=-1) {
			listUrl = listUrl+"&";
		}else {
			listUrl = listUrl+"?";
		}
		
		//표시할 첫 페이지의 -1 해준 값
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		//딱 떨어졌을 때 문제 고치는 과정
		if(currentPage%numPerBlock==0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}
		//currentpage 10 numperblock 5
		//이렇게 하면 값이 10 나옴
		//그래서 if로 currentpagesetup = 10-5 => 5
		//이렇게 "◀이전" 이곳에 5를 넣음
		
		//        1  2  3  4  5  ▶다음
		//◀이전  6  7  8  9  10  ▶다음
		//◀이전  11  12 
		
		//"◀이전" 만드는 과정
		if(totalPage>numPerBlock && currentPageSetup>0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + 
					currentPageSetup + "\">◀이전</a>&nbsp;");
			//<a href="list.jsp?pageNum=5">◀이전</a>&nbsp;
		}
		
		//바로가기 페이지( 1 2 3 4 5 ...) 이부분 만든다
		page = currentPageSetup + 1;
		
		while(page<=totalPage && page<=(currentPageSetup+numPerBlock)) {
			
			if(page==currentPage) {
				sb.append("<font color=\"Fuchsia\">" + page + 
						"</font>&nbsp;");
				//<font color="Fuchsia">1</font>&nbsp;
			}else {
				sb.append("<a href=\"" + listUrl + "pageNum=" + 
						page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=2">2</a>&nbsp;
			}
			page++;
		}
		
		//"다음▶"만드는 과정
		if(totalPage-currentPageSetup>numPerBlock) {
			
			sb.append("<a href=\"" + listUrl + "pageNum=" + 
					page + "\">다음▶</a>&nbsp;");
			//<a href="list.jsp?pageNum=6">다음▶</a>&nbsp;
		}
		
		return sb.toString();
	}
}
	
