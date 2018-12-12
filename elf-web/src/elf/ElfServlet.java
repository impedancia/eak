package elf;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ElfServlet
 */
@WebServlet("/ElfServlet")
public class ElfServlet extends HttpServlet {

	//@Resource(lookup="java:global/elf-app/elf-ejb/ElfBean!elf.ElfTownLocal")
	@EJB private ElfBean elfTown;
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ElfServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		elfTown.createFactories(true, "F1","F2");
		elfTown.createPresentFor("F1","Bencus");
		List<String> children = elfTown.getChildrenIn("F1");
		for(String ch:children) {
			response.getWriter().append(ch);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
