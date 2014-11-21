package nano;

import nano.dao.*;

import java.util.*;


/*****************************************************************
 * @description : query 한 데이타를 Jsp 에서 list 하기위한 class
 * $Id: ListData.java,v 1.6 2011/10/02 08:12:00 $
 * $Revision: 1.1 $
 *****************************************************************
 * DATE			AUTHOR		DESCRIPTION
 * ---------------------------------------------------------------
 * 2011. 12. 20	정수현		최초작성
 * 
 *****************************************************************/

public class ListData {
	private ArrayList list = null;
	private int page_current = 1;
	private int page_total = 1;
	private int totalCnt =0;
	public int itemRange = 0;   // 한 페이지에 보여줄 라인 수
	
	public ListData(){
		list = new ArrayList();
	}
	
	/**
	 * 리스트 조회
	 * @return
	 */
	public ArrayList getList(){
		return list;
	}
	
	/**
	 * 리스트 설정
	 * @param list
	 */
	public void setList(ArrayList list){
		this.list = list;
	}
	
	/**
	 * row 추가
	 * 
	 * @param obj
	 */
	public void add(Object obj){
		boolean b = list.add(obj);
	}
	
	/**
	 * 리스트의 size (검색 결과 count 가 아님)
	 * @return
	 */
	public int size(){
		return list.size();
	}

	/**
	 * i 번째 row
	 * 
	 * @param i
	 * @return
	 */
	public Record get(int i){
		return (Record)list.get(i);
	}
	
	/**
	 * 현재 페이지 
	 * @param p
	 */
	public void setCurrentPage(int p){
		this.page_current = p;
	}
	
	/**
	 * 현재 페이지 
	 * @return
	 */
	public int getCurrentPage(){
		return page_current;
	}
	
	/**
	 * 한페이지당 리스트수 설정
	 * @param p
	 */
	public void setItemRange(int p){
		this.itemRange = p;
	}
	
	
	/**
	 * 페이지 설정
	 * @param totalCnt
	 */
	public void setTotalPage(int totalCnt){
		this.totalCnt = totalCnt;
		this.page_total = (int) Math.ceil((double)totalCnt / (double)itemRange);
	}
	
	/**
	 * 총 페이지수
	 * @return
	 */
	public int getTotalPage(){
		return page_total;
	}
	
	/**
	 * 검색 리스트 결과의 총 row 수
	 * @return
	 */
	public int getTotalCount(){
		return totalCnt;
	}
	
	/**
	 * 페이지 네비게이션
	 * 
	 * @return
	 */
	public String getPageLink(){
		return DaoCom.getPageLink(page_current, page_total);
	}
	
	public String getPageLink(int pRange){
		return DaoCom.getPageLink(page_current, page_total, pRange);
	}

	/**
	 * 페이지 네비게이션 관리자용
	 * 
	 * @return
	 */
	public String getAdminPageLink(){
		return DaoCom.getAdminPageLink(page_current, page_total);
	}
	
	public String getAdminPageLink(int pRange){
		return DaoCom.getAdminPageLink(page_current, page_total, pRange);
	}

	
	
	/**
	 * 게시물 가상 번호 (역순)
	 * - 특정 페이지의 가상 번호의 시작값
	 * jsp 에서는 이 번호를 리스트 할때 1씩 빼주면 된다.
	 * 
	 * @return
	 */
	public int getVirtualNo(){
		return getTotalCount()- itemRange *(getCurrentPage()-1);
	}

}
