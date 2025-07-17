package model2.mvcboard;

import java.io.IOException;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

/*
글쓰기를 위한 컨트롤러는 페이지로 진입하기 위한 doGet()과 쓰기처리를 위한
doPost()를 한꺼번에 정의해야 한다. */
public class WriteController extends HttpServlet{
	
	/*
	글쓰기 페이지에 진입하는 것은 버튼을 클릭하여 이동하게 되므로
	GET방식의 요청이다. */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//로그인 확인을 위해 session 내장객체를 얻어온다.
		HttpSession session = req.getSession();
		//session영역에 회원인증에 관련된 속성이 없다면 로그인 페이지로 이동한다.
		if(session.getAttribute("UserId")==null) {
			JSFunction.alertLocation(resp, "로그인 후 이용해주세요", "../06Session/LoginForm.jsp");
			//Java코드가 더이상 실행되지 않도록 중지한다.
			return;
		}
		//로그인이 완료된 상태라면 쓰기페이지를 포워드한다.
		req.getRequestDispatcher("/14MVCBoard/Write.jsp").forward(req, resp);
	}
	
	/*
	글쓰기 처리는 POST방식의 요청임
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		/*
		 로그인 확인. session은 일정 시간이 지나면 자동으로 소멸되므로
		 확인이 필요하다. */
		HttpSession session = req.getSession();
		if(session.getAttribute("UserId")==null) {
			JSFunction.alertLocation(resp, "로그인 후 이용해주세요", "../06Session/LoginForm.jsp");
			return;
			
		}
		
		//1. 파일 업로드 처리======================
		// 업로드 디렉토리의 물리적 경로 확인
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		
		//파일 업로드 처리
		String originalFileName = "";
		try {
			//작성폼에서 전송한 파일을 업로드 처리
			originalFileName = FileUtil.uploadFile(req, saveDirectory);
		}
		catch (Exception e) {
			//업로드에 문제가 있다면 경고창을 띄우고 작성페이지로 이동
			JSFunction.alertLocation(resp, "파일 업로드 오류입니다.", "../nvcboard/write.do");
			return;
		}
		
		//2. 파일 업로드 외 처리=====================
		// 폼값을 DTO에 저장
		MVCBoardDTO dto = new MVCBoardDTO();
		//작성자 아이디는 session영역에 저장된 데이터 사용
		dto.setId(session.getAttribute("UserId").toString());
		//제목, 내용은 사용자가 입력한 값을 사용
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		
		//첨부한 파일이 있다면 파일명을 변경. 원본 파일명과 저장된 파일 이름 설정
		if(originalFileName != "") {
			//파일명 변경
			String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
			//업로드된 파일의 정보를 DTO 인스턴스에 저장
			dto.setOfile(originalFileName); //원본 파일명
			dto.setSfile(savedFileName);	//서버에 저장된 파일명
		}
		//DAO 인스턴스 생성 및 커넥션 풀을 통한 연결. DB에 게시 내용 저장
		MVCBoardDAO dao = new MVCBoardDAO();
		//insert 쿼리문 실행
		int result = dao.insertWrite(dto);
		//자원 반납
		dao.close();
		
		//성공 or 실패?
		if(result == 1) { 
			//글쓰기 성공하면 목록으로 이동
			resp.sendRedirect("../mvcboard/list.do");
		}
		else { 	
			//글쓰기 실패하면 쓰기페이지로 이동
			JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../mvcboard/write.do");
		}
	}
}
