package demo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class AsyncServlet
 */
@WebServlet(value = "/leak", asyncSupported = true)
public class LeakServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** ロガー */
    private static final Logger log = LoggerFactory.getLogger(LeakServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */

    public LeakServlet() {
        super();
    }

    volatile static boolean isLiveServlet = false;

    @Override
    public void init() throws ServletException {
        log.info("init " + this.toString());
        isLiveServlet = true;
        super.init();
    }

    @Override
    public void destroy() {
        log.info("destroy " + this.toString());
        isLiveServlet = false;
        super.destroy();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.isAsyncStarted()) {
            return;
        }

        AsyncContext asyncContext = request.startAsync(request, response);

        HttpSession session = request.getSession();
        AtomicBoolean asyncActive = init(session);
        asyncActive.set(true);

        LeakHolder holder = new LeakHolder();
        asyncContext.start(() -> {
            log.info("leak thread start");
            try {
                while (asyncActive.get() ) {
                    holder.set((byte) (System.currentTimeMillis() & 0xFF));
                    Thread.sleep(20);

                }
            } catch (InterruptedException e) {
            }
            log.info("leak thread end" + holder.get());
        });

        asyncContext.dispatch("/leak.jsp");
    }

    protected AtomicBoolean init(HttpSession session) {
        AtomicBoolean asyncActive = (AtomicBoolean) session.getAttribute("asyncActive");
        if (asyncActive == null) {
            asyncActive = new AtomicBoolean(true);
            session.setAttribute("asyncActive", asyncActive);
        }
        return asyncActive;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
