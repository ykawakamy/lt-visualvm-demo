package demo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(value="/release", asyncSupported = true)
public class ReleaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** ロガー */
    private static final Logger log = LoggerFactory.getLogger(ReleaseServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReleaseServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        AtomicBoolean asyncActive = (AtomicBoolean) session.getAttribute("asyncActive");

        asyncActive.set(false);

        request.getRequestDispatcher("/release.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
