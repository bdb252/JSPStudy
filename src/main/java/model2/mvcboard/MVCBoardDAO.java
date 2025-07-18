package model2.mvcboard;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.DBConnPool;

//커넥션풀을 통한 DB 연결을 위해 클래스 상속
public class MVCBoardDAO extends DBConnPool {
	
	//디폴트 생성자(직접 정의하지 않아도 자동으로 삽입됨)
	public MVCBoardDAO() {
		super();
	}
	
	//게시물의 개수를 카운트해서 int형으로 반환
	public int selectCount(Map<String, Object> map) {
		//게시물의 개수 초기화
		int totalCount = 0;
		//카운트를 위한 쿼리문
		String query = "SELECT COUNT(*) FROM mvcboard ";
		//검색을 위한 파라미터가 있으면 where절을 동적으로 추가.
		if (map.get("searchWord") != null) {
			query += "WHERE " + map.get("searchField") + " "
			       + "LIKE '%" + map.get("searchWord") + "%'";
		}
		
		try {
			//인파라미터가 없는 정적쿼리문이므로 Statement 인스턴스 생성
			stmt = con.createStatement();
			//쿼리문 실행 및 결과 반환
			rs = stmt.executeQuery(query);
			//count(*)를 실행하면 무조건 결과가 있으므로 조건문 없이 next함수 실행
			rs.next();
			//정수값이므로 getInt()함수로 결과값 인출
			totalCount = rs.getInt(1);
		}
		catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}

	//목록을 출력할 게시물을 인출
	public List<MVCBoardDTO> selectList(Map<String, Object> map) {
		/* mvcboard 테이블을 대상으로 select 쿼리문을 실행한 후 반환되는 
		 레코드를 저장하기 위해 List 계열의 컬렉션 생성 */
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		//쿼리문 작성
		String query = "SELECT * FROM mvcboard ";
		//검색어가 있으면 where절 추가
		if (map.get("searchWord") != null) {
			query += "WHERE " + map.get("searchField") + " "
			       + "LIKE '%" + map.get("searchWord") + "%' ";
		}
		//내림차순 정렬 적용
		query += "ORDER BY idx DESC";

		try {
			//쿼리문 실행을 위한 preparedStatement 인스턴스 생성
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();
			//인출된 레코드의 개수만큼 반복
			while (rs.next()) {
				//DTO 인스턴스 생성
				MVCBoardDTO dto = new MVCBoardDTO();
				
				//하나의 레코드를 DTO 인스턴스에 저장
				dto.setIdx(rs.getString(1));
				dto.setId(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setVisitcount(rs.getInt(9));

				//하나의 레코드를 저장한 DTO를 List에 추가
				board.add(dto);
			}
		}
		catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		return board;
	}
	public int insertWrite(MVCBoardDTO dto) {
		int result = 0;
		try {
			//인파라미터가 있는 insert 쿼리문 작성
			String query = "INSERT INTO mvcboard ("
					+ "idx, id, title, content, ofile, sfile)"
					+ "VALUES ("
					+ "seq_board_num.NEXTVAL, ?, ?, ?, ?, ?)";
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile());
			psmt.setString(5, dto.getSfile());
			/*
			insert, update, delete와 같은 쿼리문은 실행 후 성공한 행의 개수를
			반환하므로 int형의 반환값을 가지게 된다.
			 */
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	//열람 기능 : 게시물의 일련번호를 매개변수로 받음
	public MVCBoardDTO selectView(String idx) {
		//레코드 저장을 위해 DTO 인스턴스 생성
		MVCBoardDTO dto = new MVCBoardDTO();
		/*
		게시물 조회시 작성자의 이름까지 출력하기 위해 member 테이블과 
		내부 조인으로 게시물 인출
		 */
		String query = "SELECT Bo.*, Me.name FROM mvcboard Bo "
				+ " INNER JOIN member Me ON Bo.id=Me.id"
				+ " WHERE idx=?";
		try {
			//쿼리문 실행
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			rs = psmt.executeQuery();
			/*
			조건에 맞는 레코드가 있는 경우라면 next()를 실행했을때 true가 반환된다.
			만약 레코드가 없다면 false를 반환하게 된다.
			즉 레코드를 인출하는 쿼리문을 실행했을때는 반드시 조건문을 통해 next()함수가 실행된다.
			 */
			if(rs.next()) {
				dto.setIdx(rs.getString(1));
				dto.setId(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setVisitcount(rs.getInt(9));
				dto.setName(rs.getString(10));
			}
		}
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		//DTO인스턴스 반환
		return dto;		
	}
	
	//게시물 조회수 증가
	public void updateVisitCount(String idx) {
		//visitcount 컬럼의 값을 1 증가시킨다.
		String query = "UPDATE mvcboard SET"
				+ " visitcount=visitcount+1 "
				+ " WHERE idx=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			rs = psmt.executeQuery();
		}
		catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	public void downCountPlud(String idx) {
		String query = "UPDATE mvcboard SET"
				+ " downcount=downcount+1 "
				+ " WHERE idx=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			rs = psmt.executeQuery();
		}
		catch (Exception e) {
			System.out.println("게시물 다운로드수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	//지정한 일련번호의 게시물 삭제
	public int deletePost(String idx) {
		int result = 0;
		try {
			//인파라미터가 있는 delete 쿼리문 작성
			String query = "DELETE FROM mvcboard WHERE idx=?";
			//인파라미터 세팅 후 쿼리문 실행
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			//삭제 성공시 1, 실패시 0 반환
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	public int updatePost(MVCBoardDTO dto) {
		int result = 0;
		try {
			
			String query = "UPDATE mvcboard "
					+ "SET title=?, content=?, ofile=?, sfile=?"
					+ " WHERE idx=? and id=?";
			//인파라미터 세팅 후 쿼리문 실행
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getOfile());
			psmt.setString(4, dto.getSfile());
			psmt.setString(5, dto.getIdx());
			psmt.setString(6, dto.getId());

			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
}
