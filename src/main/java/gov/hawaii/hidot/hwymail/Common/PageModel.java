package gov.hawaii.hidot.hwymail.Common;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PageModel {
    private static int PAGE = 0;
    private static int SIZE = 15;

    private HttpServletRequest request;
    public PageModel(HttpServletRequest request) {
        this.request = request;
    }
    public void initPageAndSize(){
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            PAGE = Integer.parseInt(request.getParameter("page")) - 1;
        } else {
            PAGE =0;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            PAGE = Integer.parseInt(request.getParameter("size"));
        }
    }
    public static void setSIZE(int SIZE) {
        PageModel.SIZE = SIZE;
    }

    public static int getPAGE() {
        return PAGE;
    }
    public static int getSIZE() {
        return SIZE;
    }

}
