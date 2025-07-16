package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
web.xml에 매핑을 하는 대신 @WebServlet 어노테이션을 사용하여 요청명에 대한 매핑을 한다.
*/
@WebServlet("/12Servlet/AnnoMapping.do")
public class AnnoMapping extends HttpServlet{
	/**
	서블릿 작성시 발생되는 경고(Warning)를 위해 추가한다.
	없어도 실행에는 영향을 미치지 않는다. */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		//request 영역에 속성을 저장한 후 JSP로 포워드한다.
		req.setAttribute("message", "@webservlet으로 매핑");
		req.getRequestDispatcher("/12Servlet/AnnoMapping.jsp").forward(req, resp);
	}
}
